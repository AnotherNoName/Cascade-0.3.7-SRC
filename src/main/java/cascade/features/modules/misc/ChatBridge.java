//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatBridge
extends Module {
    Setting<Time> time = this.register(new Setting<Time>("Time", Time.Local));
    Setting<Boolean> ignoreDeaths = this.register(new Setting<Boolean>("IgnoreDeaths", true));
    Setting<Boolean> ignoreLogs = this.register(new Setting<Boolean>("IgnoreLogs", true));
    private static ChatBridge INSTANCE;

    public ChatBridge() {
        super("ChatBridge", Module.Category.MISC, "discord chat bridge");
        INSTANCE = this;
    }

    public static ChatBridge getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ChatBridge();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) throws Exception {
        if (this.isDisabled() || ChatBridge.fullNullCheck()) {
            return;
        }
        if (e.getPacket() instanceof SPacketChat) {
            SPacketChat p = (SPacketChat)e.getPacket();
            String msg = p.getChatComponent().getUnformattedText();
            if (this.ignoreDeaths.getValue().booleanValue()) {
                // empty if block
            }
            if (!this.ignoreLogs.getValue().booleanValue() || msg.contains(" joined the game") || msg.contains("left the game")) {
                // empty if block
            }
            if (msg.contains("says: ") || msg.contains("whispers: ") || msg.contains("whisper")) {
                // empty if block
            }
            if (ChatBridge.mc.currentServerData != null) {
                this.sendMessageWeb("https://discord.com/api/webhooks/XXX", "" + ChatBridge.mc.player.getName(), "**[" + ChatBridge.mc.currentServerData.serverIP + "]" + this.getTimeString() + "** `" + msg + "`");
            }
        }
    }

    int sendMessageWeb(String url, String name, String message) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        String POST_PARAMS = "{ \"username\": \"" + name + "\", \"content\": \"" + message + "\" }";
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        Thread.sleep(1L);
        return con.getResponseCode();
    }

    String getTimeString() {
        String date = new SimpleDateFormat("k:mm").format(new Date());
        return "<" + date + ">";
    }

    static enum Time {
        Local;

    }
}

