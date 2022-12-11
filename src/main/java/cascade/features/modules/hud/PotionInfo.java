//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.potion.PotionEffect
 */
package cascade.features.modules.hud;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.hud.HUDManager;
import cascade.features.setting.Setting;
import java.util.ArrayList;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class PotionInfo
extends Module {
    Setting<Page> page = this.register(new Setting<Page>("Page", Page.Potions));
    public Setting<Boolean> showLevel = this.register(new Setting<Object>("ShowLevel", Boolean.valueOf(false), v -> this.page.getValue() == Page.Settings));
    Setting<Boolean> shortnames = this.register(new Setting<Boolean>("ShortNames", true));
    public Setting<Boolean> colon = this.register(new Setting<Object>("Colon", Boolean.valueOf(true), v -> this.page.getValue() == Page.Settings));
    public Setting<Boolean> shortNames = this.register(new Setting<Object>("ShortNames", Boolean.valueOf(true), v -> this.page.getValue() == Page.Settings));
    Setting<Boolean> syncHUD = this.register(new Setting<Object>("SyncHUD", Boolean.valueOf(true), v -> this.page.getValue() == Page.Settings));
    Setting<Integer> x = this.register(new Setting<Object>("X", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.page.getValue() == Page.Settings && this.syncHUD.getValue() == false));
    Setting<Integer> y = this.register(new Setting<Object>("Y", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.page.getValue() == Page.Settings && this.syncHUD.getValue() == false));
    static PotionInfo INSTANCE;

    public PotionInfo() {
        super("PotionInfo", Module.Category.HUD, "");
        INSTANCE = this;
    }

    public static PotionInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PotionInfo();
        }
        return INSTANCE;
    }

    @Override
    public void onRender2D(Render2DEvent e) {
        if (PotionInfo.fullNullCheck()) {
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int i = HUDManager.getInstance().i;
        String speed = "";
        String weankess = "";
        String strength = "";
        if (PotionInfo.mc.player.isPotionActive(MobEffects.SPEED)) {
            speed = (this.shortNames.getValue() != false ? "Spd" : "Speed") + (this.showLevel.getValue() != false ? Integer.valueOf(PotionInfo.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier()) : "");
        }
        if (PotionInfo.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            weankess = (this.shortNames.getValue() != false ? "Wkns" : "Weakness") + (this.showLevel.getValue() != false ? Integer.valueOf(PotionInfo.mc.player.getActivePotionEffect(MobEffects.WEAKNESS).getAmplifier()) : "");
            i += 10;
        }
        if (PotionInfo.mc.player.isPotionActive(MobEffects.STRENGTH)) {
            strength = (this.shortNames.getValue() != false ? "Str" : "Strength") + (this.showLevel.getValue() != false ? Integer.valueOf(PotionInfo.mc.player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier()) : "");
            i += 10;
        }
        ArrayList effects = new ArrayList(PotionInfo.mc.player.getActivePotionEffects());
        for (PotionEffect potionEffect : effects) {
            if (potionEffect.getPotion().getName() == MobEffects.WEAKNESS.toString()) {
                i += 10;
            } else if (potionEffect.getPotion().getName() == MobEffects.SPEED.toString()) {
                i += 10;
            } else if (potionEffect.getPotion().getName() == MobEffects.STRENGTH.toString()) {
                i += 10;
            } else {
                return;
            }
            String string = Cascade.potionManager.getColoredPotionString(potionEffect);
            this.renderer.drawString(string, width - this.renderer.getStringWidth(string) - 2, 2 + i++ * 10, potionEffect.getPotion().getLiquidColor(), true);
        }
        this.renderer.drawString(speed, this.syncHUD.getValue() != false ? (float)(width - this.renderer.getStringWidth(speed) - 2) : (float)this.x.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? (float)(HUDManager.getInstance().renderingUp.getValue() != false ? height - 2 - i : 2 + i++ * 10) : (float)this.y.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? HUDManager.getInstance().getColor() : HUDManager.INSTANCE.getColor(), true);
        this.renderer.drawString(weankess, this.syncHUD.getValue() != false ? (float)(width - this.renderer.getStringWidth(weankess) - 2) : (float)this.x.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? (float)(HUDManager.getInstance().renderingUp.getValue() != false ? height - 2 - i : 2 + i++ * 10) : (float)this.y.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? HUDManager.getInstance().getColor() : HUDManager.INSTANCE.getColor(), true);
        this.renderer.drawString(strength, this.syncHUD.getValue() != false ? (float)(width - this.renderer.getStringWidth(strength) - 2) : (float)this.x.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? (float)(HUDManager.getInstance().renderingUp.getValue() != false ? height - 2 - i : 2 + i++ * 10) : (float)this.y.getValue().intValue() + 0.0f, this.syncHUD.getValue() != false ? HUDManager.getInstance().getColor() : HUDManager.INSTANCE.getColor(), true);
    }

    static enum Page {
        Potions,
        Settings;

    }
}

