package net.sssubtlety.leaves_us_in_peace;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.Direction;

public final class Util {
    private Util() { }

    public static final Direction[] HORIZONTAL_DIRECTIONS = {
            Direction.NORTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.WEST
    };

    public static boolean isMatchingLeaves(Tag<Block> leavesTag, Block block, Block currentLeaves) {
        return leavesTag != null && leavesTag.contains(block) ||
//				block == currentLeaves;
                block == currentLeaves;
    }
}
