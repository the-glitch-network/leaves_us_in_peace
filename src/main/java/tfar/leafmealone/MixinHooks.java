package tfar.leafmealone;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class MixinHooks {
	public static Block block;

	private static final Map<Block, Tag<Block>> LEAVES_TAGS = new HashMap<>();

	public static Tag<Block> getOrSetLeavesTag(ServerWorld world, Block leavesBlock) {
		Tag<Block> leavesTag = LEAVES_TAGS.get(leavesBlock);
		if (leavesTag == null) {
			String idString = Registry.BLOCK.getId(leavesBlock).toString();
			if (!idString.endsWith("s")) idString += "s";
			leavesTag = world.getTagManager().blocks().get(new Identifier(idString));
			LEAVES_TAGS.put(leavesBlock, leavesTag);
		}

		return leavesTag;
	}

	public static Tag<Block> getLeavesTag(Block leavesBlock) {
		return LEAVES_TAGS.get(leavesBlock);
	}

	public static void onReload() {
		LEAVES_TAGS.clear();
	}
}
