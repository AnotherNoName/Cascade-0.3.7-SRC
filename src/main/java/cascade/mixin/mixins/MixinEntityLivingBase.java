//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cascade.mixin.mixins;

import cascade.event.events.LiquidJumpEvent;
import cascade.features.modules.player.PacketUse;
import cascade.features.modules.visual.HandMod;
import cascade.mixin.mixins.MixinEntity;
import cascade.util.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase
extends MixinEntity {
    @Shadow
    protected int activeItemStackUseCount;
    @Shadow
    protected ItemStack activeItemStack;

    @Redirect(method={"onItemUseFinish"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLivingBase;resetActiveHand()V"))
    private void resetActiveHandHook(EntityLivingBase base) {
        if (PacketUse.getInstance().isEnabled() && base instanceof EntityPlayerSP && !Util.mc.isSingleplayer() && PacketUse.getInstance().mode.getValue() == PacketUse.Mode.NoDelay && PacketUse.getInstance().isValid(this.activeItemStack)) {
            this.activeItemStackUseCount = 0;
            ((EntityPlayerSP)base).connection.sendPacket((Packet)new CPacketPlayerTryUseItem(base.getActiveHand()));
        } else {
            base.resetActiveHand();
        }
    }

    @Inject(method={"getArmSwingAnimationEnd"}, at={@At(value="HEAD")}, cancellable=true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> nigger) {
        if (HandMod.getInstance().isEnabled() && HandMod.getInstance().swingSpeed.getValue().booleanValue()) {
            nigger.setReturnValue(HandMod.getInstance().factor.getValue());
        }
    }

    @Inject(method={"handleJumpWater"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleJumpWaterHook(CallbackInfo info) {
        LiquidJumpEvent event = new LiquidJumpEvent((EntityLivingBase)EntityLivingBase.class.cast(this));
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method={"handleJumpLava"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleJumpLavaHook(CallbackInfo info) {
        LiquidJumpEvent event = new LiquidJumpEvent((EntityLivingBase)EntityLivingBase.class.cast(this));
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }
}

