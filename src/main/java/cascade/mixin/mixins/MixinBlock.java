//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\1.12.2"!

/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package cascade.mixin.mixins;

import cascade.Cascade;
import cascade.event.events.CollisionEvent;
import cascade.util.Util;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Block.class})
public class MixinBlock {
    @Deprecated
    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void addCollisionBoxToListHook_Pre(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> cBoxes, Entity entity, boolean isActualState, CallbackInfo info) {
        CollisionEvent event;
        if (!Cascade.moduleManager.isModuleEnabled("Jesus")) {
            return;
        }
        Block block = (Block)Block.class.cast(this);
        AxisAlignedBB bb = block.getCollisionBoundingBox(state, (IBlockAccess)world, pos);
        if (bb != (event = new CollisionEvent(pos, bb, entity, block)).getBB()) {
            bb = event.getBB();
        }
        if (bb != null && entityBox.intersectsWith(bb)) {
            cBoxes.add(bb);
        }
        info.cancel();
    }

    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/util/math/AxisAlignedBB;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void addCollisionBoxToListHook(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> cBoxes, AxisAlignedBB blockBox, CallbackInfo info) {
        if (blockBox != Block.NULL_AABB && Cascade.moduleManager.isModuleEnabled("Jesus")) {
            CollisionEvent event;
            AxisAlignedBB bb = blockBox.offset(pos);
            if (bb != (event = new CollisionEvent(pos, bb, null, Util.mc.world != null ? Util.mc.world.getBlockState(pos).getBlock() : null)).getBB()) {
                bb = event.getBB();
            }
            if (bb != null && entityBox.intersectsWith(bb)) {
                cBoxes.add(bb);
            }
            info.cancel();
        }
    }
}

