//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 */
package cascade.features.modules.player;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.misc.Timer;
import cascade.util.player.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class FastUse
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.XP));
    Setting<Boolean> packet = this.register(new Setting<Object>("Packet", Boolean.valueOf(false), v -> this.page.getValue() == Page.XP));
    Setting<Integer> runs = this.register(new Setting<Object>("Runs", Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(16), v -> this.page.getValue() == Page.XP && this.packet.getValue() != false));
    Setting<Integer> delay = this.register(new Setting<Object>("Delay", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000), v -> this.page.getValue() == Page.XP && this.packet.getValue() != false));
    Setting<Boolean> fastPlace = this.register(new Setting<Object>("FastPlace", Boolean.valueOf(false), v -> this.page.getValue() == Page.Blocks));
    Timer timer = new Timer();
    boolean sentPackets;

    public FastUse() {
        super("FastUse", Module.Category.PLAYER, "fast use,");
    }

    @Override
    public void onUpdate() {
        if (FastUse.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.heldItem(Items.EXPERIENCE_BOTTLE, InventoryUtil.Hand.Both) && FastUse.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            FastUse.mc.rightClickDelayTimer = 0;
            if (this.sentPackets && this.timer.passedMs(this.delay.getValue().intValue())) {
                this.sentPackets = false;
                this.timer.reset();
            }
            if (this.packet.getValue().booleanValue() && !this.sentPackets) {
                try {
                    for (int i = 1; i < this.runs.getValue(); ++i) {
                        mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItem(InventoryUtil.heldItem(Items.EXPERIENCE_BOTTLE, InventoryUtil.Hand.Main) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
                        this.sentPackets = true;
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (this.fastPlace.getValue().booleanValue()) {
            FastUse.mc.rightClickDelayTimer = 0;
            FastUse.mc.playerController.blockHitDelay = 0;
        }
    }

    static enum Page {
        XP,
        Blocks;

    }
}

