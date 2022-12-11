//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.modules.visual;

import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.CalcUtil;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP
extends Module {
    Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    Setting<Integer> entityCap = this.register(new Setting<Integer>("EntityCap", 50, 10, 200));
    Setting<Boolean> xpBottles = this.register(new Setting<Boolean>("XpBottle", true));
    Setting<Boolean> small = this.register(new Setting<Object>("Small", Boolean.valueOf(false), v -> this.xpBottles.getValue()));
    Setting<Color> xpBotC = this.register(new Setting<Object>("BottleColor", new Color(-1), v -> this.xpBottles.getValue()));
    Setting<Color> xpBotCBox = this.register(new Setting<Object>("BottleColorBox", new Color(-1), v -> this.xpBottles.getValue()));
    Setting<Boolean> xpOrbs = this.register(new Setting<Boolean>("XpOrb", false));
    Setting<Color> xpOrbsC = this.register(new Setting<Object>("OrbColor", new Color(-1), v -> this.xpOrbs.getValue()));
    Setting<Color> xpOrbsCBox = this.register(new Setting<Object>("OrbColorBox", new Color(-1), v -> this.xpOrbs.getValue()));
    Setting<Boolean> pearl = this.register(new Setting<Boolean>("Pearl", true));
    Setting<Color> pearlC = this.register(new Setting<Object>("PearlColor", new Color(-1), v -> this.pearl.getValue()));
    Setting<Color> pearlCBox = this.register(new Setting<Object>("PearlColorBox", new Color(-1), v -> this.pearl.getValue()));
    static ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public ESP() {
        super("ESP", Module.Category.VISUAL, "");
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (ESP.fullNullCheck()) {
            return;
        }
        this.doRender();
    }

    void doRender() {
        int i = 0;
        for (Entity en : ESP.mc.world.loadedEntityList) {
            if (!(en instanceof EntityXPOrb && this.xpOrbs.getValue() != false || en instanceof EntityExpBottle && this.xpBottles.getValue() != false) && (!(en instanceof EntityEnderPearl) || !this.pearl.getValue().booleanValue() || !(CalcUtil.getDistance(en) < 300.0))) continue;
            Color color = new Color(-1);
            Color colorBox = new Color(-1);
            if (en instanceof EntityXPOrb && this.xpOrbs.getValue().booleanValue()) {
                color = this.xpOrbsC.getValue();
                colorBox = this.xpOrbsCBox.getValue();
            }
            if (en instanceof EntityExpBottle && this.xpBottles.getValue().booleanValue()) {
                color = this.xpBotC.getValue();
                colorBox = this.xpBotCBox.getValue();
            }
            if (en instanceof EntityEnderPearl && this.pearl.getValue().booleanValue()) {
                color = this.pearlC.getValue();
                colorBox = this.pearlCBox.getValue();
            }
            Vec3d interp = this.getInterpolatedRenderPos(en, mc.getRenderPartialTicks());
            boolean smallXp = this.xpBottles.getValue() != false && this.small.getValue() != false && en instanceof EntityExpBottle;
            AxisAlignedBB bb = smallXp ? new AxisAlignedBB(en.getEntityBoundingBox().minX + 1.0 - en.posX + interp.xCoord, en.getEntityBoundingBox().minY + 1.0 - en.posY + interp.yCoord, en.getEntityBoundingBox().minZ + 1.0 - en.posZ + interp.zCoord, en.getEntityBoundingBox().maxX - 1.0 - en.posX + interp.xCoord, en.getEntityBoundingBox().maxY - 1.0 - en.posY + interp.yCoord, en.getEntityBoundingBox().maxZ - 1.0 - en.posZ + interp.zCoord) : new AxisAlignedBB(en.getEntityBoundingBox().minX - 0.05 - en.posX + interp.xCoord, en.getEntityBoundingBox().minY - en.posY + interp.yCoord, en.getEntityBoundingBox().minZ - 0.05 - en.posZ + interp.zCoord, en.getEntityBoundingBox().maxX + 0.05 - en.posX + interp.xCoord, en.getEntityBoundingBox().maxY + 0.1 - en.posY + interp.yCoord, en.getEntityBoundingBox().maxZ + 0.05 - en.posZ + interp.zCoord);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glBlendFunc((int)770, (int)32772);
            mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)1.0f);
            RenderUtil.drawFilledBox(bb, ColorUtil.toRGBA(colorBox));
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            RenderUtil.drawBlockOutline(bb, color, this.lineWidth.getValue().floatValue());
            if (++i < this.entityCap.getValue()) continue;
            break;
        }
    }

    Vec3d getInterpolatedAmount(Entity e, double x, double y, double z) {
        return new Vec3d((e.posX - e.lastTickPosX) * x, (e.posY - e.lastTickPosY) * y, (e.posZ - e.lastTickPosZ) * z);
    }

    Vec3d getInterpolatedAmount(Entity e, float pTicks) {
        return this.getInterpolatedAmount(e, pTicks, pTicks, pTicks);
    }

    Vec3d getInterpolatedPos(Entity e, float pTicks) {
        return new Vec3d(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ).add(this.getInterpolatedAmount(e, pTicks));
    }

    Vec3d getInterpolatedRenderPos(Entity e, float partialTicks) {
        return this.getInterpolatedPos(e, partialTicks).subtract(ESP.mc.getRenderManager().renderPosX, ESP.mc.getRenderManager().renderPosY, ESP.mc.getRenderManager().renderPosZ);
    }
}

