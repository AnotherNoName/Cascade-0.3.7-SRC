/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cascade.mixin.mixins;

import cascade.event.events.WorldClientEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldClient.class})
public abstract class MixinWorldClient {
    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void constructorHook(CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post((Event)new WorldClientEvent.Load((WorldClient)WorldClient.class.cast(this)));
    }
}

