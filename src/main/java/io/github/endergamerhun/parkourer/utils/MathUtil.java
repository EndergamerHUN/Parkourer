package io.github.endergamerhun.parkourer.utils;

import org.bukkit.util.Vector;

public class MathUtil {
    private static final double DEG_TO_RAD = Math.PI / 180;

    public static Vector vectorFromYawAndPitch(double yaw, double pitch) {
        yaw = (yaw + 90) % 360;
        pitch = -pitch;
        double y = Math.sin(pitch * DEG_TO_RAD);
        double div = Math.cos(pitch * DEG_TO_RAD);
        double x = Math.cos(yaw * DEG_TO_RAD);
        double z = Math.sin(yaw * DEG_TO_RAD);
        x *= div;
        z *= div;
        return new Vector(x, y, z);
    }

    public static float clamp(float min, float val, float max) {
        return Math.max(min, Math.min(val, max));
    }
    public static int clamp(int min, int val, int max) {
        return Math.max(min, Math.min(val, max));
    }
}
