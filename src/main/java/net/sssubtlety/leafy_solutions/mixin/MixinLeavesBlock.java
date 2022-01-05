package net.sssubtlety.leafy_solutions.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.sssubtlety.leafy_solutions.LeafySolutions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

@Mixin(LeavesBlock.class)
abstract class MixinLeavesBlock extends Block {
	@Shadow @Final public static IntProperty DISTANCE;
	private static Block recentLeaves;

	public MixinLeavesBlock(Settings settings) {
		super(settings);
		throw new IllegalStateException("MixinLeavesBlock's dummy constructor called!");
	}

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		LeafySolutions.updateLeavesTags(this);
		recentLeaves = this;
	}

	@ModifyArgs(method = "updateDistanceFromLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LeavesBlock;getDistanceFromLog(Lnet/minecraft/block/BlockState;)I"))
	private static void checkBlockState(Args args, BlockState leavesState, WorldAccess world, BlockPos pos) {
		LeafySolutions.updateLogLeavesTags(((BlockState) args.get(0)).getBlock());
	}

	// if a log_leaves tag is found, forces vanilla `contains` call to match
	@Redirect(
			method = "getDistanceFromLog",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/tag/BlockTags;LOGS:Lnet/minecraft/tag/Tag$Identified;"))
	)
	private static boolean tryMatchLog(BlockState state, Tag<Block> tag) {
		final Block block = state.getBlock();
		Tag<Block> logLeavesTag = LeafySolutions.getLogLeavesTag(block);
		if (logLeavesTag != null)
			return logLeavesTag.contains(recentLeaves);

		else return BlockTags.LOGS.contains(block);
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/LeavesBlock"))
	private static boolean strictLeavesCheckDev(Object stateBlock, Class<?> leavesBlockClass, BlockState state) {
		return strictLeavesCheckImpl(state);
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/net/minecraft/class_2397"))
	private static boolean strictLeavesCheckProd(Object stateBlock, Class<?> leavesBlockClass, BlockState state) {
		return strictLeavesCheckImpl(state);
	}

	private static boolean strictLeavesCheckImpl(BlockState state) {
		if (state.getOrEmpty(DISTANCE).isEmpty()) return false;
		Tag<Block> leavesTag = LeafySolutions.getLeavesTag(recentLeaves);
		Block stateBlock = state.getBlock();
		return leavesTag != null && leavesTag.contains(stateBlock) ||
				stateBlock == recentLeaves;
	}

}
