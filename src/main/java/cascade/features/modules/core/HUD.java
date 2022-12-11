//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 */
package cascade.features.modules.core;

import cascade.Cascade;
import cascade.event.events.Render2DEvent;
import cascade.features.Feature;
import cascade.features.modules.Module;
import cascade.features.modules.core.ClickGui;
import cascade.features.setting.Setting;
import cascade.util.core.TextUtil;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.MathUtil;
import cascade.util.player.InventoryUtil;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class HUD
extends Module {
    private static HUD INSTANCE = new HUD();
    Setting<Boolean> renderingUp = this.register(new Setting<Boolean>("RenderingUp", false));
    Setting<Boolean> coords = this.register(new Setting<Boolean>("Coords", false));
    Setting<Boolean> simpleCoords = this.register(new Setting<Object>("SimpleCoords", Boolean.valueOf(false), v -> this.coords.getValue()));
    Setting<Boolean> direction = this.register(new Setting<Boolean>("Direction", false));
    Setting<Boolean> totems = this.register(new Setting<Boolean>("Totems", false));
    Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", false));
    Setting<Boolean> armorCount = this.register(new Setting<Object>("ArmorCount", Boolean.valueOf(false), v -> this.armor.getValue()));
    Setting<Boolean> defaultArmor = this.register(new Setting<Object>("Default", Boolean.valueOf(false), v -> this.armor.getValue()));
    Setting<Color> armorColorFrom = this.register(new Setting<Object>("ArmorColorFrom", new Color(-8912641), v -> this.armor.getValue()));
    Setting<Color> armorColorTo = this.register(new Setting<Object>("ArmorColorTo", new Color(-8912641), v -> this.armor.getValue()));
    Setting<Boolean> potions = this.register(new Setting<Boolean>("Potions", false));
    Setting<Boolean> arrayList = this.register(new Setting<Boolean>("ArrayList", true));
    Setting<Integer> arrayListY = this.register(new Setting<Object>("ArrayListY", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(50), v -> this.arrayList.getValue()));
    Setting<Boolean> test = this.register(new Setting<Object>("Test", Boolean.valueOf(false), v -> this.arrayList.getValue()));
    Setting<Boolean> pvpInfo = this.register(new Setting<Boolean>("PvpInfo", false));
    Setting<String> pvpText = this.register(new Setting<Object>("PvpText", "Cascade", v -> this.pvpInfo.getValue()));
    Setting<Welcomer> welcomer = this.register(new Setting<Welcomer>("Welcomer", Welcomer.None));
    Setting<String> welcomerText = this.register(new Setting<Object>("WelcomerText", "UID:-1", v -> this.welcomer.getValue() == Welcomer.Custom));
    Setting<Boolean> ping = this.register(new Setting<Boolean>("Ping", false));
    Setting<Boolean> tps = this.register(new Setting<Boolean>("TPS", false));
    Setting<Boolean> fps = this.register(new Setting<Boolean>("FPS", false));
    Setting<Boolean> time = this.register(new Setting<Boolean>("Time", false));
    Setting<Boolean> lagFactor = this.register(new Setting<Boolean>("LagFactor", false));
    Setting<Boolean> speed = this.register(new Setting<Boolean>("Speed", false));
    Setting<Integer> speedTicks = this.register(new Setting<Object>("Ticks", Integer.valueOf(20), Integer.valueOf(5), Integer.valueOf(100), v -> this.speed.getValue()));
    Setting<TextUtil.Color> infoColor = this.register(new Setting<Object>("InfoColor", (Object)TextUtil.Color.GRAY, v -> this.ping.getValue() != false || this.tps.getValue() != false || this.fps.getValue() != false || this.time.getValue() != false || this.speed.getValue() != false));
    Setting<Boolean> lagNotify = this.register(new Setting<Boolean>("LagNotify", false));
    public Setting<Integer> lagTime = this.register(new Setting<Object>("LagTime", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000), v -> this.lagNotify.getValue()));
    static ItemStack totem = new ItemStack(Items.field_190929_cY);
    ArrayDeque<Double> speedDeque = new ArrayDeque();
    int color;

    public HUD() {
        super("HUD", Module.Category.CORE, "Clients HUD");
        this.setInstance();
    }

    public static HUD getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUD();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        String coords;
        Object str1;
        String fpsText;
        String str;
        String str2;
        String str3;
        ArrayList effects;
        int i;
        int j;
        if (Feature.fullNullCheck()) {
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().c.getValue().getRed(), ClickGui.getInstance().c.getValue().getGreen(), ClickGui.getInstance().c.getValue().getBlue());
        if (this.pvpInfo.getValue().booleanValue()) {
            this.renderer.drawString(this.pvpText.getValue(), 2.0f, 250.0f, this.color, true);
            int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum() + (InventoryUtil.heldItem(Items.field_190929_cY, InventoryUtil.Hand.Off) ? 1 : 0);
            this.renderer.drawString(totems == 0 ? ChatFormatting.RED + "" + totems : ChatFormatting.GREEN + "" + totems, 2.0f, 260.0f, -1, true);
            int ping = Cascade.serverManager.getPing();
            String pingString = null;
            if (ping <= 50) {
                pingString = ChatFormatting.GREEN + "" + ping;
            }
            if (ping > 50 && ping <= 100) {
                pingString = ChatFormatting.YELLOW + "" + ping;
            }
            if (ping > 100) {
                pingString = ChatFormatting.RED + "" + ping;
            }
            this.renderer.drawString(pingString, 2.0f, 270.0f, this.color, true);
        }
        int[] counter1 = new int[]{1};
        int n = j = HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue() == false ? 14 : 0;
        if (this.arrayList.getValue().booleanValue()) {
            Object str4;
            Module module;
            if (this.renderingUp.getValue().booleanValue()) {
                for (int k = 0; k < Cascade.moduleManager.sortedModules.size(); ++k) {
                    module = Cascade.moduleManager.sortedModules.get(k);
                    str4 = module.getName() + ChatFormatting.DARK_GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.DARK_GRAY + "]" : "");
                    this.renderer.drawString((String)str4, width - 2 - this.renderer.getStringWidth((String)str4), 2 + j * 10 + this.arrayListY.getValue(), this.color, true);
                    ++j;
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < Cascade.moduleManager.sortedModules.size(); ++k) {
                    module = Cascade.moduleManager.sortedModules.get(k);
                    str4 = module.getName() + ChatFormatting.DARK_GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.DARK_GRAY + "]" : "");
                    this.renderer.drawString((String)str4, width - 2 - this.renderer.getStringWidth((String)str4), height - (j += 10) + this.arrayListY.getValue(), this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        int n2 = HUD.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue() != false ? 13 : (i = this.renderingUp.getValue() != false ? -2 : 0);
        if (this.renderingUp.getValue().booleanValue()) {
            if (this.potions.getValue().booleanValue()) {
                effects = new ArrayList(HUD.mc.player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    str3 = Cascade.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str3, width - this.renderer.getStringWidth(str3) - 2, height - 2 - (i += 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.lagFactor.getValue().booleanValue()) {
                str2 = "LagFactor " + TextUtil.coloredString(String.valueOf(Cascade.serverManager.getLagFactor()), this.infoColor.getValue());
                this.renderer.drawString(str2, width - this.renderer.getStringWidth(str2) - 2, height - 2 - (i += 10), this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.speed.getValue().booleanValue()) {
                double speed;
                double displaySpeed = speed = this.calcSpeed(HUD.mc.player);
                if (speed > 0.0 || HUD.mc.player.ticksExisted % 4 == 0) {
                    this.speedDeque.add(speed);
                } else {
                    this.speedDeque.pollFirst();
                }
                while (!this.speedDeque.isEmpty() && this.speedDeque.size() > this.speedTicks.getValue()) {
                    this.speedDeque.poll();
                }
                displaySpeed = this.average(this.speedDeque);
                str = "Speed " + TextUtil.coloredString(String.format("%.1f", displaySpeed) + " km/h", this.infoColor.getValue());
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, height - 2 - (i += 10), this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str5 = "Time " + TextUtil.coloredString(String.format(new SimpleDateFormat("h:mm a").format(new Date()), new Object[0]), this.infoColor.getValue());
                this.renderer.drawString(str5, width - this.renderer.getStringWidth(str5) - 2, height - 2 - (i += 10), this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                String str6 = "TPS " + TextUtil.coloredString(String.format(String.valueOf(Cascade.serverManager.getTPS()), new Object[0]), this.infoColor.getValue());
                this.renderer.drawString(str6, width - this.renderer.getStringWidth(str6) - 2, height - 2 - (i += 10), this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            fpsText = "FPS " + TextUtil.coloredString(String.format(String.valueOf(Minecraft.debugFPS), new Object[0]), this.infoColor.getValue());
            str1 = "Ping " + TextUtil.coloredString(String.format(String.valueOf(Cascade.serverManager.getPing()), new Object[0]), this.infoColor.getValue());
            if (this.renderer.getStringWidth((String)str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString((String)str1, width - this.renderer.getStringWidth((String)str1) - 2, height - 2 - (i += 10), this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, height - 2 - (i += 10), this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, height - 2 - (i += 10), this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString((String)str1, width - this.renderer.getStringWidth((String)str1) - 2, height - 2 - (i += 10), this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (this.potions.getValue().booleanValue()) {
                effects = new ArrayList(HUD.mc.player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    str3 = Cascade.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str3, width - this.renderer.getStringWidth(str3) - 2, 2 + i++ * 10, potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.lagFactor.getValue().booleanValue()) {
                str2 = "LagFactor " + TextUtil.coloredString(String.valueOf(Cascade.serverManager.getLagFactor()), this.infoColor.getValue());
                this.renderer.drawString(str2, width - this.renderer.getStringWidth(str2) - 2, height - 2 - (i += 10), this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.speed.getValue().booleanValue()) {
                double speed;
                double displaySpeed = speed = this.calcSpeed(HUD.mc.player);
                if (speed > 0.0 || HUD.mc.player.ticksExisted % 4 == 0) {
                    this.speedDeque.add(speed);
                } else {
                    this.speedDeque.pollFirst();
                }
                while (!this.speedDeque.isEmpty() && this.speedDeque.size() > this.speedTicks.getValue()) {
                    this.speedDeque.poll();
                }
                displaySpeed = this.average(this.speedDeque);
                str = "Speed " + TextUtil.coloredString(String.format("%.1f", displaySpeed) + " km/h", this.infoColor.getValue());
                this.renderer.drawString(str, width - this.renderer.getStringWidth(str) - 2, 2 + i++ * 10, this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.time.getValue().booleanValue()) {
                String str7 = "Time " + TextUtil.coloredString(String.format(new SimpleDateFormat("h:mm a").format(new Date()), new Object[0]), this.infoColor.getValue());
                this.renderer.drawString(str7, width - this.renderer.getStringWidth(str7) - 2, 2 + i++ * 10, this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (this.tps.getValue().booleanValue()) {
                String str8 = "TPS " + TextUtil.coloredString(String.format(Cascade.serverManager.getTPS() + "", new Object[0]), this.infoColor.getValue());
                this.renderer.drawString(str8, width - this.renderer.getStringWidth(str8) - 2, 2 + i++ * 10, this.color, true);
                counter1[0] = counter1[0] + 1;
            }
            fpsText = "FPS " + TextUtil.coloredString(String.format(Minecraft.debugFPS + "", new Object[0]), this.infoColor.getValue());
            str1 = "Ping " + TextUtil.coloredString(String.format(Cascade.serverManager.getPing() + "", new Object[0]), this.infoColor.getValue());
            if (this.renderer.getStringWidth((String)str1) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString((String)str1, width - this.renderer.getStringWidth((String)str1) - 2, 2 + i++ * 10, this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, 2 + i++ * 10, this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (this.fps.getValue().booleanValue()) {
                    this.renderer.drawString(fpsText, width - this.renderer.getStringWidth(fpsText) - 2, 2 + i++ * 10, this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (this.ping.getValue().booleanValue()) {
                    this.renderer.drawString((String)str1, width - this.renderer.getStringWidth((String)str1) - 2, 2 + i++ * 10, this.color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        }
        boolean inHell = HUD.mc.world.getBiome(HUD.mc.player.getPosition()).getBiomeName() == "Hell";
        int posX = (int)HUD.mc.player.posX;
        int posY = (int)HUD.mc.player.posY;
        int posZ = (int)HUD.mc.player.posZ;
        float nether = !inHell ? 0.125f : 8.0f;
        int hposX = (int)(HUD.mc.player.posX * (double)nether);
        int hposZ = (int)(HUD.mc.player.posZ * (double)nether);
        i = HUD.mc.currentScreen instanceof GuiChat ? 14 : 0;
        String coordinates = ChatFormatting.WHITE + (inHell ? posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]" : posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]");
        String direction = this.direction.getValue() != false ? ChatFormatting.WHITE + Cascade.rotationManager.getDirection4D(false) : "";
        String string = coords = this.coords.getValue() != false ? coordinates : "";
        String simpleCoord = this.coords.getValue() != false ? ChatFormatting.WHITE + (inHell ? posX + " " + posY + " " + posZ : posX + " " + posY + " " + posZ) : "";
        this.renderer.drawString(direction, 2.0f, height - (i += 10) - 11, this.color, true);
        this.renderer.drawString(this.simpleCoords.getValue() != false ? simpleCoord : coords, 2.0f, height - i, this.color, true);
        if (this.armor.getValue().booleanValue()) {
            this.renderArmor(true, this.armorCount.getValue());
        }
        if (this.totems.getValue().booleanValue()) {
            this.renderTotem();
        }
        if (this.welcomer.getValue() != Welcomer.None) {
            this.renderWelcomer();
        }
        if (this.lagNotify.getValue().booleanValue()) {
            this.renderLag();
        }
    }

    void renderArmor(boolean percent, boolean amount) {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (EntityUtil.isInLiquid() && HUD.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : HUD.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.func_190926_b()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRendererObj, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
            this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
            if (percent) {
                float blue;
                float from = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float to = 1.0f - from;
                int dmg = 100 - (int)(to * 100.0f);
                if (from > 1.0f) {
                    from = 1.0f;
                } else if (from < 0.0f) {
                    from = 0.0f;
                }
                if (to > 1.0f) {
                    to = 1.0f;
                }
                if (dmg < 0) {
                    dmg = 0;
                }
                float red = this.defaultArmor.getValue() != false ? to * 255.0f : (float)this.armorColorFrom.getValue().getRed() * from - (float)this.armorColorTo.getValue().getRed();
                float green = this.defaultArmor.getValue() != false ? from * 255.0f : (float)this.armorColorFrom.getValue().getGreen() * from - (float)this.armorColorTo.getValue().getGreen();
                float f = blue = this.defaultArmor.getValue() != false ? 0.0f : (float)this.armorColorFrom.getValue().getBlue() * from - (float)this.armorColorTo.getValue().getBlue();
                if (red < 0.0f) {
                    red *= -1.0f;
                }
                if (green < 0.0f) {
                    green *= -1.0f;
                }
                if (blue < 0.0f) {
                    blue *= -1.0f;
                }
                this.renderer.drawStringWithShadow(dmg + "", x + 8 - this.renderer.getStringWidth(dmg + "") / 2, y - 8, ColorUtil.toRGBA((int)red, (int)green, (int)blue));
            }
            if (!amount) continue;
            int a = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == is.getItem()).mapToInt(ItemStack::func_190916_E).sum();
            this.renderer.drawStringWithShadow(a == 0 ? "" : String.valueOf(a), x + 13 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    void renderTotem() {
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.field_190929_cY) {
            totems += HUD.mc.player.getHeldItemOffhand().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int y = height - 55 - (HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRendererObj, totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", x + 19 - 2 - this.renderer.getStringWidth(totems + ""), y + 9, 0xFFFFFF);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }

    void renderLag() {
        int width = this.renderer.scaledWidth;
        if (Cascade.serverManager.isServerNotResponding(this.lagTime.getValue())) {
            String text = ChatFormatting.RED + "Server not responding " + MathUtil.round((float)Cascade.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }

    void renderWelcomer() {
        int width = this.renderer.scaledWidth;
        String text = "";
        switch (this.welcomer.getValue()) {
            case None: {
                text = "";
            }
            case Custom: {
                text = this.welcomerText.getValue();
                break;
            }
            case Calendar: {
                text = MathUtil.getTimeOfDay() + HUD.mc.player.getDisplayNameString();
            }
        }
        this.renderer.drawString(text, (float)width / 2.0f - (float)this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
    }

    @Override
    public void onDisable() {
        this.speedDeque.clear();
    }

    double calcSpeed(EntityPlayerSP player) {
        double tps = 1000.0 / (double)HUD.mc.timer.field_194149_e;
        double x = player.posX - player.prevPosX;
        double z = player.posZ - player.prevPosZ;
        double speed = Math.hypot(x, z) * tps;
        return speed *= 3.6;
    }

    double average(Collection<Double> collection) {
        if (collection.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int size = 0;
        for (double element : collection) {
            sum += element;
            ++size;
        }
        return sum / (double)size;
    }

    static enum Welcomer {
        None,
        Custom,
        Calendar;

    }
}

