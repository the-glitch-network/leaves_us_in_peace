package tfar.leafmealone;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MixinHooks {
	private static final String LOG_LEAVES_TAG_DIRECTORY = "log_leaves/";
	public static Block block;

	private static final Map<Block, Tag<Block>> LEAVES_TAGS = new HashMap<>();
	private static final Map<Block, Tag<Block>> LOG_LEAVES_TAGS = new HashMap<>();

	public static void updateLeavesTags(ServerWorld world, Block leavesBlock) {
		getOrSetBlockTag(world, leavesBlock, LEAVES_TAGS, MixinHooks::leavesIdTransformer);
	}

	public static void updateLogLeavesTags(ServerWorld world, Block leavesBlock) {
		getOrSetBlockTag(world, leavesBlock, LOG_LEAVES_TAGS, MixinHooks::logIdTransformer);
	}

	public static Tag<Block> getLeavesTag(Block leavesBlock) {
		return LEAVES_TAGS.get(leavesBlock);
	}

	public static Tag<Block> getLogLeavesTag(Block logBlock) {
		return LOG_LEAVES_TAGS.get(logBlock);
	}

	public static void onReload() {
		LEAVES_TAGS.clear();
	}

	private static void getOrSetBlockTag(ServerWorld world, Block block, Map<Block, Tag<Block>> tagMap,
											   UnaryOperator<Identifier> idTransformer) {
		Tag<Block> leavesTag = tagMap.get(block);
		if (leavesTag == null) {
			final Identifier transformedId = idTransformer.apply(Registry.BLOCK.getId(block));
			leavesTag = world.getTagManager().blocks().get(transformedId);
			tagMap.put(block, leavesTag);
		}
	}

	private static Identifier leavesIdTransformer(Identifier id) {
//		String idString = id.toString();
//		if (!idString.endsWith("s")) idString += "s";
//		return new Identifier(idString);
		return id;
	}

	private static Identifier logIdTransformer(Identifier id) {
		return new Identifier(id.getNamespace(), LOG_LEAVES_TAG_DIRECTORY + id.getPath());
	}
}
