package com.tombstone.gui;

import com.tombstone.handler.ConfigurationHandler;
import com.tombstone.reference.Reference;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class ModGuiConfig extends GuiConfig {

//    public ModGuiConfig(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
    public ModGuiConfig(GuiScreen guiScreen) {
//        super(parentScreen, configElements, modID, configID, allRequireWorldRestart, allRequireMcRestart, title);
        super(guiScreen,
                new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),   // configElements
                Reference.MOD_ID,                                                                                                       // modID
//                Configuration.CATEGORY_GENERAL,                                                                                       // configID
                false,                                                                                                                  // allRequireWorldRestart
                false,                                                                                                                  // allRequireMcRestart
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));                                        // title
    }
}
