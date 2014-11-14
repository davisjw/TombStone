package com.tombstone.reference;

import com.tombstone.utility.ResourceLocationHelper;
import net.minecraft.util.ResourceLocation;

public class Textures {
    public static final String RESOURCE_PREFIX = Reference.MOD_ID.toLowerCase() + ":";

    // Base file paths

    public static final class Model {
        private static final String MODEL_TEXTURE_LOCATION = "textures/models/";
        // Model textures
        //public static final ResourceLocation MODEL_NAME = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "model.png");
        public static final ResourceLocation MODEL_TOMBSTONE_1 = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "tombstone.png");
        public static final ResourceLocation MODEL_TOMBSTONE_2 = ResourceLocationHelper.getResourceLocation(MODEL_TEXTURE_LOCATION + "tombstone2.png");
    }

    public static final class Armor {
        public static final String ARMOR_SHEET_LOCATION = "textures/armor/";
        // Armor textures
        //public static final ResourceLocation ARMOR_NAME = ResourceLocationHelper.getResourceLocation(ARMOR_SHEET_LOCATION + "armor.png");
    }

    public static final class Gui {
        public static final String GUI_SHEET_LOCATION = "textures/gui/";
        // GUI textures
        //public static final ResourceLocation GUI_NAME = ResourceLocationHelper.getResourceLocation(GUI_SHEET_LOCATION + "gui.png");
    }

    public static final class Effect {
        public static final String EFFECTS_LOCATION = "textures/effects/";
        // Effects textures
        //public static final ResourceLocation EFFECT_NAME = ResourceLocationHelper.getResourceLocation(EFFECTS_LOCATION + "effect.png");
    }

    public static final class Misc {
        // Item/Block sprite sheets
        //public static final ResourceLocation VANILLA_BLOCK_TEXTURE_SHEET = TextureMap.locationBlocksTexture;
        //public static final ResourceLocation VANILLA_ITEM_TEXTURE_SHEET = TextureMap.locationItemsTexture;
        //public static final ResourceLocation GUI_PORTABLE_CRAFTING = new ResourceLocation("textures/gui/container/crafting");
        public static final ResourceLocation TOMBSTONE_UI = ResourceLocationHelper.getResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    }
}
