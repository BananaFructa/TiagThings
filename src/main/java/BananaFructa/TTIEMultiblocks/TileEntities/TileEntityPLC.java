package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.ImmersiveEngineering.ModifiedRedstoneConnector;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.plc.OutputPLCModule;
import BananaFructa.TTIEMultiblocks.TileEntities.plc.PLCModule;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorRedstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntityPLC extends SimplifiedTileEntityMultiblockMetal<TileEntityPLC, SimplifiedMultiblockRecipe> {

    public int boardCount = 0;
    public int addTimer = 0;
    public final List<ItemStack> boards = new ArrayList<>();

    public int designComplexity = 0;
    public int availableComplexity = 0;

    public final List<PLCModule> moduleList = new ArrayList<>();
    public Map<Integer,PLCModule> idToMod = new HashMap<>();
    public int counter = 0;

    public int[] ports = new int[]{4,5,2,3};
    public TileEntityConnectorRedstone[] connectors = new TileEntityConnectorRedstone[4];
    public boolean[] hasConnector = new boolean[4];
    public int[][] setStrengths = new int[4][16];
    public boolean[][] usedChannels = new boolean[4][16];

    private ItemStack relayBoard = Utils.itemStackFromCTId("<tiagthings:early_relay_circuit>");
    private ItemStack basicBoard = Utils.itemStackFromCTId("<immersiveengineering:material:27>");
    private ItemStack advancedBoard = Utils.itemStackFromCTId("<immersiveintelligence:material:7>");
    private ItemStack[] possibleBoards = new ItemStack[]{relayBoard,basicBoard,advancedBoard};
    private static int[] boardComplexities = {1,5,10};
    private static int[] powerCosumptionFactors = {100,10,1};
    public float energyDraw = 0;
    private float fractionalEnergyDraw = 0;

    //4-b, 5-a, 2-d, 3-c


    public TileEntityPLC() {
        super(TTIEContent.plc, 1000, false,new ArrayList<>());
    }

    public void updateUsedChannels() {
        synchronized (moduleList) {
            usedChannels = new boolean[4][16];
            for (PLCModule module : moduleList) {
                if (module instanceof OutputPLCModule) {
                    OutputPLCModule out = (OutputPLCModule) module;
                    usedChannels[out.port][out.channel] = true;
                }
            }
        }
    }

    public int getStrength(int port,int channel) {
        if (connectors[port] != null) {
            return connectors[port].getNetwork().getPowerOutput(channel);
        } else return 0;
    }

    public void setStrength(int port,int channel,int value) {
        setStrengths[port][channel] = value;
    }

    public void updateInputs(int pos,byte[] values) {
        for (int i = 0;i < ports.length;i++) {
            if (ports[i] == pos) {
                for (int j = 0;j < 16;j++) {
                    if (usedChannels[i][j]) {
                        values[j] = (byte) setStrengths[i][j];
                    }
                }
                break;
            }
        }
    }

    public void setOutputs() {
        for (int i = 0;i < ports.length;i++) {
            if (connectors[i] != null && !(connectors[i] instanceof ModifiedRedstoneConnector)) {
                ModifiedRedstoneConnector mod = new ModifiedRedstoneConnector(connectors[i]);
                mod.facing = this.facing;
                mod.markDirty();
                world.setTileEntity(connectors[i].getPos(),mod);
            }
        }
    }

    @Override
    public void initPorts() {
        addEnergyPort(4);
        for (int i = 0;i < ports.length;i++) {
            if (connectors[i] != null && (connectors[i] instanceof ModifiedRedstoneConnector)) {
                connectors[i].getNetwork().updateValues();
            }
        }
    }

    public int getValue(int id,int port) {
        if (id == -1) return 0;
        if (!idToMod.containsKey(id)) return 0;
        if (port < 0) return 0;
        PLCModule mod = idToMod.get(id);
        if (port > mod.outputs.length) return 0;
        return idToMod.get(id).outputs[port];
    }

    public TileEntityConnectorRedstone getConnector(int pos) {
        BlockPos p = getBlockPosForPos(pos);
        p = p.offset(facing.getOpposite());
        TileEntity te = world.getTileEntity(p);
        if (te instanceof TileEntityConnectorRedstone) return (TileEntityConnectorRedstone) te;
        return null;
    }

    public boolean connectionAvailable(int pos) {
        return getConnector(pos) != null;
    }

    int tickCount = 0;
    int tickLap = 2;
    @Override
    public void update() {
        super.update();
        if (world.isRemote) {
            boardCount = boards.size();
        }
        if (this.world.isRemote) return;
        if (addTimer > 0) addTimer--;
        if (addTimer < 0) addTimer = 0;
        tickCount++;
        boolean hasEnergy = useEnergy();
        if (!hasEnergy) {
            for (PLCModule module : moduleList) module.reset();
        }
        if (tickCount >= tickLap) {
            tickCount = 0;
            setStrengths = new int[4][16];
            if (hasEnergy && availableComplexity >= designComplexity) {
                updateModules();
            }
            for (int i = 0;i < ports.length;i++) {
                if (connectors[i] != null) connectors[i].rsDirty = true;
            }
        }
        computeDesignComplexity();

        updateUsedChannels();
        for (int i = 0;i < ports.length;i++) {
            connectors[i] = getConnector(ports[i]);
            hasConnector[i] = connectors[i] != null;
        }
        setOutputs();

        IEUtils.notifyClientUpdate(world, pos);
    }

    public boolean useEnergy() {
        if (fractionalEnergyDraw > 1) {
            int energyToDraw = (int)fractionalEnergyDraw;
            if (this.energyStorage.extractEnergy(energyToDraw,true) == energyToDraw) {
                this.energyStorage.extractEnergy(energyToDraw,false);
                fractionalEnergyDraw -= energyToDraw;
                fractionalEnergyDraw += energyDraw;
                markDirty();
                return true;
            } // If this fails you don't just want to increase the fractional energy counter
        } else {
            fractionalEnergyDraw += energyDraw;
            return true;
        }
        return false;
    }

    public void updateModules() {
        synchronized (moduleList) {
            for (PLCModule module : moduleList) {
                module.update(this);
            }
            for (PLCModule module : moduleList) {
                module.swapBuffers();
            }
        }
    }

    private int[] toIntArray(Integer[] array) {
        int[] v = new int[array.length];
        for (int i = 0;i < array.length;i++) {
            v[i] = array[i];
        }
        return v;
    }

    // This is not that good, it sends the whole state each change, and then the evil ahh output temp map
    public void updateFromGui(NBTTagCompound tag) {
        synchronized (moduleList) {
            Map<Integer, NBTTagCompound> tempState = new HashMap<>();

            for (PLCModule module : moduleList) {
                tempState.put(module.id,module.getState());
            }
            moduleList.clear();
            idToMod.clear();
            int count = tag.getInteger("count");
            for (int i = 0; i < count; i++) {
                PLCModule mod = PLCModule.fromNBT(tag.getCompoundTag("comp_" + i));
                moduleList.add(mod);
                idToMod.put(mod.id, mod);
                if (tempState.containsKey(mod.id)) {
                    mod.readState(tempState.get(mod.id));
                }
            }
            counter = tag.getInteger("counter");
            markDirty();
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeCustomNBT(tagCompound,true);
        return new SPacketUpdateTileEntity(getPos(),1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        this.readCustomNBT(pkt.getNbtCompound(),true);
    }

    public int getBoardIndex(ItemStack stack) {
        for (int i = 0;i < possibleBoards.length;i++) {
            if (stack.getItem() == possibleBoards[i].getItem() && stack.getMetadata() == possibleBoards[i].getMetadata()) return i;
        }
        return -1;
    }

    public void handleInteract(EntityPlayer player) {
        synchronized (boards) {
            int which;
            which = getBoardIndex(player.getHeldItemMainhand());
            if (which != -1 && boards.size() < 21) {
                player.getHeldItemMainhand().shrink(1);
                boards.add(possibleBoards[which].copy());
            } else if (player.getHeldItemMainhand().isEmpty() && !boards.isEmpty()) {
                ItemStack top = boards.get(boards.size() - 1);
                boards.remove(top);
                player.addItemStackToInventory(top);
            }
            addTimer = 20 / 3;
            markDirty();
        }
    }

    public void computeDesignComplexity() {
        designComplexity = 0;
        energyDraw = 0;
        availableComplexity = 0;
        for (PLCModule module : moduleList) {
            designComplexity += module.getComplexity();
        }
        for (ItemStack board : boards) {
            int i = getBoardIndex(board);
            if (i != -1) {
                energyDraw += 0.06f * powerCosumptionFactors[i];
                availableComplexity += boardComplexities[i];
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag, boolean descPacket) {
        super.readCustomNBT(tag, descPacket);
        synchronized (moduleList) {
            moduleList.clear();
            idToMod.clear();
            int count = tag.getInteger("count");
            for (int i = 0; i < count; i++) {
                PLCModule mod = PLCModule.fromNBT(tag.getCompoundTag("comp_" + i));
                moduleList.add(mod);
                idToMod.put(mod.id, mod);
            }
            counter = tag.getInteger("counter");
            for (int i = 0;i < ports.length;i++) {
                hasConnector[i] = tag.getBoolean("has_"+i);
                setStrengths[i] = tag.getIntArray("s_"+i);
                for (int j = 0;j < 16;j++) {
                    usedChannels[i][j] = tag.getBoolean("used_"+i+"_"+j);
                }
            }
            synchronized (boards) {
                boards.clear();
                int boardCount = tag.getInteger("board_count");
                for (int i = 0; i < boardCount; i++) {
                    NBTTagCompound s = tag.getCompoundTag("board_" + i);
                    ItemStack stack = new ItemStack(s);
                    boards.add(stack);
                }
            }
            energyDraw = tag.getFloat("energy_draw");
            fractionalEnergyDraw = tag.getFloat("fractional_energy");
            availableComplexity = tag.getInteger("av_complexity");
            designComplexity = tag.getInteger("ds_complexity");
        }

    }

    @Override
    public void writeCustomNBT(NBTTagCompound tagCompound, boolean descPacket) {
        super.writeCustomNBT(tagCompound, descPacket);
        tagCompound.setInteger("count",moduleList.size());
        tagCompound.setInteger("counter",counter);
        for (int i = 0;i < moduleList.size();i++) {
            tagCompound.setTag("comp_"+i,moduleList.get(i).toNBT(true));
        }
        for (int i = 0;i < ports.length;i++) {
            tagCompound.setBoolean("has_"+i,hasConnector[i]);
            tagCompound.setIntArray("s_"+i,setStrengths[i]);
            for (int j = 0;j < 16;j++) {
                tagCompound.setBoolean("used_"+i+"_"+j,usedChannels[i][j]);
            }
        }
        synchronized (boards) {
            tagCompound.setInteger("board_count", boards.size());
            for (int i = 0; i < boards.size(); i++) {
                NBTTagCompound s = new NBTTagCompound();
                boards.get(i).writeToNBT(s);
                tagCompound.setTag("board_" + i, s);
            }
        }
        tagCompound.setInteger("av_complexity",availableComplexity);
        tagCompound.setInteger("ds_complexity",designComplexity);
        tagCompound.setFloat("energy_draw",energyDraw);
        tagCompound.setFloat("fractional_energy",fractionalEnergyDraw);
    }
}
