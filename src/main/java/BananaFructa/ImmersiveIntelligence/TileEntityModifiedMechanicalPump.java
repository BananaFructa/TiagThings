package BananaFructa.ImmersiveIntelligence;

import BananaFructa.TiagThings.ChunkPos;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.util.Utils;
import buildcraft.builders.tile.TileQuarry;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityFluidPumpAlternative;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class TileEntityModifiedMechanicalPump extends TileEntityMechanicalPump {

    public boolean blocked = false;

    private static final Method handleRotation = BananaFructa.TiagThings.Utils.getDeclaredMethod(TileEntityMechanicalPump.class,"handleRotation");

    private static final List<String> restrictedFluids = new ArrayList<String>(){{
        add("tfc:fluid/fresh_water");
        add("tfc:fluid/salt_water");
        add("tfc:fluid/hot_water");
    }};

    ArrayList<BlockPos> openList = BananaFructa.TiagThings.Utils.readDeclaredField(TileEntityMechanicalPump.class,this,"openList");
    ArrayList<BlockPos> closedList = BananaFructa.TiagThings.Utils.readDeclaredField(TileEntityMechanicalPump.class,this,"closedList");
    ArrayList<BlockPos> checked = BananaFructa.TiagThings.Utils.readDeclaredField(TileEntityMechanicalPump.class,this,"checked");
    boolean checkingArea = false;
    Fluid searchFluid = null;

    List<String> fluidsBlackListCache = new ArrayList<>();
    List<String> fluidsWhiteListCache = new ArrayList<>();

    public int getRSPower(BlockPos position) {
        int toReturn = 0;

        for(EnumFacing directions : EnumFacing.values()) {
            toReturn = Math.max(this.world.getRedstonePower(position.offset(directions, -1), directions), toReturn);
        }

        return toReturn;
    }

    public void func_73660_a() {
        this.placeCobble = false;
        ApiUtils.checkForNeedlessTicking(this);
        if (!this.dummy && !this.world.isRemote) {
            if (this.tank.getFluidAmount() > 0) {
                int i = this.outputFluid(this.tank.getFluid(), false);
                this.tank.drain(i, true);
            }

            try {
                handleRotation.invoke(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            if (getRSPower(this.getPos()) == 0 || getRSPower(getPos().add(0, 1, 0)) == 0) {
                for(EnumFacing f : EnumFacing.values()) {
                    if (this.sideConfig[f.ordinal()] == 0) {
                        BlockPos output = this.getPos().offset(f);
                        TileEntity tile = Utils.getExistingTileEntity(this.world, output);
                        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite())) {
                            IFluidHandler handler = (IFluidHandler)tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite());
                            FluidStack drain = handler.drain(500, false);
                            if (drain != null && drain.amount > 0) {
                                int out = this.outputFluid(drain, false);
                                handler.drain(out, true);
                            }
                        } else if (this.world.getTotalWorldTime() % 20L == (long)((this.getPos().getX() ^ this.getPos().getZ()) & 19) && this.world.getBlockState(this.getPos().offset(f)).getBlock() == Blocks.WATER && Config.IEConfig.Machines.pump_infiniteWater && this.tank.fill(new FluidStack(FluidRegistry.WATER, 1000), false) == 1000 && this.hasEnoughPower()) {
                            int connectedSources = 0;

                            for(EnumFacing f2 : EnumFacing.HORIZONTALS) {
                                IBlockState waterState = this.world.getBlockState(this.getPos().offset(f).offset(f2));
                                if (waterState.getBlock() == Blocks.WATER && Blocks.WATER.getMetaFromState(waterState) == 0) {
                                    ++connectedSources;
                                }
                            }

                            if (connectedSources > 1) {
                                this.tank.fill(new FluidStack(FluidRegistry.WATER, 1000), true);
                            }
                        }
                    }
                }

                if (this.world.getTotalWorldTime() % 40L == (long)(((this.getPos().getX() ^ this.getPos().getZ()) % 40 + 40) % 40)) {
                    if (this.closedList.isEmpty()) {
                        this.prepareAreaCheck();
                    } else {
                        int target = this.closedList.size() - 1;
                        BlockPos pos = (BlockPos)this.closedList.get(target);
                        FluidStack fs = Utils.drainFluidBlock(this.world, pos, false);
                        if (fs == null) {
                            this.closedList.remove(target);
                        } else if (this.tank.fill(fs, false) == fs.amount && this.hasEnoughPower()) {
                            fs = Utils.drainFluidBlock(this.world, pos, !restrictedFluids.contains(fs.getFluid().getBlock().getRegistryName().toString()));

                            this.tank.fill(fs, true);
                            this.closedList.remove(target);
                        }
                    }
                }
            }

            if (this.checkingArea) {
                this.checkAreaTick();
            }

        }
    }

    private boolean hasEnoughPower() {
        return this.rotation.getRotationSpeed() >= (float) IIConfigHandler.IIConfig.Machines.MechanicalPump.rpmMin && this.rotation.getTorque() >= (float) IIConfigHandler.IIConfig.Machines.MechanicalPump.torqueMin;
    }

    public void prepareAreaCheck() {
        openList.clear();
        closedList.clear();
        checked.clear();
        for(EnumFacing f : EnumFacing.values()) {
            if(sideConfig[f.ordinal()] == 0) {
                openList.add(getPos().offset(f));
                checkingArea = true;
            }
        }
    }

    private boolean canPumpFluidAndResrve(String fluid) {
        if (fluidsWhiteListCache.contains(fluid)) return true;
        if (fluidsBlackListCache.contains(fluid)) return false;
        if (restrictedFluids.contains(fluid)) {
            boolean occupied = false;

            ChunkPos chunkPos = new ChunkPos(world.getChunkFromBlockCoords(pos).x, world.getChunkFromBlockCoords(pos).z);

            BlockPos reserverPos = TTMain.INSTANCE.worldStorage.getReserver(fluid, chunkPos);

            TileEntity reserverEntity = null;
            if (reserverPos != null) {
                reserverEntity = world.getTileEntity(reserverPos);
                //                                                         make sure the pump wasnt destroyed and replaced V
                if (reserverEntity != null)
                    if ((reserverEntity instanceof TileEntityFluidPumpAlternative || reserverEntity instanceof TileEntityMechanicalPump) && reserverEntity != this)
                        occupied = true;
            }

            if (occupied) {
                System.out.println("Occupied at " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
                fluidsBlackListCache.add(fluid);
            }
            else if (reserverEntity != this){
                reserveFluidChunk(fluid);
                fluidsWhiteListCache.add(fluid);
            }

            return !occupied;
        }
        fluidsWhiteListCache.add(fluid);
        return true;
    }

    private void reserveFluidChunk(String fluid) {
        System.out.println("Reserved at " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        TTMain.INSTANCE.worldStorage.addReserver(fluid,new ChunkPos(world.getChunkFromBlockCoords(pos).x, world.getChunkFromBlockCoords(pos).z),pos);
    }

    public void checkAreaTick() {
        BlockPos next = null;
        final int closedListMax = 2048;
        int timeout = 0;
        List<TreeSet<Long>> restrictedSources = null;
        net.minecraft.util.math.ChunkPos lastOrigin = null;
        while(timeout < 64 && closedList.size() < closedListMax && !openList.isEmpty()) {
            timeout++;
            next = openList.get(0);
            if(!checked.contains(next)) {
                Fluid fluid = Utils.getRelatedFluid(world, next);
                if(fluid != null && BlockFluidBase.getFlowDirection(world, next) == -1000.0D && (fluid != FluidRegistry.WATER || !Config.IEConfig.Machines.pump_infiniteWater) && (searchFluid == null || fluid == searchFluid)) {
                    net.minecraft.util.math.ChunkPos nextCpos = new net.minecraft.util.math.ChunkPos(next);
                    if (restrictedSources == null || !nextCpos.equals(lastOrigin)){
                        lastOrigin = nextCpos;
                        // restricted as in placed by a drain
                        restrictedSources = BananaFructa.TiagThings.Utils.getRestrictedFluidsInArea(world, next, 4);
                    }
                    boolean ok = true;
                    Long l = BananaFructa.TiagThings.Utils.convertPosition(next.getX(), next.getZ());
                    for (TreeSet<Long> ts : restrictedSources) {
                        if (ts.contains(l)) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        String fluidName = fluid.getBlock().getRegistryName().toString();
                        if (canPumpFluidAndResrve(fluidName)) {
                            blocked = false;
                            if (searchFluid == null) searchFluid = fluid;
                            if (Utils.drainFluidBlock(world, next, false) != null) {
                                closedList.add(next);
                            }
                            for (EnumFacing f : EnumFacing.values()) {
                                BlockPos pos2 = next.offset(f);
                                fluid = Utils.getRelatedFluid(world, pos2);
                                if (!checked.contains(pos2) && !closedList.contains(pos2) && !openList.contains(pos2) && fluid != null && (fluid != FluidRegistry.WATER || !Config.IEConfig.Machines.pump_infiniteWater) && (searchFluid == null || fluid == searchFluid))
                                    openList.add(pos2);
                            }
                        } else {
                            blocked = true;
                        }
                    }
                }
                checked.add(next);
            }
            openList.remove(0);
        }
        if(closedList.size() >= closedListMax || openList.isEmpty()) {
            checkingArea = false;
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        if (nbt.hasKey("blocked")) blocked = nbt.getBoolean("blocked");
        super.readCustomNBT(nbt, descPacket);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setBoolean("blocked",blocked);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        super.writeCustomNBT(nbtTagCompound, true);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        blocked = pkt.getNbtCompound().getBoolean("blocked");
    }

}
