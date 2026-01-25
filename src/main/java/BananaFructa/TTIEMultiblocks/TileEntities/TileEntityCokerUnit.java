package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityCokerUnit extends SimplifiedTileEntityMultiblockMetal<TileEntityCokerUnit, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersivepetroleum:material>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:low_quality_steam>"),1000),new FluidStack(Utils.fluidFromCTId("<liquid:clarified_water>"),200)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>"),Utils.itemStackFromCTId("<tiagthings:petcoke>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),500)},0,20*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersivepetroleum:material>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:steam>"),1000),new FluidStack(Utils.fluidFromCTId("<liquid:clarified_water>"),200)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>"),Utils.itemStackFromCTId("<tiagthings:petcoke>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),500)},0,20*20,true));
    }};

    public static List<SimplifiedMultiblockRecipe> recipesJoined = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersivepetroleum:material>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:low_quality_steam>"),1000),new FluidStack(Utils.fluidFromCTId("<liquid:clarified_water>"),200)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>",2)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),500)},0,20*20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersivepetroleum:material>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:steam>"),1000),new FluidStack(Utils.fluidFromCTId("<liquid:clarified_water>"),200)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>",2)},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),500)},0,20*20));
    }};
    public TileEntityCokerUnit() {
        super(TTIEContent.cokerUnit, 0, false,recipes);
    }

    @Override
    public void initPorts() {
        int steamFluidIn = registerFluidTank(1000);
        registerFluidPort(7,steamFluidIn, PortType.INPUT, EnumFacing.SOUTH);
        int waterFluidIn = registerFluidTank(1000);
        registerFluidPort(4,waterFluidIn,PortType.INPUT,EnumFacing.SOUTH);
        int dieselOut = registerFluidTank(1000);
        registerFluidPort(79,dieselOut,PortType.OUTPUT,EnumFacing.NORTH);
        int bitumenIn = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(76,bitumenIn,PortType.INPUT,EnumFacing.NORTH);
        registerItemPort(212,-1,PortType.OUTPUT,EnumFacing.DOWN);
        registerItemPort(207,-1,PortType.OUTPUT,EnumFacing.DOWN);

    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        //super.onDataPacket(net,pkt);
        readCustomNBT(pkt.getNbtCompound(),true);
    }

}
