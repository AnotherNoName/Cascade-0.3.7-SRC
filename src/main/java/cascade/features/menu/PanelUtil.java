//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.menu;

import cascade.features.menu.Quad;
import cascade.features.modules.core.Menu;
import cascade.util.Util;
import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class PanelUtil
implements Util {
    static Tessellator tessellator = Tessellator.getInstance();
    static BufferBuilder builder = tessellator.getBuffer();
    static float hue = 0.0f;

    public static Quad renderBox(Quad quad) {
        PanelUtil.drawBox(quad, new Color(26, 26, 26, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(45, 45, 45, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(51, 51, 51, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(40, 40, 40, Menu.getInstance().opacity.getValue()));
        quad.shrink(1.5f);
        PanelUtil.drawBox(quad, new Color(51, 51, 51, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(45, 45, 45, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(Menu.getInstance().getColor().getRed(), Menu.getInstance().getColor().getGreen(), Menu.getInstance().getColor().getBlue(), Menu.getInstance().getColor().getAlpha()));
        quad.shrink(0.5f);
        PanelUtil.drawBox(quad, new Color(16, 16, 16, Menu.getInstance().opacity.getValue()));
        quad.shrink(0.5f);
        return quad;
    }

    static void drawBox(Quad quad, Color color) {
        PanelUtil.setup();
        double x = Math.min(quad.getX(), quad.getX1());
        double y = Math.min(quad.getY(), quad.getY1());
        double x1 = Math.max(quad.getX(), quad.getX1());
        double y1 = Math.max(quad.getY(), quad.getY1());
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x1, y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(x, y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(x, y1, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(x1, y1, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();
        PanelUtil.end();
    }

    static void drawRainbowX(Quad quad, float hue, float sat, float bright, float speed, int alpha) {
        PanelUtil.drawRainbowX(quad, hue, sat, bright, speed, 0.5f, alpha);
    }

    static void drawRainbowX(Quad quad, float hue, float sat, float bright, float speed, float pixelSpeed, int alpha) {
        PanelUtil.update(hue);
        for (float a = quad.getX() + pixelSpeed; a <= quad.getX1(); a += pixelSpeed) {
            PanelUtil.update(speed);
            Color color = PanelUtil.getColor(0.0f, sat, bright);
            PanelUtil.drawBox(new Quad(a - pixelSpeed, quad.getY(), a, quad.getY1()), PanelUtil.alpha(alpha, color));
        }
    }

    static Color alpha(int alpha, Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    static void setup() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
    }

    static void setupGradient() {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)6406);
        GL11.glDisable((int)2929);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GL11.glDisable((int)2884);
        GlStateManager.shadeModel((int)7425);
    }

    static void endGradient() {
        GL11.glEnable((int)2929);
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)6406);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    static void end() {
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    static void update(float val) {
        hue += val % 360.0f / 360.0f;
    }

    Color getColor() {
        return new Color(Color.HSBtoRGB(hue, 1.0f, 1.0f));
    }

    Color getColor(float off) {
        return new Color(Color.HSBtoRGB((hue + off) % 360.0f, 1.0f, 1.0f));
    }

    static Color getColor(float off, float sat, float bright) {
        return new Color(Color.HSBtoRGB((hue + off) % 360.0f, sat, bright));
    }

    float getHue() {
        return hue;
    }

    float getHueMultiplied() {
        return hue * 360.0f;
    }

    void setHue(float hue) {
        PanelUtil.hue = hue;
    }

    static float transform(float max, float val) {
        float f0 = val / max;
        return f0 * 1.0f;
    }

    static Color getColorStatic(int alpha) {
        Color color = new Color(Color.HSBtoRGB(PanelUtil.transform(6500.0f, System.currentTimeMillis() % 6500L), 1.0f, 1.0f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    static Color getColorStatic(float off, float speed, int alpha) {
        return new Color(Color.HSBtoRGB(PanelUtil.transform((long)(6500.0f / speed), (System.currentTimeMillis() + (long)off) % (long)(6500.0f / speed)), 1.0f, 1.0f));
    }

    static Color getColorStatic(float off, float speed, float sat, float bright, int alpha) {
        Color color = new Color(Color.HSBtoRGB(PanelUtil.transform((long)(6500.0f / speed), (System.currentTimeMillis() + (long)off) % (long)(6500.0f / speed)), sat, bright));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}

