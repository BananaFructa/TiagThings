package BananaFructa.TTIEMultiblocks.Compat.Waila;

import BananaFructa.AdvancedTFCTech.ModifiedTileEntityPowerLoomParent;
import BananaFructa.ImmersiveEngineering.ModifiedTileEntityCrusher;
import BananaFructa.ImmersiveEngineering.ModifiedTileEntityMetalPress;
import BananaFructa.ImmersiveEngineering.TEFluidPumpAlternativeModified;
import BananaFructa.ImmersiveIntelligence.TileEntityModifiedMechanicalPump;
import BananaFructa.ImmersivePetroleum.ModifiedTileEntityPumpjackParent;
import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.SignalSourceBlock;
import BananaFructa.TTIEMultiblocks.SignalSourceTileEntity;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
//import BananaFructa.TTIEMultiblocks.Utils.STEMM_ClusterClient;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.Uem.DrainFluidPlacer;
import BananaFructa.UnecologicalMethods.DrainTileEntity;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityDistillationTower;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityFluidPumpAlternative;
import net.darkhax.wawla.engine.WailaEngine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;
import tfctech.compat.waila.WailaIntegration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static BananaFructa.TTIEMultiblocks.SignalSourceBlock.POWERED;

public class TTWailaProvider implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar registrar) {
        TTWailaProvider provider = new TTWailaProvider();
        registrar.registerTailProvider(provider, TileEntityCrusher.class);
        registrar.registerBodyProvider(provider, SimplifiedTileEntityMultiblockMetal.class);
        registrar.registerBodyProvider(provider, ElectricMotorTileEntity.class);
        registrar.registerBodyProvider(provider, TileEntityMetalPress.class);
        registrar.registerBodyProvider(provider, TileEntityPumpjack.class);
        registrar.registerBodyProvider(provider, TileEntityCrusher.class);
        registrar.registerBodyProvider(provider, TileEntityPowerLoom.class);
        registrar.registerBodyProvider(provider, TEFluidPumpAlternativeModified.class);
        registrar.registerBodyProvider(provider, TileEntityModifiedMechanicalPump.class);
        registrar.registerBodyProvider(provider, TileEntityFluidPumpAlternative.class);
        registrar.registerBodyProvider(provider, TileEntityModifiedMechanicalPump.class);
        registrar.registerBodyProvider(provider, DrainTileEntity.class);
        registrar.registerBodyProvider(provider, DrainFluidPlacer.class);
        registrar.registerBodyProvider(provider, SignalSourceTileEntity.class);
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        /*if (accessor.getTileEntity() instanceof TileEntityCrusher) {
            for (String t : tooltip) {
                System.out.println(t);
                if (t.contains("IF")) {
                    tooltip.remove(t);
                    break;
                }
            }
        }*/

        return tooltip;
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        if (accessor.getTileEntity() instanceof SignalSourceTileEntity) {
            SignalSourceTileEntity te = (SignalSourceTileEntity) accessor.getTileEntity();
            tooltip.add("Status: " + ((te.good) ? "\u00a7aPowered\u00a7r" : "\u00A7cNot Powered\u00a7r"));
            tooltip.add("Signal strength: " + te.signalLevel);
        }

        /*if (accessor.getTileEntity() instanceof SimplifiedTileEntityMultiblockMetal<?,?>) {
            SimplifiedTileEntityMultiblockMetal<?,?> te = (SimplifiedTileEntityMultiblockMetal<?,?>)accessor.getTileEntity();
            int[] offset = te.offset;
            tooltip.add("Offset " + offset[0] + " " + offset[1] + " " + offset[2]);
        }*/

