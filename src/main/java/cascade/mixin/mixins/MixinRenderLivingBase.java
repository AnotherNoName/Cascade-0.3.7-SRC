//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cascade.mixin.mixins;

import cascade.event.events.ModelRenderEvent;
import cascade.event.events.RenderLivingEvent;
import cascade.util.Util;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    private static final ResourceLocation glint;
    private static final ResourceLocation RES_ITEM_GLINT;
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderMarker;
    float red;
    float green;
    float blue;

    public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
    }

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
        this.red = 0.0f;
        this.green = 0.0f;
        this.blue = 0.0f;
    }

    @Inject(method={"renderModel"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V")}, cancellable=true)
    public void renderModel(EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo info) {
        RenderLivingEvent e = new RenderLivingEvent((Entity)entityLivingBase, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, this.mainModel);
        if (Util.mc.world == null) {
            return;
        }
        MinecraftForge.EVENT_BUS.post((Event)e);
        if (e.isCanceled()) {
            info.cancel();
        }
    }

    @Redirect(method={"renderModel"}, at=@At(value="INVOKE", target="net/minecraft/client/model/ModelBase.render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderHook(ModelBase model, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderLivingBase renderLiving = (RenderLivingBase)RenderLivingBase.class.cast((Object)this);
        EntityLivingBase entity = (EntityLivingBase)entityIn;
        ModelRenderEvent.Pre event = new ModelRenderEvent.Pre(renderLiving, entity, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            model.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        MinecraftForge.EVENT_BUS.post((Event)new ModelRenderEvent.Post(renderLiving, entity, model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale));
    }

    @Shadow
    protected abstract boolean func_193115_c(EntityLivingBase var1);

    @Shadow
    protected abstract float getSwingProgress(T var1, float var2);

    @Shadow
    protected abstract float interpolateRotation(float var1, float var2, float var3);

    @Shadow
    protected abstract float handleRotationFloat(T var1, float var2);

    @Shadow
    protected abstract void rotateCorpse(T var1, float var2, float var3, float var4);

    @Shadow
    public abstract float prepareScale(T var1, float var2);

    @Shadow
    protected abstract void unsetScoreTeamColor();

    @Shadow
    protected abstract boolean setScoreTeamColor(T var1);

    @Shadow
    protected abstract void renderLivingAt(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract void unsetBrightness();

    @Shadow
    protected abstract void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7);

    @Shadow
    protected abstract void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected abstract boolean setDoRenderBrightness(T var1, float var2);

    static {
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/playerGlint.png");
        glint = new ResourceLocation("textures/shinechams.png");
    }
}

