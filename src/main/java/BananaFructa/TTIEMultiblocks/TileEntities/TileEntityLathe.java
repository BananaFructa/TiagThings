package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Gui.LatheAction;
import BananaFructa.TTIEMultiblocks.Gui.LatheActionFlag;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.STEMMInventoryHandler;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityLathe extends AnimatedOBJTileEntity<TileEntityLathe, TileEntityLathe.LatheRecipe>{

    public static List<LatheRecipe> recipes = new ArrayList<LatheRecipe>() {{
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/mild_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:material_component_iron_3>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.DRILL,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},71,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:material_component_steel_3>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.DRILL,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},71,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/tungsten>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:material_component_tungsten_3>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.DRILL,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},71,LatheTier.TUNGSTEN_STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/wrought_iron>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:material_component_iron_2>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.TURN,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/crucible_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:material_component_steel_2>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.TURN,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:2>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:14>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.GROOVE,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));

        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:15>")},new FluidStack[0],new LatheAction[]{LatheAction.DRILL,LatheAction.DRILL,LatheAction.DRILL},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:16>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.DRILL,LatheAction.TURN},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.SECOND,LatheActionFlag.FIRST},64,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:speedloader>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.TURN,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.ANY,LatheActionFlag.ANY,LatheActionFlag.ANY},44,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:toolupgrade:7>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.GROOVE,LatheAction.TURN},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:2>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:steel_lathe_tool>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.DRILL,LatheAction.TURN},new LatheActionFlag[]{LatheActionFlag.ANY,LatheActionFlag.ANY,LatheActionFlag.ANY},64,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_rod:1>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:tungsten_steel_lathe_tool>")},new FluidStack[0],new LatheAction[]{LatheAction.TURN,LatheAction.DRILL,LatheAction.TURN},new LatheActionFlag[]{LatheActionFlag.ANY,LatheActionFlag.ANY,LatheActionFlag.ANY},64,LatheTier.STEEL));

        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:crucible_steel_rod>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:14>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.GROOVE,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:mild_steel_rod>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material:34>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.GROOVE,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));
        add(new LatheRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:1>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material:34>")},new FluidStack[0],new LatheAction[]{LatheAction.GROOVE,LatheAction.GROOVE,LatheAction.GROOVE},new LatheActionFlag[]{LatheActionFlag.LAST,LatheActionFlag.NOT_LAST,LatheActionFlag.NOT_LAST},62,LatheTier.STEEL));
    }};

    public float inSpeed = 0;
    public float inTorque = 0;

    public TileEntityLathe() {
        super(TTIEContent.lathe, 0, false,recipes,TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.LATHE.getMeta()));
        setAnimation("running");
        setAnimationSpeed(0);
    }

    @Override
    public void initPorts() {
        int itemInput = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        int itemAux = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(4,itemInput,PortType.INPUT,EnumFacing.WEST); // ghost ports
        registerItemPort(4,itemInput,PortType.OUTPUT,EnumFacing.EAST);
        int rot = registerRotaryStorage();
        registerRotaryPort(5,rot,PortType.INPUT,EnumFacing.WEST);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote) return;
        if (rotaryEnergies.size() > 0) {
            IRotaryEnergy re = rotaryEnergies.get(0);
            if (re != null) {
                if (re.getRotationSpeed() / 60.0f != speed || inTorque != re.getOutputTorque()) IEUtils.notifyClientUpdate(world, pos);
                if (re.getRotationSpeed() > 0) {
                    speed = re.getRotationSpeed()/60.0f;
                    inTorque = re.getOutputTorque();
                }
                else {
                    speed -= 0.02;
                    inTorque = 0;
                }
                if (speed < 0) speed = 0;
                setAnimationSpeed(speed);
                inSpeed = re.getRotationSpeed();
            } else {
                setAnimationSpeed(0);
                IEUtils.notifyClientUpdate(world, pos);
                inSpeed = 0;
                inTorque = 0;
            }
        }
    }

    @Override
    public boolean canDoRecipe(LatheRecipe recipe) {
        ItemStack input = this.inventoryHandlers.get(0).getStackInSlot(0);
        if (input.isEmpty()) return false;
        if (!input.hasTagCompound()) return false;
        NBTTagCompound tagCompound = input.getTagCompound();
        if (!tagCompound.hasKey("lathe")) return false;
        NBTTagCompound latheC = tagCompound.getCompoundTag("lathe");
        if (!latheC.hasKey("recipe") || !latheC.hasKey("actions") || !latheC.hasKey("progress")) return false;
        int r = latheC.getInteger("recipe");
        if (recipes.indexOf(recipe) != r) return false;
        int p = latheC.getInteger("progress");
        if (p != recipe.targetProgress) return false;
        int[] actions = latheC.getIntArray("actions");
        if (actions.length != 3) return false;
        for (int i = 0;i < 3;i++) {
            if (!matchesAction(recipe.actions[i].ordinal(),actions,recipe.actionFlags[i])) return false;
        }
        return true;
    }

    @Override
    protected boolean enoughSpaceForRecipe(LatheRecipe recipe) {
        return true;
    }

    public boolean matchesAction(int action, int actions[], LatheActionFlag flag) {
        switch (flag){
            case FIRST:
                return actions[2] == action;
            case SECOND:
                return actions[1] == action;
            case LAST:
                return actions[0] == action;
            case ANY:
                for (int i = 0;i < 3;i++) if (actions[i] == action) return true; // a bit of a problem here but it doesn't matter
                return false;
            case NOT_LAST:
                return actions[1] == action || actions[2] == action;
        }
        return false;
    }

    public static class LatheRecipe extends SimplifiedMultiblockRecipe {

        public LatheAction[] actions;
        public LatheActionFlag[] actionFlags;
        public int targetProgress;
        public LatheTier tier;

        public LatheRecipe(ItemStack[] itemInputs, FluidStack[] fluidInputs, ItemStack[] itemOutputs, FluidStack[] fluidOutput, LatheAction[] actions, LatheActionFlag[] actionFlags, int targetProgress, LatheTier tier) {
            super(itemInputs, fluidInputs, itemOutputs, fluidOutput, 0,1,true);
            this.actions = actions;
            this.actionFlags = actionFlags;
            this.targetProgress = targetProgress;
            this.tier = tier;
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setString("animationName",currentAnimationName);
        nbtTagCompound.setFloat("animationSpeed",speed);
        nbtTagCompound.setFloat("inTorque",inTorque);
        nbtTagCompound.setFloat("inSpeed",inSpeed);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        inTorque = pkt.getNbtCompound().getFloat("inTorque");
        inSpeed = pkt.getNbtCompound().getFloat("inSpeed");
    }

    public enum LatheTier {
        STEEL,
        TUNGSTEN_STEEL

    };
}
