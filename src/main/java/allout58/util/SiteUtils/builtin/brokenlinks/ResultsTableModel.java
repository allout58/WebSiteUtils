package allout58.util.SiteUtils.builtin.brokenlinks;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * Created by James Hollowell on 4/7/2015.
 */
public class ResultsTableModel extends DefaultTableModel
{
    private static final String[] colNames = new String[] { "Page", "Referrer", "Referring HTML", "Status Code" };

    private List<BrokenLink> brokenLinks;

    public ResultsTableModel(List<BrokenLink> brokenLinks)
    {
        this.brokenLinks = brokenLinks;
    }

    @Override
    public int getRowCount()
    {
        if (brokenLinks == null)
            return 0;
        return brokenLinks.size();
    }

    @Override
    public int getColumnCount()
    {
        return colNames.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return colNames[column];
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        switch (column)
        {
            case 0:
                return brokenLinks.get(row).getPage();
            case 1:
                return brokenLinks.get(row).getReferringPage();
            case 2:
                return brokenLinks.get(row).getReferringContext();
            case 3:
                return brokenLinks.get(row).getStatusCode();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
