//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.movement;

import cascade.event.events.MoveEvent;
import cascade.features.modules.Module;
import cascade.util.entity.EntityUtil;
import cascade.util.player.PlayerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Module.Category.MOVEMENT, "Modifies sprinting");
    }

    @SubscribeEvent
    public void onSprint(MoveEvent e) {
        if (Sprint.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (e.getStage() == 1 && !EntityUtil.isMoving()) {
            e.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        if (this.check()) {
            Sprint.mc.player.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        if (Sprint.mc.player != null) {
            Sprint.mc.player.setSprinting(false);
        }
    }

    boolean check() {
        return !(!Sprint.mc.gameSettings.keyBindForward.isKeyDown() && !Sprint.mc.gameSettings.keyBindBack.isKeyDown() && !Sprint.mc.gameSettings.keyBindLeft.isKeyDown() && !Sprint.mc.gameSettings.keyBindRight.isKeyDown() || Sprint.mc.player == null || Sprint.mc.player.isSneaking() || Sprint.mc.player.isCollidedHorizontally || (float)Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f || PlayerUtil.isInLiquid());
    }
}

