package allout58.util.SiteUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by James Hollowell on 3/17/2015.
 */
public class Utils
{
    public static final String chromeUA = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    public static final String[] blockedExtensions = new String[] { ".pdf", ".json", ".jpg", ".jpeg", ".gif", ".png", ".doc", ".docx", ".ppt", ".pptx", ".zip" };

    public static String absoluteURL(String referer, String link)
            throws MalformedURLException
    {
        String newlink = link.replace(" ", "%20");
        String base = referer.substring(0, referer.lastIndexOf("/") + 1);
        URL url = new URL(new URL(referer), newlink);
        return url.toExternalForm();
    }

    public static String stripCssStates(String inputStyle)
    {
        if (!inputStyle.contains(":")) return inputStyle;
        String output = inputStyle;
        while (output.indexOf(":") > 0)
        {
            int indexCol = output.indexOf(":");
            int indexSpace = output.indexOf(" ", indexCol);
            int indexComma = output.indexOf(",", indexCol);
            int indexArrow = output.indexOf(">", indexCol);

            if (indexSpace > 0)
                output = output.replace(output.substring(indexCol, indexSpace), "");
            else if (indexComma > 0)
                output = output.replace(output.substring(indexCol, indexComma), "");
            else if (indexArrow > 0)
                output = output.replace(output.substring(indexCol, indexArrow), "");
            else
                output = output.substring(0, indexCol);
        }
        return output;
    }
}
