package allout58.util.SiteUtils.builtin;

import allout58.util.SiteUtils.Utils;
import allout58.util.SiteUtils.api.IModule;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by James Hollowell on 3/16/2015.
 */
public class BrokenLinksModule implements IModule
{    /*
     * Taken from org.jsoup.helper.HttpConnection.Response
     *
     * For example {@code application/atom+xml;charset=utf-8}.
     * Stepping through it: start with {@code "application/"}, follow with word
     * characters up to a {@code "+xml"}, and then maybe more ({@code .*}).
     */
    //private static final Pattern xmlContentTypeRxp = Pattern.compile("application/\\w+\\+xml.*");
    private static final Logger logger = LogManager.getLogger("BrokenLinksModule");

    private List<String> visitedPages = new ArrayList<>();
    private List<BrokenLink> brokenLinks = new ArrayList<>();
    private LinkedList<Link> pageQueue = new LinkedList<>();
    private String root;
    private boolean followRedirect = false;
    private boolean doImages = false;

    private OptionSpec followRedirectOpt;
    private OptionSpec<File> siteMapOutOpt;
    private OptionSpec doImagesOpt;

    @Override
    public JPanel getPanel()
    {
        return null;
    }

    @Override
    public void addOptionAcceptors(OptionParser parser)
    {
        followRedirectOpt = parser.acceptsAll(Arrays.asList("followRedirect", "r"), "Follow the redirection");
        siteMapOutOpt = parser.acceptsAll(Arrays.asList("siteMapOut", "s"), "Output file for a list of all discovered pages (and images).").withRequiredArg().ofType(File.class);
        doImagesOpt = parser.accepts("doImages", "Check images for availability");
    }

    @Override
    public void parseOptions(OptionSet optionSet)
    {
        try
        {
            System.out.println("Beginning Broken Links Check.");
            if (!optionSet.has("site"))
            {
                System.out.println("Site option required");
                return;
            }
            root = (String) optionSet.valueOf("site");
            followRedirect = optionSet.has(followRedirectOpt);
            doImages = optionSet.has(doImagesOpt);
            long timeStart = System.nanoTime();
            beginTraversal(root);
            long timeEnd = System.nanoTime();
            System.out.println();
            System.out.println("Broken Links:");
            for (BrokenLink b : brokenLinks)
                System.out.println(b);

            if (optionSet.has(siteMapOutOpt))
            {
                try
                {
                    File out = optionSet.valueOf(siteMapOutOpt);
                    if (!out.exists() && !out.createNewFile())
                        logger.error("Error creating file.");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(out));
                    for (String map : visitedPages)
                    {
                        bw.write(map);
                        bw.newLine();
                    }
                }
                catch (IOException e)
                {
                    logger.error("Error writing sitemap to file", e);
                }
            }

            System.out.println("======Statistics:======");
            System.out.println(String.format("%-30s:%10d", "Number of Visited Pages", visitedPages.size()));
            System.out.println(String.format("%-30s:%10.2f", "Total Time", ((double) (timeEnd - timeStart)) / 1000000000.0));
        }
        catch (MalformedURLException mue)
        {
            System.err.println("Error! Bad URL!");
            mue.printStackTrace();
        }
        catch (IOException ioe)
        {
            System.err.println("Error reading site.");
            ioe.printStackTrace();
        }
    }

    private void beginTraversal(String url) throws IOException
    {
        pageQueue.add(new Link(url, null));
        travers();
    }

    private void travers() throws IOException
    {
        while (pageQueue.size() > 0)
        {
            try
            {
                Link link = pageQueue.getFirst();

                if (!visitedPages.contains(link.getUrl()))
                {
                    visitedPages.add(link.getUrl());
                    if (visitedPages.size() % 50 == 0)
                        System.out.println("Current number of visited pages: " + visitedPages.size());
                    int statusCode = pingAddr(new URL(link.getUrl()));
                    boolean validEnding = true;
                    for (String ending : Utils.blockedExtensions)
                        validEnding &= !link.getUrl().endsWith(ending);
                    if (statusCode == 200)
                    {
                        if (validEnding)
                        {
                            Connection connect = Jsoup.connect(link.getUrl())
                                    .userAgent(Utils.chromeUA)
                                    .timeout(3000);
                            connect.execute();
                            Document document = connect.get();
                            Elements elements = document.select("a");
                            for (Element el : elements)
                            {
                                String href = el.attr("href");
                                //Ignore get parameters
                                /*if (href.contains("?"))
                                {
                                    href = href.substring(0, href.indexOf("?"));
                                }*/
                                //Ignore id links
                                if (href.contains("#"))
                                {
                                    href = href.substring(0, href.indexOf("#"));
                                }
                                //Ignore relative links and only do links on this site
                                if (!"".equals(href.trim())
                                        && (href.contains(root) || !href.startsWith("http"))
                                        && (href.startsWith("http") || !href.contains(":"))
                                        )
                                {
                                    pageQueue.add(new Link(new URL(new URL(link.getUrl()), href).toExternalForm(), el));
                                }
                            }
                            if (doImages)
                            {
                                elements = document.select("img");
                                for (Element el : elements)
                                {
                                    String href = el.attr("src");
                                    if (!"".equals(href.trim())
                                            && (href.contains(root) || !href.startsWith("http"))
                                            && (href.startsWith("http") || !href.contains(":"))
                                            )
                                    {
                                        pageQueue.add(new Link(new URL(new URL(link.getUrl()), href).toExternalForm(), el));
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        brokenLinks.add(new BrokenLink(link.getUrl(), link.getContext() == null ? "" : link.getContext().outerHtml(), link.getContext() == null ? "" : link.getContext().baseUri(), statusCode));
                    }

                }
                pageQueue.removeFirst();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private int pingAddr(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("HEAD");
        urlConnection.setRequestProperty("User-Agent", Utils.chromeUA);
        urlConnection.setInstanceFollowRedirects(followRedirect);
        urlConnection.connect();
        return urlConnection.getResponseCode();
    }

    public static class Link
    {
        private String url;
        private Element context;

        public Link(String url, Element context)
        {
            this.url = url;
            this.context = context;
        }

        public String getUrl()
        {
            return url;
        }

        public Element getContext()
        {
            return context;
        }
    }

    public static class BrokenLink
    {
        private String page;
        private String referringContext;
        private int statusCode;
        private String referringPage;

        public BrokenLink(String page, String referringContext, String referringPage, int statusCode)
        {
            this.page = page;
            this.referringContext = referringContext;
            this.referringPage = referringPage;
            this.statusCode = statusCode;
        }

        @Override
        public String toString()
        {
            return "BrokenLink{" +
                    "page='" + page + '\'' +
                    ", referringContext='" + referringContext + '\'' +
                    ", referringPage='" + referringPage + '\'' +
                    ", statusCode=" + statusCode +
                    '}';
        }
    }
}
