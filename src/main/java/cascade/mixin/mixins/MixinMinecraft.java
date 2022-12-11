/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.crash.CrashReport
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.Display
 */
package cascade.mixin.mixins;

import cascade.Cascade;
import cascade.event.events.GameLoopEvent;
import cascade.event.events.KeyEvent;
import cascade.event.events.WorldClientEvent;
import cascade.features.modules.core.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    private int gameLoop = 0;
    public WorldClient world;

    @Inject(method={"shutdownMinecraftApplet"}, at={@At(value="HEAD")})
    private void stopClient(CallbackInfo callbackInfo) {
        this.unload();
    }

    @Redirect(method={"run"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        this.unload();
    }

    @Inject(method={"runTickKeyboard"}, at={@At(value="INVOKE", remap=false, target="Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal=0, shift=At.Shift.BEFORE)})
    private void onKeyboard(CallbackInfo callbackInfo) {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        int n = i;
        if (Keyboard.getEventKeyState()) {
            KeyEvent event = new KeyEvent(i);
            MinecraftForge.EVENT_BUS.post((Event)event);
        }
    }

    @Inject(method={"getLimitFramerate"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLimitFramerateHook(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (!Display.isActive()) {
                callbackInfoReturnable.setReturnValue(Manager.getInstance().unfocusedFps.getValue());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public int getGameLoop() {
        return this.gameLoop;
    }

    @Inject(method={"runGameLoop"}, at={@At(value="HEAD")})
    private void runGameLoopHead(CallbackInfo callbackInfo) {
        ++this.gameLoop;
    }

    @Inject(method={"runGameLoop"}, at={@At(value="INVOKE", target="Lnet/minecraft/profiler/Profiler;endSection()V", ordinal=0, shift=At.Shift.AFTER)})
    private void post_ScheduledTasks(CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post((Event)new GameLoopEvent());
    }

    @Inject(method={"loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V"}, at={@At(value="HEAD")})
    private void loadWorldHook(WorldClient worldClient, String loadingMessage, CallbackInfo info) {
        if (this.world != null) {
            MinecraftForge.EVENT_BUS.post((Event)new WorldClientEvent.Unload(this.world));
        }
    }

    void unload() {
        Cascade.LOGGER.info("Initiated client shutdown.");
        Cascade.onUnload();
        Cascade.LOGGER.info("Finished client shutdown.");
    }
}

