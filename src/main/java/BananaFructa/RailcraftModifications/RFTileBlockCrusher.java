package BananaFructa.RailcraftModifications;

import BananaFructa.TiagThings.Utils;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import mods.railcraft.api.core.IVariantEnum;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.TileCrafter;
import mods.railcraft.common.blocks.logic.Logic;
import mods.railcraft.common.blocks.logic.RockCrusherLogic;
import mods.railcraft.common.blocks.logic.StructureLogic;
import mods.railcraft.common.blocks.multi.BlockRockCrusher;
import mods.railcraft.common.blocks.multi.MultiBlockPattern;
import mods.railcraft.common.blocks.multi.TileRockCrusher;
import mods.railcraft.common.gui.EnumGui;
import mods.railcraft.common.plugins.forge.WorldPlugin;
import mods.railcraft.common.util.entity.EntitySearcher;
import mods.railcraft.common.util.entity.RCEntitySelectors;
import mods.railcraft.common.util.entity.RailcraftDamageSource;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


// implemented RF
// decreased consumption by 5 times

public class RFTileBlockCrusher extends TileCrafter implements IEnergyStorage {

    private static final List<MultiBlockPattern> patterns = new ArrayList();

    public RFTileBlockCrusher() {
        this.setLogic(new StructureLogic("rock_crusher", this, patterns, new RFRockCrusherLogic(Logic.Adapter.of(this))) {
            public boolean isMapPositionValid(BlockPos pos, char mapPos) {
                IBlockState self = RFTileBlockCrusher.this.getBlockState();
                IBlockState other = WorldPlugin.getBlockState(RFTileBlockCrusher.this.world, pos);
                switch (mapPos) {
                    case 'A':
                        if (other.getBlock().isAir(other, RFTileBlockCrusher.this.world, pos)) {
                            return true;
                        }
                        break;
                    case 'B':
                    case 'D':
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                    case 'g':
                    case 'h':
                        if (self == other) {
                            return true;
                        }
                    case 'C':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '[':
                    case '\\':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
                    default:
                        break;
                    case 'O':
                        if (self != other) {
                            return true;
                        }
                }

                return false;
            }
        });
        initReflection();
    }

    public static void placeRockCrusher(World world, BlockPos pos, int patternIndex, @Nullable List<ItemStack> input, @Nullable List<ItemStack> output) {
        MultiBlockPattern pattern = (MultiBlockPattern)patterns.get(patternIndex);
        Char2ObjectMap<IBlockState> blockMapping = new Char2ObjectOpenHashMap();
        IBlockState state = RailcraftBlocks.ROCK_CRUSHER.getState((IVariantEnum)null);
        blockMapping.put('B', state);
        blockMapping.put('D', state);
        blockMapping.put('a', state);
        blockMapping.put('b', state);
        blockMapping.put('c', state);
        blockMapping.put('d', state);
        blockMapping.put('e', state);
        blockMapping.put('f', state);
        blockMapping.put('h', state);
        TileEntity tile = pattern.placeStructure(world, pos, blockMapping);
        if (tile instanceof TileRockCrusher) {
            TileRockCrusher var9 = (TileRockCrusher)tile;
        }

    }

    public void update() {
        super.func_73660_a();
        if (Game.isHost(this.getWorld()) && this.clock(8)) {
            this.getLogic(RFRockCrusherLogic.class).ifPresent((l) -> {
                BlockPos pos = this.getPos();
                BlockPos target = pos.up();
                EntitySearcher.find(EntityItem.class).around(target).in(this.world).forEach((item) -> {
                    if (l.useInternalCharge(200.0)) {
                        ItemStack stack = item.getItem().copy();
                        l.invInput.addStack(stack);
                        item.setDead();
                    }

                });
                EntitySearcher.findLiving().around(target).and(new Predicate[]{RCEntitySelectors.KILLABLE}).in(this.world).forEach((e) -> {
                    if ((l.hasInternalCapacity(1000.0) && ((Entity)e).attackEntityFrom(RailcraftDamageSource.CRUSHER, 5.0F))) {
                        if (l.hasInternalCapacity(1000.0)) l.useInternalCharge(1000);
                    }

                });
            });
        }

    }

    public EnumGui getGui() {
        return EnumGui.ROCK_CRUSHER;
    }

    public IBlockState getActualState(IBlockState base) {
        return (IBlockState)this.getLogic(StructureLogic.class).map((l) -> {
            return base.withProperty(BlockRockCrusher.ICON, l.getPatternMarker());
        }).orElse(base);
    }

    static {
        char[][][] map1 = new char[][][]{{{'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O', 'O'}, {'O', 'B', 'D', 'B', 'O'}, {'O', 'B', 'D', 'B', 'O'}, {'O', 'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O', 'O'}, {'O', 'a', 'd', 'f', 'O'}, {'O', 'c', 'e', 'h', 'O'}, {'O', 'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O'}}};
        patterns.add(new MultiBlockPattern(map1));
        char[][][] map2 = new char[][][]{{{'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O'}, {'O', 'B', 'B', 'O'}, {'O', 'D', 'D', 'O'}, {'O', 'B', 'B', 'O'}, {'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O'}, {'O', 'a', 'f', 'O'}, {'O', 'b', 'g', 'O'}, {'O', 'c', 'h', 'O'}, {'O', 'O', 'O', 'O'}}, {{'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O'}}};
        patterns.add(new MultiBlockPattern(map2));
    }

    // ===========================================

    Field storedCharge;

    void initReflection() {
        try {
            storedCharge = RockCrusherLogic.class.getDeclaredField("storedCharge");
            storedCharge.setAccessible(true);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private double getStoredCharge() {
        try {
            return (double) storedCharge.get(getLogic(RFRockCrusherLogic.class).get());
        } catch (Exception err) {
            err.printStackTrace();
            return 0;
        }
    }

    public void setStoredCharge(double charge) {
        try {
            storedCharge.set(getLogic(RFRockCrusherLogic.class).get(),charge);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.UP) return true;
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing != EnumFacing.UP) return (T)this;
        return super.getCapability(capability, facing);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        if (!getLogic(RFRockCrusherLogic.class).isPresent()) return 0;

        double charge = getStoredCharge();

        //System.out.println(charge);

        if (charge >= 7999) {
            return 0;
        }
        if (charge + maxReceive * 0.25 <= 7999) {
            if (!simulate) setStoredCharge(charge + maxReceive * 0.25);
            return maxReceive;
        } else {
            if (!simulate) setStoredCharge(8000);
            return (int)((8000 - charge) * 4);
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return (int)(getStoredCharge() * 4);
    }

    @Override
    public int getMaxEnergyStored() {
        return 8000 * 4;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
