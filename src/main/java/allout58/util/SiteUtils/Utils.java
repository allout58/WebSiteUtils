package allout58.util.SiteUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by James Hollowell on 3/17/2015.
 */
public class Utils
{
    public static String absoluteURL(String referer, String link)
            throws MalformedURLException
    {
        //String base = referer.substring(0, referer.lastIndexOf("/"));
        URL url = new URL(new URL(referer), link);
        return url.toExternalForm();
    }
}
