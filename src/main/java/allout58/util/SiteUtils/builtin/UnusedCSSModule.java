package allout58.util.SiteUtils.builtin;

import allout58.util.SiteUtils.Utils;
import allout58.util.SiteUtils.api.IModule;
import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by James Hollowell on 3/20/2015.
 */
public class UnusedCSSModule implements IModule
{
    private static Logger logger = LogManager.getLogger("UnusedCSSModule");

    private File siteMapFile;
    private String cssFile;
    private Queue<String> siteMap = new LinkedList<>();
    private List<String> cssSelectors = new ArrayList<>();
    private Map<String, Integer> selectorUsage = new HashMap<>();

    private OptionSpec<File> sitemapOpt;
    private OptionSpec<String> cssOpt;

    @Override
    public JPanel getPanel()
    {
        return null;
    }

    @Override
    public void addOptionAcceptors(OptionParser parser)
    {
        sitemapOpt = parser.acceptsAll(Arrays.asList("map", "sitemap"), "File containing a list of files to check").withRequiredArg().ofType(File.class).required();
        cssOpt = parser.accepts("css", "File or URL of CSS file to check").withRequiredArg().ofType(String.class).required();
        logger.info("Options added");
    }

    @Override
    public void parseOptions(OptionSet optionSet)
    {
        siteMapFile = optionSet.valueOf(sitemapOpt);
        cssFile = optionSet.valueOf(cssOpt);
        run();
    }

    private void run()
    {
        if (readSitemap() && readCSS())
        {
            System.out.println("Continue");
            String loc;
            while ((loc = siteMap.poll()) != null)
            {
                try
                {
                    Connection connect = Jsoup.connect(loc)
                            .userAgent(Utils.chromeUA)
                            .timeout(3000);
                    connect.execute();
                    Document document = connect.get();
                    for (String sel : cssSelectors)
                    {

                        int currentCount = selectorUsage.getOrDefault(sel, 0);
                        if (document.select(sel).size() > 0)
                            selectorUsage.put(sel, currentCount + 1);
                    }
                }
                catch (IOException e)
                {
                    logger.error(e);
                }
            }

            for (Map.Entry<String, Integer> entry : selectorUsage.entrySet())
            {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }
    }

    private boolean readSitemap()
    {
        try
        {
            if (!siteMapFile.exists())
            {
                logger.error("Sitemap file " + siteMapFile.getAbsolutePath() + " does not exist and cannot be read from.");
                return false;
            }
            BufferedReader br = new BufferedReader(new FileReader(siteMapFile));
            String line;
            while ((line = br.readLine()) != null)
            {
                siteMap.add(line);
            }
        }
        catch (IOException e)
        {
            logger.error("Error reading sitemap file.", e);
            return false;
        }
        return true;
    }

    private boolean readCSS()
    {
        try
        {
            CascadingStyleSheet cascadingStyleSheet;
            if (!cssFile.contains("://")) //Not a valid URL
            {
                File css = new File(cssFile);
                if (!css.exists())
                {
                    logger.error("Local CSS file " + css.getAbsolutePath() + " does not exist and cannot be read from.");
                    return false;
                }
                cascadingStyleSheet = CSSReader.readFromFile(css, CCharset.DEFAULT_CHARSET_OBJ, ECSSVersion.CSS30);
            }
            else
            {
                URL url = new URL(cssFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line, styles = "";
                while ((line = br.readLine()) != null)
                {
                    styles += line + System.getProperty("line.separator");
                }
                cascadingStyleSheet = CSSReader.readFromString(styles, ECSSVersion.CSS30);
            }
            if (cascadingStyleSheet == null)
            {
                logger.error("Error parsing CSS file.");
                return false;
            }
            for (CSSStyleRule rule : cascadingStyleSheet.getAllStyleRules())
            {
                for (CSSSelector val : rule.getAllSelectors())
                {
                    String style = Utils.stripCssStates(val.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
                    if (!cssSelectors.contains(style))
                    {
                        cssSelectors.add(style);
                        System.out.println(style);
                    }
                }
            }
            System.out.println("Total number of CSS style Selectors (ignoring hoverstates): " + cssSelectors.size());
        }

        catch (IOException e)
        {
            logger.error("Error reading the CSS file.", e);
            return false;
        }
        return true;
    }
}
