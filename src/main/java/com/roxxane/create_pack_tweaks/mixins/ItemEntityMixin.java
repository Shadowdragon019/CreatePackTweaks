package com.roxxane.create_pack_tweaks.mixins;

import com.roxxane.create_pack_tweaks.CptConfig;
import com.roxxane.create_pack_tweaks.mixin_inferfaces.MergeDelay;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
public abstract class ItemEntityMixin extends Entity implements TraceableEntity, MergeDelay {
    @Unique
    private int cpt$mergeDelay = 0;

    @Unique
    @Override
    public void cpt$setMergeDelay(int value) {
        cpt$mergeDelay = value;
    }

    @Unique
    @Override
    public int cpt$getMergeDelay() {
        return cpt$mergeDelay;
    }

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void setItem(ItemStack p_32046_);

    @Shadow public abstract boolean isAttackable();

    public ItemEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Unique
    public void cpt$setRandomVelocity() {
        setDeltaMovement(getDeltaMovement().x, lavaSmeltingInitialVelocity.y, getDeltaMovement().z);

        addDeltaMovement(new Vec3(
            random.nextDouble() * lavaSmeltingInitialVelocity.x * 2 - lavaSmeltingInitialVelocity.x,
            0,
            random.nextDouble() * lavaSmeltingInitialVelocity.z * 2 - lavaSmeltingInitialVelocity.z
        ));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", remap = false,
        target = "Lnet/minecraft/world/entity/item/ItemEntity;getMaxHeightFluidType()Lnet/minecraftforge/fluids/FluidType;"))
    private void tickInject(CallbackInfo ci) {
        var level = level();
        var item = getItem().getItem();
        var pos = position();
        var blockPos = blockPosition();

        if (cpt$mergeDelay > 0)
            cpt$mergeDelay--;

        if (CptConfig.isLoaded() &&
            level.getFluidState(blockPos).is(FluidTags.LAVA) &&
            (CptConfig.lavaSmelting.containsKey(item) ||
            CptConfig.lavaSmelting.containsValue(item))
        ) {
            // Prevents the item from being processed multiple times
            setPos(pos.x, Math.ceil(pos.y), pos.z);

            // Prevent items from merging & reducing throughput
            cpt$mergeDelay = 60;

            cpt$setRandomVelocity();

            if (CptConfig.lavaSmelting.containsKey(item) && random.nextInt(lavaSmeltingConversionChance) == 0) {
                getItem().shrink(1);
                var itemEntity = new ItemEntity(level, xo, yo, zo,
                    new ItemStack(CptConfig.lavaSmelting.get(item))
                );
                level.addFreshEntity(itemEntity);

                ((ItemEntityMixin) (Object) itemEntity).cpt$setRandomVelocity();
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveDataInject(CompoundTag tag, CallbackInfo ci) {
        tag.putInt("MergeDelay", cpt$mergeDelay);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveDataInject(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("MergeDelay"))
            cpt$mergeDelay = tag.getInt("MergeDelay");
    }

    @Inject(method = "isMergable", at = @At("RETURN"), cancellable = true)
    private void isMergableInject(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && cpt$mergeDelay == 0);
    }

    @SuppressWarnings("rawtypes")
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V",
        at = @At("RETURN"))
    private void initInject1(EntityType type, Level level, CallbackInfo ci) {
        cpt$mergeDelay = 30;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/item/ItemEntity;)V", at = @At("RETURN"))
    private void initInject2(ItemEntity entity, CallbackInfo ci) {
        cpt$mergeDelay = 30;
    }
}
