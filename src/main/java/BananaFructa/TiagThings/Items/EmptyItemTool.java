package BananaFructa.TiagThings.Items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;

/** This is to force Immersive Engineering Assemblers to Work */
public class EmptyItemTool extends ItemTool {

    public EmptyItemTool(String registryName,int maxStack, int maxDmg) {
        super(0,1,ToolMaterial.IRON, Collections.emptySet());
        this.setMaxDamage(maxDmg);
        this.maxStackSize = maxStack;
        this.setCreativeTab(CreativeTabs.MISC);
        setUnlocalizedName(registryName);
        setRegistryName(registryName);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
