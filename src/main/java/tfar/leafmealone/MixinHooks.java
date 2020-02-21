package tfar.leafmealone;

import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

import static net.minecraft.block.LeavesBlock.DISTANCE;

public class MixinHooks {

	public static int getDistance(BlockState state, BlockState neighbor) {
		if (BlockTags.LOGS.contains(neighbor.getBlock())) {
			return 0;
		} else {
			return neighbor.getBlock() == state.getBlock() ? neighbor.get(DISTANCE) : 7;
		}
	}

	public static BlockState handleDecay(BlockState state, IWorld worldIn, BlockPos pos) {
		int i = 7;
		try (
						BlockPos.PooledMutable blockpos$pooledmutable = BlockPos.PooledMutable.get()) {
			for (Direction direction : Direction.values()) {
				blockpos$pooledmutable.set(pos).setOffset(direction);
				i = Math.min(i, MixinHooks.getDistance(state,worldIn.getBlockState(blockpos$pooledmutable)) + 1);
				if (i == 1) {
					break;
				}
			}
		}
		return state.with(DISTANCE, i);
	}
}
