/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cascade.event.events;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderLivingEvent
extends Event {
    Entity entityLivingBase;
    float limbSwing;
    float limbSwingAmount;
    float ageInTicks;
    float netHeadYaw;
    float headPitch;
    float scaleFactor;
    ModelBase modelBase;

    public RenderLivingEvent(Entity entityLivingBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, ModelBase model) {
        this.entityLivingBase = entityLivingBase;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scaleFactor = scaleFactor;
        this.modelBase = model;
    }

    public float getAgeInTicks() {
        return this.ageInTicks;
    }

    public float getHeadPitch() {
        return this.headPitch;
    }

    public float getLimbSwing() {
        return this.limbSwing;
    }

    public Entity getEntity() {
        return this.entityLivingBase;
    }

    public float getLimbSwingAmount() {
        return this.limbSwingAmount;
    }

    public float getNetHeadYaw() {
        return this.netHeadYaw;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public ModelBase getModelBase() {
        return this.modelBase;
    }
}

