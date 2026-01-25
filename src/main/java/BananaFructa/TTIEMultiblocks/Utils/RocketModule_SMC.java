package BananaFructa.TTIEMultiblocks.Utils;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityRocketScaffold;
import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.Arrays;

public class RocketModule_SMC extends SimplifiedMultiblockClass{

    //<galacticraftcore:rocket_t1>
    //<galacticraftplanets:rocket_t3>

    public RocketModule_SMC(String name, ItemStack[][][] structure, int hSource, int lSource, int wSource, IBlockState state, IBlockState stateChild) {
        super(name, structure, hSource, lSource, wSource, state, stateChild);
    }

    public RocketModule_SMC(String name, String structureFileName, IBlockState state, IBlockState stateChild) {
        super(name, structureFileName, state, stateChild);
    }

    @Override
    public boolean createStructure(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
            enumFacing = entityPlayer.getHorizontalFacing().getOpposite();
        }

        boolean ok = false;

        BlockPos scaff = blockPos.offset(enumFacing,5);
        TileEntity te = world.getTileEntity(scaff);
        if (te instanceof TileEntityRocketScaffold) {
            TileEntityRocketScaffold tileEntityRocketScaffold = (TileEntityRocketScaffold) te;
            if (Arrays.stream(new int[]{11810,11740,11680,11750}).anyMatch((i)->{return i == tileEntityRocketScaffold.field_174879_c;})) {
                ok = true;
            }
        }
        if (!ok) {
            entityPlayer.sendMessage(new TextComponentString("Invalid rocket module scaffold! " + scaff.getX() + " " + scaff.getY() + " " + scaff.getZ()));
            return false;
        } else {
            boolean ok2 = super.createStructure(world, blockPos, enumFacing, entityPlayer);;
            if (ok2) {
                entityPlayer.addItemStackToInventory(getItem());
            }
            return ok2;
        }

    }

    public ItemStack getItem() {
        return new ItemStack(Items.AIR,1);
    }

}
