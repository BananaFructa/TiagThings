package BananaFructa.ImmersiveEngineering;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.RockUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.EventHandler;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

public class ModifiedTileEntityCrusher extends TileEntityCrusher {

    private RotaryStorage rotaryStorage;

    static {
        for (CrusherRecipe recipe : CrusherRecipe.recipeList) {
            Utils.writeDeclaredField(MultiblockRecipe.class,recipe,"totalProcessEnergy",0,false);
        }
    }

    public ModifiedTileEntityCrusher() {
        super();
        rotaryStorage = new RotaryStorage() {
            @Override
            public RotationSide getSide(@Nullable EnumFacing facing) {
                return RotationSide.INPUT;
            }
        };
        //Utils.writeDeclaredField(TileEntityMultiblockMetal.class,this,"energyStorage",new FluxStorageAdvanced(0),true);
        this.energyStorage.setEnergy(32000);
        markDirty();
    }

    protected LinkedList<NBTTagCompound> nbtQueue = new LinkedList<>();

    public boolean addProcessToQueue(MultiblockProcess<CrusherRecipe> process, boolean simulate, boolean addToPrevious) {
        /*if (addToPrevious && process instanceof MultiblockProcessInWorld && !this.processQueue.isEmpty()) {
            MultiblockProcess<CrusherRecipe> curr = this.processQueue.get(this.processQueue.size() - 1);
            if (curr instanceof MultiblockProcessInWorld && process.recipe.equals(curr.recipe)) {
                MultiblockProcessInWorld p = (MultiblockProcessInWorld) curr;
                boolean canStack = true;

                for (Object old : p.inputItems) {
                    for (Object in : ((MultiblockProcessInWorld) process).inputItems) {
                        if (OreDictionary.itemMatches((ItemStack) old, (ItemStack) in, true) && blusunrize.immersiveengineering.common.util.Utils.compareItemNBT((ItemStack) old, (ItemStack) in) && ((ItemStack) old).getCount() + ((ItemStack) in).getCount() > ((ItemStack) old).getMaxStackSize()) {
                            canStack = false;
                            break;
                        }
                    }

                    if (!canStack) {
                        break;
                    }
                }

                if (canStack) {
                    if (!simulate) {
                        for (Object old : p.inputItems) {
                            for (Object in : ((MultiblockProcessInWorld) process).inputItems) {
                                if (OreDictionary.itemMatches((ItemStack) old, (ItemStack) in, true) && blusunrize.immersiveengineering.common.util.Utils.compareItemNBT((ItemStack) old, (ItemStack) in)) {
                                    ((ItemStack) old).grow(((ItemStack) in).getCount());
                                    break;
                                }
                            }
                        }
                    }

                    return true;
                }
            }
        }*/


        if (this.getProcessQueueMaxLength() >= 0 && this.processQueue.size() >= this.getProcessQueueMaxLength()) {
            return false;
        } else {
            float dist = 1.0F;
            MultiblockProcess<CrusherRecipe> p = null;
            if (this.processQueue.size() > 0) {
                p = (MultiblockProcess) this.processQueue.get(this.processQueue.size() - 1);
                if (p != null) {
                    dist = (float) p.processTick / (float) p.maxTicks;
                }
            }

            if (p != null && dist < this.getMinProcessDistance(p)) {
                return false;
            } else {
                if (!simulate) {
                    this.processQueue.add(process);
                }

                return true;
            }
        }
    }

