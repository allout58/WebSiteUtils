package allout58.util.SiteUtils.builtin;

import allout58.util.SiteUtils.api.IModule;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

/**
 * Created by James Hollowell on 3/20/2015.
 */
public class UnusedCSSModule implements IModule
{
    private static Logger logger = LogManager.getLogger("UnusedCSSModule");

    @Override
    public JPanel getPanel()
    {
        return null;
    }

    @Override
    public void addOptionAcceptors(OptionParser parser)
    {
        parser.acceptsAll(Arrays.asList("map", "sitemap"), "File containing a list of files to check").withRequiredArg().ofType(File.class).required();
        parser.accepts("css", "File or URL of CSS file to check").withRequiredArg().ofType(String.class).required();
        logger.info("Options added");
    }

    @Override
    public void parseOptions(OptionSet optionSet)
    {

    }
}
