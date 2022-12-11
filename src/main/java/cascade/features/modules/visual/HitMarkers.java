//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.visual;

import cascade.event.events.PacketEvent;
import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HitMarkers
extends Module {
    Setting<Integer> time = this.register(new Setting<Integer>("Time", 50, 0, 1000));
    Setting<Float> size = this.register(new Setting<Float>("Size", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(8.0f)));
    Timer timer = new Timer();

    public HitMarkers() {
        super("HitMarkers", Module.Category.VISUAL, "draws hitmarkers when u hit something");
    }

    @Override
    public void onDisable() {
        this.timer.reset();
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        CPacketUseEntity p;
        if (this.isDisabled() || HitMarkers.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof CPacketUseEntity && (p = (CPacketUseEntity)e.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            this.timer.reset();
        }
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (HitMarkers.fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.time.getValue().intValue())) {
            ScaledResolution r = new ScaledResolution(mc);
            RenderUtil.drawLine((float)r.getScaledWidth() / 2.0f - 4.0f, (float)r.getScaledHeight() / 2.0f - 4.0f, (float)r.getScaledWidth() / 2.0f - this.size.getValue().floatValue(), (float)r.getScaledHeight() / 2.0f - this.size.getValue().floatValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine((float)r.getScaledWidth() / 2.0f + 4.0f, (float)r.getScaledHeight() / 2.0f - 4.0f, (float)r.getScaledWidth() / 2.0f + this.size.getValue().floatValue(), (float)r.getScaledHeight() / 2.0f - this.size.getValue().floatValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine((float)r.getScaledWidth() / 2.0f - 4.0f, (float)r.getScaledHeight() / 2.0f + 4.0f, (float)r.getScaledWidth() / 2.0f - this.size.getValue().floatValue(), (float)r.getScaledHeight() / 2.0f + this.size.getValue().floatValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
            RenderUtil.drawLine((float)r.getScaledWidth() / 2.0f + 4.0f, (float)r.getScaledHeight() / 2.0f + 4.0f, (float)r.getScaledWidth() / 2.0f + this.size.getValue().floatValue(), (float)r.getScaledHeight() / 2.0f + this.size.getValue().floatValue(), 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        }
    }
}

