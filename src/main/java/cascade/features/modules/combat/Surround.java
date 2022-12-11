//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketBlockChange
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.modules.player.Freecam;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.Timer;
import cascade.util.player.AttackUtil;
import cascade.util.player.BlockUtil;
import cascade.util.player.CrystalUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.player.ItemUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;
import cascade.util.player.TargetUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Surround
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Placements));
    Setting<Boolean> predict = this.register(new Setting<Object>("Predict", Boolean.valueOf(true), v -> this.page.getValue() == Page.Placements));
    Setting<Boolean> swapBypass = this.register(new Setting<Object>("SwapBypass", Boolean.valueOf(false), v -> this.page.getValue() == Page.Placements));
    Setting<Boolean> test = this.register(new Setting<Object>("Test", Boolean.valueOf(false), v -> this.page.getValue() == Page.Placements && this.swapBypass.getValue() != false));
    Setting<Center> center = this.register(new Setting<Object>("Center", (Object)Center.None, v -> this.page.getValue() == Page.Placements));
    Setting<Boolean> centerY = this.register(new Setting<Object>("CenterY", Boolean.valueOf(false), v -> this.page.getValue() == Page.Placements && this.center.getValue() == Center.Instant));
    Setting<Boolean> breakCrystals = this.register(new Setting<Object>("BreakCrystals", Boolean.valueOf(false), v -> this.page.getValue() == Page.Break));
    Setting<Integer> delay = this.register(new Setting<Object>("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(50), v -> this.page.getValue() == Page.Break && this.breakCrystals.getValue() != false));
    Setting<Boolean> cooldown = this.register(new Setting<Object>("Cooldown", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break && this.breakCrystals.getValue() != false));
    Setting<Boolean> prioritize = this.register(new Setting<Object>("Prioritize", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break && this.breakCrystals.getValue() != false && this.cooldown.getValue() != false));
    Setting<Boolean> render = this.register(new Setting<Object>("Render", Boolean.valueOf(false), v -> this.page.getValue() == Page.Render));
    Setting<Color> color = this.register(new Setting<Object>("Color", new Color(-1), v -> this.page.getValue() == Page.Render && this.render.getValue() != false));
    Setting<Boolean> debug = this.register(new Setting<Object>("Debug", Boolean.valueOf(false), v -> this.page.getValue() == Page.Render));
    List<BlockPos> offsets = new ArrayList<BlockPos>();
    Timer breakTimer = new Timer();
    Timer timer = new Timer();
    public boolean canPlace = false;
    double startY = 0.0;
    EntityPlayer target;

    public Surround() {
        super("Surround", Module.Category.COMBAT, "");
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            return;
        }
        this.canPlace = false;
        this.startY = Surround.mc.player.posY;
        this.breakTimer.reset();
        this.timer.reset();
        this.target = null;
        Vec3d CenterPos = EntityUtil.getCenter(Surround.mc.player.posX, Surround.mc.player.posY, Surround.mc.player.posZ);
        if (!(EntityUtil.isPlayerSafe((EntityPlayer)Surround.mc.player) || PlayerUtil.isChestBelow() || EntityUtil.isInLiquid() || Surround.mc.player.noClip || Freecam.getInstance().isEnabled())) {
            switch (this.center.getValue()) {
                case Instant: {
                    MovementUtil.setMotion(0.0, 0.0, 0.0);
                    mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(CenterPos.xCoord, this.centerY.getValue() != false ? CenterPos.yCoord : Surround.mc.player.posY, CenterPos.zCoord, true));
                    Surround.mc.player.setPosition(CenterPos.xCoord, this.centerY.getValue() != false ? CenterPos.yCoord : Surround.mc.player.posY, CenterPos.zCoord);
                    break;
                }
                case Motion: {
                    MovementUtil.setMotion((CenterPos.xCoord - Surround.mc.player.posX) / 2.0, Surround.mc.player.motionY, (CenterPos.zCoord - Surround.mc.player.posZ) / 2.0);
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (Surround.fullNullCheck()) {
            return;
        }
        if (Surround.mc.player.posY != this.startY) {
            if (Surround.mc.player.motionY < 0.0) {
                this.disable();
            }
            return;
        }
        int oldSlot = Surround.mc.player.inventory.currentItem;
        int blockSlot = this.getSlot();
        if (blockSlot == -1) {
            this.disable();
            return;
        }
        this.target = TargetUtil.getTarget(3.5);
        int blocksInTick = 0;
        this.offsets = this.getOffsets();
        for (BlockPos pos : this.offsets) {
            if (!this.canPlaceBlock(pos) || blocksInTick > 20) continue;
            if (this.swapBypass.getValue().booleanValue()) {
                ItemUtil.bypassSwap(blockSlot);
            } else {
                ItemUtil.silentSwap(blockSlot);
            }
            BlockUtil.placeBlock5(pos);
            if (this.swapBypass.getValue().booleanValue()) {
                if (this.canPlace) {
                    ItemUtil.bypassSwap(oldSlot);
                    ItemUtil.syncItem(this.test.getValue());
                }
            } else {
                ItemUtil.silentSwapRecover(oldSlot);
            }
            ++blocksInTick;
        }
    }

    @Override
    @SubscribeEvent
    public void onRender3D(Render3DEvent e) {
        if (this.render.getValue().booleanValue() && this.offsets != null && !this.offsets.isEmpty()) {
            for (BlockPos pos : this.offsets) {
                RenderUtil.drawBoxESP(pos, new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.color.getValue().getAlpha()), false, new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.color.getValue().getAlpha()), 1.5f, true, true, 60, true);
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        SPacketPlayerPosLook p;
        if (Surround.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            p = (SPacketPlayerPosLook)e.getPacket();
            this.startY = p.getY();
        }
        if (e.getPacket() instanceof SPacketBlockChange && this.predict.getValue().booleanValue() && (p = (SPacketBlockChange)e.getPacket()).getBlockState().getBlock() == Blocks.AIR && Surround.mc.player.getDistanceSq(p.getBlockPosition()) <= 6.25) {
            this.onUpdate();
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("@BlockChange");
            }
        }
        if (this.breakCrystals.getValue().booleanValue()) {
            Entity en;
            Object object;
            if (e.getPacket() instanceof SPacketSpawnObject) {
                p = (SPacketSpawnObject)e.getPacket();
                BlockPos pos = new BlockPos(p.getX(), p.getY(), p.getZ());
                if (!(p.getType() != 51 || this.cooldown.getValue().booleanValue() && Cascade.swapManager.hasSwapped() || !(Surround.mc.player.getDistanceSq(pos) > 6.25))) {
                    CPacketUseEntity predict = new CPacketUseEntity();
                    predict.entityId = p.getEntityID();
                    predict.action = CPacketUseEntity.Action.ATTACK;
                    if (this.debug.getValue().booleanValue()) {
                        Command.sendMessage("@SpawnObject(off)");
                    }
                }
            }
            if (e.getPacket() instanceof SPacketSoundEffect && (p = (SPacketSoundEffect)e.getPacket()).getCategory() == SoundCategory.BLOCKS && p.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                object = new ArrayList(Surround.mc.world.loadedEntityList).iterator();
                while (object.hasNext()) {
                    en = (Entity)object.next();
                    if (!(en instanceof EntityEnderCrystal) || !((double)Surround.mc.player.getDistanceToEntity(en) > 2.5)) continue;
                    en.setDead();
                    mc.addScheduledTask(() -> {
                        Surround.mc.world.removeEntity(en);
                        Surround.mc.world.removeEntityDangerously(en);
                    });
                    if (!this.debug.getValue().booleanValue()) continue;
                    Command.sendMessage("@SoundEffect");
                }
            }
            if (e.getPacket() instanceof SPacketExplosion && (p = (SPacketExplosion)e.getPacket()).getStrength() == 6.0f) {
                object = new ArrayList(Surround.mc.world.loadedEntityList).iterator();
                while (object.hasNext()) {
                    en = (Entity)object.next();
                    if (!(en instanceof EntityEnderCrystal) || !((double)Surround.mc.player.getDistanceToEntity(en) > 2.5)) continue;
                    en.setDead();
                    mc.addScheduledTask(() -> {
                        Surround.mc.world.removeEntity(en);
                        Surround.mc.world.removeEntityDangerously(en);
                    });
                    if (!this.debug.getValue().booleanValue()) continue;
                    Command.sendMessage("@Explosion");
                }
            }
            if (e.getPacket() instanceof SPacketDestroyEntities) {
                p = (SPacketDestroyEntities)e.getPacket();
                for (int enID : p.getEntityIDs()) {
                    Entity en2 = Surround.mc.world.getEntityByID(enID);
                    if (!(en2 instanceof EntityEnderCrystal) || !((double)Surround.mc.player.getDistanceToEntity(en2) > 2.5)) continue;
                    en2.setDead();
                    mc.addScheduledTask(() -> {
                        Surround.mc.world.removeEntity(en2);
                        Surround.mc.world.removeEntityDangerously(en2);
                    });
                    if (!this.debug.getValue().booleanValue()) continue;
                    Command.sendMessage("@DestroyEntities");
                }
            }
        }
    }

    int getSlot() {
        int slot = -1;
        slot = InventoryUtil.getBlockFromHotbar(Blocks.OBSIDIAN);
        if (slot == -1) {
            slot = InventoryUtil.getBlockFromHotbar(Blocks.ENDER_CHEST);
        }
        return slot;
    }

    boolean canPlaceBlock(BlockPos pos) {
        boolean allow = true;
        boolean attacked = false;
        if (!Surround.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByOther(pos)) {
            allow = false;
        }
        if (AttackUtil.isInterceptedByCrystal(pos) && this.breakCrystals.getValue().booleanValue()) {
            allow = false;
            EntityEnderCrystal c = null;
            for (Entity en : Surround.mc.world.loadedEntityList) {
                if (!CrystalUtil.isCrystalBreakValid(en, pos, 5.0f, 3.0f)) continue;
                c = (EntityEnderCrystal)en;
            }
            if (!(c == null || this.cooldown.getValue().booleanValue() && Cascade.swapManager.hasSwapped() || !this.breakTimer.passedMs(this.delay.getValue().intValue()))) {
                mc.getConnection().sendPacket((Packet)new CPacketUseEntity((Entity)c));
                mc.getConnection().sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                this.breakTimer.reset();
                this.timer.reset();
                attacked = true;
            }
        }
        if (this.cooldown.getValue().booleanValue() && this.prioritize.getValue().booleanValue() && attacked && !this.timer.passedMs(50L)) {
            allow = false;
        }
        return allow;
    }

    List<BlockPos> getOffsets() {
        int z;
        int x;
        double calcPosX = Surround.mc.player.posX;
        double calcPosZ = Surround.mc.player.posZ;
        BlockPos playerPos = this.getPlayerPos(true);
        ArrayList<BlockPos> offsets = new ArrayList<BlockPos>();
        double decimalX = Math.abs(calcPosX) - Math.floor(Math.abs(calcPosX));
        double decimalZ = Math.abs(calcPosZ) - Math.floor(Math.abs(calcPosZ));
        int lengthXPos = this.calcLength(decimalX, false);
        int lengthXNeg = this.calcLength(decimalX, true);
        int lengthZPos = this.calcLength(decimalZ, false);
        int lengthZNeg = this.calcLength(decimalZ, true);
        ArrayList<BlockPos> tempOffsets = new ArrayList<BlockPos>();
        offsets.addAll(this.getOverlapPos());
        for (x = 1; x < lengthXPos + 1; ++x) {
            tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, 1 + lengthZPos));
            tempOffsets.add(this.addToPlayer(playerPos, x, 0.0, -(1 + lengthZNeg)));
            if (!this.isUnsafe()) continue;
            tempOffsets.add(this.addToPlayer(playerPos, x, 1.0, 1 + lengthZPos));
            tempOffsets.add(this.addToPlayer(playerPos, x, 1.0, -(1 + lengthZNeg)));
        }
        for (x = 0; x <= lengthXNeg; ++x) {
            tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, 1 + lengthZPos));
            tempOffsets.add(this.addToPlayer(playerPos, -x, 0.0, -(1 + lengthZNeg)));
            if (!this.isUnsafe()) continue;
            tempOffsets.add(this.addToPlayer(playerPos, -x, 1.0, 1 + lengthZPos));
            tempOffsets.add(this.addToPlayer(playerPos, -x, 1.0, -(1 + lengthZNeg)));
        }
        for (z = 1; z < lengthZPos + 1; ++z) {
            tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, z));
            tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, z));
            if (!this.isUnsafe()) continue;
            tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 1.0, z));
            tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 1.0, z));
        }
        for (z = 0; z <= lengthZNeg; ++z) {
            tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 0.0, -z));
            tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 0.0, -z));
            if (!this.isUnsafe()) continue;
            tempOffsets.add(this.addToPlayer(playerPos, 1 + lengthXPos, 1.0, -z));
            tempOffsets.add(this.addToPlayer(playerPos, -(1 + lengthXNeg), 1.0, -z));
        }
        for (BlockPos pos2 : tempOffsets) {
            if (!this.hasSurroundingBlock(pos2)) {
                offsets.add(pos2.add(0, -1, 0));
            }
            offsets.add(pos2);
        }
        return offsets;
    }

    boolean hasSurroundingBlock(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (Surround.mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable()) continue;
            return true;
        }
        return false;
    }

    BlockPos addToPlayer(BlockPos playerPos, double x, double y, double z) {
        if (playerPos.getX() < 0) {
            x = -x;
        }
        if (playerPos.getY() < 0) {
            y = -y;
        }
        if (playerPos.getZ() < 0) {
            z = -z;
        }
        return playerPos.add(x, y, z);
    }

    int calcLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    List<BlockPos> getOverlapPos() {
        double calcPosX = Surround.mc.player.posX;
        double calcPosZ = Surround.mc.player.posZ;
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        double decimalX = calcPosX - Math.floor(calcPosX);
        double decimalZ = calcPosZ - Math.floor(calcPosZ);
        int offX = this.calcOffset(decimalX);
        int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos(true));
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                int properX = x * offX;
                int properZ = z * offZ;
                positions.add(this.getPlayerPos(true).add(properX, -1, properZ));
            }
        }
        return positions;
    }

    int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    BlockPos getPlayerPos(boolean self) {
        double x = self ? Surround.mc.player.posX : this.target.posX;
        double y = self ? Surround.mc.player.posY : this.target.posY;
        double z = self ? Surround.mc.player.posZ : this.target.posZ;
        double decimalPoint = y - Math.floor(y);
        return new BlockPos(x, decimalPoint > 0.8 ? Math.floor(y) + 1.0 : Math.floor(y), z);
    }

    boolean isUnsafe() {
        ItemStack helm = Surround.mc.player.inventoryContainer.getSlot(5).getStack();
        ItemStack chest = Surround.mc.player.inventoryContainer.getSlot(6).getStack();
        ItemStack leg = Surround.mc.player.inventoryContainer.getSlot(7).getStack();
        ItemStack boot = Surround.mc.player.inventoryContainer.getSlot(8).getStack();
        if ((helm.getItem() == Items.field_190931_a || chest.getItem() == Items.field_190931_a || leg.getItem() == Items.field_190931_a || boot.getItem() == Items.field_190931_a) && EntityUtil.getHealth((Entity)Surround.mc.player) <= 9.0f) {
            return true;
        }
        return EntityUtil.getHealth((Entity)Surround.mc.player) <= 6.0f;
    }

    static enum Center {
        None,
        Instant,
        Motion;

    }

    static enum Page {
        Placements,
        Break,
        Render;

    }
}

