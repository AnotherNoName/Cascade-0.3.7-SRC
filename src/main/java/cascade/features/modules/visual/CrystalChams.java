//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.modules.visual;

import cascade.event.events.RenderLivingEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class CrystalChams
extends Module {
    Setting<Boolean> texture = this.register(new Setting<Boolean>("Texture", false));
    Setting<Color> hidden = this.register(new Setting<Color>("Hidden", new Color(863502591, true)));
    Setting<Color> visible = this.register(new Setting<Color>("Visible", new Color(863502591, true)));
    Setting<Boolean> glint = this.register(new Setting<Boolean>("Glint", false));
    Setting<Float> rotations = this.register(new Setting<Float>("Rotations", Float.valueOf(0.8f), Float.valueOf(0.0f), Float.valueOf(5.0f)));
    Setting<Double> x = this.register(new Setting<Double>("X", 0.8, 0.1, 2.0));
    Setting<Double> y = this.register(new Setting<Double>("Y", 0.7, 0.1, 2.0));
    Setting<Double> z = this.register(new Setting<Double>("Z", 0.8, 0.1, 2.0));
    static ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    static ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public CrystalChams() {
        super("CrystalChams", Module.Category.VISUAL, "");
    }

    @SubscribeEvent
    public void onRenderLivingBase(RenderLivingEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getEntity() instanceof EntityEnderCrystal && e.getEntity().world == CrystalChams.mc.world) {
            GL11.glPushMatrix();
            GL11.glPushAttrib((int)1048575);
            float rotation = (float)((EntityEnderCrystal)e.getEntity()).innerRotation + mc.getRenderPartialTicks();
            float rotationMoved = MathHelper.sin((float)(rotation * 0.2f)) / 2.0f + 0.5f;
            rotationMoved = (float)((double)rotationMoved + Math.pow(rotationMoved, 2.0));
            GL11.glScaled((double)this.x.getValue(), (double)this.y.getValue(), (double)this.z.getValue());
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)6406);
            GL11.glDepthMask((boolean)false);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)2929);
            if (this.glint.getValue().booleanValue()) {
                mc.getTextureManager().bindTexture(RES_ITEM_GLINT);
            }
            if (this.texture.getValue().booleanValue()) {
                mc.getTextureManager().bindTexture(ENDER_CRYSTAL_TEXTURES);
            }
            if (!this.texture.getValue().booleanValue()) {
                GlStateManager.enableTexture2D();
            }
            GL11.glTexCoord3d((double)1.0, (double)1.0, (double)1.0);
            GL11.glEnable((int)3553);
            GL11.glBlendFunc((int)768, (int)771);
            GL11.glBlendFunc((int)770, (int)32772);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)2.0f);
            GL11.glColor4f((float)((float)this.hidden.getValue().getRed() / 255.0f), (float)((float)this.hidden.getValue().getGreen() / 255.0f), (float)((float)this.hidden.getValue().getBlue() / 255.0f), (float)((float)this.hidden.getValue().getAlpha() / 255.0f));
            e.setCanceled(true);
            GL11.glColor4f((float)((float)this.visible.getValue().getRed() / 255.0f), (float)((float)this.visible.getValue().getGreen() / 255.0f), (float)((float)this.visible.getValue().getBlue() / 255.0f), (float)((float)this.visible.getValue().getAlpha() / 255.0f));
            GL11.glPolygonMode((int)1032, (int)2880);
            e.getModelBase().render(e.getEntity(), 0.0f, rotation * this.rotations.getValue().floatValue(), rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
            GL11.glPolygonMode((int)1032, (int)6913);
            e.getModelBase().render(e.getEntity(), 0.0f, rotation * this.rotations.getValue().floatValue(), rotationMoved * 0.2f, 0.0f, 0.0f, 0.0625f);
            if (!this.texture.getValue().booleanValue()) {
                GlStateManager.disableTexture2D();
            }
            GL11.glEnable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)6406);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}

