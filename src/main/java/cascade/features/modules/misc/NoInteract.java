//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityBeacon
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.PacketEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import cascade.util.player.InventoryUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoInteract
extends Module {
    Setting<Boolean> pickaxe = this.register(new Setting<Boolean>("Pickaxe", false));

    public NoInteract() {
        super("NoInteract", Module.Category.MISC, "Prevents u from interacting with blocks");
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send e) {
        if (NoInteract.fullNullCheck() || this.isDisabled()) {
            return;
        }
        try {
            if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && !NoInteract.mc.player.isSneaking() && NoInteract.mc.gameSettings.keyBindUseItem.isKeyDown() && (InventoryUtil.heldItem(Items.EXPERIENCE_BOTTLE, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.GOLDEN_APPLE, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.CHORUS_FRUIT, InventoryUtil.Hand.Both) || InventoryUtil.heldItem((Item)Items.BOW, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.WRITABLE_BOOK, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.WRITTEN_BOOK, InventoryUtil.Hand.Both) || InventoryUtil.heldItem((Item)Items.POTIONITEM, InventoryUtil.Hand.Both) || this.pickaxe.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_PICKAXE, InventoryUtil.Hand.Main))) {
                for (TileEntity entity : NoInteract.mc.world.loadedTileEntityList) {
                    if (!(entity instanceof TileEntityEnderChest) && !(entity instanceof TileEntityBeacon) && !(entity instanceof TileEntityFurnace) && !(entity instanceof TileEntityHopper) && !(entity instanceof TileEntityChest) || !NoInteract.mc.objectMouseOver.getBlockPos().equals((Object)entity.getPos())) continue;
                    e.setCanceled(true);
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
                if (NoInteract.mc.world.getBlockState(NoInteract.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.ANVIL) {
                    e.setCanceled(true);
                    mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock e) {
        if (NoInteract.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if ((NoInteract.mc.world.getBlockState(e.getPos()).getBlock() == Blocks.ANVIL || NoInteract.mc.world.getBlockState(e.getPos()).getBlock() == Blocks.ENDER_CHEST) && !NoInteract.mc.player.isSneaking() && NoInteract.mc.gameSettings.keyBindUseItem.isKeyDown() && (InventoryUtil.heldItem(Items.EXPERIENCE_BOTTLE, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.GOLDEN_APPLE, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.CHORUS_FRUIT, InventoryUtil.Hand.Both) || InventoryUtil.heldItem((Item)Items.BOW, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.WRITABLE_BOOK, InventoryUtil.Hand.Both) || InventoryUtil.heldItem(Items.WRITTEN_BOOK, InventoryUtil.Hand.Both) || InventoryUtil.heldItem((Item)Items.POTIONITEM, InventoryUtil.Hand.Both) || this.pickaxe.getValue().booleanValue() && InventoryUtil.heldItem(Items.DIAMOND_PICKAXE, InventoryUtil.Hand.Main))) {
            e.setCanceled(true);
            mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}

