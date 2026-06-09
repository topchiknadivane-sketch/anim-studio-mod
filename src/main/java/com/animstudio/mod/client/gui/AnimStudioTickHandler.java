package com.animstudio.mod.client.gui;

import com.animstudio.mod.animation.AnimationManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.List;

public class AnimStudioTickHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return;

        List<EntityLivingBase> entities = new ArrayList<>();
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityLivingBase) {
                EntityLivingBase e = (EntityLivingBase) obj;
                if (AnimationManager.isRecording) {
                    AnimationManager.recordFrame(e);
                }
                entities.add(e);
            }
        }
        AnimationManager.tickAnimations(entities);
    }
}
