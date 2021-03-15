package net.matt_with_a_hat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.matt_with_a_hat.BetterEnd;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    
    //Purpose of mixin: add chorus ent to end spawn list

    @Inject(at=@At("TAIL"), method="addEndMobs(Lnet/minecraft/world/biome/SpawnSettings$Builder;)V")
    private static void addEndMobs(SpawnSettings.Builder builder, CallbackInfo info)
    {
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(BetterEnd.CHORUS_ENT, 8, 1, 2));
    }
}
