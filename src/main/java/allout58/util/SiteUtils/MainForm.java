package allout58.util.SiteUtils;

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
        JToolBar mainToolbar = new JToolBar();
        //mainToolbar.add()
        //main.add();
    }
}
