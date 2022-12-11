/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package cascade.util.render;

import cascade.Cascade;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f), (int)(a * 255.0f));
    }

    public static Color rainbow(int delay, float satur, float bright) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), satur / 255.0f, bright / 255.0f);
    }

    public static Color alphaStep(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float)colors[0], (float)colors[1], (float)colors[2], (float)colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
        Color color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, (float)alpha / 255.0f);
        if (entity instanceof EntityPlayer && colorFriends && Cascade.friendManager.isFriend((EntityPlayer)entity)) {
            color = new Color(0.33333334f, 1.0f, 1.0f, (float)alpha / 255.0f);
        }
        return color;
    }

    public static Color interpolate(float value, Color start, Color end) {
        float sr = (float)start.getRed() / 255.0f;
        float sg = (float)start.getGreen() / 255.0f;
        float sb = (float)start.getBlue() / 255.0f;
        float sa = (float)start.getAlpha() / 255.0f;
        float er = (float)end.getRed() / 255.0f;
        float eg = (float)end.getGreen() / 255.0f;
        float eb = (float)end.getBlue() / 255.0f;
        float ea = (float)end.getAlpha() / 255.0f;
        float r = sr * value + er * (1.0f - value);
        float g = sg * value + eg * (1.0f - value);
        float b = sb * value + eb * (1.0f - value);
        float a = sa * value + ea * (1.0f - value);
        return new Color(r, g, b, a);
    }

    public static float[] toArray(int color) {
        return new float[]{(float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, (float)(color >> 24 & 0xFF) / 255.0f};
    }

    public static class ColorHolder {
        int r;
        int g;
        int b;
        int a;

        public ColorHolder(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = 255;
        }

        public ColorHolder(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        public ColorHolder brighter() {
            return new ColorHolder(Math.min(this.r + 10, 255), Math.min(this.g + 10, 255), Math.min(this.b + 10, 255), this.getA());
        }

        public ColorHolder darker() {
            return new ColorHolder(Math.max(this.r - 10, 0), Math.max(this.g - 10, 0), Math.max(this.b - 10, 0), this.getA());
        }

        public void setGLColour() {
            this.setGLColour(-1, -1, -1, -1);
        }

        public void setGLColour(int dr, int dg, int db, int da) {
            GL11.glColor4f((float)((float)(dr == -1 ? this.r : dr) / 255.0f), (float)((float)(dg == -1 ? this.g : dg) / 255.0f), (float)((float)(db == -1 ? this.b : db) / 255.0f), (float)((float)(da == -1 ? this.a : da) / 255.0f));
        }

        public void becomeHex(int hex) {
            this.setR((hex & 0xFF0000) >> 16);
            this.setG((hex & 0xFF00) >> 8);
            this.setB(hex & 0xFF);
            this.setA(255);
        }

        public static ColorHolder fromHex(int hex) {
            ColorHolder n = new ColorHolder(0, 0, 0);
            n.becomeHex(hex);
            return n;
        }

        public static int toHex(int r, int g, int b) {
            return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        }

        public int toHex() {
            return ColorHolder.toHex(this.r, this.g, this.b);
        }

        public int getB() {
            return this.b;
        }

        public int getG() {
            return this.g;
        }

        public int getR() {
            return this.r;
        }

        public int getA() {
            return this.a;
        }

        public ColorHolder setR(int r) {
            this.r = r;
            return this;
        }

        public ColorHolder setB(int b) {
            this.b = b;
            return this;
        }

        public ColorHolder setG(int g) {
            this.g = g;
            return this;
        }

        public ColorHolder setA(int a) {
            this.a = a;
            return this;
        }

        public ColorHolder clone() {
            return new ColorHolder(this.r, this.g, this.b, this.a);
        }

        public Color toJavaColor() {
            return new Color(this.r, this.g, this.b, this.a);
        }
    }
}

