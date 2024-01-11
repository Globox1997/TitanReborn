package net.shirojr.titanfabric.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.shirojr.titanfabric.util.effects.EffectHelper;
import net.shirojr.titanfabric.util.effects.WeaponEffects;
import net.shirojr.titanfabric.util.items.Anvilable;
import net.shirojr.titanfabric.util.items.EssenceCrafting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TitanFabricSwordItem extends SwordItem implements EssenceCrafting, Anvilable {
    private final boolean hasWeaponEffects;
    private final WeaponEffects baseEffect;

    public TitanFabricSwordItem(boolean hasWeaponEffects, ToolMaterial toolMaterial, int attackDamage, float attackSpeed, WeaponEffects baseEffect, Item.Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.hasWeaponEffects = hasWeaponEffects;
        this.baseEffect = baseEffect;
    }

    @Override
    public ItemType isType() {
        return ItemType.PRODUCT;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (!isIn(group))
            return;
        if (this.hasWeaponEffects) {
            EffectHelper.generateAllEffectVersionStacks(this, stacks);
        } else {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!EffectHelper.stackHasWeaponEffect(stack.getOrCreateNbt()))
            super.appendTooltip(stack, world, tooltip, context);
        super.appendTooltip(stack, world, EffectHelper.appendSwordToolTip(tooltip, stack), context);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        EffectHelper.applyWeaponEffectOnTargetFromNBT(target.getWorld(), stack, attacker, target);
        if (this.baseEffect != null) {
            EffectHelper.applyWeaponEffectOnTarget(this.baseEffect, 1, target.getWorld(), stack, attacker, target);
        }

        return super.postHit(stack, target, attacker);
    }
}
