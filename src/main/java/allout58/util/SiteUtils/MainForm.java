package allout58.util.SiteUtils;

import allout58.util.SiteUtils.api.IModule;
import allout58.util.SiteUtils.api.ModuleManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by James Hollowell on 3/16/2015.
 */
public class MainForm extends JFrame
{
    private JPanel main = new JPanel(new BorderLayout(5, 5));

    public MainForm() throws HeadlessException
    {
        super("Site Utilities");
        this.add(main);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        String name = "";
        IModule module = null;
        while ((module = ModuleManager.getInstance().getModuleByName(name)) == null)
            name = JOptionPane.showInputDialog(this, "Name of module to load");

        main.add(module.getPanel(), BorderLayout.EAST);

        add(main);
    }
}
