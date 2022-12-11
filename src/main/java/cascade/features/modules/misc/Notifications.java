//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.monster.EntityGhast
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.network.play.server.SPacketEntityEffect
 *  net.minecraft.potion.Potion
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.Cascade;
import cascade.event.events.PacketEvent;
import cascade.features.command.Command;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Notifications
extends Module {
    Setting<TextUtil.Color> color = this.register(new Setting<TextUtil.Color>("Color", TextUtil.Color.LIGHT_PURPLE));
    Setting<TextUtil.Color> accentColor = this.register(new Setting<TextUtil.Color>("AccentColor", TextUtil.Color.DARK_PURPLE));
    Setting<Boolean> popCounter = this.register(new Setting<Boolean>("PopCounter", true));
    public HashMap<String, Integer> pops = new HashMap();
    Setting<Boolean> visualRange = this.register(new Setting<Boolean>("VisualRange", true));
    Setting<Boolean> visualSound = this.register(new Setting<Object>("Sound", Boolean.valueOf(true), v -> this.visualRange.getValue()));
    List<EntityPlayer> players = new ArrayList<EntityPlayer>();
    Setting<Boolean> ghast = this.register(new Setting<Boolean>("Ghasts", false));
    Setting<Boolean> sound = this.register(new Setting<Object>("GhastSound", Boolean.valueOf(true), v -> this.ghast.getValue()));
    Set<Entity> ghasts = new HashSet<Entity>();
    Setting<Boolean> effects = this.register(new Setting<Boolean>("Effects", false));
    Setting<Boolean> ignoreSelf = this.register(new Setting<Object>("IgnoreSelf", Boolean.valueOf(false), v -> this.effects.getValue()));
    static Notifications INSTANCE;

    public Notifications() {
        super("Notifications", Module.Category.MISC, "h");
        INSTANCE = this;
    }

    public static Notifications getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.pops.clear();
        this.ghasts.clear();
        this.players.clear();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
        if (this.isDisabled() || Notifications.fullNullCheck()) {
            return;
        }
        if (this.effects.getValue().booleanValue() && e.getPacket() instanceof SPacketEntityEffect) {
            SPacketEntityEffect p = (SPacketEntityEffect)e.getPacket();
            if (Potion.getPotionById((int)p.getEffectId()) == MobEffects.ABSORPTION || Potion.getPotionById((int)p.getEffectId()) == MobEffects.REGENERATION || Potion.getPotionById((int)p.getEffectId()) == MobEffects.RESISTANCE || Potion.getPotionById((int)p.getEffectId()) == MobEffects.FIRE_RESISTANCE) {
                return;
            }
            for (Entity en : Notifications.mc.world.loadedEntityList) {
                boolean isSelf;
                if (!(en instanceof EntityPlayer) || en.getEntityId() != p.getEntityId()) continue;
                boolean bl = isSelf = en.getEntityId() == Notifications.mc.player.getEntityId();
                if (isSelf && this.ignoreSelf.getValue().booleanValue()) {
                    return;
                }
                Command.sendMessage(TextUtil.convertColorName(this.color.getValue()) + (isSelf ? "You" : en.getName()) + " " + TextUtil.convertColorName(this.accentColor.getValue()) + (isSelf ? "have" : "has") + " received " + TextUtil.convertColorName(this.color.getValue()) + "Pig" + TextUtil.convertColorName(this.accentColor.getValue()) + ", " + TextUtil.convertColorName(this.color.getValue()) + p.getDuration() + TextUtil.convertColorName(this.accentColor.getValue()) + "s");
            }
        }
    }

    @Override
    public void onUpdate() {
        if (Notifications.fullNullCheck()) {
            return;
        }
        for (EntityPlayer p : this.players) {
            if (Notifications.mc.world.playerEntities.contains(p)) continue;
            this.players.remove(p);
            Command.sendMessage((Cascade.friendManager.isFriend(p.getName()) ? ChatFormatting.AQUA : TextUtil.convertColorName(this.accentColor.getValue())) + p.getName() + TextUtil.convertColorName(this.color.getValue()) + "has left your visual range");
        }
        for (Entity e : Notifications.mc.world.getLoadedEntityList()) {
            if (e instanceof EntityPlayer && this.visualRange.getValue().booleanValue()) {
                if (e.getEntityId() == Notifications.mc.player.getEntityId()) continue;
                if (!this.players.contains(e)) {
                    this.players.add((EntityPlayer)e);
                    Command.sendMessage((Cascade.friendManager.isFriend(e.getName()) ? ChatFormatting.AQUA : TextUtil.convertColorName(this.accentColor.getValue())) + e.getName() + TextUtil.convertColorName(this.color.getValue()) + " has entered your visual range");
                    if (this.visualSound.getValue().booleanValue()) {
                        Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
                    }
                }
            }
            if (!(e instanceof EntityGhast) || !this.ghast.getValue().booleanValue() || this.ghasts.contains(e)) continue;
            int x = (int)e.posX;
            int y = (int)e.posY;
            int z = (int)e.posZ;
            Command.sendMessage(TextUtil.convertColorName(this.color.getValue()) + "Ghast" + TextUtil.convertColorName(this.accentColor.getValue()) + " has spawned at " + TextUtil.convertColorName(this.color.getValue()) + x + "x " + y + "y " + z + "z");
            this.ghasts.add(e);
            if (!this.sound.getValue().booleanValue()) continue;
            Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
        }
    }

    public void onDeath(EntityPlayer player) {
        if (this.pops.containsKey(player.getName())) {
            int i = this.pops.get(player.getName());
            this.pops.remove(player.getName());
            if (this.isEnabled() && this.popCounter.getValue().booleanValue()) {
                if (player == Notifications.mc.player) {
                    Command.sendRemovableMessage(Cascade.chatManager.getClientMessage() + " " + TextUtil.convertColorName(this.accentColor.getValue()) + "You" + TextUtil.convertColorName(this.color.getValue()) + " died after popping " + TextUtil.convertColorName(this.accentColor.getValue()) + i + TextUtil.convertColorName(this.color.getValue()) + (i == 1 ? " totem" : " totems"), -42069);
                } else {
                    Command.sendRemovableMessage(Cascade.chatManager.getClientMessage() + " " + TextUtil.convertColorName(this.accentColor.getValue()) + player.getName() + TextUtil.convertColorName(this.color.getValue()) + " died after popping " + TextUtil.convertColorName(this.accentColor.getValue()) + i + TextUtil.convertColorName(this.color.getValue()) + (i == 1 ? " totem" : " totems"), -42069);
                }
            }
        }
    }

    public void onTotemPop(EntityPlayer player) {
        if (Notifications.fullNullCheck()) {
            return;
        }
        int i = 1;
        if (this.pops.containsKey(player.getName())) {
            i = this.pops.get(player.getName());
            this.pops.put(player.getName(), ++i);
        } else {
            this.pops.put(player.getName(), i);
        }
        if (this.isEnabled() && this.popCounter.getValue().booleanValue()) {
            if (player == Notifications.mc.player) {
                Command.sendRemovableMessage(Cascade.chatManager.getClientMessage() + " " + TextUtil.convertColorName(this.accentColor.getValue()) + "You" + TextUtil.convertColorName(this.color.getValue()) + " have popped your " + TextUtil.convertColorName(this.accentColor.getValue()) + i + this.suffix(i) + TextUtil.convertColorName(this.color.getValue()) + " totem", -42069);
            } else {
                Command.sendRemovableMessage(Cascade.chatManager.getClientMessage() + " " + TextUtil.convertColorName(this.accentColor.getValue()) + player.getName() + TextUtil.convertColorName(this.color.getValue()) + " has popped their " + TextUtil.convertColorName(this.accentColor.getValue()) + i + this.suffix(i) + TextUtil.convertColorName(this.color.getValue()) + " totem", -1337);
            }
        }
    }

    String suffix(int num) {
        if (num == 1) {
            return "st";
        }
        if (num == 2) {
            return "nd";
        }
        if (num == 3) {
            return "rd";
        }
        if (num >= 4 && num < 21) {
            return "th";
        }
        int lastDigit = num % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        if (lastDigit == 3) {
            return "rd";
        }
        return "th";
    }
}

