package BananaFructa.BCModifications;

import BananaFructa.TiagThings.Utils;
import buildcraft.api.core.BCDebugging;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.mj.MjBattery;
import buildcraft.builders.BCBuildersConfig;
import buildcraft.builders.tile.TileQuarry;
import buildcraft.lib.misc.*;
import buildcraft.lib.misc.data.AxisOrder;
import buildcraft.lib.misc.data.Box;
import buildcraft.lib.misc.data.BoxIterator;
import buildcraft.lib.misc.data.EnumAxisOrder;
import buildcraft.lib.net.PacketBufferBC;
import com.google.common.collect.ImmutableList;
import net.dries007.tfc.objects.blocks.BlockSnowTFC;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.Sys;
import scala.collection.immutable.Stream;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.*;

public class RFTileQuarry extends TileQuarry implements IEnergyStorage {

    public static final boolean DEBUG_QUARRY = BCDebugging.shouldDebugLog("builders.quarry");
    private final Utils.InstanceField<Long> MAX_POWER_PER_TICK = Utils.getAccessibleField(TileQuarry.class,this,"MAX_POWER_PER_TICK",false);
    private final Utils.InstanceField<Boolean> firstChecked = Utils.getAccessibleField(TileQuarry.class,this,"firstChecked",false);
    private final Utils.InstanceField<Long> debugPowerRate= Utils.getAccessibleField(TileQuarry.class,this,"debugPowerRate",false);
    private final Utils.InstanceField<Double> blockPercentSoFar= Utils.getAccessibleField(TileQuarry.class,this,"blockPercentSoFar",false);
    private final Utils.InstanceField<Double> moveDistanceSoFar= Utils.getAccessibleField(TileQuarry.class,this,"moveDistanceSoFar",false);
    private final Utils.InstanceField<Vec3d> collisionDrillPos= Utils.getAccessibleField(TileQuarry.class,this,"collisionDrillPos",false);
    private final Utils.InstanceField<List<AxisAlignedBB>> collisionBoxes = Utils.getAccessibleField(TileQuarry.class,this,"collisionBoxes",false);
    private final Utils.InstanceField<BoxIterator> boxIterator= Utils.getAccessibleField(TileQuarry.class,this,"boxIterator",false);
    private final Utils.InstanceField<Object> reflectedCurrentTask = Utils.getAccessibleField(TileQuarry.class,this,"currentTask",false);
    private final int conversionFactor = 2120; // 1 RF -> x MJ

    private MjBattery battery;

    int MJ2RF(long mj) {
        return (int)(mj / conversionFactor);
    }

    long RF2MJ(int rf) {
        return (long)rf * conversionFactor;
    }

