package net.shirojr.titanfabric.item.custom.misc;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.shirojr.titanfabric.screen.handler.BackPackItemScreenHandler;

public class BackPackItem extends Item {
    private final BackPackItem.Type backpackType;

    public BackPackItem(Settings settings, Type backPackType) {
        super(settings.rarity(backPackType.getRarity()));
        this.backpackType = backPackType;
    }

    public BackPackItem.Type getBackpackType() {
        return this.backpackType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        openScreen(user, user.getStackInHand(hand));
        return super.use(world, user, hand);
    }

    public static void openScreen(PlayerEntity user, ItemStack backpackItemStack) {
        World world = user.getWorld();
        if (!(backpackItemStack.getItem() instanceof BackPackItem backPackItem)) return;
        if (!world.isClient()) {
            user.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeItemStack(backpackItemStack);
                }

                @Override
                public Text getDisplayName() {
                    return new TranslatableText(backpackItemStack.getItem().getTranslationKey());
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new BackPackItemScreenHandler(syncId, player.getInventory(), inv, backPackItem.getBackpackType(), backpackItemStack);
                }
            });
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    public enum Type {
        SMALL("small", 6, Rarity.UNCOMMON),
        MEDIUM("medium", 12, Rarity.RARE),
        BIG("big", 18, Rarity.EPIC);

        private final String id;
        private final int size;
        private final Rarity rarity;

        Type(String id, int size, Rarity rarity) {
            this.id = id;
            this.size = size;
            this.rarity = rarity;
        }

        public String getId() {
            return id;
        }

        public int getSize() {
            return size;
        }

        public Rarity getRarity() {
            return rarity;
        }
    }
}

