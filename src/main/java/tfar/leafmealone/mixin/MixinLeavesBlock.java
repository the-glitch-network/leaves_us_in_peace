package tfar.leafmealone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.leafmealone.MixinHooks;

import java.util.Random;

@Mixin(LeavesBlock.class)
abstract class MixinLeavesBlock {

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		MixinHooks.block = (Block)(Object)this;
	}

	@ModifyConstant(method = "getDistanceFromLog",constant = @Constant(classValue = LeavesBlock.class))
	private static Class<?> strictCheck(Object block,Class<?> clazz) {
		return block == MixinHooks.block ? LeavesBlock.class : GrassBlock.class;
	}
}
