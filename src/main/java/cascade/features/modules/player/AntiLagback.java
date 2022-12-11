//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 *  net.minecraft.network.play.server.SPacketPlayerPosLook$EnumFlags
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.player;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiLagback
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Rotation));
    Setting<Rotations> rotations = this.register(new Setting<Object>("Rotations", (Object)Rotations.Packet, v -> this.page.getValue() == Page.Rotation));
    Setting<Boolean> removeRotFlags = this.register(new Setting<Object>("RemoveRotFlags", Boolean.valueOf(true), v -> this.page.getValue() == Page.Rotation && this.rotations.getValue() != Rotations.None));
    Setting<Mode> mode = this.register(new Setting<Object>("PosMode", (Object)Mode.None, v -> this.page.getValue() == Page.Position));
    Setting<Boolean> removePosFlags = this.register(new Setting<Object>("RemovePosFlags", Boolean.valueOf(false), v -> this.page.getValue() == Page.Position && this.mode.getValue() != Mode.None));

    public AntiLagback() {
        super("AntiLagback", Module.Category.PLAYER, "");
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (AntiLagback.fullNullCheck() || this.isDisabled() || AntiLagback.mc.currentScreen instanceof GuiDownloadTerrain) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook p = (SPacketPlayerPosLook)e.getPacket();
            if (this.mode.getValue() == Mode.Down) {
                p.y = -999.0;
            }
            if (this.mode.getValue() == Mode.Motion) {
                AntiLagback.mc.player.motionY = -10.0;
            }
            if (this.mode.getValue() != Mode.None && this.removePosFlags.getValue().booleanValue()) {
                p.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y);
            }
            float yaw = AntiLagback.mc.player.rotationYaw;
            float pitch = AntiLagback.mc.player.rotationPitch;
            if (this.rotations.getValue() == Rotations.Packet) {
                ((SPacketPlayerPosLook)e.getPacket()).yaw = yaw;
                ((SPacketPlayerPosLook)e.getPacket()).pitch = pitch;
            }
            if (this.rotations.getValue() == Rotations.New) {
                // empty if block
            }
            if (this.rotations.getValue() != Rotations.None && this.removeRotFlags.getValue().booleanValue()) {
                p.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                p.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
            }
        }
    }

    static enum Mode {
        None,
        Down,
        Motion;

    }

    static enum Rotations {
        None,
        Packet,
        New;

    }

    static enum Page {
        Rotation,
        Position;

    }
}

