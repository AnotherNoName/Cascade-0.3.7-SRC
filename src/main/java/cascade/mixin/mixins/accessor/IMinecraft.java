/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cascade.mixin.mixins.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Minecraft.class})
public interface IMinecraft {
    @Invoker(value="rightClickMouse")
    public void invokeRightClickMouse();
}

