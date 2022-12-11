/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakeLatency
extends Module {
    Setting<Float> factor = this.register(new Setting<Float>("Factor", Float.valueOf(1.3f), Float.valueOf(1.0f), Float.valueOf(2.5f)));
    Setting<Boolean> cancel = this.register(new Setting<Boolean>("Cancel", true));
    Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 50, 0, 250));
    Setting<Integer> toggle = this.register(new Setting<Integer>("Toggle", 20, 0, 250));
    Setting<Boolean> m1 = this.register(new Setting<Boolean>("m1", true));
    Timer timer = new Timer();

    public FakeLatency() {
        super("FakeLatency", Module.Category.PLAYER, "");
    }

    @Override
    public void onToggle() {
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (FakeLatency.fullNullCheck()) {
            return;
        }
        if (this.timer.passedMs(this.delay.getValue().intValue())) {
            Cascade.timerManager.set(this.factor.getValue().floatValue());
            if (this.m1.getValue().booleanValue()) {
                this.timer.reset();
            } else if (this.timer.passedMs(this.toggle.getValue().intValue())) {
                this.timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (this.isDisabled() || FakeLatency.fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue().intValue())) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (this.isDisabled() || FakeLatency.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketPlayer && !this.timer.passedMs(this.delay.getValue().intValue())) {
            e.setCanceled(this.cancel.getValue());
        }
    }
}

