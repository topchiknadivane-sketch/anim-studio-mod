package com.animstudio.mod;

import com.animstudio.mod.client.keybind.KeyBindings;
import com.animstudio.mod.client.gui.AnimStudioScreen;
import com.animstudio.mod.client.cinema.CinematicMode;
import com.animstudio.mod.animation.AnimationManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

@Mod(modid = AnimStudioMod.MODID, name = AnimStudioMod.NAME, version = AnimStudioMod.VERSION)
public class AnimStudioMod {

    public static final String MODID = "animstudio";
    public static final String NAME = "Anim Studio";
    public static final String VERSION = "1.0.0";

    public static Logger logger;

    @Mod.Instance(MODID)
    public static AnimStudioMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        KeyBindings.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        FMLCommonHandler.instance().bus().register(CinematicMode.instance);
        AnimationManager.init();
        logger.info("Anim Studio loaded!");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.openStudio.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(new AnimStudioScreen());
            }
        }
        if (KeyBindings.toggleCinematic.isPressed()) {
            CinematicMode.instance.toggle();
        }
    }
}
