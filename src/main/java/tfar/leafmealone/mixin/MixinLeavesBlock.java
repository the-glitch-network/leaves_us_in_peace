package tfar.leafmealone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfar.leafmealone.MixinHooks;

import java.util.Random;

import static tfar.leafmealone.MixinHooks.*;

@Mixin(LeavesBlock.class)
abstract class MixinLeavesBlock extends Block {
	public MixinLeavesBlock(Settings settings) {
		super(settings);
		throw new IllegalStateException("MixinLeavesBlock's dummy constructor called!");
	}

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		updateLeavesTags(world, this);
		MixinHooks.block = this;
	}

	@ModifyArgs(method = "updateDistanceFromLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LeavesBlock;getDistanceFromLog(Lnet/minecraft/block/BlockState;)I"))
	private static void checkBlockState(Args args, BlockState leavesState, WorldAccess world, BlockPos pos) {
		updateLogLeavesTags((ServerWorld) world, ((BlockState) args.get(0)).getBlock());
	}

	// if a log_leaves tag is found, forces vanilla `contains` call to match
	@Redirect(
			method = "getDistanceFromLog",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/tag/Tag$Identified;contains(Ljava/lang/Object;)Z"),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/tag/BlockTags;LOGS:Lnet/minecraft/tag/Tag$Identified;"))
	)
	private static boolean tryMatchLog(Tag.Identified<?> instance, Object o) {
		final Block block = (Block)o;
		Tag<Block> logLeavesTag = getLogLeavesTag(block);
		if (logLeavesTag != null)
			return logLeavesTag.contains(MixinHooks.block);

		else return BlockTags.LOGS.contains(block);
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/LeavesBlock"))
	private static boolean strictLeavesCheckDev(Object stateBlock, Class<?> leavesBlockClass) {
		return strictLeavesCheckImpl(stateBlock);
	}

	@Group(name = "LeavesBlockInstanceofRedirects")
	@Redirect(method = "getDistanceFromLog", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/block/net/minecraft/class_2397"))
	private static boolean strictLeavesCheckProd(Object blockOfState, Class<?> leavesBlockClass) {
		return strictLeavesCheckImpl(blockOfState);
	}

	private static boolean strictLeavesCheckImpl(Object stateBlock) {
		Tag<Block> leavesTag = getLeavesTag(MixinHooks.block);
		return leavesTag != null && leavesTag.contains((Block)stateBlock) ||
				stateBlock == MixinHooks.block;
	}

}
