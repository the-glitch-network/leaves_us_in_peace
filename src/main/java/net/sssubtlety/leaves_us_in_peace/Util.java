package net.sssubtlety.leaves_us_in_peace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.Direction;

public final class Util {
    private Util() { }

    public static final Direction[] HORIZONTAL_DIRECTIONS = {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.WEST
    };

    public static boolean isMatchingLeaves(TagKey<Block> leavesTag, BlockState block, BlockState currentLeaves) {
        return leavesTag != null && block.isIn(leavesTag) || block.isOf(currentLeaves.getBlock());
    }
}
