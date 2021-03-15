package net.matt_with_a_hat.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.matt_with_a_hat.BetterEnd;
import net.matt_with_a_hat.blocks.BlockWarpPad;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ChorusFruitItem.class)
public abstract class ChorusFruitMixin extends Item {

    //Purpose of mixin: Intercept chorus fruit teleport for warppad support

    public ChorusFruitMixin(Item.Settings settings)
    {
        super(settings);
    }

    @Inject(at=@At("HEAD"), method="finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", cancellable=true)
    private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {

        //If we're standing on a warppad, teleport. Else, normal chorus fruit implementation
        if (world.getBlockState(user.getBlockPos()) == BetterEnd.blockWarpPad.getDefaultState())
        {
            //Eat fruit and reduce amount
            info.setReturnValue(super.finishUsing(stack, world, user));

            //Find nearest warp pad by stored tree and direction
            BlockPos newpos = BlockWarpPad.warpPadTree.getNearestBlock(user.getBlockPos(), world, user.getHeadYaw());

            //Set cooldown
            if (user instanceof PlayerEntity) {
                ((PlayerEntity)user).getItemCooldownManager().set(this, 20);
            }

            //If a block exists
            if (newpos != null)
            {
                if (user.hasVehicle())
                    user.stopRiding();
                user.requestTeleport(newpos.getX() + 0.5, newpos.getY() + 0.5, newpos.getZ() + 0.5); //teleport to new block
                world.sendEntityStatus(user, (byte)46);
                SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                world.playSound((PlayerEntity)null, newpos.getX() + 0.5, newpos.getY() + 0.5, newpos.getZ() + 0.5, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                user.playSound(soundEvent, 1.0F, 1.0F);
                info.cancel(); //cancel out of function, don't call chorus fruit teleport
            }
        }
    }
}
