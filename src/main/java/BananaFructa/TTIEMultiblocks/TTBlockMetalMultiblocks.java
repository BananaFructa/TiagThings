package BananaFructa.TTIEMultiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.BlockIEMultiblock;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

public class TTBlockMetalMultiblocks extends BlockIEMultiblock<TTBlockTypes_MetalMultiblock> {
    public TTBlockMetalMultiblocks() {
        super("tt_metal_multiblock", Material.IRON, PropertyEnum.create("type", TTBlockTypes_MetalMultiblock.class), ItemBlockIEBase.class, new Object[]{IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP});
    }

    public boolean useCustomStateMapper() {
        return true;
    }

    public String getCustomStateMapping(int meta, boolean itemBlock) {
        return BlockTypes_MetalMultiblock.values()[meta].needsCustomState() ? TTBlockTypes_MetalMultiblock.values()[meta].getCustomState() : null;
    }

    public EnumPushReaction func_149656_h(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Nullable
    @Override
    public TileEntity createBasicTE(World world, TTBlockTypes_MetalMultiblock ttBlockTypes_metalMultiblock) {
        switch (ttBlockTypes_metalMultiblock) {
            case ELECTRIC_HEATER:
                return null; // TODO: tile entity
        }
        return null;
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean allowHammerHarvest(IBlockState state) {
        return true;
    }

}
