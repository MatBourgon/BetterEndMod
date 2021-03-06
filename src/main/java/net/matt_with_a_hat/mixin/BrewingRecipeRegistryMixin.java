package net.matt_with_a_hat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.matt_with_a_hat.BetterEnd;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    
    @Inject(at=@At("TAIL"), method="registerDefaults()V")
    private static void registerDefaults(CallbackInfo info)
    {
        invokeRegisterPotionRecipe(Potions.AWKWARD, BetterEnd.itemVersEye, BetterEnd.POTION_ATTRACTION);
        invokeRegisterPotionRecipe(BetterEnd.POTION_ATTRACTION, Items.GLOWSTONE_DUST, BetterEnd.POTION_ATTRACTION_STRONG);
        invokeRegisterPotionRecipe(BetterEnd.POTION_ATTRACTION, Items.REDSTONE, BetterEnd.POTION_ATTRACTION_LONG);
    }

    @Invoker("registerPotionRecipe")
    public static void invokeRegisterPotionRecipe(Potion input, Item item, Potion Output)
    {
        throw new AssertionError();
    }
}