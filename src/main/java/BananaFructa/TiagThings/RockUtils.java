package BananaFructa.TiagThings;

import jdk.nashorn.internal.ir.Block;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
import net.dries007.tfc.world.classic.worldgen.vein.Vein;
import net.dries007.tfc.world.classic.worldgen.vein.VeinRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import tfcflorae.objects.items.ItemSack;

import java.util.*;

public class RockUtils {

    public static String[] rockItemsStrings = new String[] {
            "<tfc:rock/catlinite>",
            "<tfc:rock/novaculite>",
            "<tfc:rock/soapstone>",
            "<tfc:rock/komatiite>",
            "<tfc:rock/granite>",
            "<tfc:rock/diorite>",
            "<tfc:rock/gabbro>",
            "<tfc:rock/shale>",
            "<tfc:rock/claystone>",
            "<tfc:rock/rocksalt>",
            "<tfc:rock/limestone>",
            "<tfc:rock/conglomerate>",
            "<tfc:rock/dolomite>",
            "<tfc:rock/chert>",
            "<tfc:rock/chalk>",
            "<tfc:rock/rhyolite>",
            "<tfc:rock/basalt>",
            "<tfc:rock/andesite>",
            "<tfc:rock/dacite>",
            "<tfc:rock/quartzite>",
            "<tfc:rock/slate>",
            "<tfc:rock/phyllite>",
            "<tfc:rock/schist>",
            "<tfc:rock/gneiss>",
            "<tfc:rock/marble>",
            "<tfc:rock/breccia>",
            "<tfc:rock/porphyry>",
            "<tfc:rock/peridotite>",
            "<tfc:rock/mudstone>",
            "<tfc:rock/sandstone>",
            "<tfc:rock/siltstone>",
            "<tiagthings:crushed_rock>"
    };

    public static List<ItemStack> rockStacks = new ArrayList<>();

    public static void init() {
        for (String rock : rockItemsStrings) {
            rockStacks.add(Utils.itemStackFromCTId(rock));
        }
    }

    public static boolean isRock(ItemStack stack) {
        for (ItemStack itemStack : rockStacks) {
            if (stack.getItem() == itemStack.getItem()) return true;
        }
        return false;
    }

    public static RockTraces getTrace(World world, BlockPos pos) {
        List<ChunkPos> chunks = new LinkedList();
        // script sampled from CommandFindVeins.class
        int radius = 10;
        int chunkX = world.getChunkFromBlockCoords(pos).x;
        int chunkZ = world.getChunkFromBlockCoords(pos).z;
        BlockPos.MutableBlockPos posm = new BlockPos.MutableBlockPos(0, 0, 0);

        for(int x = chunkX - radius; x <= chunkX + radius; ++x) {
            for(int z = chunkZ - radius; z <= chunkZ + radius; ++z) {
                posm.setPos(x * 16, 0, z * 16);
                if (world.isBlockLoaded(posm) || world.isChunkGeneratedAt(x, z)) {
                    chunks.add(new ChunkPos(posm));
                }
            }
        }

        Set<BlockPos> veinsFound = new HashSet();

        Vein v = null;
        double distnace = 0;

        for (ChunkPos cpos : chunks) {
            Chunk target = world.getChunkFromChunkCoords(cpos.x,cpos.z);
            ChunkDataTFC chunkData = ChunkDataTFC.get(target);
            for (Vein vs : chunkData.getGeneratedVeins()) {
                if (!veinsFound.contains(vs.getPos())) {
                    veinsFound.add(vs.getPos());
                    if (v == null) {
                        v = vs;
                        distnace = pos.distanceSq(vs.getPos());
                    } else {
                        if (distnace > pos.distanceSq(vs.getPos())) {
                            v = vs;
                            distnace = pos.distanceSq(vs.getPos());
                        }
                    }
                }
            }
        }

        for (RockTraces traces : RockTraces.values()) {
            for (String name : traces.veinNames) {
                if (name.equals(v.getType().getRegistryName())) return traces;
            }
        }
        return RockTraces.NONE;
    }

}
