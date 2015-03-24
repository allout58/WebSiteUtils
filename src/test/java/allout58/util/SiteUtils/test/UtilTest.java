package allout58.util.SiteUtils.test; /**
 * Created by James Hollowell on 3/23/2015.
 */

import allout58.util.SiteUtils.Utils;
import org.junit.Assert;
import org.junit.Test;

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
}
