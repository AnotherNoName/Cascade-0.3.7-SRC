//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cascade.mixin.mixins;

import cascade.event.events.PushEvent;
import cascade.event.events.StepEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double prevPosY;
    @Shadow
    public double lastTickPosY;
    @Shadow
    public float prevRotationYaw;
    @Shadow
    public float stepHeight;
    private Float prevHeight;

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();

    @Redirect(method={"applyEntityCollision"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }

    @Inject(method={"move"}, at={@At(value="FIELD", target="net/minecraft/entity/Entity.onGround:Z", ordinal=1)})
    private void onGroundHook(MoverType type, double x, double y, double z, CallbackInfo info) {
        if (EntityPlayerSP.class.isInstance(this)) {
            StepEvent event = new StepEvent(0, this.getEntityBoundingBox(), this.stepHeight);
            MinecraftForge.EVENT_BUS.post((Event)event);
            this.prevHeight = Float.valueOf(this.stepHeight);
            this.stepHeight = event.getHeight();
        }
    }

    @Inject(method={"move"}, at={@At(value="INVOKE", target="net/minecraft/entity/Entity.setEntityBoundingBox(Lnet/minecraft/util/math/AxisAlignedBB;)V", ordinal=7, shift=At.Shift.AFTER)})
    private void setEntityBoundingBoxHook(MoverType type, double x, double y, double z, CallbackInfo info) {
        if (EntityPlayerSP.class.isInstance(this)) {
            StepEvent event = new StepEvent(1, this.getEntityBoundingBox(), this.prevHeight != null ? this.prevHeight.floatValue() : 0.0f);
            MinecraftForge.EVENT_BUS.post((Event)event);
        }
    }
}

