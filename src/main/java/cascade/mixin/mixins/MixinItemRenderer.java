//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  org.lwjgl.opengl.GL11
 */
package cascade.mixin.mixins;

import cascade.features.modules.visual.HandMod;
import cascade.features.modules.visual.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection = true;

    @Inject(method={"rotateArm"}, at={@At(value="HEAD")}, cancellable=true)
    public void pigHook(float partialTicks, CallbackInfo ci) {
        if (HandMod.getInstance().isEnabled() && HandMod.getInstance().noSway.getValue().booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noOverlay.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isEnabled() && NoRender.getInstance().noOverlay.getValue().booleanValue()) {
            ci.cancel();
        }
    }

    @Shadow
    protected abstract void renderArmFirstPerson(float var1, float var2, EnumHandSide var3);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (HandMod.getInstance().isEnabled() && hand == EnumHand.MAIN_HAND && stack.func_190926_b()) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.pushMatrix();
                GL11.glPushAttrib((int)1048575);
                GL11.glPolygonMode((int)1032, (int)6913);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glEnable((int)2848);
                GL11.glEnable((int)3042);
                GL11.glColor4f((float)HandMod.getInstance().fill.getValue().getRed(), (float)HandMod.getInstance().fill.getValue().getGreen(), (float)HandMod.getInstance().fill.getValue().getBlue(), (float)HandMod.getInstance().fill.getValue().getAlpha());
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
            if (!stack.field_190928_g) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
            }
            this.injection = true;
        }
    }

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Inject(method={"renderItemSide"}, at={@At(value="HEAD")})
    public void renderItemSide(EntityLivingBase entityLivingBase, ItemStack stack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo info) {
        if (HandMod.getInstance().isEnabled() && entityLivingBase == this.mc.player) {
            GlStateManager.scale((float)HandMod.getInstance().sizeX.getValue().floatValue(), (float)HandMod.getInstance().sizeY.getValue().floatValue(), (float)HandMod.getInstance().sizeZ.getValue().floatValue());
            if (this.mc.player.getActiveItemStack() != stack) {
                GlStateManager.translate((double)(HandMod.getInstance().x.getValue().floatValue() * 0.1f * (float)(leftHanded ? -1 : 1)), (double)(HandMod.getInstance().y.getValue().floatValue() * 0.1f), (double)((double)HandMod.getInstance().z.getValue().floatValue() * 0.1));
            }
        }
    }
}

