//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.client.settings.IKeyConflictContext
 *  net.minecraftforge.client.settings.KeyConflictContext
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 */
package cascade.features.modules.movement;

import cascade.Cascade;
import cascade.event.events.MoveEvent;
import cascade.features.modules.Module;
import cascade.features.modules.player.Freecam;
import cascade.features.setting.Setting;
import cascade.mixin.mixins.accessor.ITimer;
import cascade.util.player.MovementUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow
extends Module {
    Setting<Boolean> invMove = this.register(new Setting<Boolean>("InvMove", true));
    Setting<Boolean> ice = this.register(new Setting<Boolean>("Ice", true));
    public Setting<Boolean> soulSand = this.register(new Setting<Boolean>("SoulSand", true));
    Setting<Boolean> strict = this.register(new Setting<Boolean>("Strict", false));
    Setting<Boolean> webs = this.register(new Setting<Boolean>("Webs", true));
    Setting<WebMode> webMode = this.register(new Setting<Object>("WebMode", (Object)WebMode.Motion, v -> this.webs.getValue()));
    Setting<Float> factor = this.register(new Setting<Object>("Factor", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), v -> this.webs.getValue()));
    Setting<Boolean> strafe = this.register(new Setting<Object>("Strafe", Boolean.valueOf(true), v -> this.webs.getValue()));
    private static NoSlow INSTANCE;
    static KeyBinding[] keys;

    public NoSlow() {
        super("NoSlow", Module.Category.MOVEMENT, "omg no slow");
        INSTANCE = this;
    }

    public static NoSlow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlow();
        }
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        if (NoSlow.fullNullCheck()) {
            return;
        }
        if (this.webs.getValue().booleanValue() && this.webMode.getValue() == WebMode.Timer && ((ITimer)NoSlow.mc.timer).getTickLength() != 50.0f) {
            Cascade.timerManager.reset();
        }
    }

    @Override
    public void onUpdate() {
        if (NoSlow.fullNullCheck()) {
            return;
        }
        if (this.ice.getValue().booleanValue()) {
            Blocks.ICE.setDefaultSlipperiness(0.6f);
            Blocks.PACKED_ICE.setDefaultSlipperiness(0.6f);
            Blocks.FROSTED_ICE.setDefaultSlipperiness(0.6f);
        }
        if (this.invMove.getValue().booleanValue()) {
            if (NoSlow.mc.currentScreen instanceof GuiChat || NoSlow.mc.currentScreen == null) {
                return;
            }
            for (KeyBinding bind : keys) {
                if (Keyboard.isKeyDown((int)bind.getKeyCode())) {
                    if (bind.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
                        bind.setKeyConflictContext((IKeyConflictContext)KeyConflictContext.UNIVERSAL);
                    }
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)true);
                    continue;
                }
                KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
            }
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent e) {
        if (this.isDisabled()) {
            return;
        }
        if (this.webs.getValue().booleanValue() && NoSlow.mc.player.isInWeb) {
            if (Freecam.getInstance().isEnabled()) {
                return;
            }
            if (this.strafe.getValue().booleanValue()) {
                MovementUtil.strafe(e, MovementUtil.getSpeed());
            }
            if (this.webMode.getValue() == WebMode.Motion) {
                if (NoSlow.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    e.setY(-this.factor.getValue().floatValue());
                }
            } else if (!NoSlow.mc.player.onGround && NoSlow.mc.gameSettings.keyBindSneak.isKeyDown()) {
                Cascade.timerManager.set(this.factor.getValue().floatValue());
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent e) {
        if (NoSlow.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            if (this.strict.getValue().booleanValue()) {
                mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(NoSlow.mc.player.inventory.currentItem));
            }
            e.getMovementInput().moveStrafe *= 5.0f;
            e.getMovementInput().field_192832_b *= 5.0f;
        }
    }

    static {
        keys = new KeyBinding[]{NoSlow.mc.gameSettings.keyBindForward, NoSlow.mc.gameSettings.keyBindRight, NoSlow.mc.gameSettings.keyBindBack, NoSlow.mc.gameSettings.keyBindLeft, NoSlow.mc.gameSettings.keyBindJump, NoSlow.mc.gameSettings.keyBindSprint};
    }

    static enum WebMode {
        Motion,
        Timer;

    }
}

