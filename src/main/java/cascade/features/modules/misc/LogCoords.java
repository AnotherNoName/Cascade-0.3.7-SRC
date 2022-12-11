//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 */
package cascade.features.modules.misc;

import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class LogCoords
extends Module {
    Setting<Integer> maxRadius = this.register(new Setting<Integer>("MaxRadius", 500, 100, 1000));

    public LogCoords() {
        super("LogCoords", Module.Category.MISC, "");
    }

    @SubscribeEvent
    public void onPlayerLeave(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        if (!mc.isSingleplayer() && this.isEnabled() && !LogCoords.fullNullCheck()) {
            if (LogCoords.mc.player.posX > (double)this.maxRadius.getValue().intValue() || LogCoords.mc.player.posZ > (double)this.maxRadius.getValue().intValue()) {
                return;
            }
            int x = (int)LogCoords.mc.player.posX;
            int y = (int)LogCoords.mc.player.posY;
            int z = (int)LogCoords.mc.player.posZ;
            String coords = "Logout coords: " + x + "x  " + y + "y " + z + "z";
            StringSelection data = new StringSelection(coords);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(data, data);
        }
    }
}

