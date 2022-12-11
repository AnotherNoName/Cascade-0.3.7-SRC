//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 */
package cascade.manager;

import cascade.features.Feature;
import cascade.features.modules.hud.HUDManager;
import cascade.features.modules.hud.PotionInfo;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionManager
extends Feature {
    private final Map<EntityPlayer, PotionList> potions = new ConcurrentHashMap<EntityPlayer, PotionList>();

    public List<PotionEffect> getOwnPotions() {
        return this.getPlayerPotions((EntityPlayer)PotionManager.mc.player);
    }

    public List<PotionEffect> getPlayerPotions(EntityPlayer player) {
        PotionList list = this.potions.get(player);
        List<PotionEffect> potions = new ArrayList<PotionEffect>();
        if (list != null) {
            potions = list.getEffects();
        }
        return potions;
    }

    public PotionEffect[] getImportantPotions(EntityPlayer player) {
        PotionEffect[] array = new PotionEffect[3];
        for (PotionEffect effect : this.getPlayerPotions(player)) {
            Potion potion = effect.getPotion();
            switch (I18n.format((String)potion.getName(), (Object[])new Object[0]).toLowerCase()) {
                case "strength": {
                    array[0] = effect;
                }
                case "weakness": {
                    array[1] = effect;
                }
                case "speed": {
                    array[2] = effect;
                }
            }
        }
        return array;
    }

    public String getPotionString(PotionEffect effect) {
        Potion potion = effect.getPotion();
        return I18n.format((String)potion.getName(), (Object[])new Object[0]) + " " + (effect.getAmplifier() + 1) + " " + ChatFormatting.WHITE + Potion.getPotionDurationString((PotionEffect)effect, (float)1.0f);
    }

    public String getPotionStringHUD(PotionEffect effect) {
        Potion potion = effect.getPotion();
        String string = potion.getName();
        if (PotionInfo.getInstance().shortNames.getValue().booleanValue()) {
            // empty if block
        }
        return I18n.format((String)string, (Object[])new Object[0]) + " " + (PotionInfo.getInstance().showLevel.getValue() != false ? Integer.valueOf(effect.getAmplifier() + 1) : "") + (PotionInfo.getInstance().colon.getValue() != false ? ": " : " ") + (Object)((Object)HUDManager.getInstance().infoColor.getValue()) + Potion.getPotionDurationString((PotionEffect)effect, (float)1.0f);
    }

    public String getColoredPotionString(PotionEffect effect) {
        return this.getPotionString(effect);
    }

    public String getColoredPotionStringHud(PotionEffect effect) {
        return this.getPotionStringHUD(effect);
    }

    public static class PotionList {
        private final List<PotionEffect> effects = new ArrayList<PotionEffect>();

        public void addEffect(PotionEffect effect) {
            if (effect != null) {
                this.effects.add(effect);
            }
        }

        public List<PotionEffect> getEffects() {
            return this.effects;
        }
    }
}

