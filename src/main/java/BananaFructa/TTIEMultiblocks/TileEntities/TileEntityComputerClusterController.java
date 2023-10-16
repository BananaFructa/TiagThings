package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IMachineSet;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityComputerClusterController extends SimplifiedTileEntityMultiblockMetal<TileEntityComputerClusterController, SimplifiedMultiblockRecipe> {

    public int activeClusterUnits = 0;
    public int totalProcessingPower = 0;
    public int usedProcessingPower = 0;


    public final int maxRequests = 8;
    public int usedRequests = 0;
    public int lastUsedRequests = 0;
    public int lastUsedProcessingPower = 0;

    private static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add (new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[0],new ItemStack[0],new FluidStack[0],4096,60*20));
    }};

    public TileEntityComputerClusterController() {
        super(TTIEContent.computerClusterController, 16000, false,recipes);
    }

    @Override
    public void update() {
        super.update();
        if (this.field_174879_c == 0) this.world.setTileEntity(pos,new TileEntityComputerClusterController_AE2(this)); // AE port
        if (world.isRemote) return;
        if (isDummy()) return;

        if (IEUtils.ae2PreTick(world)) {
            activeClusterUnits = 0;
            if (isWorking()) {
                BlockPos pos = getBlockPosForPos(0);
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityComputerClusterController_AE2) {
                    TileEntityComputerClusterController_AE2 computerClusterController_ae2 = (TileEntityComputerClusterController_AE2) te;
                    try {
                        IMachineSet machineSet = computerClusterController_ae2.getProxy().getGrid().getMachines(TileEntityComputerClusterUnit_AE2.class);
                        for (IGridNode node : machineSet) {
                            DimensionalCoord coord = node.getGridBlock().getLocation();
                            TileEntityComputerClusterUnit_AE2 teClusterUnit = (TileEntityComputerClusterUnit_AE2) coord.getWorld().getTileEntity(coord.getPos());
                            TileEntityComputerClusterUnit master = teClusterUnit.master();
                            if (master != null && !master.tempPaired && master.isWorking()) {
                                activeClusterUnits++;
                                master.tempPaired = true;
                            }
                        }
                    } catch (GridAccessException e) {
                    }
                }
            }
            totalProcessingPower = activeClusterUnits * 4;
            lastUsedRequests = usedRequests;
            lastUsedProcessingPower = usedProcessingPower;
            world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
            usedProcessingPower = 0;
            usedRequests = 0;
        }
    }

    @Override
    public void initPorts() {
        addEnergyPort(6);
    }

    public boolean doProcessRequest(int processingRequired) {
        if (usedProcessingPower + processingRequired <= totalProcessingPower && usedRequests < maxRequests) {
            usedRequests++;
            usedProcessingPower += processingRequired;
            return true;
        }
        return false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        nbt.setInteger("lastUsedPower",lastUsedProcessingPower);
        nbt.setInteger("lastRequests",lastUsedRequests);
        nbt.setInteger("clusterUnits",activeClusterUnits);
        nbt.setInteger("totalPower",totalProcessingPower);
        nbt.setInteger("usedPower",usedProcessingPower);
        nbt.setInteger("usedRequests",usedRequests);
        super.writeCustomNBT(nbt, descPacket);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        activeClusterUnits = nbt.getInteger("clusterUnits");
        totalProcessingPower = nbt.getInteger("totalPower");
        usedProcessingPower = nbt.getInteger("usedProcessingPower");
        usedRequests = nbt.getInteger("usedRequests");
        lastUsedProcessingPower = nbt.getInteger("lastUsedPower");
        lastUsedRequests = nbt.getInteger("lastRequests");
    }
}
