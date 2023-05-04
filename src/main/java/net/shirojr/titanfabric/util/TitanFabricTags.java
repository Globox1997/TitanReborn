package net.shirojr.titanfabric.util;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.shirojr.titanfabric.TitanFabric;

public class TitanFabricTags {

    public static class Blocks {
        // public static final TagKey<Block> RANDOMENTRY = createCommonTag("random_blocks");
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(TitanFabric.MOD_ID, name));
        }

        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> ARROWS = createCommonTag("arrows");

        private static TagKey<Item> createTag(String name) {

            return TagKey.of(Registry.ITEM_KEY ,new Identifier(TitanFabric.MOD_ID, name));
        }

        private static TagKey<Item> createCommonTag(String name) {

            return TagKey.of(Registry.ITEM_KEY ,new Identifier("c", name));
        }
    }
}
