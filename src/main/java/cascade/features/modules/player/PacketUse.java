//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemBucketMilk
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketUse
extends Module {
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NoDelay));
    Setting<Float> speed = this.register(new Setting<Object>("Speed", Float.valueOf(25.0f), Float.valueOf(1.0f), Float.valueOf(25.0f), v -> this.mode.getValue() == Mode.Packet));
    Setting<Boolean> cancel = this.register(new Setting<Boolean>("Cancel", true));
    Setting<Integer> runs = this.register(new Setting<Object>("Runs", Integer.valueOf(32), Integer.valueOf(1), Integer.valueOf(64), v -> this.mode.getValue() == Mode.Packet));
    static PacketUse INSTANCE;

    public PacketUse() {
        super("PacketUse", Module.Category.PLAYER, "");
        INSTANCE = this;
    }

    public static PacketUse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PacketUse();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (PacketUse.fullNullCheck() || !this.isValid(PacketUse.mc.player.getActiveItemStack())) {
            return;
        }
        if (this.mode.getValue() == Mode.Update) {
            EnumHand hand = PacketUse.mc.player.getActiveHand();
            if (hand == null) {
                hand = PacketUse.mc.player.getHeldItemOffhand().equals(PacketUse.mc.player.getActiveItemStack()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            }
            mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItem(hand));
        }
        if (this.mode.getValue() == Mode.Packet && (float)PacketUse.mc.player.getItemInUseMaxCount() > this.speed.getValue().floatValue() - 1.0f && this.speed.getValue().floatValue() < 25.0f) {
            for (int i = 0; i < this.runs.getValue(); ++i) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayer(PacketUse.mc.player.onGround));
            }
            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            PacketUse.mc.player.stopActiveHand();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        CPacketPlayerTryUseItem p;
        if (this.isDisabled() || PacketUse.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketPlayerDigging && this.cancel.getValue().booleanValue() && this.isValid(PacketUse.mc.player.getActiveItemStack()) && (p = (CPacketPlayerDigging)e.getPacket()).getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && p.getFacing() == EnumFacing.DOWN && p.getPosition().equals((Object)BlockPos.ORIGIN)) {
            e.setCanceled(true);
        }
        if (e.getPacket() instanceof CPacketPlayerTryUseItem && this.mode.getValue() == Mode.Update && this.isValid(PacketUse.mc.player.getHeldItem((p = (CPacketPlayerTryUseItem)e.getPacket()).getHand()))) {
            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    public boolean isValid(ItemStack stack) {
        return stack != null && PacketUse.mc.player.isHandActive() && (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemPotion || stack.getItem() instanceof ItemBucketMilk);
    }

    public static enum Mode {
        NoDelay,
        Update,
        Packet;

    }
}

