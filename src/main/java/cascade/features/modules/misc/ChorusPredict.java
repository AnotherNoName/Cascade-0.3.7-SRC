//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChorusPredict
extends Module {
    Setting<Color> color = this.register(new Setting<Color>("Color", new Color(120, 0, 255, 20)));
    static ChorusPredict INSTANCE;
    Timer timer = new Timer();
    BlockPos pos;

    public ChorusPredict() {
        super("ChorusPredict", Module.Category.MISC, "");
        INSTANCE = this;
    }

    public static ChorusPredict getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ChorusPredict();
        }
        return INSTANCE;
    }

    @Override
    public void onToggle() {
        this.timer.reset();
        this.pos = null;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        SPacketSoundEffect p;
        if (e.getPacket() instanceof SPacketSoundEffect && this.isEnabled() && ((p = (SPacketSoundEffect)e.getPacket()).getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || p.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT)) {
            this.timer.reset();
            this.pos = new BlockPos(p.getX(), p.getY(), p.getZ());
        }
    }

    @Override
    public void onRender3D(Render3DEvent e) {
        if (this.pos == null || this.timer.passedMs(1000L)) {
            return;
        }
        RenderUtil.drawBoxESP(this.pos, new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), 255), 0.1f, true, true, this.color.getValue().getAlpha());
    }
}

