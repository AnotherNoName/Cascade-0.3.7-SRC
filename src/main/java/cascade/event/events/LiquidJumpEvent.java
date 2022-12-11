/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 */
package cascade.event.events;

import cascade.event.EventStage;
import net.minecraft.entity.EntityLivingBase;

public class LiquidJumpEvent
extends EventStage {
    private final EntityLivingBase entity;

    public LiquidJumpEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}

