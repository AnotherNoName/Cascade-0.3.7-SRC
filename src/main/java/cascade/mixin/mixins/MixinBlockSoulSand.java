//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockSoulSand
 *  net.minecraft.block.material.MapColor
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package cascade.mixin.mixins;

import cascade.features.modules.movement.NoSlow;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BlockSoulSand.class})
public class MixinBlockSoulSand
extends Block {
    public MixinBlockSoulSand() {
        super(Material.SAND, MapColor.BROWN);
    }

    @Inject(method={"onEntityCollision"}, at={@At(value="HEAD")}, cancellable=true)
    public void onEntityCollisionHook(World worldIn, BlockPos pos, IBlockState state, Entity entityIn, CallbackInfo ci) {
        if (NoSlow.getInstance().isEnabled() && NoSlow.getInstance().soulSand.getValue().booleanValue()) {
            ci.cancel();
        }
    }
}

