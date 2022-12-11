//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.event.events.BlockEvent;
import cascade.event.events.UpdateWalkingPlayerEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import cascade.util.misc.Timer;
import cascade.util.player.BlockUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Nuker
extends Module {
    Setting<Float> distance = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(6.0f)));
    Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Attack", 50, 1, 1000));
    Setting<Boolean> nuke = this.register(new Setting<Boolean>("Nuke", false));
    Setting<Mode> mode = this.register(new Setting<Object>("Mode", (Object)Mode.Nuke, v -> this.nuke.getValue()));
    Setting<Boolean> shulkers = this.register(new Setting<Boolean>("Shulkers", true));
    Setting<Boolean> echests = this.register(new Setting<Boolean>("EChests", false));
    Setting<Boolean> hoppers = this.register(new Setting<Boolean>("Hoppers", false));
    Setting<Boolean> anvils = this.register(new Setting<Boolean>("Anvils", true));
    Setting<Boolean> netherAura = this.register(new Setting<Boolean>("NetherAura", false));
    Setting<Double> targetBreak = this.register(new Setting<Object>("TargetBreak", Double.valueOf(2.5), Double.valueOf(0.1), Double.valueOf(3.0), v -> this.netherAura.getValue()));
    Setting<Boolean> noSelf = this.register(new Setting<Object>("NoSelf", Boolean.valueOf(false), v -> this.netherAura.getValue()));
    Timer timer = new Timer();
    Block selected;

    public Nuker() {
        super("Nuker", Module.Category.PLAYER, "");
    }

    @Override
    public void onToggle() {
        this.selected = null;
        this.timer.reset();
    }

    @SubscribeEvent
    public void onClickBlock(BlockEvent e) {
        Block block;
        if (e.getStage() == 3 && this.mode.getValue() != Mode.All && (block = Nuker.mc.world.getBlockState(e.pos).getBlock()) != null && block != this.selected) {
            this.selected = block;
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.isEnabled()) {
            if (this.nuke.getValue().booleanValue()) {
                BlockPos pos = null;
                switch (this.mode.getValue()) {
                    case Selection: 
                    case Nuke: {
                        pos = this.getClosestBlockSelection();
                        break;
                    }
                    case All: {
                        pos = this.getClosestBlockAll();
                    }
                }
                if (pos != null) {
                    if (this.mode.getValue() != Mode.Nuke) {
                        if (this.canBreak(pos)) {
                            Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
                            mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                        }
                    } else {
                        for (int i = 0; i < 2; ++i) {
                            pos = this.getClosestBlockSelection();
                            if (pos == null || !this.timer.passedMs(this.delay.getValue().intValue())) continue;
                            Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
                            mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                            this.timer.reset();
                        }
                    }
                }
            }
            ArrayList<Block> blocklist = new ArrayList<Block>();
            if (this.shulkers.getValue().booleanValue()) {
                this.breakBlocks(BlockUtil.shulkerList);
            }
            if (this.echests.getValue().booleanValue()) {
                blocklist.add(Blocks.ENDER_CHEST);
            }
            if (this.hoppers.getValue().booleanValue()) {
                blocklist.add((Block)Blocks.HOPPER);
            }
            if (this.anvils.getValue().booleanValue()) {
                blocklist.add(Blocks.ANVIL);
            }
            if (this.netherAura.getValue().booleanValue()) {
                // empty if block
            }
            if (!blocklist.isEmpty()) {
                this.breakBlocks(blocklist);
            }
        }
    }

    void breakBlocks(List<Block> blocks) {
        BlockPos pos = this.getNearestBlock(blocks);
        if (pos != null && this.canBreak(pos)) {
            Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
            mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    boolean canBreak(BlockPos pos) {
        IBlockState bs = Nuker.mc.world.getBlockState(pos);
        Block b = bs.getBlock();
        return b.getBlockHardness(bs, (World)Nuker.mc.world, pos) != -1.0f;
    }

    BlockPos getNearestBlock(List<Block> blocks) {
        double maxDist = MathUtil.square(this.distance.getValue().floatValue());
        BlockPos ret = null;
        for (double x = maxDist; x >= -maxDist; x -= 1.0) {
            for (double y = maxDist; y >= -maxDist; y -= 1.0) {
                for (double z = maxDist; z >= -maxDist; z -= 1.0) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + x, Nuker.mc.player.posY + y, Nuker.mc.player.posZ + z);
                    double dist = Nuker.mc.player.getDistanceSq((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                    if (!(dist <= maxDist) || !blocks.contains(Nuker.mc.world.getBlockState(pos).getBlock()) || !this.canBreak(pos)) continue;
                    maxDist = dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    BlockPos getClosestBlockAll() {
        float maxDist = this.distance.getValue().floatValue();
        BlockPos ret = null;
        for (float x = maxDist; x >= -maxDist; x -= 1.0f) {
            for (float y = maxDist; y >= -maxDist; y -= 1.0f) {
                for (float z = maxDist; z >= -maxDist; z -= 1.0f) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + (double)x, Nuker.mc.player.posY + (double)y, Nuker.mc.player.posZ + (double)z);
                    double dist = Nuker.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                    if (!(dist <= (double)maxDist) || Nuker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || Nuker.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid || !this.canBreak(pos) || !((double)pos.getY() >= Nuker.mc.player.posY)) continue;
                    maxDist = (float)dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    BlockPos getClosestBlockSelection() {
        float maxDist = this.distance.getValue().floatValue();
        BlockPos ret = null;
        for (float x = maxDist; x >= -maxDist; x -= 1.0f) {
            for (float y = maxDist; y >= -maxDist; y -= 1.0f) {
                for (float z = maxDist; z >= -maxDist; z -= 1.0f) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + (double)x, Nuker.mc.player.posY + (double)y, Nuker.mc.player.posZ + (double)z);
                    double dist = Nuker.mc.player.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                    if (!(dist <= (double)maxDist) || Nuker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || Nuker.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid || Nuker.mc.world.getBlockState(pos).getBlock() != this.selected || !this.canBreak(pos) || !((double)pos.getY() >= Nuker.mc.player.posY)) continue;
                    maxDist = (float)dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    static enum Mode {
        Selection,
        All,
        Nuke;

    }
}

