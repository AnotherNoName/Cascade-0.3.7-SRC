//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.event.events.PacketEvent;
import cascade.event.events.UpdateWalkingPlayerEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.player.HoleUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Strafe
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.General));
    Setting<Boolean> strict = this.register(new Setting<Object>("Strict", Boolean.valueOf(false), v -> this.page.getValue() == Page.General));
    Setting<Sneaking> sneaking = this.register(new Setting<Object>("Sneaking", (Object)Sneaking.Cancel, v -> this.page.getValue() == Page.General));
    Setting<Boolean> step = this.register(new Setting<Object>("Step", Boolean.valueOf(false), v -> this.page.getValue() == Page.General));
    Setting<Float> height = this.register(new Setting<Object>("Height", Float.valueOf(1.9f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.page.getValue() == Page.General && this.step.getValue() != false));
    Setting<Boolean> kbBoost = this.register(new Setting<Object>("KbBoost", Boolean.valueOf(true), v -> this.page.getValue() == Page.Knockback));
    Setting<Double> kbFactor = this.register(new Setting<Object>("KbFactor", Double.valueOf(6.0), Double.valueOf(1.0), Double.valueOf(12.0), v -> this.page.getValue() == Page.Knockback && this.kbBoost.getValue() != false));
    Setting<Boolean> kbStep = this.register(new Setting<Object>("StepOnBoost", Boolean.valueOf(true), v -> this.page.getValue() == Page.Knockback && this.kbBoost.getValue() != false));
    Setting<Float> kbHeight = this.register(new Setting<Object>("StepHeight", Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(2.0f), v -> this.page.getValue() == Page.Knockback && this.kbBoost.getValue() != false && this.kbStep.getValue() != false));
    static Strafe INSTANCE;
    boolean stepFlag;
    double distance;
    double lastDist;
    boolean boost;
    double speed;
    int stage;

    public Strafe() {
        super("Strafe", Module.Category.MOVEMENT, "");
        INSTANCE = this;
    }

    public static Strafe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Strafe();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (Strafe.mc.player != null) {
            this.speed = MovementUtil.getSpeed();
            this.distance = MovementUtil.getDistanceXZ();
        }
        this.stage = 4;
        this.lastDist = 0.0;
    }

    @Override
    public void onDisable() {
        if (Strafe.fullNullCheck()) {
            return;
        }
        if (this.stepFlag) {
            Strafe.mc.player.stepHeight = 0.6f;
        }
        this.stepFlag = false;
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (Strafe.fullNullCheck() || !MovementUtil.isMoving() || this.isDisabled() || this.sneaking.getValue() == Sneaking.Pause && Strafe.mc.player.isSneaking() || Strafe.mc.player.capabilities.isFlying || Cascade.packetManager.getCaughtPPS()) {
            return;
        }
        if (this.step.getValue().booleanValue()) {
            if (EntityUtil.isInLiquid()) {
                Strafe.mc.player.stepHeight = 0.6f;
            } else {
                MovementUtil.step(this.height.getValue().floatValue());
                this.stepFlag = true;
            }
        } else if (this.kbStep.getValue().booleanValue() && !this.shouldBoost(true)) {
            Strafe.mc.player.stepHeight = 0.6f;
        }
        if (EntityUtil.isInLiquid() || Strafe.mc.player.isOnLadder() || PlayerUtil.isClipping()) {
            return;
        }
        if (this.stage == 1) {
            this.speed = 1.35 * MovementUtil.calcEffects(2873.0) - 0.01;
        } else if (this.stage == 2) {
            if (!(EntityUtil.isInLiquid() || Strafe.mc.player.isInWeb || HoleUtil.isInHole(Strafe.mc.player.getPosition()) && (!HoleUtil.isInHole(Strafe.mc.player.getPosition()) || this.step.getValue().booleanValue() || this.shouldBoost(true) && this.kbStep.getValue().booleanValue()))) {
                double yMotion;
                Strafe.mc.player.motionY = yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                e.setY(yMotion);
            }
            if (this.shouldBoost(false) && this.kbStep.getValue().booleanValue()) {
                MovementUtil.step(this.kbHeight.getValue().floatValue());
                this.stepFlag = true;
            }
            this.speed = this.shouldBoost(false) ? this.kbFactor.getValue() / 10.0 : this.speed * (this.boost ? 1.6835 : 1.395);
        } else if (this.stage == 3) {
            if (this.shouldBoost(true) && this.kbStep.getValue().booleanValue()) {
                MovementUtil.step(this.kbHeight.getValue().floatValue());
                this.stepFlag = true;
            }
            this.speed = this.shouldBoost(false) ? this.kbFactor.getValue() / 10.0 : this.distance - 0.66 * (this.distance - MovementUtil.calcEffects(0.2873));
            this.boost = !this.boost;
        } else {
            if ((Strafe.mc.world.getCollisionBoxes(null, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.isCollidedVertically) && this.stage > 0) {
                this.stage = MovementUtil.isMoving() ? 1 : 0;
            }
            this.speed = this.distance - this.distance / 159.0;
        }
        this.speed = Math.min(this.speed, MovementUtil.calcEffects(10.0));
        this.speed = Math.max(this.speed, MovementUtil.calcEffects(0.2873));
        MovementUtil.strafe(e, this.strict.getValue() != false ? MovementUtil.getSpeed() : this.speed);
        ++this.stage;
    }

    @SubscribeEvent
    public void onWalkingUpdate(UpdateWalkingPlayerEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (!MovementUtil.isWasdPressed()) {
            MovementUtil.setMotion(0.0, Strafe.mc.player.motionY, 0.0);
        }
        this.distance = MovementUtil.getDistanceXZ();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (this.isDisabled() || Strafe.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            this.distance = 0.0;
            this.speed = 0.0;
            this.stage = 4;
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        CPacketEntityAction p;
        if (this.isDisabled() || Strafe.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketEntityAction && this.sneaking.getValue() == Sneaking.Cancel && (p = (CPacketEntityAction)e.getPacket()).getAction() == CPacketEntityAction.Action.START_SNEAKING) {
            Strafe.mc.player.setSneaking(false);
            e.setCanceled(true);
        }
    }

    boolean shouldBoost(boolean stepCheck) {
        return this.kbBoost.getValue() != false && Cascade.packetManager.getPacketE() != null && Cascade.packetManager.getCaughtE() && (double)Cascade.packetManager.getPacketE().getStrength() == 6.0 && (stepCheck || Strafe.mc.player.posY - Cascade.packetManager.getPacketE().posY >= -0.9) && Strafe.mc.player.getDistance(Cascade.packetManager.getPacketE().getX(), Cascade.packetManager.getPacketE().getY(), Cascade.packetManager.getPacketE().getZ()) <= 12.0;
    }

    static enum Sneaking {
        None,
        Pause,
        Cancel;

    }

    static enum Page {
        General,
        Knockback;

    }
}

