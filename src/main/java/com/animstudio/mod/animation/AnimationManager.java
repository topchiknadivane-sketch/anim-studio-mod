package com.animstudio.mod.animation;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AnimationManager {

    public static final Map<UUID, EntityAnimation> animations = new HashMap<>();
    public static boolean isRecording = false;
    public static UUID recordingTarget = null;
    private static long recordStart = 0;

    public static void init() {
        animations.clear();
    }

    public static void startRecording(UUID entityId) {
        isRecording = true;
        recordingTarget = entityId;
        recordStart = System.currentTimeMillis();
        if (!animations.containsKey(entityId)) {
            animations.put(entityId, new EntityAnimation());
        } else {
            animations.get(entityId).keyframes.clear();
        }
    }

    public static void stopRecording() {
        isRecording = false;
        recordingTarget = null;
    }

    public static void recordFrame(Entity entity) {
        if (!isRecording || recordingTarget == null) return;
        if (!entity.getUniqueID().equals(recordingTarget)) return;
        EntityAnimation anim = animations.get(recordingTarget);
        if (anim == null) return;
        long time = System.currentTimeMillis() - recordStart;
        Keyframe kf = new Keyframe(time, entity.posX, entity.posY, entity.posZ,
                entity.rotationYaw, entity.rotationPitch);
        anim.keyframes.add(kf);
    }

    public static void playAnimation(Entity entity, float speed) {
        EntityAnimation anim = animations.get(entity.getUniqueID());
        if (anim == null) return;
        anim.playing = true;
        anim.speed = speed;
        anim.playStartTime = System.currentTimeMillis();
        anim.currentFrame = 0;
    }

    public static void tickAnimations(List<Entity> entities) {
        for (Entity entity : entities) {
            EntityAnimation anim = animations.get(entity.getUniqueID());
            if (anim == null || !anim.playing) continue;
            long elapsed = (long) ((System.currentTimeMillis() - anim.playStartTime) * anim.speed);
            Keyframe kf = anim.getFrameAtTime(elapsed);
            if (kf != null) {
                entity.setPositionAndRotation(kf.x, kf.y, kf.z, kf.yaw, kf.pitch);
            } else {
                anim.playing = false;
            }
        }
    }

    public static class EntityAnimation {
        public List<Keyframe> keyframes = new ArrayList<>();
        public boolean playing = false;
        public float speed = 1.0f;
        public long playStartTime = 0;
        public int currentFrame = 0;

        public Keyframe getFrameAtTime(long timeMs) {
            for (int i = keyframes.size() - 1; i >= 0; i--) {
                if (keyframes.get(i).timeMs <= timeMs) {
                    return keyframes.get(i);
                }
            }
            return keyframes.isEmpty() ? null : keyframes.get(0);
        }
    }

    public static class Keyframe {
        public long timeMs;
        public double x, y, z;
        public float yaw, pitch;

        public Keyframe(long timeMs, double x, double y, double z, float yaw, float pitch) {
            this.timeMs = timeMs;
            this.x = x; this.y = y; this.z = z;
            this.yaw = yaw; this.pitch = pitch;
        }
    }
}
