package BananaFructa.RailcraftModifications;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IChargeBlock;
import mods.railcraft.common.blocks.BlockMeta;
import mods.railcraft.common.blocks.multi.BlockStructure;
import mods.railcraft.common.blocks.multi.TileRockCrusher;
import mods.railcraft.common.items.ItemCharge;
import mods.railcraft.common.items.RailcraftItems;
import mods.railcraft.common.plugins.forge.CraftingPlugin;
import mods.railcraft.common.util.property.PropertyCharacter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.Random;

@BlockMeta.Tile(RFTileBlockCrusher.class)
public class RFBlockRockCrusher extends BlockStructure<TileRockCrusher> implements IChargeBlock {
        public static final IProperty<Character> ICON = PropertyCharacter.create("icon", new char[]{'O', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'B', 'D'});
        private static final Map<Charge, ChargeSpec> CHARGE_SPECS;

        public RFBlockRockCrusher() {
                super(Material.IRON);
                this.setSoundType(SoundType.METAL);
                this.setDefaultState(this.getDefaultState().withProperty(ICON, 'O'));
                this.setHarvestLevel("pickaxe", 1);
                this.setTickRandomly(true);
        }

        public Map<Charge, IChargeBlock.ChargeSpec> getChargeSpecs(IBlockState state, IBlockAccess world, BlockPos pos) {
                return CHARGE_SPECS;
        }

        protected BlockStateContainer createBlockState() {
                return new BlockStateContainer(this, new IProperty[]{ICON});
        }

        public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
                super.updateTick(worldIn, pos, state, rand);
                this.registerNode(state, worldIn, pos);
                //this.getTileEntity(state,worldIn,pos);
        }

        public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
                super.func_176213_c(worldIn, pos, state);
                this.registerNode(state, worldIn, pos);
        }

        public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
                super.func_180663_b(worldIn, pos, state);
                this.deregisterNode(worldIn, pos);
        }

        @SideOnly(Side.CLIENT)
        public Tuple<Integer, Integer> getTextureDimensions() {
                return new Tuple(4, 3);
        }

        public void defineRecipes() {
                ItemStack stack = new ItemStack(this, 4);
                CraftingPlugin.addShapedRecipe(stack, new Object[]{"DPD", "PSP", "DMD", 'D', "gemDiamond", 'P', new ItemStack(Blocks.PISTON), 'M', RailcraftItems.CHARGE, ItemCharge.EnumCharge.MOTOR, 'S', "blockSteel"});
        }

        static {
                CHARGE_SPECS = ChargeSpec.make(Charge.distribution, ConnectType.BLOCK, 0.025);
        }
}
