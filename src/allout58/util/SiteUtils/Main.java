package allout58.util.SiteUtils;

import allout58.util.SiteUtils.api.IModule;
import allout58.util.SiteUtils.api.ModuleManager;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            System.out.println("Modules:");
            for (String n : ModuleManager.getInstance().getAllNames())
                System.out.println(n);
        }
        else
        {
            OptionParser parser = new OptionParser();
            OptionSpec helpOpt = parser.acceptsAll(Arrays.asList("?", "h", "help"), "show module specific options").forHelp();
            parser.accepts("site", "Site to work with.").withRequiredArg().ofType(String.class);

            if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("help"))
            {
                doHelp(parser);
            }
            else if (args[0].equalsIgnoreCase("list"))
            {
                System.out.println("Modules:");
                for (String n : ModuleManager.getInstance().getAllNames())
                    System.out.println(n);
            }
            else
            {
                IModule module = ModuleManager.getInstance().getModuleByName(args[0]);
                if (module == null)
                {
                    System.err.println("Error finding module " + args[0]);
                }
                else
                {
                    module.addOptionAcceptors(parser);
                    OptionSet options = parser.parse(Arrays.copyOfRange(args, 1, args.length));

                    if (options.has(helpOpt))
                    {
                        doHelp(parser);
                    }
                    else
                    {
                        module.parseOptions(options);
                    }

                }
            }
        }
    }

    private static void doHelp(OptionParser parser)
    {
        try
        {
            System.out.println("Usage: websiteutils <moduleName> [options]  - run module <moduleName> with options [options]");
            System.out.println("       websiteutils list   - lists all modules");
            System.out.println("       websiteutils help   - displays general help only.");

            System.out.println();
            parser.printHelpOn(System.out);
        }
        catch (IOException ignored)
        {
        }
    }

}
