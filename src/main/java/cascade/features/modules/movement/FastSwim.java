//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.features.modules.Module;
import cascade.features.modules.exploit.LiquidFlight;
import cascade.features.modules.player.Freecam;
import cascade.features.setting.Setting;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastSwim
extends Module {
    Setting<Boolean> flight = this.register(new Setting<Boolean>("Flight", false));
    Setting<Double> wHorizontal = this.register(new Setting<Double>("WaterHorizontal", 2.0, 0.0, 20.0));
    Setting<Double> wUp = this.register(new Setting<Double>("WaterUp", 2.0, 0.0, 20.0));
    Setting<Double> wDown = this.register(new Setting<Double>("WaterDown", 2.0, 0.0, 20.0));
    Setting<Double> lHorizontal = this.register(new Setting<Double>("LavaHorizontal", 4.9, 0.0, 20.0));
    Setting<Double> lUp = this.register(new Setting<Double>("LavaUp", 3.0, 0.0, 20.0));
    Setting<Double> lDown = this.register(new Setting<Double>("LavaDown", 3.0, 0.0, 20.0));
    Setting<Boolean> kbBoost = this.register(new Setting<Boolean>("KbBoost", true));
    Setting<Boolean> onGroundBypass = this.register(new Setting<Object>("OnGroundBypass", Boolean.valueOf(true), v -> this.kbBoost.getValue()));
    Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(16.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), v -> this.kbBoost.getValue()));

    public FastSwim() {
        super("FastSwim", Module.Category.MOVEMENT, "Makes u go faster in liquids");
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        double downMultiplier;
        double multiplier;
        if (this.isDisabled()) {
            return;
        }
        if (!PlayerUtil.isInLiquid() || Freecam.getInstance().isEnabled() || Cascade.packetManager.getCaughtPPS()) {
            return;
        }
        mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)FastSwim.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        FastSwim.mc.player.setSprinting(true);
        double d = this.shouldBoost() ? (double)this.factor.getValue().floatValue() : (multiplier = FastSwim.mc.player.isInLava() ? this.lHorizontal.getValue() : this.wHorizontal.getValue() / 2.0);
        if (!LiquidFlight.getInstance().isEnabled() || !LiquidFlight.getInstance().isValid || LiquidFlight.getInstance().ticks <= 0) {
            if (this.shouldBoost() && this.onGroundBypass.getValue().booleanValue()) {
                FastSwim.mc.player.onGround = false;
            }
            if (MovementUtil.isMoving()) {
                e.setX(e.getX() * multiplier);
                e.setZ(e.getZ() * multiplier);
            }
        }
        double upMultiplier = FastSwim.mc.player.isInLava() ? this.lUp.getValue().doubleValue() : this.wUp.getValue().doubleValue();
        double d2 = downMultiplier = FastSwim.mc.player.isInLava() ? this.lDown.getValue().doubleValue() : this.wDown.getValue().doubleValue();
        if (FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && !FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
            e.setY(e.getY() * upMultiplier);
        } else if (FastSwim.mc.gameSettings.keyBindSneak.isKeyDown() && !FastSwim.mc.gameSettings.keyBindJump.isKeyDown()) {
            e.setY(e.getY() * downMultiplier);
        } else if (this.flight.getValue().booleanValue()) {
            e.setY(e.getY() * 0.01);
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent e) {
        if (FastSwim.fullNullCheck() || this.isDisabled() || Freecam.getInstance().isEnabled() || Cascade.packetManager.getCaughtPPS() || LiquidFlight.getInstance().isEnabled()) {
            return;
        }
        if (FastSwim.mc.gameSettings.keyBindSneak.isKeyDown() && PlayerUtil.isInLiquid() && MovementUtil.isMoving()) {
            e.getMovementInput().moveStrafe *= 5.0f;
            e.getMovementInput().field_192832_b *= 5.0f;
        }
    }

    boolean shouldBoost() {
        return this.kbBoost.getValue() != false && (!FastSwim.mc.player.onGround || this.onGroundBypass.getValue() != false) && Cascade.packetManager.isValid();
    }
}

