//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Profile
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package cascade.features.modules.visual;

import cascade.Cascade;
import cascade.event.events.RenderLivingEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Color;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Chams
extends Module {
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Fill));
    Setting<Boolean> self = this.register(new Setting<Boolean>("Self", false));
    Setting<Boolean> texture = this.register(new Setting<Boolean>("Texture", false));
    Setting<Color> hiddenColor = this.register(new Setting<Color>("Hidden", new Color(863502591, true)));
    Setting<Color> visibleColor = this.register(new Setting<Color>("Visible", new Color(863502591, true)));
    Setting<Color> hiddenFColor = this.register(new Setting<Color>("HiddenFriend", new Color(855687167, true)));
    Setting<Color> visibleFColor = this.register(new Setting<Color>("VisibleFriend", new Color(855687167, true)));
    Setting<Boolean> glint = this.register(new Setting<Boolean>("Glint", false));
    Setting<Boolean> custom = this.register(new Setting<Object>("Custom", Boolean.valueOf(false), v -> this.glint.getValue()));
    Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(20.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.glint.getValue()));
    Setting<Float> scale = this.register(new Setting<Object>("Scale", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(10.0f), v -> this.glint.getValue()));
    static ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    static ResourceLocation CUSTOM = new ResourceLocation("textures/playerGlint.png");
    static Chams INSTANCE;

    public Chams() {
        super("Chams", Module.Category.VISUAL, "");
        INSTANCE = this;
    }

    public static Chams getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onRenderLivingBase(RenderLivingEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getEntity() instanceof EntityPlayer && e.getEntity().world == Chams.mc.world) {
            if (!this.self.getValue().booleanValue() && e.getEntity() == Chams.mc.player) {
                return;
            }
            ((EntityPlayer)e.getEntity()).hurtTime = 0;
            GlStateManager.pushMatrix();
            GL11.glPushAttrib((int)1048575);
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.disableAlpha();
            boolean bipedLeftArmwear = false;
            boolean bipedRightArmwear = false;
            boolean bipedLeftLegwear = false;
            boolean bipedRightLegwear = false;
            boolean bipedBodyWear = false;
            boolean bipedHeadwear = false;
            GlStateManager.disableLighting();
            if (!this.texture.getValue().booleanValue()) {
                if (e.getModelBase() instanceof ModelPlayer) {
                    ModelPlayer modelPlayer = (ModelPlayer)e.getModelBase();
                    bipedLeftArmwear = modelPlayer.bipedLeftArmwear.showModel;
                    bipedRightArmwear = modelPlayer.bipedRightArmwear.showModel;
                    bipedLeftLegwear = modelPlayer.bipedLeftLegwear.showModel;
                    bipedRightLegwear = modelPlayer.bipedRightLegwear.showModel;
                    bipedBodyWear = modelPlayer.bipedBodyWear.showModel;
                    bipedHeadwear = modelPlayer.bipedHeadwear.showModel;
                    modelPlayer.bipedLeftArmwear.showModel = false;
                    modelPlayer.bipedRightArmwear.showModel = false;
                    modelPlayer.bipedLeftLegwear.showModel = false;
                    modelPlayer.bipedRightLegwear.showModel = false;
                    modelPlayer.bipedBodyWear.showModel = false;
                    modelPlayer.bipedHeadwear.showModel = false;
                }
                GlStateManager.disableTexture2D();
            }
            GL11.glPolygonMode((int)1032, (int)6914);
            GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            GL11.glDepthFunc((int)518);
            Color hiddenC = Cascade.friendManager.isFriend(e.getEntity().getName()) ? new Color(this.hiddenFColor.getValue().getRed(), this.hiddenFColor.getValue().getGreen(), this.hiddenFColor.getValue().getBlue(), this.hiddenFColor.getValue().getAlpha()) : new Color(this.hiddenColor.getValue().getRed(), this.hiddenColor.getValue().getGreen(), this.hiddenColor.getValue().getBlue(), this.hiddenColor.getValue().getAlpha());
            GL11.glColor4d((double)((float)hiddenC.getRed() / 255.0f), (double)((float)hiddenC.getGreen() / 255.0f), (double)((float)hiddenC.getBlue() / 255.0f), (double)((float)hiddenC.getAlpha() / 255.0f));
            GL11.glLineWidth((float)2.0f);
            this.handleMode(e);
            Color visibleC = Cascade.friendManager.isFriend(e.getEntity().getName()) ? new Color(this.visibleFColor.getValue().getRed(), this.visibleFColor.getValue().getGreen(), this.visibleFColor.getValue().getBlue(), this.visibleFColor.getValue().getAlpha()) : new Color(this.visibleColor.getValue().getRed(), this.visibleColor.getValue().getGreen(), this.visibleColor.getValue().getBlue(), this.visibleColor.getValue().getAlpha());
            e.setCanceled(true);
            GL11.glDepthFunc((int)515);
            GL11.glColor4d((double)((float)visibleC.getRed() / 255.0f), (double)((float)visibleC.getGreen() / 255.0f), (double)((float)visibleC.getBlue() / 255.0f), (double)((float)visibleC.getAlpha() / 255.0f));
            this.handleMode(e);
            GL11.glDepthFunc((int)513);
            if (!this.texture.getValue().booleanValue()) {
                GlStateManager.enableTexture2D();
                if (e.getModelBase() instanceof ModelPlayer) {
                    ModelPlayer modelPlayer = (ModelPlayer)e.getModelBase();
                    modelPlayer.bipedLeftArmwear.showModel = bipedLeftArmwear;
                    modelPlayer.bipedRightArmwear.showModel = bipedRightArmwear;
                    modelPlayer.bipedLeftLegwear.showModel = bipedLeftLegwear;
                    modelPlayer.bipedRightLegwear.showModel = bipedRightLegwear;
                    modelPlayer.bipedBodyWear.showModel = bipedBodyWear;
                    modelPlayer.bipedHeadwear.showModel = bipedHeadwear;
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

    void handleMode(RenderLivingEvent e) {
        if (this.glint.getValue().booleanValue()) {
            this.renderShine(e);
        }
        switch (this.mode.getValue()) {
            case Fill: {
                if (!this.texture.getValue().booleanValue()) break;
                this.renderModel(e);
                break;
            }
            case Both: {
                if (this.texture.getValue().booleanValue()) {
                    this.renderModel(e);
                }
            }
            case Wireframe: {
                GL11.glPolygonMode((int)1032, (int)6913);
                this.renderModel(e);
            }
        }
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    void renderModel(RenderLivingEvent e) {
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
        e.getModelBase().render(e.getEntity(), e.getLimbSwing(), e.getLimbSwingAmount(), e.getAgeInTicks(), e.getNetHeadYaw(), e.getHeadPitch(), e.getScaleFactor());
    }

    void renderShine(RenderLivingEvent e) {
        mc.getTextureManager().bindTexture(this.custom.getValue() != false ? CUSTOM : RES_ITEM_GLINT);
        GL11.glTexCoord3d((double)1.0, (double)1.0, (double)1.0);
        GL11.glEnable((int)3553);
        GL11.glBlendFunc((int)768, (int)771);
        GL11.glBlendFunc((int)770, (int)32772);
        if (!this.texture.getValue().booleanValue()) {
            GlStateManager.enableTexture2D();
        }
        for (int i = 0; i < 2; ++i) {
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            float f8 = 0.33333334f * this.scale.getValue().floatValue();
            GlStateManager.scale((float)f8, (float)f8, (float)f8);
            GlStateManager.rotate((float)(30.0f - (float)i * 60.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            GlStateManager.translate((float)0.0f, (float)(((float)Chams.mc.player.ticksExisted + mc.getRenderPartialTicks()) * (0.001f + (float)i * 0.003f) * this.speed.getValue().floatValue()), (float)0.0f);
            GlStateManager.matrixMode((int)5888);
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.0f);
            this.renderModel(e);
        }
        GlStateManager.matrixMode((int)5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode((int)5888);
        if (!this.texture.getValue().booleanValue()) {
            GlStateManager.disableTexture2D();
        }
    }

    static enum Mode {
        Fill,
        Wireframe,
        Both;

    }
}

