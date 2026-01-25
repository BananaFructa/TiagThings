package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import BananaFructa.TTIEMultiblocks.IECopy.ItemBlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public class TTBlockMetalMultiblocks extends BlockTTMultiblock<TTBlockTypes_MetalMultiblock> {

    static PropertyInteger animProp = PropertyInteger.create("anim_id",0,1);

    public TTBlockMetalMultiblocks() {
        super("tt_metal_multiblock", Material.IRON, PropertyEnum.create("type", TTBlockTypes_MetalMultiblock.class), animProp, ItemBlockTTBase.class, new Object[]{IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP,animProp});
        //this.setAllNotNormalBlock();
        this.setHardness(3.0F);
        this.setResistance(15.0F);
        this.lightOpacity = 0;
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock.ELECTRIC_HEATER.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock.FLARE_STACK.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock.COAL_BOILER.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock.CLARIFIER.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock.UM_PHOTOLITHOGRAPHY_MACHINE.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
    }

    public boolean useCustomStateMapper() {
        return true;
    }

    public String getCustomStateMapping(int meta, boolean itemBlock) {
        return TTBlockTypes_MetalMultiblock.values()[meta].needsCustomState() ? TTBlockTypes_MetalMultiblock.values()[meta].getCustomState() : null;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Nullable
    @Override
    public TileEntity createBasicTE(World world, TTBlockTypes_MetalMultiblock ttBlockTypes_metalMultiblock) {
        switch (ttBlockTypes_metalMultiblock) {
            case ELECTRIC_HEATER:
            case ELECTRIC_HEATER_CHILD:
                return new TileEntityElectricHeater();
            case FLARE_STACK:
            case FLARE_STACK_CHILD:
                return new TileEntityFlareStack();
            case COAL_BOILER:
            case COAL_BOILER_CHILD:
                return new TileEntityCoalBoiler();
            case CLARIFIER:
            case CLARIFIER_CHILD:
                return new TileEntityClarifier();
            case WATER_FILTER:
            case WATER_FILTER_CHILD:
                return new TileEntityWaterFilter();
            case OIL_BOILER:
            case OIL_BOILER_CHILD:
                return new TileEntityOilBoiler();
            case UM_PHOTOLITHOGRAPHY_MACHINE:
            case UM_PHOTOLITHOGRAPHY_MACHINE_CHILD:
                return new TileEntityUMPLM();
            /*case COMPUTER_CLUSTER_UNIT:
            case COMPUTER_CLUSTER_UNIT_CHILD:
                return new TileEntityComputerClusterUnit();*/
        }
        return null;
    }

    @Override
    public boolean allowHammerHarvest(IBlockState state) {
        return true;
    }


    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + ((IEBlockInterfaces.IIEMetaBlock)this).getIEBlockName();
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return super.isSideSolid(base_state, world, pos, side);
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return true;
    }
}
