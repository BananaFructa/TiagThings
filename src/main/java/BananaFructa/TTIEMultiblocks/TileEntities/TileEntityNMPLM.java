package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.IEContent;
import mctmods.immersivetechnology.common.ITContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityNMPLM extends SimplifiedTileEntityMultiblockMetal<TileEntityNMPLM, SimplifiedMultiblockRecipe> {

    public int process = 1;

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{new ItemStack(ItemLoaderHandler.siliconWafer,1)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hydrogen>"),10000)}, new ItemStack[]{new ItemStack(ItemLoaderHandler.printedSiliconWafer1,1)},new FluidStack[0],2048,30 * 20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{new ItemStack(ItemLoaderHandler.siliconWafer,1)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:hydrogen>"),50000)}, new ItemStack[]{new ItemStack(ItemLoaderHandler.printedSiliconWafer2,1)},new FluidStack[0],2048,60 * 20));
    }};

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        return super.canDoRecipe(recipe) && recipe == recipes.get(process);
    }

    public TileEntityNMPLM() {
        super(TTIEContent.nmPhotolitographyMachine, 16000, true,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(64);
        addRedstonePorts(0);
        int itemInput = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        int itemOutput = registerItemHandler(1,new boolean[]{false},new boolean[]{true});
        registerItemPort(5,itemInput,PortType.INPUT,EnumFacing.WEST);
        registerItemPort(0,itemOutput,PortType.OUTPUT,EnumFacing.EAST);
        int fluidInput = registerFluidTank(50000);
        registerFluidPort(5,fluidInput,PortType.INPUT,EnumFacing.SOUTH);
    }

    public void changeProcess(EntityPlayer player) {
        process++;
        if (process == 2) process = 0;
        if (process == 0) player.sendMessage(new TextComponentString("Process technology changed to: um"));
        else if (process == 1)player.sendMessage(new TextComponentString("Process technology changed to: nm"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        nbt.setInteger("plm_process",process);
        super.writeCustomNBT(nbt, descPacket);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        process = nbt.getInteger("plm_process");
        super.readCustomNBT(nbt, descPacket);
    }
}
