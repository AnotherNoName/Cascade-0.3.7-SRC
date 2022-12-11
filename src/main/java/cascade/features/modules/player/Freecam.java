//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketSetPassengers
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.event.events.PacketEvent;
import cascade.event.events.PushEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.MathUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.TargetUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freecam
extends Module {
    Setting<Float> speed = this.register(new Setting<Float>("Speed", Float.valueOf(0.2f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    Setting<Boolean> view = this.register(new Setting<Boolean>("3D", false));
    Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    Setting<Boolean> legit = this.register(new Setting<Boolean>("Legit", false));
    Setting<Boolean> feetTeleport = this.register(new Setting<Boolean>("TargetFeetTP", false));
    Setting<Boolean> copyYawPitch = this.register(new Setting<Object>("CopyYawPitch", Boolean.valueOf(false), v -> this.feetTeleport.getValue()));
    AxisAlignedBB oldBoundingBox;
    public static EntityOtherPlayerMP entity;
    static Freecam INSTANCE;
    Vec3d position;
    Entity riding;
    float yaw;
    float pitch;

    public Freecam() {
        super("Freecam", Module.Category.PLAYER, "");
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Freecam getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Freecam();
        }
        return INSTANCE;
    }

    public static EntityOtherPlayerMP getFreecamEntity() {
        if (INSTANCE.isEnabled() && entity != null) {
            return entity;
        }
        return null;
    }

    @Override
    public void onEnable() {
        EntityPlayer target;
        if (Freecam.fullNullCheck()) {
            return;
        }
        this.oldBoundingBox = Freecam.mc.player.getEntityBoundingBox();
        Freecam.mc.player.setEntityBoundingBox(new AxisAlignedBB(Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ, Freecam.mc.player.posX, Freecam.mc.player.posY, Freecam.mc.player.posZ));
        if (Freecam.mc.player.getRidingEntity() != null) {
            this.riding = Freecam.mc.player.getRidingEntity();
            Freecam.mc.player.dismountRidingEntity();
        }
        entity = new EntityOtherPlayerMP((World)Freecam.mc.world, Freecam.mc.session.getProfile());
        entity.copyLocationAndAnglesFrom((Entity)Freecam.mc.player);
        Freecam.entity.rotationYaw = Freecam.mc.player.rotationYaw;
        Freecam.entity.rotationYawHead = Freecam.mc.player.rotationYawHead;
        Freecam.entity.inventory.copyInventory(Freecam.mc.player.inventory);
        Freecam.mc.world.addEntityToWorld(726804364, (Entity)entity);
        this.position = Freecam.mc.player.getPositionVector();
        this.yaw = Freecam.mc.player.rotationYaw;
        this.pitch = Freecam.mc.player.rotationPitch;
        Freecam.mc.player.noClip = true;
        if (this.feetTeleport.getValue().booleanValue() && (target = TargetUtil.getTarget(7.5)) != null) {
            Freecam.mc.player.setPosition(target.posX, target.posY - 1.0, target.posZ);
            if (this.copyYawPitch.getValue().booleanValue()) {
                Freecam.mc.player.rotationYaw = target.rotationYaw;
                Freecam.mc.player.rotationPitch = target.rotationPitch;
            }
        }
    }

    @Override
    public void onDisable() {
        if (Freecam.fullNullCheck()) {
            return;
        }
        Freecam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
        if (this.riding != null) {
            Freecam.mc.player.startRiding(this.riding, true);
        }
        if (entity != null) {
            Freecam.mc.world.removeEntity((Entity)entity);
        }
        if (this.position != null) {
            Freecam.mc.player.setPosition(this.position.xCoord, this.position.yCoord, this.position.zCoord);
        }
        Freecam.mc.player.rotationYaw = this.yaw;
        Freecam.mc.player.rotationPitch = this.pitch;
        Freecam.mc.player.noClip = false;
    }

    @Override
    public void onUpdate() {
        if (Freecam.fullNullCheck()) {
            return;
        }
        Freecam.mc.player.noClip = true;
        Freecam.mc.player.setVelocity(0.0, 0.0, 0.0);
        Freecam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        double[] dir = MathUtil.directionSpeed(this.speed.getValue().floatValue());
        if (MovementUtil.isMoving()) {
            MovementUtil.setMotion(dir[0], Freecam.mc.player.motionY, dir[1]);
        } else {
            Freecam.mc.player.motionX = 0.0;
            Freecam.mc.player.motionZ = 0.0;
        }
        Freecam.mc.player.setSprinting(false);
        if (this.view.getValue().booleanValue() && !Freecam.mc.gameSettings.keyBindSneak.isKeyDown() && !Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY = (double)this.speed.getValue().floatValue() * -MathUtil.degToRad(Freecam.mc.player.rotationPitch) * (double)Freecam.mc.player.movementInput.field_192832_b;
        }
        if (Freecam.mc.gameSettings.keyBindJump.isKeyDown()) {
            Freecam.mc.player.motionY += (double)this.speed.getValue().floatValue();
        }
        if (Freecam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Freecam.mc.player.motionY -= (double)this.speed.getValue().floatValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.isEnabled()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (this.isDisabled()) {
            return;
        }
        if (this.legit.getValue().booleanValue() && entity != null && e.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer)e.getPacket()).x = Freecam.entity.posX;
            ((CPacketPlayer)e.getPacket()).y = Freecam.entity.posY;
            ((CPacketPlayer)e.getPacket()).z = Freecam.entity.posZ;
            return;
        }
        if (this.packet.getValue().booleanValue()) {
            if (e.getPacket() instanceof CPacketPlayer) {
                e.setCanceled(true);
            }
        } else if (!(e.getPacket() instanceof CPacketUseEntity || e.getPacket() instanceof CPacketPlayerTryUseItem || e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock || e.getPacket() instanceof CPacketPlayer || e.getPacket() instanceof CPacketVehicleMove || e.getPacket() instanceof CPacketChatMessage || e.getPacket() instanceof CPacketKeepAlive)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        Entity riding;
        if (this.isDisabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketSetPassengers && (riding = Freecam.mc.world.getEntityByID(((SPacketSetPassengers)e.getPacket()).getEntityId())) != null && riding == this.riding) {
            this.riding = null;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook p = (SPacketPlayerPosLook)e.getPacket();
            if (this.packet.getValue().booleanValue()) {
                if (entity != null) {
                    entity.setPositionAndRotation(p.getX(), p.getY(), p.getZ(), p.getYaw(), p.getPitch());
                }
                this.position = new Vec3d(p.getX(), p.getY(), p.getZ());
                mc.getConnection().sendPacket((Packet)new CPacketConfirmTeleport(p.getTeleportId()));
                e.setCanceled(true);
            } else {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent e) {
        if (e.getStage() == 1 && this.isEnabled()) {
            e.setCanceled(true);
        }
    }
}

