/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 */
package cascade.mixin.mixins.accessor;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={EntityRenderer.class})
public interface IEntityRenderer {
    @Invoker(value="setupCameraTransform")
    public void setupCameraTransformInvoker(float var1, int var2);

    @Accessor(value="rendererUpdateCount")
    public int getRendererUpdateCount();

    @Accessor(value="rainXCoords")
    public float[] getRainXCoords();

    @Accessor(value="rainYCoords")
    public float[] getRainYCoords();

    @Accessor(value="farPlaneDistance")
    public float getFarPlaneDistance();

    @Accessor(value="fovModifierHandPrev")
    public float getFovModifierHandPrev();

    @Accessor(value="fovModifierHand")
    public float getFovModifierHand();

    @Accessor(value="debugView")
    public boolean isDebugView();
}

