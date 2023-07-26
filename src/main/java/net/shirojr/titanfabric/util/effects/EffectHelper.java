package net.shirojr.titanfabric.util.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.shirojr.titanfabric.TitanFabric;
import net.shirojr.titanfabric.item.custom.TitanFabricArrowItem;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Helper class for TitanFabric {@linkplain WeaponEffects}
 */
public class EffectHelper {
    public static final String EFFECTS_NBT_KEY = TitanFabric.MODID + ".effect";
    public static final String EFFECTS_STRENGTH_NBT_KEY = TitanFabric.MODID + ".effect.strength";

    /**
     * Returns {@linkplain WeaponEffects TitanFabric WeaponEffect} strength. It does not translate directly to StatusEffect's amplifier!<br><br>
     * Used for StatusEffect duration and amplifier calculation
     *
     * @param itemStack
     * @return Strength of the Weapon Effect
     */
    public static int getEffectStrength(ItemStack itemStack) {
        return itemStack.getOrCreateNbt().getInt(EFFECTS_STRENGTH_NBT_KEY);
    }

    /**
     * Sets the {@linkplain WeaponEffects TitanFabric WeaponEffect} strength. It does not translate directly to StatusEffect's amplifier!<br><br>
     * Used to write custom NBT information to the ItemStack
     *
     * @param itemStack
     * @param strength
     * @return
     */
    public static ItemStack setEffectStrength(ItemStack itemStack, int strength) {
        itemStack.getOrCreateNbt().putInt(EFFECTS_STRENGTH_NBT_KEY, strength);
        return itemStack;
    }

    /**
     * Sets the {@linkplain WeaponEffects TitanFabric WeaponEffect}.<br><br>
     * Used to write custom NBT information to the ItemStack
     * @param itemStack
     * @param effect
     * @return
     */
    public static ItemStack getStackWithEffect(ItemStack itemStack, WeaponEffects effect) {
        itemStack.getOrCreateNbt().putString(EFFECTS_NBT_KEY, effect.getId());
        return itemStack;
    }

    /**
     *
     * @param itemStack
     * @return True, if the ItemStack contains any {@linkplain WeaponEffects TitanFabric WeaponEffect}
     */
    public static boolean stackHasWeaponEffect(ItemStack itemStack) {
        return itemStack.getOrCreateNbt().contains(EFFECTS_NBT_KEY);
    }

    /**
     * Builds the ToolTip TranslationKey for the ItemStack which has a TitanFabric Weapon Effect
     * @param tooltip original tooltip of the ItemStack
     * @param stack original ItemStack
     * @return ItemStack with description for current {@linkplain WeaponEffects TitanFabric WeaponEffect}
     */
    public static List<Text> appendToolTip(List<Text> tooltip, ItemStack stack) {
        String translation = "tooltip.titanfabric." + EffectHelper.getEffectStrength(stack);

        switch (WeaponEffects.getEffect(stack.getOrCreateNbt().getString(EffectHelper.EFFECTS_NBT_KEY))) {
            case BLIND -> translation += "Blind";
            case FIRE -> translation += "Fire";
            case POISON -> translation += "Poison";
            case WEAK -> translation += "Weak";
            case WITHER -> translation += "Wither";
        }

        tooltip.add(new TranslatableText(translation));
        return tooltip;
    }

    private static List<WeaponEffects> getSwordEffects() {
        return Arrays.stream(WeaponEffects.values()).toList();
    }

    private static List<WeaponEffects> getArrowEffects() {
        return Arrays.stream(WeaponEffects.values()).filter(effect -> effect != WeaponEffects.FIRE).toList();
    }

    /**
     * Generates a List of ItemStacks from a single base ItemStack. The List contains all possible variants of the
     * base ItemStack in combination with the {@linkplain WeaponEffects TitanFabric WeaponEffects}
     * @param baseItem original ItemStack
     * @param stacks list of all registered ItemStacks.
     * @return list of all registered ItemStacks with the newly generated {@linkplain WeaponEffects TitanFabric WeaponEffect} ItemStack variants
     */
    public static DefaultedList<ItemStack> generateAllEffectVersionStacks(Item baseItem, DefaultedList<ItemStack> stacks) {
        //FIXME: wrong rendering of the creative menu's "clear items" slot might be coming from here?
        // breakpoint it and see, if any stacks from the incoming stacks parameter are being overwritten
        List<WeaponEffects> possibleEffects = getSwordEffects();
        if (baseItem instanceof TitanFabricArrowItem) possibleEffects = getArrowEffects();

        for (WeaponEffects entry : possibleEffects) {
            ItemStack firstEffect = EffectHelper.getStackWithEffect(new ItemStack(baseItem), entry);
            stacks.add(setEffectStrength(firstEffect, 1));
            ItemStack secondEffect = EffectHelper.getStackWithEffect(new ItemStack(baseItem), entry);
            stacks.add(setEffectStrength(secondEffect, 2));
        }
        return stacks;
    }


    public static void applyWeaponEffectOnTargetFromNBT(World world, ItemStack itemStack, LivingEntity user, LivingEntity target) {
        String currentEffect = itemStack.getOrCreateNbt().getString(EFFECTS_NBT_KEY);
        if (currentEffect == null) return;

        int strength = EffectHelper.getEffectStrength(itemStack);

        applyWeaponEffectOnTarget(WeaponEffects.getEffect(currentEffect), strength, world, itemStack, user, target);
    }


    public static void applyWeaponEffectOnTarget(WeaponEffects effect, int effectStrength, World world, ItemStack itemStack, LivingEntity user, LivingEntity target) {
        if (world.isClient() || effect == null) return;
        if(world.getRandom().nextInt(100) >= (25 * effectStrength)) return;

        //TODO: change values for balancing as needed
        switch (effect) {
            case BLIND -> {
                target.addStatusEffect(new StatusEffectInstance(
                        effect.getStatusEffect(), effectStrength > 1 ? 4 : 10, effectStrength - 1)
                );
            }
            case FIRE -> target.setOnFireFor(effectStrength > 1 ? 10 : 5);
            case POISON -> {
                target.addStatusEffect(new StatusEffectInstance(
                        effect.getStatusEffect(), effectStrength > 1 ? 5 : 10, effectStrength - 1)
                );
            }
            case WEAK -> {
                target.addStatusEffect(new StatusEffectInstance(
                        effect.getStatusEffect(), effectStrength > 1 ? 6 : 10, effectStrength - 1)
                );
            }
            case WITHER -> {
                target.addStatusEffect(new StatusEffectInstance(
                        effect.getStatusEffect(), effectStrength > 1 ? 7 : 10, effectStrength - 1)
                );
            }
        }
    }
}