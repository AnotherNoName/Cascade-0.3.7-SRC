//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.ItemTooltipEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.Render2DEvent;
import cascade.features.modules.Module;
import cascade.features.modules.core.ClickGui;
import cascade.util.misc.Timer;
import cascade.util.render.ColorUtil;
import cascade.util.render.RenderUtil;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolTips
extends Module {
    static ResourceLocation SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
    Map<EntityPlayer, ItemStack> spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
    Map<EntityPlayer, Timer> playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
    static ToolTips INSTANCE;
    int textRadarY = 0;

    public ToolTips() {
        super("ToolTips", Module.Category.MISC, "awa owo");
        INSTANCE = this;
    }

    public static ToolTips getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ToolTips();
        }
        return INSTANCE;
    }

    public static void displayInv(ItemStack stack, String name) {
        try {
            Item item = stack.getItem();
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.blockType = shulker.getBlock();
            entityBox.setWorldObj((World)ToolTips.mc.world);
            ItemStackHelper.func_191283_b((NBTTagCompound)stack.getTagCompound().getCompoundTag("BlockEntityTag"), (NonNullList)entityBox.field_190596_f);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.func_190575_a(name == null ? stack.getDisplayName() : name);
            IInventory inventory = null;
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                ToolTips.mc.player.displayGUIChest(inventory);
            }).start();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onUpdate() {
        if (ToolTips.fullNullCheck()) {
            return;
        }
        for (EntityPlayer player : ToolTips.mc.world.playerEntities) {
            if (player == null || !(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) || ToolTips.mc.player == player) continue;
            ItemStack stack = player.getHeldItemMainhand();
            this.spiedPlayers.put(player, stack);
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (ToolTips.fullNullCheck()) {
            return;
        }
        int x = -3;
        int y = 124;
        this.textRadarY = 0;
        for (EntityPlayer player : ToolTips.mc.world.playerEntities) {
            Timer playerTimer;
            if (this.spiedPlayers.get(player) == null) continue;
            if (player.getHeldItemMainhand() == null || !(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                playerTimer = this.playerTimers.get(player);
                if (playerTimer == null) {
                    Timer timer = new Timer();
                    timer.reset();
                    this.playerTimers.put(player, timer);
                } else if (playerTimer.passedS(3.0)) {
                    continue;
                }
            } else if (player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                playerTimer.reset();
                this.playerTimers.put(player, playerTimer);
            }
            ItemStack stack = this.spiedPlayers.get(player);
            this.renderShulkerToolTip(stack, x, y, player.getName());
            this.textRadarY = (y += 78) - 10 - 114 + 2;
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void makeTooltip(ItemTooltipEvent event) {
    }

    public void renderShulkerToolTip(ItemStack stack, int x, int y, String name) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            mc.getTextureManager().bindTexture(SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.disableDepth();
            Color color = new Color(ClickGui.getInstance().c.getValue().getRed(), ClickGui.getInstance().c.getValue().getGreen(), ClickGui.getInstance().c.getValue().getBlue(), 200);
            this.renderer.drawStringWithShadow(name == null ? stack.getDisplayName() : name, x + 8, y + 6, ColorUtil.toRGBA(color));
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            NonNullList nonnulllist = NonNullList.func_191197_a((int)27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x + i % 9 * 18 + 8;
                int iY = y + i / 9 * 18 + 18;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 501.0f;
                RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                RenderUtil.itemRender.renderItemOverlayIntoGUI(ToolTips.mc.fontRendererObj, itemStack, iX, iY, (String)null);
                ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 0.0f;
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

