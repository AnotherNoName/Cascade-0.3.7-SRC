//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.util.math.Vec3d
 */
package cascade.features.modules.visual;

import cascade.Cascade;
import cascade.event.events.Render3DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.misc.Notifications;
import cascade.features.setting.Setting;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.MathUtil;
import cascade.util.render.ColorUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.Vec3d;

public class Nametags
extends Module {
    Setting<Double> size = this.register(new Setting<Double>("Size", 0.1, 0.1, 3.0));
    Setting<Double> scale = this.register(new Setting<Double>("Scale", 8.0, 1.0, 10.0));
    Setting<Double> scaleFactor = this.register(new Setting<Double>("ScaleFactor", 6.0, 1.0, 10.0));
    Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPops", true));
    Setting<Boolean> entityID = this.register(new Setting<Boolean>("ID", false));
    Setting<Boolean> lowercase = this.register(new Setting<Boolean>("Lowercase", true));
    Setting<Boolean> rect = this.register(new Setting<Boolean>("Rectangle", false));
    Setting<Color> rectC = this.register(new Setting<Object>("RectColor", new Color(0, 0, 0, 45), v -> this.rect.getValue()));
    Setting<Color> rectFriendC = this.register(new Setting<Object>("RectFriend", new Color(85, 255, 255, 45), v -> this.rect.getValue()));
    Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline", true));
    Setting<Color> c = this.register(new Setting<Object>("Color", new Color(85, 85, 255, 255), v -> this.outline.getValue()));
    Setting<Color> friendC = this.register(new Setting<Object>("FriendColor", new Color(85, 255, 255, 255), v -> this.outline.getValue()));
    Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(0.1f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.outline.getValue()));
    static Nametags INSTANCE;

    public Nametags() {
        super("Nametags", Module.Category.VISUAL, "");
        INSTANCE = this;
    }

    public static Nametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Nametags();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Nametags.fullNullCheck()) {
            return;
        }
        for (EntityPlayer player : Nametags.mc.world.playerEntities) {
            if (player == null || player.equals((Object)Nametags.mc.player) || !player.isEntityAlive()) continue;
            double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX;
            double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY;
            double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ;
            this.renderProperNameTag(player, x, y, z, event.getPartialTicks());
        }
    }

    void renderProperNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        int color;
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        Vec3d interpolatedPosition = this.getInterpolatedPosition((Entity)player, mc.getRenderPartialTicks());
        double distance = mc.getRenderViewEntity().getDistance(interpolatedPosition.xCoord, interpolatedPosition.yCoord, interpolatedPosition.zCoord);
        double scale = Math.max(this.size.getValue() * this.scale.getValue(), this.size.getValue() * distance) / (this.scaleFactor.getValue() * 10.0);
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-Nametags.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Nametags.mc.getRenderManager().playerViewX, (float)(Nametags.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        if (this.rect.getValue().booleanValue()) {
            color = ColorUtil.toRGBA(Cascade.friendManager.isFriend(player) ? new Color(this.rectFriendC.getValue().getRed(), this.rectFriendC.getValue().getGreen(), this.rectFriendC.getValue().getBlue(), this.rectFriendC.getValue().getAlpha()) : new Color(this.rectC.getValue().getRed(), this.rectC.getValue().getGreen(), this.rectC.getValue().getBlue(), this.rectC.getValue().getAlpha()));
            this.drawRect(-width - 2, -(Nametags.mc.fontRendererObj.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, color);
        }
        if (this.outline.getValue().booleanValue()) {
            color = ColorUtil.toRGBA(Cascade.friendManager.isFriend(player) ? new Color(this.friendC.getValue().getRed(), this.friendC.getValue().getGreen(), this.friendC.getValue().getBlue(), this.friendC.getValue().getAlpha()) : new Color(this.c.getValue().getRed(), this.c.getValue().getGreen(), this.c.getValue().getBlue(), this.c.getValue().getAlpha()));
            this.drawOutlineRect(-width - 2, -(Nametags.mc.fontRendererObj.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, color);
        }
        GlStateManager.enableAlpha();
        ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        GlStateManager.pushMatrix();
        int xOffset = -8;
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack == null) continue;
            xOffset -= 8;
        }
        xOffset -= 8;
        ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
            renderOffhand.stackSize = 1;
        }
        this.renderItemStack(renderOffhand, xOffset, -26, this.armor.getValue());
        xOffset += 16;
        for (ItemStack stack2 : player.inventory.armorInventory) {
            if (stack2 == null) continue;
            ItemStack armourStack = stack2.copy();
            if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                armourStack.stackSize = 1;
            }
            this.renderItemStack(armourStack, xOffset, -26, this.armor.getValue());
            xOffset += 16;
        }
        this.renderItemStack(renderMainHand, xOffset, -26, this.armor.getValue());
        GlStateManager.popMatrix();
        this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() + 1), this.getDisplayColor(player));
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
        GlStateManager.popMatrix();
    }

    void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    void drawOutlineRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue().floatValue());
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    void renderItemStack(ItemStack stack, int x, int y, boolean item) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        if (item) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRendererObj, stack, x, y);
        }
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y - 8;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("god", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        if (this.hasDurability(stack)) {
            int percent = this.getRoundedDamage(stack);
            String color = percent >= 60 ? "\u00a7a" : (percent >= 25 ? "\u00a7e" : "\u00a7c");
            this.renderer.drawStringWithShadow(color + percent, x * 2, enchantmentY, -1);
        }
    }

    String getDisplayTag(EntityPlayer player) {
        float health;
        String name;
        String string = name = this.lowercase.getValue() != false ? player.getDisplayName().getFormattedText().toLowerCase() : player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        String color = (health = EntityUtil.getHealth((Entity)player)) > 18.0f ? "\u00a7a" : (health > 16.0f ? "\u00a72" : (health > 12.0f ? "\u00a7e" : (health > 8.0f ? "\u00a76" : (health > 5.0f ? "\u00a7c" : "\u00a74"))));
        String pingStr = "";
        try {
            int responseTime = mc.getConnection().getPlayerInfo(player.getUniqueID()).getResponseTime();
            pingStr = pingStr + ChatFormatting.WHITE + responseTime + "ms ";
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        String popStr = " ";
        if (this.totemPops.getValue().booleanValue() && Notifications.getINSTANCE().pops.get(player.getName()) != null) {
            popStr = popStr + ChatFormatting.WHITE + "-" + Notifications.getINSTANCE().pops.get(player.getName());
        }
        String idString = "";
        if (this.entityID.getValue().booleanValue()) {
            idString = idString + player.getEntityId() + " ";
        }
        name = name + color + " " + (health > 0.0f ? Float.valueOf(MathUtil.round(health, 1)) : "0.0");
        return pingStr + idString + name + popStr;
    }

    int getDisplayColor(EntityPlayer player) {
        int color = -1;
        if (Cascade.friendManager.isFriend(player)) {
            return ColorUtil.toRGBA(new Color(85, 255, 255, 255));
        }
        return color;
    }

    double interpolate(double previous, double current, float partialTicks) {
        return previous + (current - previous) * (double)partialTicks;
    }

    boolean hasDurability(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    float getDamageInPercent(ItemStack stack) {
        return (float)this.getItemDamage(stack) / (float)stack.getMaxDamage() * 100.0f;
    }

    int getRoundedDamage(ItemStack stack) {
        return (int)this.getDamageInPercent(stack);
    }

    Vec3d getInterpolatedPosition(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(new Vec3d(entity.posX - entity.lastTickPosX, entity.posY - entity.lastTickPosY, entity.posZ - entity.lastTickPosZ).scale((double)ticks));
    }
}

