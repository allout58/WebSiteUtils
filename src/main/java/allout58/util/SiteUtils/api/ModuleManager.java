package allout58.util.SiteUtils.api;

import allout58.util.SiteUtils.builtin.BrokenLinksModule;
import allout58.util.SiteUtils.builtin.UnusedCSSModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 3/16/2015.
 */
public class ModuleManager
{
    private static ModuleManager instance;

    private Map<String, IModule> moduleMap;

    protected ModuleManager()
    {
        moduleMap = new HashMap<>();
        registerBuiltin();
    }

    public static ModuleManager getInstance()
    {
        if (instance == null)
            instance = new ModuleManager();
        return instance;
    }

    /**
     * Register a module with a specified name.
     *
     * @param name   Name to register module with
     * @param module Module to register
     */
    public void registerModule(String name, IModule module)
    {
        moduleMap.put(name, module);
    }

    /**
     * Find a registered module by name. Returns null if not found.
     *
     * @param name Name of module to find.
     * @return Module named <code>name</code>
     */
    public IModule getModuleByName(String name)
    {
        return moduleMap.get(name);
    }

    /**
     * Get all the registered modules from this manager.
     *
     * @return All modules
     */
    public IModule[] getAllModules()
    {
        return moduleMap.values().toArray(new IModule[moduleMap.values().size()]);
    }

    /**
     * Get all the registered names from this manager.
     *
     * @return All Module names
     */
    public String[] getAllNames()
    {
        return moduleMap.keySet().toArray(new String[moduleMap.keySet().size()]);
    }

    protected void registerBuiltin()
    {
        registerModule("BrokenLinks", new BrokenLinksModule());
        registerModule("UnusedCSS", new UnusedCSSModule());
    }
}
