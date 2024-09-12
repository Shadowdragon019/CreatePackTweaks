package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.roxxane.create_pack_tweaks.CptConfig.lavaSmeltingConversionChance;
import static com.roxxane.create_pack_tweaks.CptConfig.lavaSmeltingInitialVelocity;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity implements TraceableEntity {
    @Unique
    public int createPackTweaks$mergeDelay = 0;

    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void setItem(ItemStack p_32046_);

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/ItemEntity;getMaxHeightFluidType()Lnet/minecraftforge/fluids/FluidType;"
        )
    )
    private void tickInject(CallbackInfo ci) {
        var level = level();
        var item = getItem().getItem();
        var pos = position();
        var blockPos = blockPosition();

        if (createPackTweaks$mergeDelay > 0)
            createPackTweaks$mergeDelay--;

        if (CptConfig.isLoaded() &&
            level.getFluidState(blockPos).is(FluidTags.LAVA) &&
            (CptConfig.lavaSmelting.containsKey(item) ||
            CptConfig.lavaSmelting.containsValue(item))
        ) {
            // Prevents the item from being processed multiple times
            setPos(pos.x, Math.ceil(pos.y), pos.z);

            // Prevent items from merging & reducing throughput
            createPackTweaks$mergeDelay = 60;

            setDeltaMovement(
                random.nextDouble() * lavaSmeltingInitialVelocity.x * 2 - lavaSmeltingInitialVelocity.x,
                lavaSmeltingInitialVelocity.y,
                random.nextDouble() * lavaSmeltingInitialVelocity.z * 2 - lavaSmeltingInitialVelocity.z
            );

            if (CptConfig.lavaSmelting.containsKey(item) && random.nextInt(lavaSmeltingConversionChance) == 0) {
                getItem().shrink(1);
                level.addFreshEntity(new ItemEntity(level, xo, yo, zo,
                    new ItemStack(CptConfig.lavaSmelting.get(item)),
                    random.nextDouble() * lavaSmeltingInitialVelocity.x * 2 - lavaSmeltingInitialVelocity.x,
                    lavaSmeltingInitialVelocity.y,
                    random.nextDouble() * lavaSmeltingInitialVelocity.z * 2 - lavaSmeltingInitialVelocity.z
                ));
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveDataInject(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("MergeDelay", createPackTweaks$mergeDelay);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveDataInject(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("MergeDelay"))
            createPackTweaks$mergeDelay = tag.getInt("MergeDelay");
    }

    @Inject(method = "isMergable", at = @At("RETURN"), cancellable = true)
    private void isMergableInject(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && createPackTweaks$mergeDelay == 0);
    }
}
