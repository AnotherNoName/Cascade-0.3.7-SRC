//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cascade.features.modules.misc;

import cascade.event.events.DeathEvent;
import cascade.features.modules.Module;
import cascade.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillEffect
extends Module {
    Setting<Boolean> lightning = this.register(new Setting<Boolean>("Lightning", true));
    Setting<Boolean> sound = this.register(new Setting<Object>("Sound", Boolean.valueOf(true), v -> this.lightning.getValue()));
    int ticks;
    boolean done;

    public KillEffect() {
        super("KillEffect", Module.Category.MISC, "");
    }

    @Override
    public void onTick() {
        ++this.ticks;
        if (this.ticks > 20) {
            this.ticks = 0;
            this.done = false;
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent e) {
        if (KillEffect.fullNullCheck() || this.isDisabled()) {
            return;
        }
        if (this.lightning.getValue().booleanValue() && !this.done) {
            EntityLightningBolt bolt = new EntityLightningBolt((World)KillEffect.mc.world, e.player.posX, e.player.posY, e.player.posZ, false);
            bolt.setLocationAndAngles(e.player.posX, e.player.posY, e.player.posZ, KillEffect.mc.player.rotationYaw, KillEffect.mc.player.rotationPitch);
            KillEffect.mc.world.spawnEntityInWorld((Entity)bolt);
            if (this.sound.getValue().booleanValue()) {
                KillEffect.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_THUNDER, 1.0f, 1.0f);
                KillEffect.mc.player.playSound(SoundEvents.ENTITY_LIGHTNING_IMPACT, 1.0f, 1.0f);
            }
            this.done = true;
        }
    }
}

