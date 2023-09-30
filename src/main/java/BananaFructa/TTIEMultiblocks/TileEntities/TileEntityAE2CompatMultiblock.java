package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import appeng.tile.grid.AENetworkTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

/**
 * CAN ONLY BE USED FOR BLOCKS THAT ARE DUMMIES FOR AE2 CONNECTIONS AS IT HAS NONE OF THE LOGIC REQUIRED TO BE THE MASTER BLOCK
 */
public class TileEntityAE2CompatMultiblock<T extends TileEntityMultiblockPart<T>> extends AENetworkTile {

    public boolean formed = false;
    public int pos = -1;
    public int[] offset = new int[]{0, 0, 0};
    public boolean mirrored = false;
    public EnumFacing facing;
    public long onlyLocalDissassembly;
    protected final int[] structureDimensions;

    SimplifiedMultiblockClass multiblockClass;

    protected TileEntityAE2CompatMultiblock(SimplifiedMultiblockClass multiblockClass, TileEntityMultiblockPart<?> oldPart) {
        this.multiblockClass = multiblockClass;
        this.structureDimensions = multiblockClass.size;

        this.formed = oldPart.formed;
        this.pos = oldPart.field_174879_c;
        this.offset = oldPart.offset;
        this.mirrored = oldPart.mirrored;
        this.facing = oldPart.facing;
        this.onlyLocalDissassembly = oldPart.onlyLocalDissassembly;
    }

    public void disassemble()
    {
        if(formed && !world.isRemote)
        {
            BlockPos startPos = getOrigin();
            BlockPos masterPos = getPos().add(-offset[0], -offset[1], -offset[2]);
            long time = world.getTotalWorldTime();
            for(int yy=0;yy<structureDimensions[0];yy++)
                for(int ll=0;ll<structureDimensions[1];ll++)
                    for(int ww=0;ww<structureDimensions[2];ww++)
                    {
                        int w = mirrored?-ww:ww;
                        BlockPos pos = startPos.offset(facing, ll).offset(facing.rotateY(), w).add(0, yy, 0);
                        ItemStack s = ItemStack.EMPTY;

                        TileEntity te = world.getTileEntity(pos);
                        if(te instanceof TileEntityMultiblockPart)
                        {
                            TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
                            Vec3i diff = pos.subtract(masterPos);
                            if (part.offset[0]!=diff.getX()||part.offset[1]!=diff.getY()||part.offset[2]!=diff.getZ())
                                continue;
                            else if (time!=part.onlyLocalDissassembly)
                            {
                                s = part.getOriginalBlock();
                                part.formed = false;
                            }
                        } else if (te instanceof TileEntityAE2CompatMultiblock) {
                            TileEntityAE2CompatMultiblock part = (TileEntityAE2CompatMultiblock) te;
                            Vec3i diff = pos.subtract(masterPos);
                            if (part.offset[0]!=diff.getX()||part.offset[1]!=diff.getY()||part.offset[2]!=diff.getZ())
                                continue;
                            else if (time!=part.onlyLocalDissassembly)
                            {
                                s = part.getOriginalBlock();
                                part.formed = false;
                            }
                        }
                        if(pos.equals(getPos()))
                            s = this.getOriginalBlock();
                        IBlockState state = Utils.getStateFromItemStack(s);
                        if(state!=null)
                        {
                            if(pos.equals(getPos()))
                                world.spawnEntity(new EntityItem(world, pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5, s));
                            else
                                replaceStructureBlock(pos, state, s, yy,ll,ww);
                        }
                    }
        }
    }

    public BlockPos getOrigin() {
        return getBlockPosForPos(0);
    }

    public BlockPos getBlockPosForPos(int targetPos)
    {
        int blocksPerLevel = structureDimensions[1]*structureDimensions[2];
        // dist = target position - current position
        int distH = (targetPos/blocksPerLevel)-(pos/blocksPerLevel);
        int distL = (targetPos%blocksPerLevel / structureDimensions[2])-(pos%blocksPerLevel / structureDimensions[2]);
        int distW = (targetPos%structureDimensions[2])-(pos%structureDimensions[2]);
        int w = mirrored?-distW:distW;
        return getPos().offset(facing, distL).offset(facing.rotateY(), w).add(0, distH, 0);
    }

    public ItemStack getOriginalBlock()
    {
        if(pos<0)
            return ItemStack.EMPTY;
        ItemStack s = ItemStack.EMPTY;
        try{
            int blocksPerLevel = structureDimensions[1]*structureDimensions[2];
            int h = (pos/blocksPerLevel);
            int l = (pos%blocksPerLevel / structureDimensions[2]);
            int w = (pos%structureDimensions[2]);
            s = this.multiblockClass.getStructureManual()[h][l][w];
        }catch(Exception e){e.printStackTrace();}
        return s.copy();
    }

    public void replaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w)
    {
        if(state.getBlock()==this.getBlockType())
            world.setBlockToAir(pos);
        world.setBlockState(pos, state);
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof IEBlockInterfaces.ITileDrop)
            ((IEBlockInterfaces.ITileDrop)tile).readOnPlacement(null, stack);
    }

    public T master()
    {
        assert (!(offset[0]==0&&offset[1]==0&&offset[2]==0)); // THIS SHOULD NOT HAPPEN
        BlockPos masterPos = getPos().add(-offset[0],-offset[1],-offset[2]);
        TileEntity te = Utils.getExistingTileEntity(world, masterPos);
        return (T)te;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        formed = nbt.getBoolean("formed");
        pos = nbt.getInteger("pos");
        offset = nbt.getIntArray("offset");
        mirrored = nbt.getBoolean("mirrored");
        facing = EnumFacing.getFront(nbt.getInteger("facing"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("formed", formed);
        nbt.setInteger("pos", pos);
        nbt.setIntArray("offset", offset);
        nbt.setBoolean("mirrored", mirrored);
        nbt.setInteger("facing", facing.ordinal());
        return super.writeToNBT(nbt);
    }

}
