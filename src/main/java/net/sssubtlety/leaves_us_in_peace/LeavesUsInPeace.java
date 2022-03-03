package net.sssubtlety.leaves_us_in_peace;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class LeavesUsInPeace {
	public static final String NAMESPACE = "leaves_us_in_peace";

	private static final String TREE_TYPES_SUB_PATH = "tree_types/";
	private static final String LEAVES_GROUPS_SUB_PATH = "leaves_groups/";
	private static final String LOGS_WITHOUT_LEAVES_PATH = "logs_without_leaves";

	public static final TagKey<Block> LOGS_WITHOUT_LEAVES = TagKey.of(Registry.BLOCK_KEY, new Identifier(NAMESPACE, LOGS_WITHOUT_LEAVES_PATH));

	private static final Map<Block, TagKey<Block>> LEAVES_TAGS = new HashMap<>();
	private static final Map<Block, TagKey<Block>> TREES_TAGS = new HashMap<>();

	public static void updateLeavesTags(Block leavesBlock) {
		updateBlockTag(leavesBlock, LEAVES_TAGS, LEAVES_GROUPS_SUB_PATH);
	}

	public static void updateLogLeavesTags(Block block) {
		updateBlockTag(block, TREES_TAGS, TREE_TYPES_SUB_PATH);
	}

	public static TagKey<Block> getLeavesTag(Block leavesBlock) {
		return LEAVES_TAGS.get(leavesBlock);
	}

	public static TagKey<Block> getLeavesForLog(Block block) {
		return TREES_TAGS.get(block);
	}

	private static void updateBlockTag(Block block, Map<Block, TagKey<Block>> tagMap, String subPath) {
		if (!tagMap.containsKey(block)) {
			final Identifier id = Registry.BLOCK.getId(block);
			tagMap.put(block, TagKey.of(Registry.BLOCK_KEY, new Identifier(id.getNamespace(), subPath + id.getPath())));
		}
	}
}