    public RFTileQuarry() {
        super();
        MjBattery newBattery = new MjBattery(RF2MJ(24000)); // old capacity was too big, new one is 24k RF
        Utils.writeDeclaredField(TileQuarry.class,this,"battery",newBattery,true);
        battery = Utils.readDeclaredField(TileQuarry.class,this,"battery");
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T)this;
        return super.getCapability(capability, facing);
    }

    LinkedList<BlockPos> toCheck = Utils.readDeclaredField(TileQuarry.class,this,"toCheck");
    Box miningBox = Utils.readDeclaredField(TileQuarry.class,this,"miningBox");
    Set<BlockPos> frameBreakBlockPoses = Utils.readDeclaredField(TileQuarry.class,this,"frameBreakBlockPoses");
    Set<BlockPos> framePlaceFramePoses = Utils.readDeclaredField(TileQuarry.class,this,"framePlaceFramePoses");

    private static Class<?> task,taskBreakBlock,taskAddFrame,taskMoveDrill;
    private static Constructor<?> addFrameConstructor,moveDrillConstructor,breakBlockConstructor;


    static {
        try {
            task = Class.forName("buildcraft.builders.tile.TileQuarry$Task");
            taskBreakBlock = Class.forName("buildcraft.builders.tile.TileQuarry$TaskBreakBlock");
            taskAddFrame = Class.forName("buildcraft.builders.tile.TileQuarry$TaskAddFrame");
            taskMoveDrill = Class.forName("buildcraft.builders.tile.TileQuarry$TaskMoveDrill");
            //addFrameConstructor = taskAddFrame.getDeclaredConstructors()[1];
            addFrameConstructor = taskAddFrame.getDeclaredConstructor(TileQuarry.class,BlockPos.class);
            addFrameConstructor.setAccessible(true);
           // moveDrillConstructor = taskMoveDrill.getDeclaredConstructors()[1];
            moveDrillConstructor = taskMoveDrill.getDeclaredConstructor(TileQuarry.class, Vec3d.class,Vec3d.class);
            moveDrillConstructor.setAccessible(true);
            //breakBlockConstructor = taskBreakBlock.getDeclaredConstructors()[1];
            breakBlockConstructor = taskBreakBlock.getDeclaredConstructor(TileQuarry.class, BlockPos.class);
            breakBlockConstructor.setAccessible(true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } /*catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }*/ catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ResourceLocation ADVANCEMENT_COMPLETE = Utils.readDeclaredField(TileQuarry.class,null,"ADVANCEMENT_COMPLETE");

    private static final Method check = Utils.getDeclaredMethod(TileQuarry.class,"check",BlockPos.class);
    private static final Method clientTick = Utils.getDeclaredMethod(task,"clientTick");
    private static final Method getRequiredPowerThisTick = Utils.getDeclaredMethod(task,"getRequiredPowerThisTick");
    private static final Method addPower = Utils.getDeclaredMethod(task,"addPower",long.class);
    @Override
    public void update() {
        try {
            // thank god for obfed names
            if (this.drillPos == null) {
                this.collisionBoxes.set(ImmutableList.of());
                this.collisionDrillPos.set(null);
            }

            if (this.world.isRemote) {
                this.prevClientDrillPos = this.clientDrillPos;
                this.clientDrillPos = this.drillPos;
                if (this.currentTask != null) {
                    clientTick.invoke(this.currentTask);
                }

            } else if (this.frameBox.isInitialized() && this.miningBox.isInitialized()) {
                if (!this.toCheck.isEmpty()) {
                    for (int i = 0; i < (this.firstChecked.get() ? 10 : 500); ++i) {
                        BlockPos blockPos = (BlockPos) this.toCheck.pollFirst();
                        check.invoke(this,blockPos);
                        this.toCheck.addLast(blockPos);
                    }
                }

                if (this.firstChecked.get()) {
                    long max;
                    if (this.battery.getStored() > this.battery.getCapacity() / 2L) {
                        max = MAX_POWER_PER_TICK.get();
                    } else {
                        long roundedUp = this.battery.getStored() + MjAPI.MJ / 2L;
                        if (roundedUp > Long.MAX_VALUE / MAX_POWER_PER_TICK.get()) {
                            max = BigInteger.valueOf(roundedUp).multiply(BigInteger.valueOf(MAX_POWER_PER_TICK.get())).divide(BigInteger.valueOf(this.battery.getCapacity() / 2L)).longValue();
                        } else {
                            max = MAX_POWER_PER_TICK.get() * roundedUp / (this.battery.getCapacity() / 2L);
                        }

                        max = MathUtil.clamp(max, 0L, MAX_POWER_PER_TICK.get());
                    }

                    Utils.writeDeclaredField(TileQuarry.class, this, "debugPowerRate", max, false);
                    this.blockPercentSoFar.set(0.0);
                    this.moveDistanceSoFar.set(0.0);
                    int maxTasks = Math.max(1, (int) (max * (long) BCBuildersConfig.quarryMaxTasksPerTick / MAX_POWER_PER_TICK.get()));
                    boolean sendUpdate = false;

                    label150:
                    for (int i = 0; i < maxTasks; ++i) {
                        if (this.currentTask != null) {
                            long needed = (long)getRequiredPowerThisTick.invoke(this.currentTask);
                            int mult = BCBuildersConfig.quarryTaskPowerDivisor;
                            long added;
                            if (mult > 0) {
                                long nNeeded = needed * (long) (mult + i) / (long) mult;
                                long leftover = needed * (long) (mult + i) % (long) mult;
                                long power = this.battery.extractPower(0L, Math.min(max, nNeeded));
                                max -= power;
                                added = power * (long) mult / (long) (mult + i);
                                if (leftover > 0L) {
                                    ++added;
                                }
                            } else {
                                added = this.battery.extractPower(0L, Math.min(max, needed));
                                max -= added;
                            }

                            if (!(Boolean)addPower.invoke(this.currentTask,added)) {
                                sendUpdate = true;
                                break;
                            }

                            this.currentTask = null;
                        }

                        if (!this.frameBreakBlockPoses.isEmpty()) {
                            BlockPos blockPos = (BlockPos) this.frameBreakBlockPoses.iterator().next();
                            if (this.canMine(blockPos)) {
                                this.drillPos = null;
                                reflectedCurrentTask.set(breakBlockConstructor.newInstance(this,blockPos));
                                sendUpdate = true;
                            }

                            this.check.invoke(this,blockPos);
                        } else {
                            if (!this.framePlaceFramePoses.isEmpty()) {
                                Iterator var20 = this.framePoses.iterator();

                                while (var20.hasNext()) {
                                    BlockPos blockPos = (BlockPos) var20.next();
                                    if (this.framePlaceFramePoses.contains(blockPos)) {
                                        this.check.invoke(this,blockPos);
                                        if (this.framePlaceFramePoses.contains(blockPos)) {
                                            this.drillPos = null;
                                            this.reflectedCurrentTask.set(addFrameConstructor.newInstance(this,blockPos));
                                            sendUpdate = true;
                                            continue label150;
                                        }
                                    }
                                }
                            }

                            if (this.boxIterator.get() == null || this.drillPos == null) {
                                this.boxIterator.set(this.createBoxIterator());

                                while ((this.canMoveThrough(this.boxIterator.get().getCurrent()) || !this.canMine(this.boxIterator.get().getCurrent()) || !this.canMoveDownTo(this.boxIterator.get().getCurrent())) && this.boxIterator.get().advance() != null) {
                                }

                                this.drillPos = new Vec3d(this.miningBox.closestInsideTo(this.pos));
                            }

                            if (this.boxIterator != null && this.boxIterator.get().hasNext()) {
                                while ((this.canMoveThrough(this.boxIterator.get().getCurrent()) || !this.canMine(this.boxIterator.get().getCurrent()) || !this.canMoveDownTo(this.boxIterator.get().getCurrent())) && this.boxIterator.get().advance() != null) {
                                }

                                if (this.boxIterator.get().hasNext()) {
                                    boolean found = false;
                                    if (this.drillPos.squareDistanceTo(new Vec3d(this.boxIterator.get().getCurrent())) >= 1.0) {
                                        this.reflectedCurrentTask.set(moveDrillConstructor.newInstance(this,this.drillPos, new Vec3d(this.boxIterator.get().getCurrent())));
                                        found = true;
                                    } else if (this.canMine(this.boxIterator.get().getCurrent())) {
                                        reflectedCurrentTask.set(breakBlockConstructor.newInstance(this,this.boxIterator.get().getCurrent()));
                                        found = true;
                                    }

                                    if (found) {
                                        sendUpdate = true;
                                    } else {
                                        AxisAlignedBB box = this.miningBox.getBoundingBox();
                                        if (box.maxX - box.minX == 63.0 && box.maxZ - box.minZ == 63.0) {
                                            AdvancementUtil.unlockAdvancement(this.getOwner().getId(), ADVANCEMENT_COMPLETE);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    this.debugPowerRate.set(this.debugPowerRate.get() - max);
                    if (sendUpdate) {
                        this.sendNetworkUpdate(NET_RENDER_DATA);
                    }

                }
            }
        } catch (IllegalAccessException | InvocationTargetException err) {
            throw new RuntimeException(err);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        long mj = RF2MJ(maxReceive);

        long current = battery.getStored();

        if (battery.isFull()) return 0;
        else if (current + mj <= battery.getCapacity()) {
            if (!simulate) battery.addPower(mj,false);
            sendNetworkUpdate(NET_RENDER_DATA);
            return maxReceive;
        } else {
            if (!simulate) battery.addPower(battery.getCapacity() - current,false);
            sendNetworkUpdate(NET_RENDER_DATA);
            return MJ2RF(battery.getCapacity() - current);
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return MJ2RF(battery.getStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return MJ2RF(battery.getCapacity());
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }


    private boolean canMine(BlockPos blockPos) {
        if (world.getBlockState(blockPos).getBlockHardness(world, blockPos) < 0) {
            return false;
        }
        Fluid fluid = BlockUtil.getFluidWithFlowing(world, blockPos);
        return fluid == null || fluid.getViscosity() <= 1000;
    }

    private boolean canMoveThrough(BlockPos blockPos) {
        if (world.isAirBlock(blockPos) || world.getBlockState(blockPos).getBlock() instanceof BlockSnowTFC) {
            return true;
        }
        Fluid fluid = BlockUtil.getFluidWithFlowing(world, blockPos);
        return fluid != null && fluid.getViscosity() <= 1000;
    }

    private boolean canMoveDownTo(BlockPos blockPos) {
        for (int y = miningBox.max().getY(); y > blockPos.getY(); y--) {
            if (!canMoveThrough(VecUtil.replaceValue(blockPos, EnumFacing.Axis.Y, y))) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    private BoxIterator createBoxIterator() {
        long x = getPos().getX();
        long y = getPos().getY();
        long z = getPos().getZ();
        long seed = ((x & 0xFFFF) << 0) | ((y & 0xFFFF) << 16) | ((z & 0xFFFF << 32));

        Random rand = new Random(seed);
        EnumAxisOrder axisOrder = rand.nextBoolean() ? EnumAxisOrder.XZY : EnumAxisOrder.ZXY;
        AxisOrder.Inversion inv = AxisOrder.Inversion.getFor(rand.nextBoolean(), rand.nextBoolean(), false);
        return new BoxIterator(miningBox, AxisOrder.getFor(axisOrder, inv), true);

    }

}
