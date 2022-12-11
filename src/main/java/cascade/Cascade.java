//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.Mod$Instance
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package cascade;

import cascade.features.modules.core.Manager;
import cascade.manager.ChatManager;
import cascade.manager.ColorManager;
import cascade.manager.ConfigManager;
import cascade.manager.EventManager;
import cascade.manager.FriendManager;
import cascade.manager.InventoryManager;
import cascade.manager.ModuleManager;
import cascade.manager.PacketManager;
import cascade.manager.PlayerManager;
import cascade.manager.PositionManager;
import cascade.manager.PotionManager;
import cascade.manager.RotationManager;
import cascade.manager.ServerManager;
import cascade.manager.Simulation;
import cascade.manager.SpeedManager;
import cascade.manager.SwapManager;
import cascade.manager.TextManager;
import cascade.manager.TimerManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="cascade", name="Cascade", version="0.3.7")
public class Cascade {
    public static final String MODNAME = "Cascade";
    public static final String MODVER = "0.3.7";
    public static final Logger LOGGER = LogManager.getLogger((String)"Cascade");
    public static ChatManager chatManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ColorManager colorManager;
    public static InventoryManager inventoryManager;
    public static PacketManager packetManager;
    public static PlayerManager playerManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static SwapManager swapManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static Simulation simulation;
    public static EventManager eventManager;
    public static TimerManager timerManager;
    public static TextManager textManager;
    public static Minecraft mc;
    @Mod.Instance
    public static Cascade INSTANCE;
    private static boolean unloaded;

    public static void load() {
        LOGGER.info("\n\nLoading Cascade");
        unloaded = false;
        textManager = new TextManager();
        chatManager = new ChatManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        rotationManager = new RotationManager();
        eventManager = new EventManager();
        timerManager = new TimerManager();
        speedManager = new SpeedManager();
        swapManager = new SwapManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        packetManager = new PacketManager();
        playerManager = new PlayerManager();
        serverManager = new ServerManager();
        simulation = new Simulation();
        colorManager = new ColorManager();
        positionManager = new PositionManager();
        configManager = new ConfigManager();
        moduleManager.init();
        configManager.init();
        eventManager.init();
        textManager.init(true);
        moduleManager.onLoad();
        packetManager.load();
        playerManager.load();
        simulation.load();
        swapManager.load();
        timerManager.load();
        LOGGER.info("Cascade successfully loaded!\n");
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            timerManager.unload();
            moduleManager.onUnload();
            configManager.saveConfig(Cascade.configManager.config.replaceFirst("cascade/", ""));
            moduleManager.onUnloadPost();
            packetManager.unload();
            playerManager.unload();
            simulation.unload();
            swapManager.unload();
            timerManager.unload();
            unloaded = true;
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Display.setTitle((String)(Manager.getInstance().title.getValueAsString() == null ? "Cascade 0.3.7" : Manager.getInstance().title.getValueAsString()));
        Cascade.load();
    }

    static {
        mc = Minecraft.getMinecraft();
        unloaded = false;
    }
}

