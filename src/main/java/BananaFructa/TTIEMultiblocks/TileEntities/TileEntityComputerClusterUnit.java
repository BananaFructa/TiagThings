package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.block.networking.BlockCableBus;
import appeng.client.render.cablebus.CableBusRenderState;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TileEntityComputerClusterUnit extends SimplifiedTileEntityMultiblockMetal<TileEntityComputerClusterUnit, SimplifiedMultiblockRecipe> {

    public boolean paired = false;
    public boolean tempPaired = false;

    private static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       add (new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[0],new ItemStack[0],new FluidStack[0],4096,60*20));
    }};

    public TileEntityComputerClusterUnit() {
        super(TTIEContent.computerClusterUnit, 16000, false,recipes);
    }

    @Override
    public void update() {
        if (this.field_174879_c == 0) this.world.setTileEntity(pos,new TileEntityComputerClusterUnit_AE2(this)); // AE port
        super.update();
        if (world.isRemote) return;
        if (isDummy()) return;

        if (!IEUtils.ae2PostTick(world)) return;
        if (paired ^ tempPaired) world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
        paired = tempPaired;
        tempPaired = false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setBoolean("paired",paired);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        paired = nbt.getBoolean("paired");
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void initPorts() {
        addEnergyPort(6);
    }
}