    public void onEntityCollision(World world, Entity entity) {
        boolean bpos = this.field_174879_c == 16 || this.field_174879_c == 17 || this.field_174879_c == 18 || this.field_174879_c == 21 || this.field_174879_c == 22 || this.field_174879_c == 23 || this.field_174879_c == 26 || this.field_174879_c == 27 || this.field_174879_c == 28;
        if (bpos && !world.isRemote && entity != null && !entity.isDead && !this.isRSDisabled()) {
            TileEntityCrusher master = (TileEntityCrusher)this.master();
            if (master == null) {
                return;
            }

            Vec3d center = (new Vec3d(master.getPos())).addVector((double)0.5F, (double)0.75F, (double)0.5F);
            AxisAlignedBB crusherInternal = new AxisAlignedBB(center.x - (double)1.0625F, center.y, center.z - (double)1.0625F, center.x + (double)1.0625F, center.y + (double)1.25F, center.z + (double)1.0625F);
            if (!entity.getEntityBoundingBox().intersects(crusherInternal)) {
                return;
            }

            if (entity instanceof EntityItem && !((EntityItem)entity).getItem().isEmpty()) {
                ItemStack stack = ((EntityItem)entity).getItem();
                if (stack.isEmpty()) {
                    return;
                }

                CrusherRecipe recipe = master.findRecipeForInsertion(stack);
                if (recipe == null) {
                    return;
                }

                ItemStack displayStack = recipe.getDisplayStack(stack);
                TileEntityMultiblockMetal.MultiblockProcess<CrusherRecipe> process = new TileEntityMultiblockMetal.MultiblockProcessInWorld(recipe, 0.5F, blusunrize.immersiveengineering.common.util.Utils.createNonNullItemStackListFromItemStack(displayStack));
                if (master.addProcessToQueue(process, true, true)) {
                    master.addProcessToQueue(process, false, true);
                    if (RockUtils.isRock(stack)) {
                        ((ModifiedTileEntityCrusher)master).nbtQueue.add(stack.getTagCompound());
                        System.out.println("Added " + ((ModifiedTileEntityCrusher)master()).nbtQueue.size());
                        master.markDirty();
                    }
                    stack.shrink(displayStack.getCount());
                    if (stack.getCount() <= 0) {
                        entity.setDead();
                    }
                }
            } else if (entity instanceof EntityLivingBase && (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).capabilities.disableDamage)) {
                int consumed = master.energyStorage.extractEnergy(80, true);
                if (consumed > 0) {
                    master.energyStorage.extractEnergy(consumed, false);
                    EventHandler.crusherMap.put(entity.getUniqueID(), master);
                    entity.attackEntityFrom(IEDamageSources.crusher, (float)consumed / 20.0F);
                }
            }
        }

    }

    static ItemStack crushedRock = Utils.itemStackFromCTId("<tiagthings:crushed_rock>");

    public void doProcessOutput(ItemStack output) {
        BlockPos pos = this.getPos().add(0, -1, 0).offset(this.facing, -2);
        TileEntity inventoryTile = this.world.getTileEntity(pos);
        ModifiedTileEntityCrusher master = ((ModifiedTileEntityCrusher)master());
        if (master() != null && !master.nbtQueue.isEmpty() && (master.nbtQueue.peek() == null || !master.nbtQueue.peek().hasKey("trace"))) {
            master.nbtQueue.poll();
            System.out.println("Removed " + master.nbtQueue.size());
            master.markDirty();
        }
        else if (output.getItem() == crushedRock.getItem() && master() != null && !master.nbtQueue.isEmpty()) {
            output.setTagCompound(master.nbtQueue.poll());
            master().markDirty();
            System.out.println("Removed " + master.nbtQueue.size());
            markDirty();
        }
        if (inventoryTile != null) {
            output = blusunrize.immersiveengineering.common.util.Utils.insertStackIntoInventory(inventoryTile, output, this.facing);
        }

        if (!output.isEmpty()) {
            blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(this.world, pos, output, this.facing.getOpposite());
        }

    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setTag("rotary",rotaryStorage.toNBT());
        int size = nbtQueue.size();
        nbt.setInteger("nbtQ_size",size);
        int index = 0;
        for (NBTTagCompound tag : nbtQueue) {
            if (tag != null) nbt.setTag("nbtQ_"+index,tag);
            index++;
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        rotaryStorage.fromNBT(nbt.getCompoundTag("rotary"));
        nbtQueue.clear();
        int size = nbt.getInteger("nbtQ_size");
        for (int i = 0;i < size;i++) {
            if (nbt.hasKey("nbQ_"+i)) nbtQueue.add((NBTTagCompound) nbt.getTag("nbtQ_"+i));
            else nbtQueue.add(null);
        }
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (isDummy()) return;
        IEUtils.inputRotaryPower(this,getPosInput(),rotaryStorage);
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<CrusherRecipe> process) {
        boolean good = rotaryStorage.getOutputTorque() >= getMinTorque() && rotaryStorage.getOutputRotationSpeed() >= getMinSpeed() && rotaryStorage.getOutputRotationSpeed() <= getMaxSpeed();
        int lastEnergy = this.energyStorage.getEnergyStored();
        this.energyStorage.setEnergy(good ? 32000 : 0);
        if (lastEnergy != energyStorage.getEnergyStored()) {
            markDirty();
            IEUtils.notifyClientUpdate(world,pos);
        }
        return good;
    }

    @Override
    public int getEnergyStored(@Nullable EnumFacing fd) {
        return -1;
    }

    public static float getMinTorque() {
        return 20;
    }

    public static float getMinSpeed() {
        return 30;
    }

    public static float getMaxSpeed() {
        return 60;
    }

    public static int getPosInput() {return 20;}
}
