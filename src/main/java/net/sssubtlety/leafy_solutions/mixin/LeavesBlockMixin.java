package net.sssubtlety.leafy_solutions.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.sssubtlety.leafy_solutions.LeafySolutions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

import static net.sssubtlety.leafy_solutions.FeatureControl.*;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {
	private static Block recentLeaves;
	private static final Direction[] HORIZONTAL_DIRECTIONS = {
			Direction.NORTH,
			Direction.SOUTH,
			Direction.EAST,
			Direction.WEST
	};

	@Shadow @Final public static IntProperty DISTANCE;
	@Shadow @Final public static BooleanProperty PERSISTENT;

	@Shadow public abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

	public LeavesBlockMixin(Settings settings) {
		super(settings);
		throw new IllegalStateException("MixinLeavesBlock's dummy constructor called!");
	}

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		if (shouldMatchLeavesTypes()) LeafySolutions.updateLeavesTags(this);
		recentLeaves = this;
	}

	@ModifyArgs(method = "updateDistanceFromLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LeavesBlock;getDistanceFromLog(Lnet/minecraft/block/BlockState;)I"))
	private static void checkBlockState(Args args, BlockState leavesState, WorldAccess world, BlockPos pos) {
		if (shouldMatchLogsToLeaves()) LeafySolutions.updateLogLeavesTags(((BlockState) args.get(0)).getBlock());
	}

	// If a log_leaves tag is found, match it. Otherwise, just match all logs like vanilla
	@Redirect(
			method = "getDistanceFromLog",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/Tag;)Z"),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/tag/BlockTags;LOGS:Lnet/minecraft/tag/Tag$Identified;"))
	)
	private static boolean tryMatchLog(BlockState state, Tag<Block> tag) {
		final Block block = state.getBlock();
		if (recentLeaves != null && shouldMatchLogsToLeaves()) {
			Tag<Block> logLeavesTag = LeafySolutions.getLogLeavesTag(block);
			if (logLeavesTag != null)
				return logLeavesTag.contains(recentLeaves);
		}

		return BlockTags.LOGS.contains(block);
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
		if (shouldIgnorePersistentLaves()) {
			final Optional<Boolean> optPersistent = state.getOrEmpty(PERSISTENT);
			if (optPersistent.isPresent() && optPersistent.get()) return false;
		}

		if (recentLeaves != null && shouldMatchLeavesTypes()) {
			if (state.getOrEmpty(DISTANCE).isEmpty()) return false;
			Tag<Block> leavesTag = LeafySolutions.getLeavesTag(recentLeaves);
			Block stateBlock = state.getBlock();
			return isMatchingLeaves(leavesTag, stateBlock);
		}

		return state.getBlock() instanceof LeavesBlock;
	}

	@ModifyArgs(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void postScheduledTick(Args args, BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// ignore persistent leaves
		if (state.get(PERSISTENT)) return;
		final int leavesDecayDelay = getLeavesDecayDelay();
		if (leavesDecayDelay >= 0) {
			BlockState newState = args.get(1);
			if (newState.get(DISTANCE) >= 7) {
//				LeavesBlock.dropStacks(state, world, pos);
				world.removeBlock(pos, false);
//				randomTick(state, world, pos, random);
				if (shouldUpdateDiagonalLeaves()) {
					Tag<Block> leavesTag = LeafySolutions.getLeavesTag(this);
					getDiagonalPositions(pos).forEach(blockPos -> updateIfMatchingLeaves(world, blockPos, leavesTag, leavesDecayDelay));
				}
			}

		}
	}

	private static Collection<BlockPos> getDiagonalPositions(BlockPos pos) {
		final Collection<BlockPos> diagonalPositions = new LinkedList<>();
		for (Direction direction : Direction.values()) {
			if (direction.getHorizontal() >= 0)
				diagonalPositions.add(pos.offset(direction).offset(direction.rotateYClockwise()));
			else {
				final BlockPos vOffsetPos = pos.offset(direction);
				for (Direction horizontal : HORIZONTAL_DIRECTIONS)
					diagonalPositions.add(vOffsetPos.offset(direction).offset(horizontal.rotateYClockwise()));
			}
		}

		return diagonalPositions;
	}

	private static void updateIfMatchingLeaves(WorldAccess world, BlockPos blockPos, Tag<Block> leavesTag, int delay) {
		final Block block = world.getBlockState(blockPos).getBlock();
		if (isMatchingLeaves(leavesTag, block))
			world.createAndScheduleBlockTick(blockPos, block, delay);
	}

	private static boolean isMatchingLeaves(Tag<Block> leavesTag, Block block) {
		return leavesTag != null && leavesTag.contains(block) ||
				block == recentLeaves;
	}
}
