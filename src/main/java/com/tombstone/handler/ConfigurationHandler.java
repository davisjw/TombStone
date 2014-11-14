package com.tombstone.handler;

import com.tombstone.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler
{
    public static Configuration configuration;

    public static boolean canCraft = true;          // Allow crafting tombstones
    public static boolean allowEmpty = true;        // Allow empty tombstones to be created on death
    public static boolean allowGUI = false;         // Allow access to GUI or break-only
    public static String mineLevel = "stone";       // What mining level is allow to harvest the tombstones
    public static String dateFormat = "m/d/y";      // Date format for tombstones; m/d/y (US), y-m-d (nonUS)
    public static int security = 0;                 // Looting Security level: 0=anyone, 1=same team, 2=owner only

    /** Date Format for tombstone:
     * m/d/y (US), y-m-d (nonUS), (any other combination is possible)
     */

    public static void init(File configFile) {
        if (configuration == null)
            configuration = new Configuration(configFile);
        loadConfiguration();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID))
        {
            // Re-read configuration
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        try {
            // Load config file
//            configuration.load();

            // Get properties
            canCraft = configuration.get(Configuration.CATEGORY_GENERAL, "can_craft_tombstones", canCraft, "if true, then decorative tombstones can be crafted").getBoolean(false);
            allowEmpty = configuration.get(Configuration.CATEGORY_GENERAL, "create_empty_tombstones", allowEmpty, "if true, then empty tombstones can be created").getBoolean(false);
            allowGUI = configuration.get(Configuration.CATEGORY_GENERAL, "allow_gui_access", allowGUI, "if true, then chest style gui available; otherwise break-only access").getBoolean(false);
            security = configuration.get(Configuration.CATEGORY_GENERAL, "security_level", security, "Security access to tombstone loot: 0=public, 1=team, 2=owner only").getInt();
            mineLevel = configuration.get(Configuration.CATEGORY_GENERAL, "harvest_level", mineLevel, "The harvest level required to break tombstones (ex. stone)").getString();
//            stringsList.add(new DummyConfigElement<String>("cycleString", "this", ConfigGuiType.STRING, "fml.config.sample.cycleString", new String[] {"this", "property", "cycles", "through", "a", "list", "of", "valid", "choices"}));

            dateFormat = configuration.get(Configuration.CATEGORY_GENERAL, "date_format", "m/d/y", "Format of date on tombstones. American style is m/d/y while international standard is y-m-d.  H:M:S is available").getString();
        } catch (Exception e) {
            // Log exceptions
        } finally {
            // Save configs
            if (configuration.hasChanged()) {
                configuration.save();
            }
        }
    }


}
