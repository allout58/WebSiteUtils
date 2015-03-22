package allout58.util.SiteUtils.builtin;

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

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
                    cssSelectors.add(val.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
                    System.out.println(val.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0));
                }
            }
            System.out.println("Total number of CSS style Selectors: " + cssSelectors.size());
        }

        catch (IOException e)
        {
            logger.error("Error reading the CSS file.", e);
            return false;
        }
        return true;
    }
}
