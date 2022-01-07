package net.sssubtlety.leaves_us_in_peace;

import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static net.sssubtlety.leaves_us_in_peace.FeatureControl.shouldFetchTranslationUpdates;

public class LeavesUsInPeace {
	public static final String NAMESPACE = "leaves_us_in_peace";

	private static final String TREE_TYPES_PATH = "tree_types/";
	private static final String LEAVES_GROUPS_PATH = "leaves_groups/";
	private static final String LOGS_WITHOUT_LEAVES_PATH = "logs_without_leaves";

	public static final Tag<Block> LOGS_WITHOUT_LEAVES = TagFactory.BLOCK.create(new Identifier(NAMESPACE, LOGS_WITHOUT_LEAVES_PATH));

	private static final Map<Block, Tag<Block>> LEAVES_TAGS = new HashMap<>();
	private static final Map<Block, Tag<Block>> TREES_TAGS = new HashMap<>();

	public static void updateLeavesTags(Block leavesBlock) {
		updateBlockTag(leavesBlock, LEAVES_TAGS, LEAVES_GROUPS_PATH);
	}

	public static void updateLogLeavesTags(Block block) {
		updateBlockTag(block, TREES_TAGS, TREE_TYPES_PATH);
	}

	public static Tag<Block> getLeavesTag(Block leavesBlock) {
		return LEAVES_TAGS.get(leavesBlock);
	}

	public static Tag<Block> getLeavesForLog(Block block) {
		return TREES_TAGS.get(block);
	}

	public static void onReload() {
		LEAVES_TAGS.clear();
		TREES_TAGS.clear();
	}

	private static void updateBlockTag(Block block, Map<Block, Tag<Block>> tagMap, String subPath) {
		if (tagMap.get(block) == null) {
			final Identifier id = Registry.BLOCK.getId(block);
			tagMap.put(block, ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.BLOCK_KEY).getTag(
					new Identifier(id.getNamespace(), subPath + id.getPath())));
		}
	}

	public static class Init implements ModInitializer {
		static {
			FeatureControl.init();

			ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> onReload());

			if (shouldFetchTranslationUpdates())
				CrowdinTranslate.downloadTranslations("leaves-us-in-peace");
		}

		@Override
		public void onInitialize() { }
	}
}
