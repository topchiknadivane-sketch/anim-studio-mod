package com.animstudio.mod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DummyPlayerEntity extends EntityLiving {

    public String skinOwner = "";
    public boolean isPlayerDuplicate = false;

    public DummyPlayerEntity(World world) {
        super(world);
        this.setSize(0.6f, 1.8f);
        this.noClip = false;
    }

    public DummyPlayerEntity(World world, EntityPlayer source) {
        this(world);
        this.setPosition(source.posX, source.posY, source.posZ);
        this.rotationYaw = source.rotationYaw;
        this.rotationPitch = source.rotationPitch;
        this.skinOwner = source.getGameProfile().getName();
        this.isPlayerDuplicate = true;
    }

    public DummyPlayerEntity(World world, String skinName) {
        this(world);
        this.skinOwner = skinName;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setString("skinOwner", skinOwner);
        tag.setBoolean("isPlayerDuplicate", isPlayerDuplicate);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        skinOwner = tag.getString("skinOwner");
        isPlayerDuplicate = tag.getBoolean("isPlayerDuplicate");
    }

    @Override
    protected String getLivingSound() { return null; }
    @Override
    protected String getHurtSound() { return null; }
    @Override
    protected String getDeathSound() { return null; }
    @Override
    protected float getSoundVolume() { return 0; }
}
