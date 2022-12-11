//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.modules.visual;

import cascade.Cascade;
import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.HoleUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HoleESP
extends Module {
    Setting<Boolean> glint = this.register(new Setting<Boolean>("Glint", true));
    Setting<Float> range = this.register(new Setting<Float>("Range", Float.valueOf(8.4f), Float.valueOf(1.0f), Float.valueOf(10.0f)));
    Setting<Boolean> aroundEnemies = this.register(new Setting<Boolean>("AroundEnemies", false));
    Setting<Float> enemyRange = this.register(new Setting<Object>("EnemyRange", Float.valueOf(3.5f), Float.valueOf(1.0f), Float.valueOf(10.0f), v -> this.aroundEnemies.getValue()));
    Setting<Boolean> box = this.register(new Setting<Boolean>("Box", true));
    Setting<Color> boxC = this.register(new Setting<Object>("BoxColor", new Color(-1), v -> this.box.getValue()));
    Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    Setting<Color> outC = this.register(new Setting<Object>("OutColor", new Color(-1), v -> this.outline.getValue()));
    Setting<Boolean> cross = this.register(new Setting<Boolean>("Cross", false));
    Setting<Color> crossC = this.register(new Setting<Object>("CrossColor", new Color(-1), v -> this.cross.getValue()));
    Setting<Float> lw = this.register(new Setting<Object>("LineWidth", Float.valueOf(0.1f), Float.valueOf(0.1f), Float.valueOf(6.0f), v -> this.outline.getValue() != false || this.cross.getValue() != false));
    Setting<Double> height = this.register(new Setting<Object>("Height", Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(1.0), v -> this.box.getValue() != false || this.outline.getValue() != false));
    static ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    List<HoleUtil.Hole> holes = new ArrayList<HoleUtil.Hole>();
    static HoleESP INSTANCE;

    public HoleESP() {
        super("HoleESP", Module.Category.VISUAL, "");
        INSTANCE = this;
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (HoleESP.fullNullCheck()) {
            return;
        }
        this.holes = HoleUtil.getHoles(this.range.getValue().floatValue(), HoleESP.mc.player.getPosition(), true);
        if (this.aroundEnemies.getValue().booleanValue()) {
            for (EntityPlayer en : HoleESP.mc.world.playerEntities) {
                if (Cascade.friendManager.isFriend(en) || en == HoleESP.mc.player || en.isDead) continue;
                this.holes.addAll(HoleUtil.getHoles(this.enemyRange.getValue().floatValue(), en.getPosition(), true));
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (HoleESP.mc.renderViewEntity == null) {
            return;
        }
        for (HoleUtil.Hole hole : this.holes) {
            if (this.glint.getValue().booleanValue()) {
                GL11.glBlendFunc((int)770, (int)32772);
                mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
            }
            Color boxColor = new Color(this.boxC.getValue().getRed(), this.boxC.getValue().getGreen(), this.boxC.getValue().getBlue(), this.boxC.getValue().getAlpha());
            Color outlineColor = new Color(this.outC.getValue().getRed(), this.outC.getValue().getGreen(), this.outC.getValue().getBlue(), this.outC.getValue().getAlpha());
            Color crossColor = new Color(this.crossC.getValue().getRed(), this.crossC.getValue().getGreen(), this.crossC.getValue().getBlue(), this.crossC.getValue().getAlpha());
            RenderUtil.drawHoleESP(hole.pos1, this.box.getValue(), this.outline.getValue(), this.cross.getValue(), boxColor, outlineColor, crossColor, this.lw.getValue().floatValue(), this.height.getValue());
            if (hole.doubleHole) {
                RenderUtil.drawHoleESP(hole.pos2, this.box.getValue(), this.outline.getValue(), this.cross.getValue(), boxColor, outlineColor, crossColor, this.lw.getValue().floatValue(), this.height.getValue());
            }
            if (!this.glint.getValue().booleanValue()) continue;
            mc.getTextureManager().deleteTexture(RES_ITEM_GLINT);
        }
    }
}

