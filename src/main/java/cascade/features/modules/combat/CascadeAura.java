//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import cascade.util.misc.Timer;
import cascade.util.player.CrystalUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CascadeAura
extends Module {
    static CascadeAura INSTANCE;
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Place));
    Setting<Float> placeRange = this.register(new Setting<Object>("PlaceRange", Float.valueOf(5.0f), Float.valueOf(0.1f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.Place));
    Setting<Float> placeWallRange = this.register(new Setting<Object>("PlaceWall", Float.valueOf(3.0f), Float.valueOf(0.1f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.Place));
    Setting<Integer> placeDelay = this.register(new Setting<Object>("PlaceDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500), v -> this.page.getValue() == Page.Place));
    Setting<Boolean> holePlacement = this.register(new Setting<Object>("HolePlacement", Boolean.valueOf(false), v -> this.page.getValue() == Page.Place));
    Setting<Boolean> prePlace = this.register(new Setting<Object>("PrePlace(test)", Boolean.valueOf(false), v -> this.page.getValue() == Page.Place));
    Setting<Boolean> rePlace = this.register(new Setting<Object>("RePlace(test)", Boolean.valueOf(false), v -> this.page.getValue() == Page.Place));
    Setting<Float> breakRange = this.register(new Setting<Object>("BreakRange", Float.valueOf(5.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.Break));
    Setting<Float> breakWallRange = this.register(new Setting<Object>("BreakWall", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), v -> this.page.getValue() == Page.Break));
    Setting<Integer> breakDelay = this.register(new Setting<Object>("BreakDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(500), v -> this.page.getValue() == Page.Break));
    Setting<Boolean> cooldown = this.register(new Setting<Object>("Cooldown", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break));
    Setting<Boolean> explosion = this.register(new Setting<Object>("Explosion", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break));
    Setting<Boolean> instant = this.register(new Setting<Object>("Instant", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break));
    Setting<Boolean> predict = this.register(new Setting<Object>("Predict", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break));
    Setting<Boolean> sound = this.register(new Setting<Object>("Sound", Boolean.valueOf(true), v -> this.page.getValue() == Page.Break));
    public Setting<Float> range = this.register(new Setting<Object>("Range", Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(15.0f), v -> this.page.getValue() == Page.Calc));
    Setting<Double> faceplaceHp = this.register(new Setting<Object>("FaceplaceHP", Double.valueOf(10.1), Double.valueOf(0.1), Double.valueOf(36.0), v -> this.page.getValue() == Page.Calc));
    Setting<Double> minFaceDmg = this.register(new Setting<Object>("MinFaceDamage", Double.valueOf(1.2), Double.valueOf(0.1), Double.valueOf(12.0), v -> this.page.getValue() == Page.Calc));
    Setting<Integer> faceDelay = this.register(new Setting<Object>("FaceDelay", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(500), v -> this.page.getValue() == Page.Calc));
    Setting<Double> minDamage = this.register(new Setting<Object>("MinDamage", Double.valueOf(5.1), Double.valueOf(0.1), Double.valueOf(36.0), v -> this.page.getValue() == Page.Calc));
    Setting<Double> maxSelfDamage = this.register(new Setting<Object>("MaxSelfDamage", Double.valueOf(6.2), Double.valueOf(0.1), Double.valueOf(36.0), v -> this.page.getValue() == Page.Calc));
    Setting<Integer> armorPercentage = this.register(new Setting<Object>("Armor%", Integer.valueOf(33), Integer.valueOf(0), Integer.valueOf(100), v -> this.page.getValue() == Page.Calc));
    Setting<Boolean> rotate = this.register(new Setting<Object>("Rotate", Boolean.valueOf(false), v -> this.page.getValue() == Page.Misc));
    Setting<Boolean> debug = this.register(new Setting<Object>("Debug", Boolean.valueOf(false), v -> this.page.getValue() == Page.Misc && this.rotate.getValue() != false));
    Setting<Boolean> packetDebug = this.register(new Setting<Object>("PacketDebug", Boolean.valueOf(false), v -> this.page.getValue() == Page.Misc));
    public Setting<Swing> swing = this.register(new Setting<Object>("Swing", (Object)Swing.None, v -> this.page.getValue() == Page.Misc));
    Setting<SwingOn> swingOn = this.register(new Setting<Object>("SwingOn", (Object)SwingOn.Break, v -> this.page.getValue() == Page.Misc && this.swing.getValue() != Swing.None));
    Setting<Boolean> positionRender = this.register(new Setting<Object>("PosRender", Boolean.valueOf(false), v -> this.page.getValue() == Page.Render));
    Setting<Color> positionColor = this.register(new Setting<Object>("PosColor", new Color(63, 255, 15), v -> this.page.getValue() == Page.Render && this.positionRender.getValue() != false));
    public Set<BlockPos> placeSet = new HashSet<BlockPos>();
    public Set<Integer> breakSet = new HashSet<Integer>();
    Timer placeTimer = new Timer();
    Timer breakTimer = new Timer();
    Timer faceTimer = new Timer();
    EntityPlayer currentTarget;
    EnumFacing enumFacing;
    double currentDamage;
    float targetHealth;
    BlockPos placingPos;
    BlockPos renderPos;
    float selfHealth;
    boolean lowArmor;
    float pitch = 0.0f;
    float yaw = 0.0f;
    int ticks;

    public CascadeAura() {
        super("CascadeAura", Module.Category.COMBAT, "");
        INSTANCE = this;
    }

    public static CascadeAura getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CascadeAura();
        }
        return INSTANCE;
    }

    @Override
    public void onToggle() {
        this.placeSet.clear();
        this.breakSet.clear();
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.faceTimer.reset();
        this.enumFacing = null;
        this.placingPos = null;
        this.renderPos = null;
        this.targetHealth = 0.0f;
        this.selfHealth = 0.0f;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    @Override
    public String getDisplayInfo() {
        if (!CascadeAura.fullNullCheck() && this.currentTarget != null) {
            return this.currentTarget.getName();
        }
        return null;
    }

    @Override
    public void onUpdate() {
        if (CascadeAura.fullNullCheck()) {
            return;
        }
        if (this.ticks++ > 20) {
            this.ticks = 0;
            this.placeSet.clear();
            this.breakSet.clear();
            this.placingPos = null;
            this.renderPos = null;
        }
        this.selfHealth = CrystalUtil.getHealth((EntityLivingBase)CascadeAura.mc.player);
        this.holdRotation();
        this.doBreak();
        this.doPlace();
    }

    void doBreak() {
        Entity crystal = null;
        double dmg = 0.5;
        for (Entity en : CascadeAura.mc.world.loadedEntityList) {
            boolean shouldFaceplace;
            if (!CrystalUtil.isBreakValid(en, this.breakRange.getValue().floatValue(), this.breakWallRange.getValue().floatValue()) || !this.currentTarget.isEntityAlive() || CascadeAura.mc.player.getDistanceToEntity((Entity)this.currentTarget) > this.range.getValue().floatValue()) continue;
            double selfDmg = CrystalUtil.calculateDamage(en.posX, en.posY, en.posZ, (Entity)CascadeAura.mc.player);
            double targetDmg = CrystalUtil.calculateDamage(en.posX, en.posY, en.posZ, (Entity)this.currentTarget);
            boolean bl = shouldFaceplace = (double)this.targetHealth <= this.faceplaceHp.getValue();
            if (shouldFaceplace && targetDmg < this.minFaceDmg.getValue() || targetDmg < this.minDamage.getValue() && !shouldFaceplace && !this.lowArmor || selfDmg >= (double)CrystalUtil.getHealth((EntityLivingBase)CascadeAura.mc.player) || this.maxSelfDamage.getValue() < selfDmg || dmg > targetDmg) continue;
            crystal = en;
            dmg = targetDmg;
        }
        if (!this.breakTimer.passedMs(this.breakDelay.getValue().intValue())) {
            return;
        }
        if (!(crystal == null || this.cooldown.getValue().booleanValue() && Cascade.swapManager.hasSwapped())) {
            this.breakSet.add(crystal.getEntityId());
            this.rotateTo(crystal);
            mc.getConnection().sendPacket((Packet)new CPacketUseEntity(crystal));
            this.breakTimer.reset();
            if (this.swing.getValue() != Swing.None && this.swingOn.getValue() != SwingOn.Place) {
                EnumHand hand = this.swing.getValue() == Swing.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                CascadeAura.mc.player.swingArm(hand);
            }
            if (this.rePlace.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(crystal.getPosition().down(), EnumFacing.UP, InventoryUtil.heldItem(Items.END_CRYSTAL, InventoryUtil.Hand.Off) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)crystal.posX, (float)crystal.posY, (float)crystal.posZ));
                this.placeTimer.reset();
            }
        }
    }

    void doPlace() {
        BlockPos placePos = null;
        double maxDamage = 0.5;
        for (BlockPos pos : CrystalUtil.getSphere(this.placeRange.getValue().floatValue(), true)) {
            if (!CrystalUtil.canPlaceCrystal(pos, this.holePlacement.getValue() == false, this.placeWallRange.getValue().floatValue())) continue;
            for (EntityPlayer player : CascadeAura.mc.world.playerEntities) {
                boolean shouldFaceplace;
                if (CascadeAura.mc.world.playerEntities.size() == 0 || !player.isEntityAlive() || Cascade.friendManager.isFriend(player) || player == CascadeAura.mc.player || CascadeAura.mc.player.getDistanceToEntity((Entity)player) > this.range.getValue().floatValue()) continue;
                this.targetHealth = CrystalUtil.getHealth((EntityLivingBase)player);
                this.lowArmor = CrystalUtil.isArmorLow(player, this.armorPercentage.getValue());
                double selfDmg = CrystalUtil.calculateDamage((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, (Entity)CascadeAura.mc.player);
                double targetDmg = CrystalUtil.calculateDamage((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, (Entity)player);
                boolean bl = shouldFaceplace = (double)this.targetHealth <= this.faceplaceHp.getValue();
                if (shouldFaceplace && targetDmg < this.minFaceDmg.getValue() || targetDmg < this.minDamage.getValue() && !shouldFaceplace && !this.lowArmor || selfDmg >= (double)CrystalUtil.getHealth((EntityLivingBase)CascadeAura.mc.player) || selfDmg >= targetDmg || this.maxSelfDamage.getValue() < selfDmg || maxDamage > targetDmg) continue;
                this.currentTarget = player;
                placePos = pos;
                maxDamage = targetDmg;
            }
        }
        if (!InventoryUtil.heldItem(Items.END_CRYSTAL, InventoryUtil.Hand.Both)) {
            return;
        }
        if (!this.placeTimer.passedMs(this.placeDelay.getValue().intValue())) {
            return;
        }
        if (maxDamage != 0.5) {
            RayTraceResult r;
            EnumFacing f;
            if (CascadeAura.mc.world.getBlockState(placePos.up()).getBlock() == Blocks.FIRE) {
                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, placePos.up(), EnumFacing.DOWN));
                mc.getConnection().sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, placePos.up(), EnumFacing.DOWN));
            }
            this.enumFacing = f = (r = CascadeAura.mc.world.rayTraceBlocks(new Vec3d(CascadeAura.mc.player.posX, CascadeAura.mc.player.posY + (double)CascadeAura.mc.player.getEyeHeight(), CascadeAura.mc.player.posZ), new Vec3d((double)placePos.x + 0.5, (double)placePos.y + 1.0, (double)placePos.z + 0.5))) == null || r.sideHit == null ? EnumFacing.UP : r.sideHit;
            this.rotateToPos(placePos, f);
            mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, InventoryUtil.heldItem(Items.END_CRYSTAL, InventoryUtil.Hand.Off) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)placePos.x, (float)placePos.y, (float)placePos.z));
            this.placeTimer.reset();
            this.placeSet.add(placePos);
            this.placingPos = placePos;
            this.renderPos = placePos;
            this.currentDamage = maxDamage;
            if (this.swing.getValue() != Swing.None && this.swingOn.getValue() != SwingOn.Break) {
                EnumHand hand = this.swing.getValue() == Swing.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
                CascadeAura.mc.player.swingArm(hand);
            }
        } else {
            this.renderPos = null;
        }
    }

    @Override
    @SubscribeEvent
    public void onRender3D(Render3DEvent e) {
        if (this.renderPos != null && this.positionRender.getValue().booleanValue()) {
            RenderUtil.drawBoxESP(this.renderPos, new Color(this.positionColor.getValue().getRed(), this.positionColor.getValue().getGreen(), this.positionColor.getValue().getBlue(), this.positionColor.getValue().getAlpha()), false, new Color(this.positionColor.getValue().getRed(), this.positionColor.getValue().getGreen(), this.positionColor.getValue().getBlue(), this.positionColor.getValue().getAlpha()), 1.5f, true, true, 60, true);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        CPacketPlayerTryUseItemOnBlock p;
        if (this.isDisabled() || CascadeAura.fullNullCheck() || !this.rotate.getValue().booleanValue() || e.getStage() != 0 || this.placingPos == null) {
            return;
        }
        if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            p = (CPacketPlayerTryUseItemOnBlock)e.getPacket();
            p.facingX = this.placingPos.getX();
            p.facingY = this.placingPos.getY();
            p.facingZ = this.placingPos.getZ();
        }
        if (e.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)e.getPacket();
            p.yaw = this.yaw;
            p.pitch = this.pitch;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        Entity en;
        Object object;
        SPacketSpawnObject p;
        if (this.isDisabled() || CascadeAura.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof SPacketSpawnObject) {
            p = (SPacketSpawnObject)e.getPacket();
            if (p.getType() != 51 || !this.placeSet.contains(new BlockPos(p.getX(), p.getY(), p.getZ()).down())) {
                return;
            }
            if (!(!this.predict.getValue().booleanValue() || this.cooldown.getValue().booleanValue() && Cascade.swapManager.hasSwapped())) {
                CPacketUseEntity pe = new CPacketUseEntity();
                pe.entityId = p.getEntityID();
                pe.action = CPacketUseEntity.Action.ATTACK;
                mc.getConnection().sendPacket((Packet)pe);
                if (this.packetDebug.getValue().booleanValue()) {
                    Command.sendMessage("@SpawnObject->attack");
                }
            }
        }
        if (e.getPacket() instanceof SPacketSoundEffect && this.sound.getValue().booleanValue() && (p = (SPacketSoundEffect)e.getPacket()).getCategory() == SoundCategory.BLOCKS && p.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            object = new ArrayList(CascadeAura.mc.world.loadedEntityList).iterator();
            while (object.hasNext()) {
                en = (Entity)object.next();
                if (!(en instanceof EntityEnderCrystal)) continue;
                double d = en.getDistance(p.getX(), p.getY(), p.getZ());
                Float f = CascadeAura.mc.player.canEntityBeSeen(en) ? this.breakRange.getValue() : this.breakWallRange.getValue();
                if (!(d <= (double)f.floatValue())) continue;
                en.setDead();
                mc.addScheduledTask(() -> {
                    CascadeAura.mc.world.removeEntity(en);
                    CascadeAura.mc.world.removeEntityDangerously(en);
                });
                if (!this.packetDebug.getValue().booleanValue()) continue;
                Command.sendMessage("@SoundEffect->attack");
            }
        }
        if (e.getPacket() instanceof SPacketExplosion && this.explosion.getValue().booleanValue() && (p = (SPacketExplosion)e.getPacket()).getStrength() == 6.0f) {
            object = new ArrayList(CascadeAura.mc.world.loadedEntityList).iterator();
            while (object.hasNext()) {
                en = (Entity)object.next();
                if (!(en instanceof EntityEnderCrystal)) continue;
                double d = en.getDistance(p.getX(), p.getY(), p.getZ());
                Float f = CascadeAura.mc.player.canEntityBeSeen(en) ? this.breakRange.getValue() : this.breakWallRange.getValue();
                if (!(d <= (double)f.floatValue())) continue;
                en.setDead();
                mc.addScheduledTask(() -> {
                    CascadeAura.mc.world.removeEntity(en);
                    CascadeAura.mc.world.removeEntityDangerously(en);
                });
                if (!this.packetDebug.getValue().booleanValue()) continue;
                Command.sendMessage("@Explosion->attack");
            }
        }
        if (e.getPacket() instanceof SPacketDestroyEntities && this.instant.getValue().booleanValue()) {
            p = (SPacketDestroyEntities)e.getPacket();
            for (int enID : p.getEntityIDs()) {
                Entity en2 = CascadeAura.mc.world.getEntityByID(enID);
                if (!(en2 instanceof EntityEnderCrystal)) continue;
                float f = en2.getDistanceToEntity((Entity)CascadeAura.mc.player);
                Float f2 = CascadeAura.mc.player.canEntityBeSeen(en2) ? this.breakRange.getValue() : this.breakWallRange.getValue();
                if (!(f <= f2.floatValue())) continue;
                en2.setDead();
                mc.addScheduledTask(() -> {
                    CascadeAura.mc.world.removeEntity(en2);
                    CascadeAura.mc.world.removeEntityDangerously(en2);
                });
                if (this.packetDebug.getValue().booleanValue()) {
                    Command.sendMessage("@DestroyEntities->attack");
                }
                if (!this.prePlace.getValue().booleanValue()) continue;
                mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(en2.posX, en2.posY, en2.posZ).down(), EnumFacing.UP, InventoryUtil.heldItem(Items.END_CRYSTAL, InventoryUtil.Hand.Off) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)en2.posX, (float)en2.posY, (float)en2.posZ));
                this.placeTimer.reset();
                if (this.packetDebug.getValue().booleanValue()) {
                    Command.sendMessage("@DestroyEntities->place");
                }
                RenderUtil.drawBlockCrossedESP(new BlockPos(en2.posX, en2.posY, en2.posZ).down(), new Color(120, 0, 255, 30), 1.0f, true);
            }
        }
    }

    void holdRotation() {
        if (this.placingPos != null && this.enumFacing != null && this.rotate.getValue().booleanValue()) {
            this.rotateToPos(this.placingPos, this.enumFacing);
        }
    }

    void rotateTo(Entity en) {
        if (!this.rotate.getValue().booleanValue()) {
            return;
        }
        float[] angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionEyes(mc.getRenderPartialTicks()), en.getPositionVector());
        this.yaw = angle[0];
        this.pitch = angle[1];
        if (this.debug.getValue().booleanValue()) {
            CascadeAura.mc.player.rotationYaw = angle[0];
            CascadeAura.mc.player.rotationYawHead = angle[0];
            CascadeAura.mc.player.rotationPitch = angle[1];
        }
    }

    void rotateToPos(BlockPos pos, EnumFacing f) {
        if (!this.rotate.getValue().booleanValue()) {
            return;
        }
        float[] angle = new float[]{};
        if (f == EnumFacing.UP) {
            Vec3d vec = new Vec3d((double)pos.up().getX() - 0.5, (double)((float)pos.up().getY() - 1.0f), (double)pos.up().getZ() + 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        } else if (f == EnumFacing.DOWN) {
            Vec3d vec = new Vec3d((double)pos.down().getX() - 0.5, (double)((float)pos.down().getY() - 1.0f), (double)pos.down().getZ() - 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        } else if (f == EnumFacing.WEST) {
            Vec3d vec = new Vec3d((double)pos.west().getX() - 0.5, (double)((float)pos.west().getY() - 1.0f), (double)pos.west().getZ() - 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        } else if (f == EnumFacing.EAST) {
            Vec3d vec = new Vec3d((double)pos.east().getX() - 0.5, (double)((float)pos.east().getY() - 1.0f), (double)pos.east().getZ() - 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        } else if (f == EnumFacing.NORTH) {
            Vec3d vec = new Vec3d((double)pos.north().getX() - 0.5, (double)((float)pos.north().getY() - 1.0f), (double)pos.north().getZ() - 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        } else if (f == EnumFacing.SOUTH) {
            Vec3d vec = new Vec3d((double)pos.south().getX() - 0.5, (double)((float)pos.south().getY() - 1.0f), (double)pos.south().getZ() - 0.5);
            angle = MathUtil.calcAngle(CascadeAura.mc.player.getPositionVector(), vec);
        }
        this.yaw = angle[0];
        this.pitch = angle[1];
        if (this.debug.getValue().booleanValue()) {
            CascadeAura.mc.player.rotationYaw = angle[0];
            CascadeAura.mc.player.rotationYawHead = angle[0];
            CascadeAura.mc.player.rotationPitch = angle[1];
        }
    }

    EntityPlayer getTarget() {
        EntityPlayer target = null;
        for (EntityPlayer p : CascadeAura.mc.world.playerEntities) {
            if (p == CascadeAura.mc.player || Cascade.friendManager.isFriend(p) || CascadeAura.mc.player.getDistanceToEntity((Entity)p) > this.range.getValue().floatValue() || !p.isEntityAlive()) continue;
            for (BlockPos pos : CrystalUtil.getSphere(this.placeRange.getValue().floatValue(), true)) {
                if (!CrystalUtil.canPlaceCrystal(pos, this.holePlacement.getValue() == false, this.placeWallRange.getValue().floatValue())) continue;
                double d = CrystalUtil.calculateDamage((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5, (Entity)p);
            }
            target = p;
        }
        return target;
    }

    static enum SwingOn {
        Break,
        Place,
        Both;

    }

    public static enum Swing {
        Mainhand,
        Offhand,
        None;

    }

    static enum Page {
        Place,
        Break,
        Calc,
        Misc,
        Render;

    }
}

