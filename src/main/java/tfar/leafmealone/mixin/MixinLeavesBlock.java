package tfar.leafmealone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.leafmealone.MixinHooks;

import java.util.Random;

@Mixin(LeavesBlock.class)
abstract class MixinLeavesBlock extends Block {
	public MixinLeavesBlock(Settings settings) {
		super(settings);
		throw new IllegalStateException("MixinLeavesBlock's dummy constructor called!");
	}

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		MixinHooks.block = this;
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/LeavesBlock"))
	private static boolean strictCheckDev(Object blockOfState, Class<?> leavesBlockClass) {
		return blockOfState == MixinHooks.block;
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/net/minecraft/class_2397"))
	private static boolean strictCheckProd(Object blockOfState, Class<?> leavesBlockClass) {
		return blockOfState == MixinHooks.block;
	}
}
