package net.sssubtlety.leaves_us_in_peace.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.sssubtlety.leaves_us_in_peace.LeavesUsInPeace;
import net.sssubtlety.leaves_us_in_peace.Util;
import org.spongepowered.asm.mixin.Dynamic;
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

import static net.sssubtlety.leaves_us_in_peace.FeatureControl.*;
import static net.sssubtlety.leaves_us_in_peace.LeavesUsInPeace.LOGS_WITHOUT_LEAVES;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {
	private static BlockState currentLeavesState;

	@Shadow @Final public static IntProperty DISTANCE;
	@Shadow @Final public static BooleanProperty PERSISTENT;

	@Shadow public abstract void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

	public LeavesBlockMixin(Settings settings) {
		super(settings);
		throw new IllegalStateException("MixinLeavesBlock's dummy constructor called!");
	}

	@Inject(method = "scheduledTick",at = @At(value = "HEAD"))
	private void captureBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
		if (shouldMatchLeavesTypes()) LeavesUsInPeace.updateLeavesTags(this);
		currentLeavesState = state;
	}

	@ModifyArgs(method = "updateDistanceFromLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/LeavesBlock;getDistanceFromLog(Lnet/minecraft/block/BlockState;)I"))
	private static void checkBlockState(Args args, BlockState leavesState, WorldAccess world, BlockPos pos) {
		if (shouldMatchLogsToLeaves()) LeavesUsInPeace.updateLogLeavesTags(((BlockState) args.get(0)).getBlock());
	}

	// If a log_leaves tag is found, match it. Otherwise, match all logs like vanilla
	@Redirect(
			method = "getDistanceFromLog",
			at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/tag/TagKey;)Z"),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/tag/BlockTags;LOGS:Lnet/minecraft/tag/TagKey;"))
	)
	private static boolean tryMatchLog(BlockState state, TagKey<Block> tagKey) {
		final Block block = state.getBlock();
		if (shouldMatchLogsToLeaves()) {
			if (state.isIn(LOGS_WITHOUT_LEAVES)) return false;
			if (currentLeavesState != null) {
				TagKey<Block> logLeavesTag = LeavesUsInPeace.getLeavesForLog(block);
				if (logLeavesTag != null) return currentLeavesState.isIn(logLeavesTag);
			}
		}

		return state.isIn(BlockTags.LOGS);
	}

	@ModifyConstant(method = "getDistanceFromLog", constant = @Constant(classValue = LeavesBlock.class))
	@Dynamic //MCDev doesn't know an `obj instanceof Clazz` redirect can return a boolean
	private static boolean matchLeaves(Object block, Class<?> leavesBlockClass, BlockState state) {
		if (shouldIgnorePersistentLeaves()) {
			// non-persistent only care about other non-persistent,
			//   persistent care about BOTH non/persistent
			if (currentLeavesState != null && !currentLeavesState.get(PERSISTENT)) {
				final Optional<Boolean> optOtherPersistent = state.getOrEmpty(PERSISTENT);
				if (optOtherPersistent.isPresent()) {
					if (optOtherPersistent.get()) return false;
				}
			}
		}

		if (currentLeavesState != null && shouldMatchLeavesTypes()) {
			if (state.getOrEmpty(DISTANCE).isEmpty()) return false;
			TagKey<Block> leavesTag = LeavesUsInPeace.getLeavesTag(currentLeavesState.getBlock());
			return Util.isMatchingLeaves(leavesTag, state, currentLeavesState);
		}

		return state.getBlock() instanceof LeavesBlock;
	}

	@Inject(method = "scheduledTick", at = @At("TAIL"))
	private void postScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (!state.get(PERSISTENT) && shouldAccelerateLeavesDecay()) {
			if (state.get(DISTANCE) >= 7) {
				randomTick(state, world, pos, random);
				if (shouldUpdateDiagonalLeaves()) {
					TagKey<Block> leavesTag = LeavesUsInPeace.getLeavesTag(this);
					getDiagonalPositions(pos).forEach(blockPos -> updateIfMatchingLeaves(world, blockPos, leavesTag, random));
				}
			} else if (world.getBlockState(pos).get(DISTANCE) >= 7) {
				world.createAndScheduleBlockTick(pos, this, getDecayDelay(random));
			}
		}
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
	private void postRemoveBlock(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (shouldDoDecayingLeavesEffects()) this.spawnBreakParticles(world, null, pos, state);
	}

	private static Collection<BlockPos> getDiagonalPositions(BlockPos pos) {
		final Collection<BlockPos> diagonalPositions = new LinkedList<>();
		for (Direction direction : Direction.values()) {
			if (direction.getHorizontal() >= 0)
				diagonalPositions.add(pos.offset(direction).offset(direction.rotateYClockwise()));
			else {
				final BlockPos vOffsetPos = pos.offset(direction);
				for (Direction horizontal : Util.HORIZONTAL_DIRECTIONS)
					diagonalPositions.add(vOffsetPos.offset(direction).offset(horizontal.rotateYClockwise()));
			}
		}

		return diagonalPositions;
	}

	private static void updateIfMatchingLeaves(WorldAccess world, BlockPos blockPos, TagKey<Block> leavesTag, Random random) {
		final BlockState block = world.getBlockState(blockPos);
		if (Util.isMatchingLeaves(leavesTag, block, currentLeavesState))
			world.createAndScheduleBlockTick(blockPos, block.getBlock(), getDecayDelay(random));
	}
}
