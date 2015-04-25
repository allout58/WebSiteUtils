package allout58.util.SiteUtils.builtin.brokenlinks;

/**
 * Created by James Hollowell on 4/7/2015.
 */
public class BrokenLink
{
    private String page;
    private String referringContext;
    private int statusCode;
    private String referringPage;

    public BrokenLink(String page, String referringContext, String referringPage, int statusCode)
    {
        this.page = page;
        this.referringContext = referringContext;
        this.referringPage = referringPage;
        this.statusCode = statusCode;
    }

    public String getPage()
    {
        return page;
    }

    public String getReferringContext()
    {
        return referringContext;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getReferringPage()
    {
        return referringPage;
    }

    @Override
    public String toString()
    {
        return "BrokenLink{" +
                "page='" + page + '\'' +
                ", referringContext='" + referringContext + '\'' +
                ", referringPage='" + referringPage + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
