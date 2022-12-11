//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package cascade.manager;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.Feature;
import cascade.features.gui.CascadeGui;
import cascade.features.modules.Module;
import cascade.features.modules.combat.Aura;
import cascade.features.modules.combat.AutoArmor;
import cascade.features.modules.combat.Burrow;
import cascade.features.modules.combat.CascadeAura;
import cascade.features.modules.combat.Crits;
import cascade.features.modules.combat.HoleFillR;
import cascade.features.modules.combat.Offhand;
import cascade.features.modules.combat.Surround;
import cascade.features.modules.combat.Trap;
import cascade.features.modules.core.ClickGui;
import cascade.features.modules.core.FontMod;
import cascade.features.modules.core.HUD;
import cascade.features.modules.core.Manager;
import cascade.features.modules.core.Menu;
import cascade.features.modules.exploit.ChorusDelay;
import cascade.features.modules.exploit.Clip;
import cascade.features.modules.exploit.CoordExploit;
import cascade.features.modules.exploit.HubTP;
import cascade.features.modules.exploit.LiquidFlight;
import cascade.features.modules.exploit.PacketFly;
import cascade.features.modules.hud.Coords;
import cascade.features.modules.hud.HUDManager;
import cascade.features.modules.hud.Ping;
import cascade.features.modules.hud.PotionInfo;
import cascade.features.modules.hud.PvpInfo;
import cascade.features.modules.hud.TextRadar;
import cascade.features.modules.hud.Watermark;
import cascade.features.modules.misc.AutoReply;
import cascade.features.modules.misc.ChatBridge;
import cascade.features.modules.misc.ChorusPredict;
import cascade.features.modules.misc.Debug;
import cascade.features.modules.misc.EntityTrace;
import cascade.features.modules.misc.FakePlayer;
import cascade.features.modules.misc.IgnoreUnicode;
import cascade.features.modules.misc.KillEffect;
import cascade.features.modules.misc.LogCoords;
import cascade.features.modules.misc.LogSpots;
import cascade.features.modules.misc.NoInteract;
import cascade.features.modules.misc.NoSuffocation;
import cascade.features.modules.misc.Notifications;
import cascade.features.modules.misc.TrueDurability;
import cascade.features.modules.movement.AirStrafe;
import cascade.features.modules.movement.AntiVoid;
import cascade.features.modules.movement.AutoCenter;
import cascade.features.modules.movement.FastFall;
import cascade.features.modules.movement.FastSwim;
import cascade.features.modules.movement.Flight;
import cascade.features.modules.movement.HoleSnap;
import cascade.features.modules.movement.LongJump;
import cascade.features.modules.movement.NoSlow;
import cascade.features.modules.movement.Sprint;
import cascade.features.modules.movement.Step;
import cascade.features.modules.movement.Strafe;
import cascade.features.modules.movement.TickBoost;
import cascade.features.modules.movement.Velocity;
import cascade.features.modules.movement.YPort;
import cascade.features.modules.player.AntiAim;
import cascade.features.modules.player.AntiLagback;
import cascade.features.modules.player.FakeLatency;
import cascade.features.modules.player.FastUse;
import cascade.features.modules.player.Freecam;
import cascade.features.modules.player.LiquidInteract;
import cascade.features.modules.player.Mine;
import cascade.features.modules.player.Nuker;
import cascade.features.modules.player.PacketUse;
import cascade.features.modules.player.Refill;
import cascade.features.modules.player.Scaffold;
import cascade.features.modules.player.XCarry;
import cascade.features.modules.visual.Ambience;
import cascade.features.modules.visual.Animations;
import cascade.features.modules.visual.BlockHighlight;
import cascade.features.modules.visual.CameraClip;
import cascade.features.modules.visual.Chams;
import cascade.features.modules.visual.Crosshair;
import cascade.features.modules.visual.CrystalChams;
import cascade.features.modules.visual.ESP;
import cascade.features.modules.visual.EntityTrails;
import cascade.features.modules.visual.GlintMod;
import cascade.features.modules.visual.HandMod;
import cascade.features.modules.visual.HitMarkers;
import cascade.features.modules.visual.HoleESP;
import cascade.features.modules.visual.MotionRender;
import cascade.features.modules.visual.Nametags;
import cascade.features.modules.visual.NoRender;
import cascade.features.modules.visual.OffscreenESP;
import cascade.features.modules.visual.PopChams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public ArrayList<Module> mods = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();

    public void init() {
        this.mods.add(new ClickGui());
        this.mods.add(new FontMod());
        this.mods.add(new HUD());
        this.mods.add(new Manager());
        this.mods.add(new Menu());
        this.mods.add(new Aura());
        this.mods.add(new AutoArmor());
        this.mods.add(new Burrow());
        this.mods.add(new Crits());
        this.mods.add(new CascadeAura());
        this.mods.add(new HoleFillR());
        this.mods.add(new Offhand());
        this.mods.add(new Surround());
        this.mods.add(new Trap());
        this.mods.add(new ChorusDelay());
        this.mods.add(new Clip());
        this.mods.add(new CoordExploit());
        this.mods.add(new HubTP());
        this.mods.add(new LiquidFlight());
        this.mods.add(new PacketFly());
        this.mods.add(new AutoReply());
        this.mods.add(new ChatBridge());
        this.mods.add(new ChorusPredict());
        this.mods.add(new Debug());
        this.mods.add(new EntityTrace());
        this.mods.add(new FakePlayer());
        this.mods.add(new IgnoreUnicode());
        this.mods.add(new KillEffect());
        this.mods.add(new LogCoords());
        this.mods.add(new LogSpots());
        this.mods.add(new NoInteract());
        this.mods.add(new NoSuffocation());
        this.mods.add(new Notifications());
        this.mods.add(new TrueDurability());
        this.mods.add(new Ambience());
        this.mods.add(new Animations());
        this.mods.add(new BlockHighlight());
        this.mods.add(new CameraClip());
        this.mods.add(new Chams());
        this.mods.add(new Crosshair());
        this.mods.add(new CrystalChams());
        this.mods.add(new EntityTrails());
        this.mods.add(new ESP());
        this.mods.add(new GlintMod());
        this.mods.add(new HitMarkers());
        this.mods.add(new HoleESP());
        this.mods.add(new MotionRender());
        this.mods.add(new Nametags());
        this.mods.add(new NoRender());
        this.mods.add(new OffscreenESP());
        this.mods.add(new PopChams());
        this.mods.add(new HandMod());
        this.mods.add(new AirStrafe());
        this.mods.add(new AntiVoid());
        this.mods.add(new AutoCenter());
        this.mods.add(new FastFall());
        this.mods.add(new FastSwim());
        this.mods.add(new Flight());
        this.mods.add(new HoleSnap());
        this.mods.add(new LongJump());
        this.mods.add(new NoSlow());
        this.mods.add(new Sprint());
        this.mods.add(new Step());
        this.mods.add(new Strafe());
        this.mods.add(new TickBoost());
        this.mods.add(new Velocity());
        this.mods.add(new YPort());
        this.mods.add(new AntiAim());
        this.mods.add(new AntiLagback());
        this.mods.add(new FakeLatency());
        this.mods.add(new FastUse());
        this.mods.add(new Freecam());
        this.mods.add(new LiquidInteract());
        this.mods.add(new Mine());
        this.mods.add(new Nuker());
        this.mods.add(new PacketUse());
        this.mods.add(new Refill());
        this.mods.add(new Scaffold());
        this.mods.add(new XCarry());
        this.mods.add(new Coords());
        this.mods.add(new HUDManager());
        this.mods.add(new Ping());
        this.mods.add(new PotionInfo());
        this.mods.add(new PvpInfo());
        this.mods.add(new TextRadar());
        this.mods.add(new Watermark());
    }

    public Module getModuleByName(String name) {
        for (Module module : this.mods) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.mods) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isEnabled();
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.mods) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.mods) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.mods.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.mods.forEach(arg_0 -> ((EventBus)MinecraftForge.EVENT_BUS).register(arg_0));
        this.mods.forEach(Module::onLoad);
    }

    public void onUpdate() {
        try {
            if (!ModuleManager.fullNullCheck()) {
                this.mods.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
            }
        }
        catch (Exception ex) {
            Cascade.LOGGER.info("Caught an exception from ModuleManager");
            ex.printStackTrace();
        }
    }

    public void onTick() {
        this.mods.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.mods.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.mods.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.mods.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.mods.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.mods.forEach(arg_0 -> ((EventBus)MinecraftForge.EVENT_BUS).unregister(arg_0));
    }

    public void onUnloadPost() {
        for (Module module : this.mods) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof CascadeGui) {
            return;
        }
        this.mods.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
}

