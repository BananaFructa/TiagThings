package BananaFructa.FLModifications;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityElectricLantern;
import com.eerussianguy.firmalife.blocks.BlockClimateStation;
import com.eerussianguy.firmalife.init.StatePropertiesFL;
import com.eerussianguy.firmalife.util.GreenhouseHelpers;
import com.eerussianguy.firmalife.util.HelpersFL;
import net.dries007.tfc.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class BlockClimateStationM extends BlockClimateStation {

    public BlockClimateStationM(int tier) {
        super(tier);
    }

    @Override
    public void func_180645_a(World world, BlockPos pos, IBlockState state, Random random) {
        TileEntityElectricLantern lantern = Helpers.getTE(world,pos.up(),TileEntityElectricLantern.class);
        world.setBlockState(pos, state.withProperty(StatePropertiesFL.STASIS, GreenhouseHelpers.isMultiblockValid(world, pos, state, false, this.tier) && lantern != null && lantern.active));
    }

    @Override
    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
            TileEntityElectricLantern lantern = Helpers.getTE(world,pos.up(),TileEntityElectricLantern.class);
            boolean visual = this.tier > 0;
            boolean greenHouseValid = GreenhouseHelpers.isMultiblockValid(world, pos, state, visual, this.tier);
            boolean lanternValid = lantern != null && lantern.active;
            boolean valid = greenHouseValid && lanternValid;
            world.setBlockState(pos, state.withProperty(StatePropertiesFL.STASIS, valid));
            if (!valid || !visual) {
                if (valid) {
                    player.sendMessage(new TextComponentTranslation("tooltip.firmalife.valid"));
                } else if (!greenHouseValid) {
                    player.sendMessage(new TextComponentTranslation("tooltip.firmalife.invalid"));
                } else if (lantern == null) {
                    HelpersFL.sendBoundingBoxPacket(world, pos.up(), pos.up(), 1.0F, 0.0F, 0.0F, true);
                    player.sendMessage(new TextComponentTranslation("Missing powered lantern"));
                } else if (!lantern.active) {
                    HelpersFL.sendBoundingBoxPacket(world, pos.up(), pos.up(), 1.0F, 0.0F, 0.0F, true);
                    player.sendMessage(new TextComponentTranslation("Lantern not powered up"));
                }
            }
        }

        return true;
    }
}
