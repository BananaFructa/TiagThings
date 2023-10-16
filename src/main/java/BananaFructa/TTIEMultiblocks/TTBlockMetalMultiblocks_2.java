package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import BananaFructa.TTIEMultiblocks.IECopy.ItemBlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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

public class TTBlockMetalMultiblocks_2 extends BlockTTMultiblock<TTBlockTypes_MetalMultiblock_2> {
    public TTBlockMetalMultiblocks_2() {
        super("tt_metal_multiblock_2", Material.IRON, PropertyEnum.create("type", TTBlockTypes_MetalMultiblock_2.class), ItemBlockTTBase.class, new Object[]{IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP});
        for (int i = 0;i < TTBlockTypes_MetalMultiblock_2.values().length;i++) {
            if (i != TTBlockTypes_MetalMultiblock_2.MASONRY_HEATER.getMeta() && i!=TTBlockTypes_MetalMultiblock_2.MEMORY_FORMATTER_CHILD.getMeta()) setNotNormalBlock(i);
        }
        this.setAllNotNormalBlock();
        this.setHardness(3.0F);
        this.setResistance(15.0F);
        this.lightOpacity = 0;
        this.setMetaBlockLayer(TTBlockTypes_MetalMultiblock_2.CLAY_OVEN.getMeta(), new BlockRenderLayer[]{BlockRenderLayer.CUTOUT});
    }

    public boolean useCustomStateMapper() {
        return true;
    }

    public String getCustomStateMapping(int meta, boolean itemBlock) {
        return TTBlockTypes_MetalMultiblock_2.values()[meta].needsCustomState() ? TTBlockTypes_MetalMultiblock_2.values()[meta].getCustomState() : null;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Nullable
    @Override
    public TileEntity createBasicTE(World world, TTBlockTypes_MetalMultiblock_2 ttBlockTypes_metalMultiblock) {
        switch (ttBlockTypes_metalMultiblock) {
            case COMPUTER_CLUSTER_CONTROLLER:
            case COMPUTER_CLUSTER_CONTROLLER_CHILD:
                return new TileEntityComputerClusterController();
            case MEMORY_FORMATTER:
            case MEMORY_FORMATTER_CHILD:
                return new TileEntityMemoryFormatter();
            case CLAY_OVEN:
            case CLAY_OVEN_CHILD:
                return new TileEntityClayOven();
            case MASONRY_HEATER:
            case MASONRY_HEATER_CHILD:
                return new TileEntityMasonryHeater();
            case ROCKET_SCAFFOLD:
            case ROCKET_SCAFFOLD_CHILD:
            case ROCKET_SCAFFOLD_BODY:
                return new TileEntityRocketScaffold();
        }
        return null;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityLivingBase && !((EntityLivingBase) entityIn).isOnLadder() && isLadder(state, worldIn, pos, (EntityLivingBase) entityIn))
        {
            float f5 = 0.15F;
            if (entityIn.motionX < -f5)
                entityIn.motionX = -f5;
            if (entityIn.motionX > f5)
                entityIn.motionX = f5;
            if (entityIn.motionZ < -f5)
                entityIn.motionZ = -f5;
            if (entityIn.motionZ > f5)
                entityIn.motionZ = f5;

            entityIn.fallDistance = 0.0F;
            if (entityIn.motionY < -0.15D)
                entityIn.motionY = -0.15D;

            if (entityIn.motionY < 0 && entityIn instanceof EntityPlayer && entityIn.isSneaking())
            {
                entityIn.motionY = .05;
                return;
            }
            if (entityIn.collidedHorizontally)
                entityIn.motionY = .2;
        }
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        int m = state.getBlock().getMetaFromState(state);
        if (m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_CHILD.getMeta() || m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_BODY.getMeta() || m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD.getMeta()) {
            return true;
        }
        return super.isLadder(state, world, pos, entity);
    }

    static AxisAlignedBB ladder = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        int m = state.getBlock().getMetaFromState(state);
        if (m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_CHILD.getMeta() || m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_BODY.getMeta() || m == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD.getMeta()) {
            return ladder;
        }
        return super.getCollisionBoundingBox(state, worldIn, pos);
    }
}
