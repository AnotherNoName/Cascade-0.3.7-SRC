//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package cascade.features.menu;

import cascade.util.Util;
import net.minecraft.client.gui.ScaledResolution;

public class Quad
implements Util {
    private float x;
    private float y;
    private float x1;
    private float y1;

    public Quad() {
    }

    public Quad(float x, float y, float x1, float y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public boolean isWithinIncluding(float x, float y) {
        return x >= this.x && x <= this.x1 && y >= this.y && y <= this.y1;
    }

    public boolean isWithin(float x, float y) {
        return x > this.x && x < this.x1 && y > this.y && y < this.y1;
    }

    public boolean insideScreen() {
        return this.insideScreenX() && this.insideScreenY();
    }

    public boolean insideScreenX() {
        ScaledResolution res = new ScaledResolution(mc);
        return this.x >= 0.0f && this.x1 <= (float)res.getScaledWidth();
    }

    public boolean insideScreenY() {
        ScaledResolution res = new ScaledResolution(mc);
        return this.y >= 0.0f && this.y <= (float)res.getScaledHeight();
    }

    public void shrink(float amount) {
        this.setX(this.getX() + amount);
        this.setY(this.getY() + amount);
        this.setX1(this.getX1() - amount);
        this.setY1(this.getY1() - amount);
    }

    public Quad clone() {
        return new Quad(this.x, this.y, this.x1, this.y1);
    }

    public float width() {
        return this.x1 - this.x;
    }

    public float height() {
        return this.y1 - this.y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX1() {
        return this.x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return this.y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }
}