        if (accessor.getTileEntity() instanceof TileEntityCoalBoiler) {
            TileEntityCoalBoiler te = ((TileEntityCoalBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Working at " + String.format("%.02f", 100-(100/15.0f)*te.redstoneLevel) + "%");
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                IItemHandler itemHandler = te.inventoryHandlers.get(0);
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());

                FluidTank inputTank = te.tanks.get(0);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(1);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");

            }
        }
        if (accessor.getTileEntity() instanceof TileEntityOilBoiler) {
            TileEntityOilBoiler te = ((TileEntityOilBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Working at " + String.format("%.02f", 100-(100/15.0f)*te.redstoneLevel) + "%");
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                FluidTank inputFluid = te.tanks.get(0);
                if (inputFluid == null || inputFluid.getFluid() == null) tooltip.add("Fuel Fluid Input: Empty");
                else tooltip.add("Fuel Fluid Input: " + inputFluid.getFluid().getLocalizedName() + " " + inputFluid.getFluidAmount() + " mb");

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(2);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");

            }
        }
        if (accessor.getTileEntity() instanceof TileEntityNMPLM) {
            TileEntityNMPLM te = ((TileEntityNMPLM)accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Current process: " + (te.process == 0 ? "um" : "nm"));
            }
        }
        if (accessor.getTileEntity() instanceof TileEntityCCM) {
            TileEntityCCM te = ((TileEntityCCM)accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Mode: " + te.getMode().getName());
            }
        }
        if (accessor.getTileEntity() instanceof TileEntityEUVPLM) {
            TileEntityEUVPLM te = ((TileEntityEUVPLM)accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Current process: " + (te.process == 0 ? "um" : (te.process == 1 ? "nm" : "EUV")));
            }
        }
        if (accessor.getTileEntity() instanceof TileEntityGasCentrifuge) {
            TileEntityGasCentrifuge te = ((TileEntityGasCentrifuge) accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                FluidTank inputFluid = te.tanks.get(0);
                if (inputFluid == null || inputFluid.getFluid() == null) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + inputFluid.getFluid().getLocalizedName() + " " + inputFluid.getFluidAmount() + " mb");

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Primary Output: Empty");
                else tooltip.add("Primary Output: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(2);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Secondary Output: Empty");
                else tooltip.add("Secondary Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");
            }
        }

        /*if (accessor.getTileEntity() instanceof TileEntityComputerClusterController) {
            TileEntityComputerClusterController te = ((TileEntityComputerClusterController) accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Cluster Units Paired: " + te.activeClusterUnits);
                tooltip.add("Processes running: " + te.lastUsedRequests + "/" + te.maxRequests);
                tooltip.add("Resource utilization: " + te.lastUsedProcessingPower + "/" + te.totalProcessingPower);
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityComputerClusterUnit) {
            TileEntityComputerClusterUnit te = (TileEntityComputerClusterUnit) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Pair status: " + (te.paired ? "PAIRED" : "UNPAIRED"));
                tooltip.add("Total processing resources: 4");
            }
        }

        if (accessor.getTileEntity() instanceof STEMM_ClusterClient) {
            STEMM_ClusterClient<?,?> te = (STEMM_ClusterClient<?,?>) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = (STEMM_ClusterClient<?,?>)te.master();
                tooltip.add("Cluster connection status: " + te.connected.getName());
                int usedPower = 0;
                if (te.processQueue.size() > 0) {
                    usedPower = te.getUsedPower();
                }
                tooltip.add("Using " + usedPower + " processing power");
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityMemoryFormatter) {
            TileEntityMemoryFormatter te = (TileEntityMemoryFormatter) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                if (te.isWorking()) {
                    float prog = (float)te.processQueue.get(0).processTick/(float)te.processQueue.get(0).maxTicks;
                    int hashes = (int)(20*prog);
                    int dots = 20 - hashes;
                    String hs = "";
                    String ds = "";
                    for (int i = 0;i < hashes;i++) hs +="#";
                    for (int i = 0;i < dots;i++) ds +="-";
                    tooltip.add("Progress " + String.format("%.2f",prog*100) + "% ["+hs+ds+"]");
                }
            }
        }*/

