package com.animstudio.mod.client.gui;

import com.animstudio.mod.animation.AnimationManager;
import com.animstudio.mod.client.cinema.CinematicMode;
import com.animstudio.mod.entity.DummyPlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AnimStudioScreen extends GuiScreen {

    private static final int TAB_MOBS      = 0;
    private static final int TAB_SKINS     = 1;
    private static final int TAB_DUPLICATE = 2;
    private static final int TAB_ANIMATION = 3;

    private int currentTab = TAB_MOBS;
    private UUID selectedEntityId = null;
    private float animSpeed = 1.0f;

    private static final List<String> POPULAR_SKINS = Arrays.asList(
        "Notch", "Jeb_", "Dream", "Technoblade", "Ph1LzA",
        "Skeppy", "BadBoyHalo", "Tubbo", "TommyInnit", "Wilbur"
    );

    @Override
    public void initGui() {
        super.initGui();
        buildButtons();
    }

    private void buildButtons() {
        buttonList.clear();

        int tabY = 10;
        int tabW = 100;
        buttonList.add(new GuiButton(10, width / 2 - 210, tabY, tabW, 20, "Мобы"));
        buttonList.add(new GuiButton(11, width / 2 - 105, tabY, tabW, 20, "Скины"));
        buttonList.add(new GuiButton(12, width / 2,       tabY, tabW, 20, "Дубль"));
        buttonList.add(new GuiButton(13, width / 2 + 105, tabY, tabW, 20, "Анимация"));

        int startY = 50;

        if (currentTab == TAB_MOBS) {
            String[] mobs = {"Creeper", "Zombie", "Skeleton", "Spider", "Enderman",
                             "Cow", "Pig", "Sheep", "Chicken", "Wolf"};
            for (int i = 0; i < mobs.length; i++) {
                int col = i % 2;
                int row = i / 2;
                buttonList.add(new GuiButton(100 + i,
                    width / 2 - 110 + col * 115, startY + row * 25,
                    110, 20, mobs[i]));
            }
        }

        if (currentTab == TAB_SKINS) {
            for (int i = 0; i < POPULAR_SKINS.size(); i++) {
                int col = i % 2;
                int row = i / 2;
                buttonList.add(new GuiButton(200 + i,
                    width / 2 - 110 + col * 115, startY + row * 25,
                    110, 20, POPULAR_SKINS.get(i)));
            }
        }

        if (currentTab == TAB_DUPLICATE) {
            buttonList.add(new GuiButton(300, width / 2 - 100, startY, 200, 24,
                "Дублировать себя"));
            buttonList.add(new GuiButton(301, width / 2 - 100, startY + 35, 200, 24,
                "Удалить все дубли"));
        }

        if (currentTab == TAB_ANIMATION) {
            String recLabel = AnimationManager.isRecording ? "Стоп запись" : "Начать запись";
            buttonList.add(new GuiButton(400, width / 2 - 105, startY,      100, 22, recLabel));
            buttonList.add(new GuiButton(401, width / 2,       startY,      100, 22, "Воспроизвести"));
            buttonList.add(new GuiButton(402, width / 2 - 105, startY + 30, 100, 22, "Скорость -"));
            buttonList.add(new GuiButton(403, width / 2,       startY + 30, 100, 22, "Скорость +"));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Minecraft mc = Minecraft.getMinecraft();

        switch (button.id) {
            case 10: currentTab = TAB_MOBS;      buildButtons(); return;
            case 11: currentTab = TAB_SKINS;     buildButtons(); return;
            case 12: currentTab = TAB_DUPLICATE; buildButtons(); return;
            case 13: currentTab = TAB_ANIMATION; buildButtons(); return;
        }

        if (button.id >= 100 && button.id < 200) {
            spawnMob(button.id - 100, mc);
        }
        if (button.id >= 200 && button.id < 300) {
            spawnSkinDummy(POPULAR_SKINS.get(button.id - 200), mc);
        }
        if (button.id == 300) {
            duplicatePlayer(mc);
        }
        if (button.id == 301) {
            removeAllDummies(mc);
        }

        if (button.id == 400) {
            if (AnimationManager.isRecording) {
                AnimationManager.stopRecording();
            } else {
                if (selectedEntityId != null) {
                    AnimationManager.startRecording(selectedEntityId);
                }
            }
            buildButtons();
        }
        if (button.id == 401) {
            if (selectedEntityId != null) {
                for (Object obj : mc.theWorld.loadedEntityList) {
                    if (obj instanceof EntityLivingBase) {
                        EntityLivingBase e = (EntityLivingBase) obj;
                        if (e.getUniqueID().equals(selectedEntityId)) {
                            AnimationManager.playAnimation(e, animSpeed);
                            break;
                        }
                    }
                }
            }
        }
        if (button.id == 402) {
            animSpeed = Math.max(0.25f, animSpeed - 0.25f);
        }
        if (button.id == 403) {
            animSpeed = Math.min(4.0f, animSpeed + 0.25f);
        }
    }

    private void spawnMob(int index, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        EntityLivingBase entity = null;
        double x = mc.thePlayer.posX + 2;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;
        switch (index) {
            case 0: entity = new EntityCreeper(mc.theWorld); break;
            case 1: entity = new EntityZombie(mc.theWorld);  break;
            case 2: entity = new EntitySkeleton(mc.theWorld);break;
            case 3: entity = new EntitySpider(mc.theWorld);  break;
            case 4: entity = new EntityEnderman(mc.theWorld);break;
            case 5: entity = new EntityCow(mc.theWorld);     break;
            case 6: entity = new EntityPig(mc.theWorld);     break;
            case 7: entity = new EntitySheep(mc.theWorld);   break;
            case 8: entity = new EntityChicken(mc.theWorld); break;
            case 9: entity = new EntityWolf(mc.theWorld);    break;
        }
        if (entity != null) {
            entity.setPosition(x, y, z);
            mc.theWorld.spawnEntityInWorld(entity);
            selectedEntityId = entity.getUniqueID();
        }
    }

    private void spawnSkinDummy(String skinName, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        DummyPlayerEntity dummy = new DummyPlayerEntity(mc.theWorld, skinName);
        dummy.setPosition(
            mc.thePlayer.posX + 2,
            mc.thePlayer.posY,
            mc.thePlayer.posZ
        );
        mc.theWorld.spawnEntityInWorld(dummy);
        selectedEntityId = dummy.getUniqueID();
    }

    private void duplicatePlayer(Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        DummyPlayerEntity dummy = new DummyPlayerEntity(mc.theWorld, mc.thePlayer);
        mc.theWorld.spawnEntityInWorld(dummy);
        selectedEntityId = dummy.getUniqueID();
    }

    private void removeAllDummies(Minecraft mc) {
        if (mc.theWorld == null) return;
        List<Object> toRemove = new java.util.ArrayList<>();
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof DummyPlayerEntity) {
                toRemove.add(obj);
            }
        }
        for (Object obj : toRemove) {
            ((DummyPlayerEntity) obj).setDead();
        }
        selectedEntityId = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int panelW = 260;
        int panelH = 260;
        int panelX = (width - panelW) / 2;
        int panelY = 35;
        drawRect(panelX, panelY, panelX + panelW, panelY + panelH, 0xCC1a1a2e);

        String title = "§b§lAnim Studio";
        fontRendererObj.drawStringWithShadow(title, (width - fontRendererObj.getStringWidth("Anim Studio")) / 2, panelY + 5, 0xFFFFFF);

        if (currentTab == TAB_ANIMATION) {
            int infoY = panelY + panelH - 50;
            String speedStr = "§7Скорость: §f" + String.format("%.2f", animSpeed) + "x";
            fontRendererObj.drawStringWithShadow(speedStr, panelX + 10, infoY, 0xFFFFFF);
            if (selectedEntityId != null) {
                String selStr = "§7Выбрано: §a" + selectedEntityId.toString().substring(0, 8) + "...";
                fontRendererObj.drawStringWithShadow(selStr, panelX + 10, infoY + 12, 0xFFFFFF);
            } else {
                fontRendererObj.drawStringWithShadow("§cСначала заспавни моба!", panelX + 10, infoY + 12, 0xFFFFFF);
            }
            if (AnimationManager.isRecording) {
                fontRendererObj.drawStringWithShadow("§c● ЗАПИСЬ...", panelX + 10, infoY + 24, 0xFF0000);
            }
        }

        if (currentTab == TAB_DUPLICATE) {
            int infoY = panelY + panelH - 30;
            fontRendererObj.drawStringWithShadow(
                "§7Дубль появится рядом с тобой",
                panelX + 10, infoY, 0xAAAAAA
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
