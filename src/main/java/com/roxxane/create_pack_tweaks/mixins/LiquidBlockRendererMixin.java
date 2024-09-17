package com.roxxane.create_pack_tweaks.mixins;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("UnstableApiUsage")
@Mixin(LiquidBlockRenderer.class)
public abstract class LiquidBlockRendererMixin {
    @Shadow
    private static boolean isNeighborSameFluid(FluidState pFirstState, FluidState pSecondState) {
        return false;
    }

    @Shadow
    public static boolean shouldRenderFace(BlockAndTintGetter pLevel, BlockPos pPos, FluidState pFluidState, BlockState pBlockState, Direction pSide, FluidState pNeighborFluid) {
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Shadow
    private static boolean isFaceOccludedByNeighbor(BlockGetter pLevel, BlockPos pPos, Direction pSide, float pHeight, BlockState pBlockState) {
        return false;
    }

    @Shadow protected abstract float getHeight(BlockAndTintGetter pLevel, Fluid pFluid, BlockPos pPos, BlockState pBlockState, FluidState pFluidState);

    @Shadow protected abstract float calculateAverageHeight(BlockAndTintGetter pLevel, Fluid pFluid, float pCurrentHeight, float pHeight1, float pHeight2, BlockPos pPos);

    @Shadow protected abstract int getLightColor(BlockAndTintGetter pLevel, BlockPos pPos);

    @Shadow protected abstract void vertex(VertexConsumer pConsumer, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float alpha, float pU, float pV, int pPackedLight);

    /**
     * @author Roxxane
     * @reason Render flowing fluid differently & name the damn variables
     */
    @Overwrite
    public void tesselate(BlockAndTintGetter level, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        TextureAtlasSprite[] textureAtlasSprites = net.minecraftforge.client.ForgeHooksClient.getFluidSprites(level, pos, fluidState);
        int tintColor = net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, level, pos);

        float alpha = (float)(tintColor >> 24 & 255) / 255.0F;
        float red = (float)(tintColor >> 16 & 255) / 255.0F;
        float green = (float)(tintColor >> 8 & 255) / 255.0F;
        float blue = (float)(tintColor & 255) / 255.0F;

        BlockState downBlockState = level.getBlockState(pos.relative(Direction.DOWN));
        BlockState upBlockState = level.getBlockState(pos.relative(Direction.UP));
        BlockState northBlockState = level.getBlockState(pos.relative(Direction.NORTH));
        FluidState northFluidState = northBlockState.getFluidState();
        BlockState southBlockState = level.getBlockState(pos.relative(Direction.SOUTH));
        FluidState southFluidState = southBlockState.getFluidState();
        BlockState westBlockState = level.getBlockState(pos.relative(Direction.WEST));
        FluidState westFluidState = westBlockState.getFluidState();
        BlockState eastBlockState = level.getBlockState(pos.relative(Direction.EAST));
        FluidState eastFluidState = eastBlockState.getFluidState();

        boolean isAboveDifferentFluid = !isNeighborSameFluid(fluidState, upBlockState.getFluidState());
        boolean shouldRenderDownFace = shouldRenderFace(level, pos, fluidState, blockState, Direction.DOWN, downBlockState.getFluidState()) && !isFaceOccludedByNeighbor(level, pos, Direction.DOWN, 0.8888889F, downBlockState);
        boolean shouldRenderNorthFace = shouldRenderFace(level, pos, fluidState, blockState, Direction.NORTH, northFluidState);
        boolean shouldRenderSouthFace = shouldRenderFace(level, pos, fluidState, blockState, Direction.SOUTH, southFluidState);
        boolean shouldRenderWestFace = shouldRenderFace(level, pos, fluidState, blockState, Direction.WEST, westFluidState);
        boolean shouldRenderEastFace = shouldRenderFace(level, pos, fluidState, blockState, Direction.EAST, eastFluidState);

        if (isAboveDifferentFluid || shouldRenderDownFace || shouldRenderEastFace || shouldRenderWestFace || shouldRenderNorthFace || shouldRenderSouthFace) {
            float downShade = level.getShade(Direction.DOWN, true);
            float upShade = level.getShade(Direction.UP, true);
            float northShade = level.getShade(Direction.NORTH, true);
            float westShade = level.getShade(Direction.WEST, true);
            Fluid fluidType = fluidState.getType();
            float fluidHeight = getHeight(level, fluidType, pos, blockState, fluidState);
            float averageFluidHeightNE;
            float averageFluidHeightNW;
            float averageFluidHeightSE;
            float averageFluidHeightSW;

            if (fluidHeight >= 1.0F) {
                averageFluidHeightNE = 1.0F;
                averageFluidHeightNW = 1.0F;
                averageFluidHeightSE = 1.0F;
                averageFluidHeightSW = 1.0F;

            } else {
                float fluidHeightNorth = getHeight(level, fluidType, pos.north(), northBlockState, northFluidState);
                float fluidHeightSouth = getHeight(level, fluidType, pos.south(), southBlockState, southFluidState);
                float fluidHeightEast = getHeight(level, fluidType, pos.east(), eastBlockState, eastFluidState);
                float fluidHeightWest = getHeight(level, fluidType, pos.west(), westBlockState, westFluidState);
                averageFluidHeightNE = calculateAverageHeight(level, fluidType, fluidHeight, fluidHeightNorth, fluidHeightEast, pos.relative(Direction.NORTH).relative(Direction.EAST));
                averageFluidHeightNW = calculateAverageHeight(level, fluidType, fluidHeight, fluidHeightNorth, fluidHeightWest, pos.relative(Direction.NORTH).relative(Direction.WEST));
                averageFluidHeightSE = calculateAverageHeight(level, fluidType, fluidHeight, fluidHeightSouth, fluidHeightEast, pos.relative(Direction.SOUTH).relative(Direction.EAST));
                averageFluidHeightSW = calculateAverageHeight(level, fluidType, fluidHeight, fluidHeightSouth, fluidHeightWest, pos.relative(Direction.SOUTH).relative(Direction.WEST));

            }

            double chunkX = pos.getX() & 15;
            double chunkY = pos.getY() & 15;
            double chunkZ = pos.getZ() & 15;
            float verticalOffset = shouldRenderDownFace ? 0.001F : 0.0F;
            if (isAboveDifferentFluid && !isFaceOccludedByNeighbor(level, pos, Direction.UP, Math.min(Math.min(averageFluidHeightNW, averageFluidHeightSW), Math.min(averageFluidHeightSE, averageFluidHeightNE)), upBlockState)) {
                averageFluidHeightNW -= 0.001F;
                averageFluidHeightSW -= 0.001F;
                averageFluidHeightSE -= 0.001F;
                averageFluidHeightNE -= 0.001F;

                float shadedRed = upShade * red;
                float shadedBlue = upShade * green;
                float shadedGreen = upShade * blue;

                // Render top
                int lightColor = getLightColor(level, pos);
                TextureAtlasSprite stillSprite = textureAtlasSprites[0];
                float uMin = stillSprite.getU(0);
                float uMax = stillSprite.getU(16);
                float vMin = stillSprite.getV(0);
                float vMax = stillSprite.getV(16);
                vertex(vertexConsumer, chunkX + 0, chunkY + averageFluidHeightNW, chunkZ + 0, shadedRed, shadedBlue, shadedGreen, alpha, uMin, vMin, lightColor);
                vertex(vertexConsumer, chunkX + 0, chunkY + averageFluidHeightSW, chunkZ + 1, shadedRed, shadedBlue, shadedGreen, alpha, uMin, vMax, lightColor);
                vertex(vertexConsumer, chunkX + 1, chunkY + averageFluidHeightSE, chunkZ + 1, shadedRed, shadedBlue, shadedGreen, alpha, uMax, vMax, lightColor);
                vertex(vertexConsumer, chunkX + 1, chunkY + averageFluidHeightNE, chunkZ + 0, shadedRed, shadedBlue, shadedGreen, alpha, uMax, vMin, lightColor);

                // Render ceiling
                if (fluidState.shouldRenderBackwardUpFace(level, pos.above())) {
                    vertex(vertexConsumer, chunkX + 0, chunkY + averageFluidHeightNW, chunkZ + 0, shadedRed, shadedBlue, shadedGreen, alpha, uMin, vMin, lightColor);
                    vertex(vertexConsumer, chunkX + 0, chunkY + averageFluidHeightSW, chunkZ + 1, shadedRed, shadedBlue, shadedGreen, alpha, uMin, vMax, lightColor);
                    vertex(vertexConsumer, chunkX + 1, chunkY + averageFluidHeightSE, chunkZ + 1, shadedRed, shadedBlue, shadedGreen, alpha, uMax, vMax, lightColor);
                    vertex(vertexConsumer, chunkX + 1, chunkY + averageFluidHeightNE, chunkZ + 0, shadedRed, shadedBlue, shadedGreen, alpha, uMax, vMin, lightColor);
                }
            }

            // Down face
            if (shouldRenderDownFace) {
                float f40 = textureAtlasSprites[0].getU0();
                float f41 = textureAtlasSprites[0].getU1();
                float f42 = textureAtlasSprites[0].getV0();
                float f43 = textureAtlasSprites[0].getV1();
                int belowLightColor = getLightColor(level, pos.below());
                float shadedRed = downShade * red;
                float shadedGreen = downShade * green;
                float shadedBlue = downShade * blue;
                vertex(vertexConsumer, chunkX, chunkY + verticalOffset, chunkZ + 1, shadedRed, shadedGreen, shadedBlue, alpha, f40, f43, belowLightColor);
                vertex(vertexConsumer, chunkX, chunkY + verticalOffset, chunkZ, shadedRed, shadedGreen, shadedBlue, alpha, f40, f42, belowLightColor);
                vertex(vertexConsumer, chunkX + 1, chunkY + verticalOffset, chunkZ, shadedRed, shadedGreen, shadedBlue, alpha, f41, f42, belowLightColor);
                vertex(vertexConsumer, chunkX + 1, chunkY + verticalOffset, chunkZ + 1, shadedRed, shadedGreen, shadedBlue, alpha, f41, f43, belowLightColor);
            }

            // Renders sides
            int lightColor = getLightColor(level, pos);
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                float heightA;
                float heightB;
                double x1;
                double z1;
                double x2;
                double z2;
                boolean shouldRenderFace;
                switch (direction) {
                    case NORTH:
                        heightA = averageFluidHeightNW;
                        heightB = averageFluidHeightNE;
                        x1 = chunkX;
                        x2 = chunkX + 1;
                        z1 = chunkZ + 0.001F;
                        z2 = chunkZ + 0.001F;
                        shouldRenderFace = shouldRenderNorthFace;
                        break;
                    case SOUTH:
                        heightA = averageFluidHeightSE;
                        heightB = averageFluidHeightSW;
                        x1 = chunkX + 1;
                        x2 = chunkX;
                        z1 = chunkZ + 1 - 0.001F;
                        z2 = chunkZ + 1 - 0.001F;
                        shouldRenderFace = shouldRenderSouthFace;
                        break;
                    case WEST:
                        heightA = averageFluidHeightSW;
                        heightB = averageFluidHeightNW;
                        x1 = chunkX + 0.001F;
                        x2 = chunkX + 0.001F;
                        z1 = chunkZ + 1;
                        z2 = chunkZ;
                        shouldRenderFace = shouldRenderWestFace;
                        break;
                    default:
                        heightA = averageFluidHeightNE;
                        heightB = averageFluidHeightSE;
                        x1 = chunkX + 1 - 0.001F;
                        x2 = chunkX + 1 - 0.001F;
                        z1 = chunkZ;
                        z2 = chunkZ + 1;
                        shouldRenderFace = shouldRenderEastFace;
                }

                if (shouldRenderFace && !isFaceOccludedByNeighbor(level, pos, direction, Math.max(heightA, heightB), level.getBlockState(pos.relative(direction)))) {
                    float directionalShade = direction.getAxis() == Direction.Axis.Z ? northShade : westShade;
                    float shadedRed = upShade * directionalShade * red;
                    float shadedGreen = upShade * directionalShade * green;
                    float shadedBlue = upShade * directionalShade * blue;

                    TextureAtlasSprite stillSprite = textureAtlasSprites[0];
                    float uMin = stillSprite.getU(0);
                    float uMax = stillSprite.getU(16);
                    float vMin = stillSprite.getV(0);
                    float vMax = stillSprite.getV(16);

                    vertex(vertexConsumer, x1, chunkY + heightA, z1, shadedRed, shadedGreen, shadedBlue, alpha, uMin, vMin, lightColor);
                    vertex(vertexConsumer, x2, chunkY + heightB, z2, shadedRed, shadedGreen, shadedBlue, alpha, uMin, vMax, lightColor);
                    vertex(vertexConsumer, x2, chunkY + verticalOffset, z2, shadedRed, shadedGreen, shadedBlue, alpha, uMax, vMax, lightColor);
                    vertex(vertexConsumer, x1, chunkY + verticalOffset, z1, shadedRed, shadedGreen, shadedBlue, alpha, uMax, vMin, lightColor);
                }
            }
        }
    }
}