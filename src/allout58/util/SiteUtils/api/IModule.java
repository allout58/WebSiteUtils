package allout58.util.SiteUtils.api;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import javax.swing.*;

/**
 * Created by James Hollowell on 3/16/2015.
 */
public interface IModule
{
    /**
     * Get a panel that will be used to represent this module. Should be a singleton.
     *
     * @return The panel
     */
    JPanel getPanel();

    /**
     * Add the options for this module
     * <p>
     * For the time being, none of these options should be required
     *
     * @param parser
     */
    void addOptionAcceptors(OptionParser parser);

    void parseOptions(OptionSet optionSet);
}
