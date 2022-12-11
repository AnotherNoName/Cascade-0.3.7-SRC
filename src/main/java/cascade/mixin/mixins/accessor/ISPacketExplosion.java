/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketExplosion
 */
package cascade.mixin.mixins.accessor;

import net.minecraft.network.play.server.SPacketExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={SPacketExplosion.class})
public interface ISPacketExplosion {
    @Accessor(value="motionX")
    public void setX(float var1);

    @Accessor(value="motionY")
    public void setY(float var1);

    @Accessor(value="motionZ")
    public void setZ(float var1);

    @Accessor(value="motionX")
    public float getX();

    @Accessor(value="motionY")
    public float getY();

    @Accessor(value="motionZ")
    public float getZ();
}

