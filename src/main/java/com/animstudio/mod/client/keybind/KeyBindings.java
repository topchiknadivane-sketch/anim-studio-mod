package com.animstudio.mod.client.keybind;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    public static KeyBinding openStudio;
    public static KeyBinding toggleCinematic;

    public static void register() {
        openStudio = new KeyBinding(
            "key.animstudio.open",
            Keyboard.KEY_NONE,
            "key.categories.animstudio"
        );
        toggleCinematic = new KeyBinding(
            "key.animstudio.cinematic",
            Keyboard.KEY_NONE,
            "key.categories.animstudio"
        );
        ClientRegistry.registerKeyBinding(openStudio);
        ClientRegistry.registerKeyBinding(toggleCinematic);
    }
}
