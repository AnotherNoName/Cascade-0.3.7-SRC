/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.block.model.IBakedModel
 *  net.minecraft.item.ItemStack
 */
package cascade.mixin.mixins;

import cascade.features.modules.visual.GlintMod;
import cascade.features.modules.visual.HandMod;
import java.awt.Color;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderItem.class})
public abstract class MixinRenderItem {
    @Shadow
    private void func_191967_a(IBakedModel model, int color, ItemStack stack) {
    }

    @Redirect(method={"renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void renderModelColor(RenderItem renderItem, IBakedModel model, ItemStack stack) {
        Color color = HandMod.getInstance().isEnabled() ? new Color((float)HandMod.getInstance().c.getValue().getRed() / 255.0f, (float)HandMod.getInstance().c.getValue().getGreen() / 255.0f, (float)HandMod.getInstance().c.getValue().getBlue() / 255.0f, (float)HandMod.getInstance().c.getValue().getAlpha() / 255.0f) : new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.func_191967_a(model, color.getRGB(), stack);
    }

    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index=1)
    private int renderEffect(int oldValue) {
        if (GlintMod.getInstance().isEnabled()) {
            return GlintMod.getInstance().c.getValue().getRGB();
        }
        return oldValue;
    }
}

