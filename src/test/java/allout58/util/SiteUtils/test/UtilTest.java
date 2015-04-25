package allout58.util.SiteUtils.test; /**
 * Created by James Hollowell on 3/23/2015.
 */

import allout58.util.SiteUtils.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Test for {@link allout58.util.SiteUtils.Utils}
 */
public class UtilTest
{
    @Test
    public void stripCssStatesTest()
    {
        Assert.assertEquals("No state", "a", Utils.stripCssStates("a"));
        Assert.assertEquals("One state, end of line", "a", Utils.stripCssStates("a:hover"));
        Assert.assertEquals("Sub each selector, one state, end of line", "p>a", Utils.stripCssStates("p>a:hover"));
        Assert.assertEquals("Sub each selector, one state, middle of line", "p>a>p", Utils.stripCssStates("p>a:hover>p"));
        Assert.assertEquals("Sub selector, one state, end of line", "p a", Utils.stripCssStates("p a:hover"));
        Assert.assertEquals("Sub selector, one state, middle of line", "p a p", Utils.stripCssStates("p a:hover p"));
        Assert.assertEquals("Multiple selector, one state, end of line", "p,a", Utils.stripCssStates("p,a:hover"));
        Assert.assertEquals("Multiple selector, one state, middle of line", "p,a,p", Utils.stripCssStates("p,a:hover,p"));
    }

    @Test
    public void absoluteURLTest() throws MalformedURLException
    {
        Assert.assertEquals("Completely different site", new URL("http://www.google.com").toString(), Utils.absoluteURL("http://testcareer.clemson.edu/index.php", "http://www.google.com"));
        Assert.assertEquals("Relative, same level", new URL("http://testcareer.clemson.edu/events.php").toString(), Utils.absoluteURL("http://testcareer.clemson.edu/index.php", "events.php"));
        Assert.assertEquals("Relative, lower level", new URL("http://testcareer.clemson.edu/calendar/event.php").toString(), Utils.absoluteURL("http://testcareer.clemson.edu/index.php", "calendar/event.php"));
        Assert.assertEquals("Absolute", new URL("http://testcareer.clemson.edu/calendar/event.php").toString(), Utils.absoluteURL("http://testcareer.clemson.edu/internship_programs/index.php", "/calendar/event.php"));
        Assert.assertEquals("Relative, higher level", new URL("http://testcareer.clemson.edu/events.php").toString(), Utils.absoluteURL("http://testcareer.clemson.edu/internship_programs/index.php", "../events.php"));
    }
}
