//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package cascade.features.modules.combat;

import cascade.Cascade;
import cascade.features.gui.CascadeGui;
import cascade.features.modules.Module;
import cascade.features.modules.player.XCarry;
import cascade.features.setting.Bind;
import cascade.features.setting.Setting;
import cascade.util.entity.CombatUtil;
import cascade.util.entity.EntityUtil;
import cascade.util.misc.Timer;
import cascade.util.player.InventoryUtil;
import cascade.util.player.TargetUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class AutoArmor
extends Module {
    Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 85, 0, 500));
    Setting<Boolean> autoMend = this.register(new Setting<Boolean>("AutoMend", false));
    Setting<Float> enemyRange = this.register(new Setting<Object>("EnemyRange", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(25.0f), v -> this.autoMend.getValue()));
    Setting<Integer> minPerc = this.register(new Setting<Object>("Min%", Integer.valueOf(80), Integer.valueOf(1), Integer.valueOf(100), v -> this.autoMend.getValue()));
    Setting<Boolean> onlyInHole = this.register(new Setting<Boolean>("OnlyInHole", true));
    Setting<Boolean> curse = this.register(new Setting<Boolean>("AllowCurseOfBind", false));
    Setting<Integer> packets = this.register(new Setting<Integer>("Packets", 3, 1, 12));
    Setting<Bind> elytraBind = this.register(new Setting<Bind>("ElytraSwap", new Bind(-1)));
    Setting<Boolean> updateController = this.register(new Setting<Boolean>("Update", true));
    Setting<Boolean> shiftClick = this.register(new Setting<Boolean>("ShiftClick", false));
    Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    List<Integer> doneSlots = new ArrayList<Integer>();
    Timer elytraTimer = new Timer();
    Timer timer = new Timer();
    boolean elytraOn = false;
    EntityPlayer closest;

    public AutoArmor() {
        super("AutoArmor", Module.Category.COMBAT, "");
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if (Keyboard.getEventKeyState() && !(AutoArmor.mc.currentScreen instanceof CascadeGui) && this.elytraBind.getValue().getKey() == Keyboard.getEventKey()) {
            this.elytraOn = !this.elytraOn;
        }
    }

    @Override
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.elytraOn = false;
    }

    @Override
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.timer.reset();
        this.elytraTimer.reset();
    }

    @Override
    public void onUpdate() {
        if (AutoArmor.fullNullCheck() || AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int slot4;
            ItemStack feet;
            int slot3;
            ItemStack legging;
            int slot2;
            ItemStack chest;
            int slot;
            if (this.autoMend.getValue().booleanValue() && InventoryUtil.holdingItem(ItemExpBottle.class) && AutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown() && this.isSafe()) {
                int bootDamage;
                int leggingDamage;
                int chestDamage;
                int helmDamage;
                if (this.onlyInHole.getValue().booleanValue() && !EntityUtil.isSafe((Entity)AutoArmor.mc.player, 1.0, false)) {
                    return;
                }
                ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
                if (!helm.field_190928_g && (helmDamage = CombatUtil.getRoundedDamage(helm)) >= this.minPerc.getValue()) {
                    this.takeOffSlot(5);
                }
                ItemStack chest2 = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack();
                if (!chest2.field_190928_g && (chestDamage = CombatUtil.getRoundedDamage(chest2)) >= this.minPerc.getValue()) {
                    this.takeOffSlot(6);
                }
                ItemStack legging2 = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack();
                if (!legging2.field_190928_g && (leggingDamage = CombatUtil.getRoundedDamage(legging2)) >= this.minPerc.getValue()) {
                    this.takeOffSlot(7);
                }
                ItemStack feet2 = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack();
                if (!feet2.field_190928_g && (bootDamage = CombatUtil.getRoundedDamage(feet2)) >= this.minPerc.getValue()) {
                    this.takeOffSlot(8);
                }
                return;
            }
            ItemStack helm = AutoArmor.mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.field_190931_a && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(5, slot);
            }
            if ((chest = AutoArmor.mc.player.inventoryContainer.getSlot(6).getStack()).getItem() == Items.field_190931_a) {
                if (this.taskList.isEmpty()) {
                    if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                        int elytraSlot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                        if (elytraSlot != -1) {
                            if (elytraSlot < 5 && elytraSlot > 1 || !this.shiftClick.getValue().booleanValue()) {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot));
                                this.taskList.add(new InventoryUtil.Task(6));
                            } else {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
                            }
                            if (this.updateController.getValue().booleanValue()) {
                                this.taskList.add(new InventoryUtil.Task());
                            }
                            this.elytraTimer.reset();
                        }
                    } else if (!this.elytraOn && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                        this.getSlotOn(6, slot2);
                    }
                }
            } else if (this.elytraOn && chest.getItem() != Items.ELYTRA && this.elytraTimer.passedMs(500L)) {
                if (this.taskList.isEmpty()) {
                    slot2 = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, XCarry.getInstance().isOn());
                    if (slot2 != -1) {
                        this.taskList.add(new InventoryUtil.Task(slot2));
                        this.taskList.add(new InventoryUtil.Task(6));
                        this.taskList.add(new InventoryUtil.Task(slot2));
                        if (this.updateController.getValue().booleanValue()) {
                            this.taskList.add(new InventoryUtil.Task());
                        }
                    }
                    this.elytraTimer.reset();
                }
            } else if (!this.elytraOn && chest.getItem() == Items.ELYTRA && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
                slot2 = InventoryUtil.findItemInventorySlot((Item)Items.DIAMOND_CHESTPLATE, false, XCarry.getInstance().isOn());
                if (slot2 == -1 && (slot2 = InventoryUtil.findItemInventorySlot((Item)Items.IRON_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1 && (slot2 = InventoryUtil.findItemInventorySlot((Item)Items.GOLDEN_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1 && (slot2 = InventoryUtil.findItemInventorySlot((Item)Items.CHAINMAIL_CHESTPLATE, false, XCarry.getInstance().isOn())) == -1) {
                    slot2 = InventoryUtil.findItemInventorySlot((Item)Items.LEATHER_CHESTPLATE, false, XCarry.getInstance().isOn());
                }
                if (slot2 != -1) {
                    this.taskList.add(new InventoryUtil.Task(slot2));
                    this.taskList.add(new InventoryUtil.Task(6));
                    this.taskList.add(new InventoryUtil.Task(slot2));
                    if (this.updateController.getValue().booleanValue()) {
                        this.taskList.add(new InventoryUtil.Task());
                    }
                }
                this.elytraTimer.reset();
            }
            if ((legging = AutoArmor.mc.player.inventoryContainer.getSlot(7).getStack()).getItem() == Items.field_190931_a && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(7, slot3);
            }
            if ((feet = AutoArmor.mc.player.inventoryContainer.getSlot(8).getStack()).getItem() == Items.field_190931_a && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.getValue(), XCarry.getInstance().isOn())) != -1) {
                this.getSlotOn(8, slot4);
            }
        }
        if (this.timer.passedMs((int)((float)this.delay.getValue().intValue() * Cascade.serverManager.getTpsFactor()))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.packets.getValue(); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    void takeOffSlot(int slot) {
        if (!this.taskList.isEmpty()) {
            return;
        }
        int target = -1;
        for (int i : InventoryUtil.findEmptySlots(XCarry.getInstance().isOn())) {
            if (this.doneSlots.contains(target)) continue;
            target = i;
            this.doneSlots.add(i);
        }
        if (target != -1) {
            if (target < 5 && target > 0 || !this.shiftClick.getValue().booleanValue()) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
            } else {
                this.taskList.add(new InventoryUtil.Task(slot, true));
            }
            if (this.updateController.getValue().booleanValue()) {
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    void getSlotOn(int slot, int target) {
        if (!this.taskList.isEmpty()) {
            return;
        }
        this.doneSlots.remove((Object)target);
        if (target < 5 && target > 0 || !this.shiftClick.getValue().booleanValue()) {
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
        } else {
            this.taskList.add(new InventoryUtil.Task(target, true));
        }
        if (this.updateController.getValue().booleanValue()) {
            this.taskList.add(new InventoryUtil.Task());
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.elytraOn) {
            return "Elytra";
        }
        return null;
    }

    boolean isSafe() {
        this.closest = TargetUtil.getTarget(this.enemyRange.getValue().floatValue());
        return this.closest == null;
    }
}

