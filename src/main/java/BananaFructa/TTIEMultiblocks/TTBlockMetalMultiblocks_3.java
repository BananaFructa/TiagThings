package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import BananaFructa.TTIEMultiblocks.IECopy.ItemBlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

/**
 * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
 * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
 */

public class TTBlockMetalMultiblocks_3 extends BlockTTMultiblock<TTBlockTypes_MetalMultiblock_3> {

    static PropertyInteger animProp = PropertyInteger.create("anim_id",0,12);

    public TTBlockMetalMultiblocks_3() {
        super("tt_metal_multiblock_3", Material.IRON, PropertyEnum.create("type", TTBlockTypes_MetalMultiblock_3.class),animProp, ItemBlockTTBase.class, new Object[]{IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP,animProp});
        this.setAllNotNormalBlock();
        this.setHardness(3.0F);
        this.setResistance(15.0F);
        this.lightOpacity = 0;
    }

    public boolean useCustomStateMapper() {
        return true;
    }

    public String getCustomStateMapping(int meta, boolean itemBlock) {
        return TTBlockTypes_MetalMultiblock_3.values()[meta].needsCustomState() ? TTBlockTypes_MetalMultiblock_3.values()[meta].getCustomState() : null;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Nullable
    @Override
    public TileEntity createBasicTE(World world, TTBlockTypes_MetalMultiblock_3 ttBlockTypes_metalMultiblock) {
        switch (ttBlockTypes_metalMultiblock) {
            case SHAFT_FURNACE:
            case SHAFT_FURNACE_CHILD:
                return new TileEntityShaftFurnace();
            case FBR:
            case FBR_CHILD:
                return new TileEntityFBR();
            case CCM:
            case CCM_CHILD:
                return new TileEntityCCM();
            case SMALL_COAL_BOILER:
            case SMALL_COAL_BOILER_CHILD:
                return new TileEntitySmallCoalBoiler();
            case STEAM_ENGINE:
            case STEAM_ENGINE_CHILD:
                return new TileEntitySteamEngine();
            case LATHE:
            case LATHE_CHILD:
                return new TileEntityLathe();
            case METAL_ROLLER:
            case METAL_ROLLER_CHILD:
                return new TileEntityMetalRoller();
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
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && state.getValue(this.property).hasGui)
        {
            player.openGui(TTMain.INSTANCE, state.getValue(this.property).guiId, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }
}
