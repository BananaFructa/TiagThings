package BananaFructa.TTIEMultiblocks.IECopy;

//import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.*;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockTTMultiblock<E extends Enum<E> & BlockTTBase.IBlockEnum> extends BlockTTTileProvider<E> {

    public BlockTTMultiblock(String name, Material material, PropertyEnum<E> mainProperty, PropertyInteger animProperty, Class<? extends ItemBlockTTBase> itemBlock, Object... additionalProperties)
    {
        super(name, material, mainProperty,animProperty, itemBlock, combineProperties(additionalProperties, IEProperties.FACING_HORIZONTAL,IEProperties.MULTIBLOCKSLAVE));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        state = super.getActualState(state, world, pos);
        return state;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntityMultiblockPart && world.getGameRules().getBoolean("doTileDrops"))
        {
            TileEntityMultiblockPart tile = (TileEntityMultiblockPart)tileEntity;
            if(!tile.formed && tile.field_174879_c==-1 && !tile.getOriginalBlock().isEmpty())
                world.spawnEntity(new EntityItem(world, pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5, tile.getOriginalBlock().copy()));

            if(tile.formed && tile instanceof IIEInventory)
            {
                IIEInventory master = (IIEInventory)tile.master();
                if(master!=null && (!(master instanceof IEBlockInterfaces.ITileDrop) || !((IEBlockInterfaces.ITileDrop)master).preventInventoryDrop()) && master.getDroppedItems()!=null)
                    for(ItemStack s : master.getDroppedItems())
                        if(!s.isEmpty())
                            world.spawnEntity(new EntityItem(world, pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5, s.copy()));
            }
        }
        if(tileEntity instanceof TileEntityMultiblockPart)
            ((TileEntityMultiblockPart)tileEntity).disassemble();
        // ADDED
        /*else if (tileEntity instanceof TileEntityAE2CompatMultiblock) {
            ((TileEntityAE2CompatMultiblock)tileEntity).disassemble();
        }*/
        // =====
        super.breakBlock(world, pos, state);
    }
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityMultiblockPart<?>))
            super.getDrops(drops, world, pos, state, fortune);
    }
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        ItemStack stack = getOriginalBlock(world, pos);
        if(!stack.isEmpty())
            return stack;
        return super.getPickBlock(state, target, world, pos, player);
    }
    public ItemStack getOriginalBlock(World world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityMultiblockPart)
            return ((TileEntityMultiblockPart)te).getOriginalBlock();
        return ItemStack.EMPTY;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollidedWithBlock(world, pos, state, entity);
    }
}
