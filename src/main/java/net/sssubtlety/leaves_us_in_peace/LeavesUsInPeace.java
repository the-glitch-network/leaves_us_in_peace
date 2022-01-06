package net.sssubtlety.leaves_us_in_peace;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class LeavesUsInPeace {
	public static final String NAMESPACE = "leaves_us_in_peace";

	private static final String TREES_PATH = "trees/";

	private static final Map<Block, Tag<Block>> LEAVES_TAGS = new HashMap<>();
	private static final Map<Block, Tag<Block>> TREES_TAGS = new HashMap<>();

	public static void updateLeavesTags(Block leavesBlock) {
		updateBlockTag(leavesBlock, LEAVES_TAGS, LeavesUsInPeace::leavesIdTransformer);
	}

	public static void updateLogLeavesTags(Block block) {
		updateBlockTag(block, TREES_TAGS, LeavesUsInPeace::logIdTransformer);
	}

	public static Tag<Block> getLeavesTag(Block leavesBlock) {
		return LEAVES_TAGS.get(leavesBlock);
	}

	public static Tag<Block> getLogLeavesTag(Block logBlock) {
		return TREES_TAGS.get(logBlock);
	}

	public static void onReload() {
		LEAVES_TAGS.clear();
		TREES_TAGS.clear();
	}

	private static void updateBlockTag(Block block, Map<Block, Tag<Block>> tagMap,
									   UnaryOperator<Identifier> idTransformer) {

		if (tagMap.get(block) == null)
			tagMap.put(block, ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getTag(
					idTransformer.apply(Registry.BLOCK.getId(block))));
	}

	private static Identifier leavesIdTransformer(Identifier id) {
		return id;
	}

	private static Identifier logIdTransformer(Identifier id) {
		return new Identifier(id.getNamespace(), TREES_PATH + id.getPath());
	}

	public static class Init implements ModInitializer {
		@Override
		public void onInitialize() {
			ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> onReload());
		}
	}
}
