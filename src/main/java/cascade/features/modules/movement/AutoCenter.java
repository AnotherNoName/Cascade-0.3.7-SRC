//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.Vec3d
 */
package cascade.features.modules.movement;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.HoleUtil;
import cascade.util.player.MovementUtil;
import cascade.util.player.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;

public class AutoCenter
extends Module {
    Setting<Boolean> always = this.register(new Setting<Boolean>("Always", true));
    Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.Instant));
    Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(2.6f), Float.valueOf(0.1f), Float.valueOf(15.0f), v -> this.mode.getValue() == Mode.Timer));

    public AutoCenter() {
        super("AutoCenter", Module.Category.MOVEMENT, "zaza");
    }

    @Override
    public void onEnable() {
        if (AutoCenter.fullNullCheck() || this.always.getValue().booleanValue()) {
            return;
        }
        try {
            this.doCenter();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (AutoCenter.fullNullCheck() || !this.always.getValue().booleanValue()) {
            return;
        }
        try {
            this.doCenter();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void doCenter() {
        if (!AutoCenter.mc.player.onGround || AutoCenter.mc.player.noClip || PlayerUtil.isInLiquid() || PlayerUtil.isChestBelow()) {
            return;
        }
        Vec3d pos = null;
        if (HoleUtil.is2x1(AutoCenter.mc.player.getPosition())) {
            pos = AutoCenter.getCenter(false);
        }
        if (HoleUtil.is2x2(AutoCenter.mc.player.getPosition())) {
            pos = AutoCenter.getCenter(true);
        }
        switch (this.mode.getValue()) {
            case Instant: {
                MovementUtil.setMotion(0.0, 0.0, 0.0);
                mc.getConnection().sendPacket((Packet)new CPacketPlayer.Position(pos.xCoord, AutoCenter.mc.player.posY, pos.zCoord, true));
                AutoCenter.mc.player.setPosition(pos.xCoord, AutoCenter.mc.player.posY, pos.zCoord);
                break;
            }
            case Motion: {
                MovementUtil.setMotion((pos.xCoord - AutoCenter.mc.player.posX) / 2.0, AutoCenter.mc.player.motionY, (pos.zCoord - AutoCenter.mc.player.posZ) / 2.0);
                break;
            }
        }
    }

    public static Vec3d getCenter(boolean is2x2) {
        double z;
        double y;
        double x;
        if (is2x2) {
            x = Math.floor(AutoCenter.mc.player.posX) + 0.5;
            y = Math.floor(AutoCenter.mc.player.posY);
            z = Math.floor(AutoCenter.mc.player.posZ) + 0.5;
        } else {
            x = Math.floor(AutoCenter.mc.player.posX) + 0.5;
            y = Math.floor(AutoCenter.mc.player.posY);
            z = Math.floor(AutoCenter.mc.player.posZ) + 0.5;
        }
        return new Vec3d(x, y, z);
    }

    static enum Mode {
        Instant,
        Motion,
        Timer;

    }
}

