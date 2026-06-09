package com.animstudio.mod.client.cinema;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import org.lwjgl.input.Mouse;

public class CinematicMode {

    public static final CinematicMode instance = new CinematicMode();

    private boolean active = false;
    private float flySpeed = 0.15f;
    private float smoothYaw = 0;
    private float smoothPitch = 0;
    private static final float SMOOTH_FACTOR = 0.08f;

    public void toggle() {
        active = !active;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (active) {
            mc.thePlayer.capabilities.allowFlying = true;
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.sendPlayerAbilities();
            mc.gameSettings.showDebugInfo = false;
        } else {
            if (!mc.thePlayer.capabilities.isCreativeMode) {
                mc.thePlayer.capabilities.allowFlying = false;
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.sendPlayerAbilities();
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!active) return;
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        EntityClientPlayerMP player = mc.thePlayer;

        smoothYaw = smoothYaw + (player.rotationYaw - smoothYaw) * SMOOTH_FACTOR;
        smoothPitch = smoothPitch + (player.rotationPitch - smoothPitch) * SMOOTH_FACTOR;
        player.rotationYaw = smoothYaw;
        player.rotationPitch = smoothPitch;

        player.capabilities.flySpeed = flySpeed;
        player.sendPlayerAbilities();
    }

    public void setFlySpeed(float speed) {
        this.flySpeed = speed;
    }

    public float getFlySpeed() {
        return flySpeed;
    }
}
