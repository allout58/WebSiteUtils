package allout58.util.SiteUtils.builtin.brokenlinks;

import org.jsoup.nodes.Element;

/**
 * Created by James Hollowell on 4/7/2015.
 */
public class Link
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
