package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class STEMM_ClusterClient<M extends SimplifiedTileEntityMultiblockMetal<M,R> ,R extends SimplifiedMultiblockRecipe> extends SimplifiedTileEntityMultiblockMetal<M,R>{

    public ClusterUtils.ConnectionStatus connected = ClusterUtils.ConnectionStatus.IDLE;

    public STEMM_ClusterClient(SimplifiedMultiblockClass instance, int energyStorage, boolean redstoneControl, List<R> recipes) {
        super(instance, energyStorage, redstoneControl, recipes);
    }

    @Override
    public void update() {
        if (!world.isRemote || isDummy()) {
            if (!isWorking()) {
                if (connected != ClusterUtils.ConnectionStatus.IDLE) {
                    connected = ClusterUtils.ConnectionStatus.IDLE;
                    IEUtils.notifyClientUpdate(world, getPos());
                }
            }
            else {
                if (IEUtils.ae2PostTick(world)) {
                    ClusterUtils.ConnectionStatus newConnected = ClusterUtils.requestProcessingPower(getProcessingPowerForRecipe(this.processQueue.get(0).recipe), world.getTileEntity(getBlockPosForPos(getPosOfPort())));
                    if (connected != newConnected) IEUtils.notifyClientUpdate(world, getPos());
                    connected = newConnected;
                }
            }
            if (connected != ClusterUtils.ConnectionStatus.HOST_NOT_FOUND && connected != ClusterUtils.ConnectionStatus.INSUFFICIENT_RESOURCES) super.update();
        } else {
            super.update();
        }
    }

    public abstract int getProcessingPowerForRecipe(R recipe);
    protected abstract int getPosOfPort();

    public int getUsedPower() {
        if (!this.processQueue.isEmpty()) {
            return getProcessingPowerForRecipe(processQueue.get(0).recipe);
        }
        return 0;
    }

    @Override
    public final int getProcessQueueMaxLength() {
        return 1;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("conStatus",connected.ordinal());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        connected = ClusterUtils.ConnectionStatus.values()[nbt.getInteger("conStatus")];
    }
}
