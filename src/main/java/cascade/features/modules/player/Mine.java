//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.Cascade;
import cascade.event.events.BlockEvent;
import cascade.event.events.PacketEvent;
import cascade.event.events.ReachEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import cascade.util.misc.Timer;
import cascade.util.player.BlockUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Mine
extends Module {
    Setting<Boolean> reach = this.register(new Setting<Boolean>("Reach", false));
    Setting<Float> add = this.register(new Setting<Object>("Add", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(2.0f), v -> this.reach.getValue()));
    Setting<Integer> range = this.register(new Setting<Integer>("Range", 6, 1, 20));
    Setting<Color> startC = this.register(new Setting<Color>("StartColor", new Color(0xFF0000)));
    Setting<Color> endC = this.register(new Setting<Color>("endColor", new Color(65280)));
    Setting<Boolean> render = this.register(new Setting<Boolean>("Render", true));
    Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.render.getValue()));
    static Mine INSTANCE = new Mine();
    IBlockState blockState;
    EnumFacing lastFacing = null;
    Timer timer = new Timer();
    boolean isMining = false;
    BlockPos lastPos = null;
    BlockPos currentPos;
    EnumFacing facing;

    public Mine() {
        super("Mine", Module.Category.PLAYER, "");
        INSTANCE = this;
    }

    public static Mine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Mine();
        }
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        this.facing = null;
    }

    @SubscribeEvent
    public void onReachEvent(ReachEvent e) {
        if (Mine.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (Mine.mc.gameSettings.keyBindAttack.isKeyDown() && this.reach.getValue().booleanValue()) {
            e.setDistance(4.5f + this.add.getValue().floatValue());
        }
    }

    @Override
    public void onUpdate() {
        if (Mine.fullNullCheck()) {
            return;
        }
        Mine.mc.playerController.blockHitDelay = 0;
        if (this.currentPos != null) {
            if (Mine.mc.player.getDistanceSq(this.currentPos) > MathUtil.square(this.range.getValue().intValue())) {
                this.currentPos = null;
                this.blockState = null;
                return;
            }
            if (Mine.mc.world.getBlockState(this.currentPos) != this.blockState || Mine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.blockState = null;
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Mine.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (this.render.getValue().booleanValue() && this.currentPos != null) {
            Color color = this.timer.passedMs((int)(2000.0f * Cascade.serverManager.getTpsFactor())) ? this.endC.getValue() : this.startC.getValue();
            RenderUtil.drawBoxESP(this.currentPos, color, false, color, this.lineWidth.getValue().floatValue(), true, true, 30, false);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        CPacketPlayerDigging packet;
        if (Mine.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)e.getPacket()) != null && packet.getPosition() != null) {
            for (Entity entity : Mine.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(packet.getPosition()))) {
                if (!(entity instanceof EntityEnderCrystal)) continue;
                this.showAnimation(false, null, null);
                return;
            }
            if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                this.showAnimation(true, packet.getPosition(), packet.getFacing());
            }
            if (packet.getAction().equals((Object)CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                this.showAnimation(false, null, null);
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (e.getStage() == 4 && BlockUtil.canBreak(e.pos)) {
            if (this.currentPos == null) {
                this.currentPos = e.pos;
                this.blockState = Mine.mc.world.getBlockState(this.currentPos);
                this.timer.reset();
            }
            Mine.mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, e.pos, e.facing));
            mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.pos, e.facing));
            this.facing = e.facing;
            e.setCanceled(true);
        }
    }

    void showAnimation(boolean isMining, BlockPos lastPos, EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
}