        if (accessor.getTileEntity() instanceof TileEntityClayOven) {
            TileEntityClayOven te = (TileEntityClayOven) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                for (TileEntityMultiblockMetal.MultiblockProcess<SimplifiedMultiblockRecipe> process : te.processQueue) {
                    tooltip.add(process.recipe.getItemInputs().get(0).stack.getDisplayName() + ": " + (int)((process.maxTicks - process.processTick)/(60*20)) + " hour(s) remaining");
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityOpenHearthFurnace) {
            TileEntityOpenHearthFurnace te = ((TileEntityOpenHearthFurnace)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");

                ItemStack stack = te.inventoryHandlers.get(0).getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());
                if (te.tanks.get(0).getFluidAmount() != 0) {
                    tooltip.add("Liquid Fuel: " + te.tanks.get(0).getFluid().getLocalizedName() + " " + te.tanks.get(0).getFluidAmount() + " mb");
                }

                if (te.tanks.get(1).getFluidAmount() != 0) {
                    if (te.tanks.get(1).getFluid().getFluid() == TileEntityOpenHearthFurnace.liquidSteel) {
                        tooltip.add(te.tanks.get(1).getFluid().getLocalizedName() + ": " + te.tanks.get(1).getFluidAmount() + " mb");
                    } else if (te.tanks.get(1).getFluid().getFluid() == TileEntityOpenHearthFurnace.liquidCrucibleSteel) {
                        tooltip.add(te.tanks.get(1).getFluid().getLocalizedName() + ": " + te.tanks.get(1).getFluidAmount() + " mb");
                    } else {
                        tooltip.add(te.tanks.get(1).getFluid().getLocalizedName() + ": " + te.tanks.get(1).getFluidAmount() + " mb");
                    }
                }
                int idleTime = te.idleTime;
                if (idleTime > 0) {
                    if (te.tanks.get(1).getFluid().getFluid() == TileEntityOpenHearthFurnace.liquidCrucibleSteel) {
                        tooltip.add("Decarburization in: " + (int)((TileEntityOpenHearthFurnace.idleTimeToMildSteel - idleTime)/20) + " second");
                    } else {
                        tooltip.add("Decarburization in: " + (int)((TileEntityOpenHearthFurnace.idleTimeToMildSteel - idleTime)/20) + " second");
                    }
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntitySmallCoalBoiler) {
            TileEntitySmallCoalBoiler te = ((TileEntitySmallCoalBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                IItemHandler itemHandler = te.inventoryHandlers.get(0);
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(0);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");
            }
        }

        if (accessor.getTileEntity() instanceof ElectricMotorTileEntity) {
            ElectricMotorTileEntity te = ((ElectricMotorTileEntity) accessor.getTileEntity());
            IEnergyStorage storage = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
            if (storage != null) {
                tooltip.add("RF " + storage.getEnergyStored() + "/" +storage.getMaxEnergyStored());
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityDistillationTower) {
            for (int i = 0;i < tooltip.size();i++) {
                if (tooltip.get(i).contains("IF") || tooltip.get(i).contains("FE")) {
                    tooltip.remove(i);
                    i--;
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityCokeOvenBattery) {
            TileEntityCokeOvenBattery te = ((TileEntityCokeOvenBattery)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                FluidTank inputFluid = te.tanks.get(0);
                if (inputFluid == null || inputFluid.getFluid() == null) tooltip.add("Fuel Fluid Input: Empty");
                else tooltip.add("Fuel Fluid Input: " + inputFluid.getFluid().getLocalizedName() + " " + inputFluid.getFluidAmount() + " mb");

                IItemHandler itemHandler = te.inventoryHandlers.get(0);
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Fuel Solid Input: Empty");
                else tooltip.add("Fuel Solid Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());


            }
        }

        if (accessor.getTileEntity() instanceof TileEntityMetalPress) {
            removeFE(accessor, tooltip);
            TileEntityMetalPress te = ((TileEntityMetalPress) accessor.getTileEntity()).master();
            RotaryStorage rs = new RotaryStorage();
            IEUtils.inputRotaryPower(te,ModifiedTileEntityMetalPress.getPosInput(),rs);
            tooltip.add("Rotary Input");
            float torque = rs.getOutputTorque();
            float speed = rs.getOutputRotationSpeed();
            float minSpeed = ModifiedTileEntityMetalPress.getMinSpeed();
            float maxSpeed = ModifiedTileEntityMetalPress.getMaxSpeed();
            float minTorque = ModifiedTileEntityMetalPress.getMinTorque();
            tooltip.add("  - Speed: " + String.format("%.2f",speed) + " RPM" + (speed >= minSpeed && speed <= maxSpeed ? "\u00a7a" : "\u00a7c") + " Requires " + String.format("%.2f",minSpeed) + "-" + String.format("%.2f",maxSpeed) + " RPM");
            tooltip.add("  - Torque: " + String.format("%.2f",torque) + " Nm"+ (torque >= minTorque ? "\u00a7a" : "\u00a7c") + " Requires at least " + String.format("%.2f",minTorque) + " Nm");
        }

        if (accessor.getTileEntity() instanceof TileEntityPumpjack) {
            removeFE(accessor, tooltip);
            TileEntityPumpjack.TileEntityPumpjackParent te = (TileEntityPumpjack.TileEntityPumpjackParent)((TileEntityPumpjack) accessor.getTileEntity()).master();
            RotaryStorage rs = new RotaryStorage();
            IEUtils.inputRotaryPower(te,ModifiedTileEntityPumpjackParent.getPosInput(),rs);
            tooltip.add("Rotary Input");
            float torque = rs.getOutputTorque();
            float speed = rs.getOutputRotationSpeed();
            float minSpeed = ModifiedTileEntityPumpjackParent.getMinSpeed();
            float maxSpeed = ModifiedTileEntityPumpjackParent.getMaxSpeed();
            float minTorque = ModifiedTileEntityPumpjackParent.getMinTorque();
            tooltip.add("  - Speed: " + String.format("%.2f",speed) + " RPM" + (speed >= minSpeed && speed <= maxSpeed ? "\u00a7a" : "\u00a7c") + " Requires " + String.format("%.2f",minSpeed) + "-" + String.format("%.2f",maxSpeed) + " RPM");
            tooltip.add("  - Torque: " + String.format("%.2f",torque) + " Nm"+ (torque >= minTorque ? "\u00a7a" : "\u00a7c") + " Requires at least " + String.format("%.2f",minTorque) + " Nm");
        }

        if (accessor.getTileEntity() instanceof TileEntityCrusher) {
            removeFE(accessor, tooltip);
            TileEntityCrusher te = ((TileEntityCrusher) accessor.getTileEntity()).master();
            RotaryStorage rs = new RotaryStorage();
            IEUtils.inputRotaryPower(te,ModifiedTileEntityCrusher.getPosInput(),rs);
            tooltip.add("Rotary Input");
            float torque = rs.getOutputTorque();
            float speed = rs.getOutputRotationSpeed();
            float minSpeed = ModifiedTileEntityCrusher.getMinSpeed();
            float maxSpeed = ModifiedTileEntityCrusher.getMaxSpeed();
            float minTorque = ModifiedTileEntityCrusher.getMinTorque();
            tooltip.add("  - Speed: " + String.format("%.2f",speed) + " RPM" + (speed >= minSpeed && speed <= maxSpeed ? "\u00a7a" : "\u00a7c") + " Requires " + String.format("%.2f",minSpeed) + "-" + String.format("%.2f",maxSpeed) + " RPM");
            tooltip.add("  - Torque: " + String.format("%.2f",torque) + " Nm"+ (torque >= minTorque ? "\u00a7a" : "\u00a7c") + " Requires at least " + String.format("%.2f",minTorque) + " Nm");
        }

        if (accessor.getTileEntity() instanceof TileEntityPowerLoom) {
            removeFE(accessor, tooltip);
            TileEntityPowerLoom.TileEntityPowerLoomParent te = (TileEntityPowerLoom.TileEntityPowerLoomParent)((TileEntityPowerLoom) accessor.getTileEntity()).master();
            RotaryStorage rs = new RotaryStorage();
            IEUtils.inputRotaryPower(te, ModifiedTileEntityPowerLoomParent.getPosInput(),rs);
            tooltip.add("Rotary Input");
            float torque = rs.getOutputTorque();
            float speed = rs.getOutputRotationSpeed();
            float minSpeed = ModifiedTileEntityPowerLoomParent.getMinSpeed();
            float maxSpeed = ModifiedTileEntityPowerLoomParent.getMaxSpeed();
            float minTorque = ModifiedTileEntityPowerLoomParent.getMinTorque();
            tooltip.add("  - Speed: " + String.format("%.2f",speed) + " RPM" + (speed >= minSpeed && speed <= maxSpeed ? "\u00a7a" : "\u00a7c") + " Requires " + String.format("%.2f",minSpeed) + "-" + String.format("%.2f",maxSpeed) + " RPM");
            tooltip.add("  - Torque: " + String.format("%.2f",torque) + " Nm"+ (torque >= minTorque ? "\u00a7a" : "\u00a7c") + " Requires at least " + String.format("%.2f",minTorque) + " Nm");
        }

        if (accessor.getTileEntity() instanceof TEFluidPumpAlternativeModified) {
            TEFluidPumpAlternativeModified te = (TEFluidPumpAlternativeModified)(accessor.getTileEntity());
            if (te.blocked) {
                tooltip.add("\u00a7c There is another pump in this chuck pumping from the same body of water!\u00a7r");
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityModifiedMechanicalPump) {
            TileEntityModifiedMechanicalPump te = (TileEntityModifiedMechanicalPump)(accessor.getTileEntity());
            if (te.blocked) {
                tooltip.add("\u00a7c There is another pump in this chuck pumping from the same body of water!\u00a7r");
            }
        }

        /*if (accessor.getWorld().getTileEntity(accessor.getTileEntity().getPos()) instanceof DrainFluidPlacer) {
            tooltip.add("\u00a7bThe drain will try and form a basin of water around itself if supplied with water.");
        } else if (accessor.getWorld().getTileEntity(accessor.getTileEntity().getPos()) instanceof DrainTileEntity) {
            tooltip.add("\u00a7bThe drain will dump the received waste liquids in the surrounding body of water.");
        }*/

        return tooltip;
    }

    private void removeFE(IWailaDataAccessor accessor, List<String> tooltip) {
        for (String t : tooltip) {
            if (t.contains("FE")) {
                tooltip.remove(t);
                break;
            }
        }
        if (accessor.getNBTData().hasKey("Energy")) {
            accessor.getNBTData().removeTag("Energy");
        }
    }
}
