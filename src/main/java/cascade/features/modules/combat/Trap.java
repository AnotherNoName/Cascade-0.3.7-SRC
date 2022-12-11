//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.CalcUtil;
import cascade.util.misc.MathUtil;
import cascade.util.player.AttackUtil;
import cascade.util.player.BlockUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.player.ItemUtil;
import cascade.util.player.TargetUtil;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Trap
extends Module {
    Setting<Double> placeRange = this.register(new Setting<Double>("PlaceRange", 6.0, 0.1, 6.0));
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Top));
    Setting<Boolean> roof = this.register(new Setting<Object>("Roof", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.SingleBlock));
    Setting<Boolean> breakCrystals = this.register(new Setting<Boolean>("BreakCrystals", false));
    Setting<Boolean> cooldown = this.register(new Setting<Object>("Cooldown", Boolean.valueOf(true), v -> this.breakCrystals.getValue()));
    Setting<Double> breakRange = this.register(new Setting<Object>("BreakRange", Double.valueOf(3.0), Double.valueOf(0.1), Double.valueOf(6.0), v -> this.breakCrystals.getValue()));
    Setting<Double> breakWallRange = this.register(new Setting<Object>("BreakWallRange", Double.valueOf(3.0), Double.valueOf(0.1), Double.valueOf(6.0), v -> this.breakCrystals.getValue()));
    Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));
    BlockPos[] fullOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(0, 1, -1), new BlockPos(1, 2, 0), new BlockPos(0, 2, 0)};
    BlockPos[] cityOffsets = new BlockPos[]{new BlockPos(1, 1, 0), new BlockPos(1, 1, 1), new BlockPos(0, 1, 1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, -1), new BlockPos(0, 1, -1), new BlockPos(-1, 1, -1), new BlockPos(1, 2, 0), new BlockPos(0, 2, 0)};
    BlockPos[] surroundOffsets = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 0)};
    BlockPos[] headOffsets = new BlockPos[]{new BlockPos(0, 2, 0)};
    BlockPos[] roofOffsets = new BlockPos[]{new BlockPos(0, 2, 0), new BlockPos(1, 2, 0), new BlockPos(0, 2, 1), new BlockPos(-1, 2, 0), new BlockPos(0, 2, -1)};
    BlockPos[] offsets = new BlockPos[0];
    EntityPlayer target;

    public Trap() {
        super("Trap", Module.Category.COMBAT, "");
    }

    @Override
    public void onUpdate() {
        if (Trap.fullNullCheck()) {
            return;
        }
        this.target = TargetUtil.getTarget(6.0);
        if (this.target == null) {
            return;
        }
        int slot = InventoryUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
        int oldSlot = Trap.mc.player.inventory.currentItem;
        if (slot == -1) {
            return;
        }
        int bpt = 0;
        switch (this.mode.getValue()) {
            case Full: {
                this.offsets = this.offsetBlocks(this.fullOffsets, this.getPlayerPos((Entity)this.target));
                break;
            }
            case City: {
                this.offsets = this.offsetBlocks(this.cityOffsets, this.getPlayerPos((Entity)this.target));
                break;
            }
            case Top: {
                this.offsets = this.getObbyToHead(this.getPlayerPos((Entity)this.target));
                break;
            }
            case SingleBlock: {
                this.offsets = this.offsetBlocks(this.roof.getValue() != false ? this.roofOffsets : this.headOffsets, this.getPlayerPos((Entity)this.target));
            }
        }
        for (BlockPos pos : this.offsets) {
            if (!this.canPlaceBlock(pos)) continue;
            ItemUtil.silentSwap(slot);
            BlockUtil.placeBlock5(pos);
            ItemUtil.silentSwapRecover(oldSlot);
            if (++bpt > 20) break;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        SPacketBlockChange p;
        if (Trap.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketBlockChange && (p = (SPacketBlockChange)e.getPacket()).getBlockState().getBlock() == Blocks.AIR && Trap.mc.player.getDistanceSq(p.getBlockPosition()) <= MathUtil.square(this.placeRange.getValue())) {
            if (this.offsets == null || this.offsets == new BlockPos[0]) {
                return;
            }
            int slot = InventoryUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
            int oldSlot = Trap.mc.player.inventory.currentItem;
            if (slot == -1) {
                return;
            }
            int bpt = 0;
            for (BlockPos pos : this.offsets) {
                if (pos != p.getBlockPosition() || !this.canPlaceBlock(pos)) continue;
                ItemUtil.silentSwap(slot);
                BlockUtil.placeBlock5(pos);
                ItemUtil.silentSwapRecover(oldSlot);
                if (this.debug.getValue().booleanValue()) {
                    Command.sendMessage("@BlockChange");
                }
                if (++bpt > 3) break;
            }
        }
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        if (!Trap.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByOther(pos)) {
            allow = false;
        }
        if (CalcUtil.getDistanceFromPos2Pos(pos.x, pos.y, pos.z, Trap.mc.player.posX, Trap.mc.player.posY, Trap.mc.player.posZ) > this.placeRange.getValue()) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByCrystal(pos)) {
            if (!this.breakCrystals.getValue().booleanValue()) {
                allow = false;
            }
            EntityEnderCrystal c = null;
            for (Entity en : Trap.mc.world.loadedEntityList) {
                if (en == null || !(en instanceof EntityEnderCrystal) || en.isDead) continue;
                if ((double)Trap.mc.player.getDistanceToEntity(en) > (Trap.mc.player.canEntityBeSeen(en) ? this.breakRange.getValue() : this.breakWallRange.getValue()) || CalcUtil.getDistanceFromPos2Entity(en, pos.x, pos.y, pos.z) > 2.5) continue;
                c = (EntityEnderCrystal)en;
            }
            if (!(c == null || this.cooldown.getValue().booleanValue() && Cascade.swapManager.hasSwapped())) {
                mc.getConnection().sendPacket((Packet)new CPacketUseEntity(c));
                mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                allow = true;
            }
        }
        return allow;
    }

    BlockPos getPlayerPos(Entity player) {
        double decimalPoint = player.posY - Math.floor(player.posY);
        return new BlockPos(player.posX, decimalPoint > 0.8 ? Math.floor(player.posY) + 1.0 : Math.floor(player.posY), player.posZ);
    }

    BlockPos[] offsetBlocks(BlockPos[] toOffset, BlockPos offsetPlace) {
        BlockPos[] offsets = new BlockPos[toOffset.length];
        int index = 0;
        for (BlockPos blockPos : toOffset) {
            offsets[index] = offsetPlace.add((Vec3i)blockPos);
            ++index;
        }
        return offsets;
    }

    public BlockPos[] getObbyToHead(BlockPos feet) {
        ArrayList<BlockPos> obbyToHead = new ArrayList<BlockPos>();
        BlockPos head = feet.add((Vec3i)new BlockPos(0, 1, 0));
        if (this.getSurroundedBlock(head) != null) {
            obbyToHead.add(this.getSurroundedBlock(head).add((Vec3i)new BlockPos(0, 1, 0)));
            obbyToHead.add(head.add((Vec3i)new BlockPos(0, 1, 0)));
        } else if (this.getSurroundedBlock(feet) != null) {
            obbyToHead.add(this.getSurroundedBlock(feet).add((Vec3i)new BlockPos(0, 1, 0)));
        } else if (this.getSurroundedBlock(feet.add(0, -1, 0)) != null) {
            obbyToHead.add(this.getSurroundedBlock(feet.add((Vec3i)new BlockPos(0, -1, 0))).add((Vec3i)new BlockPos(0, 1, 0)));
        }
        BlockPos[] blocks = new BlockPos[obbyToHead.size()];
        return obbyToHead.toArray(blocks);
    }

    BlockPos getSurroundedBlock(BlockPos feet) {
        for (BlockPos offset : this.surroundOffsets) {
            IBlockState blockState = Trap.mc.world.getBlockState(feet.add((Vec3i)offset));
            if (blockState.getMaterial().isReplaceable()) continue;
            return feet.add((Vec3i)offset);
        }
        return null;
    }

    static enum Mode {
        Full,
        City,
        Top,
        SingleBlock;

    }
}

