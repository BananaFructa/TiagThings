package BananaFructa.TTIEMultiblocks.Utils;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_1;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalMultiblock;
import blusunrize.immersiveengineering.common.util.Utils;
import com.sun.org.apache.xalan.internal.res.XSLTErrorResources_zh_TW;
import net.mcft.copy.backpacks.client.RendererBackpack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;
import net.minecraft.item.*;

import java.io.*;
import java.util.HashMap;

public class SimplifiedMultiblockClass implements MultiblockHandler.IMultiblock {
    public ItemStack[][][] structure;
    public int hSource,lSource,wSource;
    public IngredientStack[] materials;
    public String name;
    public IBlockState state;
    public IBlockState stateChild;
    public int[] size;
    public Class<? extends TileEntity>[] teOverload;
    public AnimationGroup animationGroup = null;

    public SimplifiedMultiblockClass(String name, ItemStack[][][] structure, int hSource, int lSource, int wSource, IBlockState state, IBlockState stateChild, Class<? extends TileEntity>... teOverload) {
        this.name = name;
        this.structure = structure;
        this.hSource = hSource;
        this.lSource = lSource;
        this.wSource = wSource;
        this.state = state;
        this.stateChild = stateChild;
        this.materials = IEUtils.getMaterialsForStructure(structure);
        this.size = new int[]{this.structure.length,this.structure[0].length,this.structure[0][0].length};
        this.teOverload = teOverload;
    }

    public SimplifiedMultiblockClass(String name, String structureFileName, IBlockState state, IBlockState stateChild,Class<? extends TileEntity>... teOverload) {
        InputStream stream =  getClass().getClassLoader().getResourceAsStream("assets/"+ TTMain.modId+"/structure_files/"+structureFileName);
        try {
            NBTTagCompound nbt = CompressedStreamTools.readCompressed(stream);
            int sizeH = nbt.getInteger("sizeH");
            int sizeL = nbt.getInteger("sizeL");
            int sizeW = nbt.getInteger("sizeW");
            this.size = new int[]{sizeH,sizeL,sizeW};
            hSource = nbt.getInteger("originH");
            lSource = nbt.getInteger("originL");
            wSource = nbt.getInteger("originW");

            structure = new ItemStack[sizeH][sizeL][sizeW];
            for (int h = 0;h < structure.length;h++) {
                for (int l = 0;l < structure[0].length;l++) {
                    for (int w = 0;w < structure[0][0].length;w++) {
                        if (nbt.hasKey(h + "-" + l + "-" + w)) structure[h][l][w] = new ItemStack((NBTTagCompound) nbt.getTag(h + "-" + l + "-" + w));
                        else structure[h][l][w] = ItemStack.EMPTY;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.name = name;
        this.state = state;
        this.stateChild = stateChild;
        this.materials = IEUtils.getMaterialsForStructure(structure);
        this.teOverload = teOverload;
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public boolean isBlockTrigger(IBlockState iBlockState) {
        ItemStack source = structure[hSource][lSource][wSource];
        return Utils.blockstateMatches(iBlockState, Block.getBlockFromItem(source.getItem()),source.getMetadata());
    }

    @Override
    public boolean createStructure(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
            enumFacing = entityPlayer.getHorizontalFacing().getOpposite();
        }
        Tuple<Boolean, Boolean> result = IEUtils.checkStructure(
                world,
                blockPos,
                enumFacing,
                structure,
                hSource,
                lSource,
                wSource
        );

        if (!result.getFirst()) return false;

        boolean mirrored = result.getSecond();

        IEUtils.populateMultiblock(
                world,
                blockPos,
                enumFacing,
                structure,
                hSource,
                lSource,
                wSource,
                state.getBlock() instanceof BlockTTMultiblock ? state.withProperty(IEProperties.FACING_HORIZONTAL,enumFacing) : state,
                stateChild.getBlock() instanceof BlockTTMultiblock ? stateChild.withProperty(IEProperties.FACING_HORIZONTAL,enumFacing) : stateChild,
                mirrored,
                teOverload
        );

        return true;
    }

    @Override
    public ItemStack[][][] getStructureManual() {
        return structure;
    }

    @Override
    public IngredientStack[] getTotalMaterials() {
        return materials;
    }

    @Override
    public boolean overwriteBlockRender(ItemStack itemStack, int i) {
        return false;
    }

    @Override
    public float getManualScale() {
        if (state.getBlock() == TTIEContent.ttBlockMetalMultiblock) {
            switch (TTBlockTypes_MetalMultiblock.values()[state.getBlock().getMetaFromState(state)]) {
                case ELECTRIC_HEATER:
                    return 16;
                case FLARE_STACK:
                    return 8;
                case COAL_BOILER:
                case OIL_BOILER:
                    return 4f;
                case CLARIFIER:
                    return 5;
            }
        }
        if (state.getBlock() == TTIEContent.ttBlockMetalMultiblock_1) {
            switch (TTBlockTypes_MetalMultiblock_1.values()[state.getBlock().getMetaFromState(state)]) {
                case NMPLM:
                    return 10.0f;
                case EUVPLM:
                    return 8.5f;
            }
        }
        return 16;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderFormedStructure() {
        return false;
    } // TODO:AAAAAAAAAAAAAAAAAAAAAAAAAAaaaaa....aaaaaaaaaaAAAAAAAAAAAAAAA

    @SideOnly(Side.CLIENT)
    @Override
    public void renderFormedStructure() {
        ItemStack renderStack = new ItemStack(Item.getItemFromBlock(state.getBlock()),1,state.getBlock().getMetaFromState(state));

        GlStateManager.translate(1,0,0);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.disableCull();
        ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.enableCull();
    }
}
