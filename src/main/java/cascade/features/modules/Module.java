//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules;

import cascade.Cascade;
import cascade.event.events.ClientEvent;
import cascade.event.events.ModuleToggleEvent;
import cascade.event.events.Render2DEvent;
import cascade.event.events.Render3DEvent;
import cascade.features.Feature;
import cascade.features.modules.core.Manager;
import cascade.features.setting.Bind;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Module
extends Feature {
    String description;
    Category category;
    public Setting<Boolean> message = this.register(new Setting<Boolean>("Message", true));
    public Setting<Boolean> enabled = this.register(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.register(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<Bind>("Keybind", new Bind(-1)));
    public String name;
    Module module;

    public Module(String name, Category category, String description) {
        super(name);
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public String getDisplayInfo() {
        return null;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(true);
        this.onToggle();
        this.onEnable();
        if (this.module.shouldNotify()) {
            TextComponentString text = new TextComponentString(Cascade.chatManager.getClientMessage() + " " + TextUtil.coloredString(this.module.getName(), Manager.getInstance().textMain.getValue()) + ChatFormatting.GREEN + " enabled.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)text, 1);
        }
        MinecraftForge.EVENT_BUS.post((Event)new ModuleToggleEvent.Enable(this));
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void disable() {
        this.enabled.setValue(false);
        this.onToggle();
        this.onDisable();
        if (this.module.shouldNotify()) {
            TextComponentString text = new TextComponentString(Cascade.chatManager.getClientMessage() + " " + TextUtil.coloredString(this.module.getName(), Manager.getInstance().textMain.getValue()) + ChatFormatting.RED + " disabled.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)text, 1);
        }
        MinecraftForge.EVENT_BUS.post((Event)new ModuleToggleEvent.Disable(this));
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean shouldNotify() {
        return this.message.getValue();
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public String getFullArrayString() {
        return this.getName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    @SubscribeEvent
    public void onModuleToggle(ClientEvent e) {
        if (e.getStage() == 0 || e.getStage() == 1) {
            this.module = (Module)e.getFeature();
        }
    }

    public static enum Category {
        VISUAL("Visual"),
        COMBAT("Combat"),
        EXPLOIT("Exploit"),
        MISC("Misc"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CORE("Core"),
        HUD("HUD");

        String name;

        private Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

