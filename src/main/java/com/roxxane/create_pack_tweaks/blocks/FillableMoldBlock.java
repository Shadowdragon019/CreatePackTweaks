package com.roxxane.create_pack_tweaks.blocks;

import com.roxxane.create_pack_tweaks.blocks.entities.CptBlockEntities;
import com.roxxane.create_pack_tweaks.blocks.state_properties.CptStateProperties;
import com.roxxane.create_pack_tweaks.blocks.state_properties.MaterialState;
import com.roxxane.create_pack_tweaks.blocks.state_properties.ShapeState;
import com.roxxane.create_pack_tweaks.items.CptItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

// TODO: If in water, extinguish well consuming water
@SuppressWarnings("deprecation")
public class FillableMoldBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final VoxelShape east = box(0, 0, 2, 16, 5, 14);
    public static final VoxelShape west = box(0, 0, 2, 16, 5, 14);
    public static final VoxelShape south = box(2, 0, 0, 14, 5, 16);
    public static final VoxelShape north = box(2, 0, 0, 14, 5, 16);

    public static final Map<ShapeState, Map<MaterialState, ItemEntry<Item>>> materialItemMap = Map.of(
        ShapeState.ingot, Map.of(
            MaterialState.mushyPaste, CptItems.smallPileOfMushyPaste,
            MaterialState.mushyBrick, CptItems.mushyBrick
        )
    );

    public FillableMoldBlock(Properties pProperties) {
        super(pProperties);

        registerDefaultState(
            getStateDefinition().any()
                .setValue(CptStateProperties.material, MaterialState.none)
                .setValue(CptStateProperties.shape, ShapeState.ingot)
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
        );
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED))
            return Fluids.WATER.getSource(false);
        else {
            return Fluids.EMPTY.defaultFluidState();
        }
    }

    @Override
    public @NotNull VoxelShape getShape(
        BlockState state,
        @NotNull BlockGetter level,
        @NotNull BlockPos blockPos,
        @NotNull CollisionContext ctx
    ) {
        return switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST -> east;
            case WEST -> west;
            case SOUTH -> south;
            case NORTH -> north;
            default -> throw new IllegalStateException("Not a possible direction! How?!");
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = defaultBlockState();
        Direction direction = ctx.getClickedFace();
        FluidState fluidState = level.getFluidState(pos);

        if (!ctx.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        } else {
            state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
        }
        state.setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);

        return state;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(
            CptStateProperties.material,
            CptStateProperties.shape,
            BlockStateProperties.WATERLOGGED,
            BlockStateProperties.HORIZONTAL_FACING
        );
    }

    @Override
    public @NotNull InteractionResult use(
        @NotNull BlockState state,
        @NotNull Level level,
        @NotNull BlockPos pos,
        @NotNull Player player,
        @NotNull InteractionHand hand,
        @NotNull BlockHitResult hit
    ) {
        var itemInHand = player.getItemInHand(hand);
        var currentMaterial = state.getValue(CptStateProperties.material);

        if (hand == InteractionHand.MAIN_HAND)
            for (var shapeEntry : materialItemMap.entrySet())
                if (state.getValue(CptStateProperties.shape) == shapeEntry.getKey())
                    for (var entry : shapeEntry.getValue().entrySet()) {
                        var entryMaterial = entry.getKey();
                        var entryItem = entry.getValue().get();

                        if (currentMaterial == MaterialState.none && itemInHand.is(entryItem)) {
                            if (!player.getAbilities().instabuild)
                                player.getItemInHand(hand).shrink(1);
                            level.setBlock(pos, state.setValue(CptStateProperties.material, entryMaterial),
                                1 | 2);
                            return InteractionResult.SUCCESS;
                        } else if (currentMaterial == entryMaterial && itemInHand.isEmpty()) {
                            player.addItem(entryItem.getDefaultInstance());
                            level.setBlock(pos, state.setValue(CptStateProperties.material, MaterialState.none),
                                1 | 2);
                            return InteractionResult.SUCCESS;
                        }
                    }

        return super.use(state, level, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return CptBlockEntities.mold.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        if (level.isClientSide())
            return null;
        return createTickerHelper(
            type,
            CptBlockEntities.mold.get(),
            (tickLevel, tickPos, tickState, tickBlockEntity) ->
                tickBlockEntity.serverTick(tickLevel, tickPos, tickState)
        );
    }

    // Makes the model visible
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}