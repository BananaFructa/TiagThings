package BananaFructa.Galacticraft;

import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class MapGenDungeonEmpty extends MapGenDungeon {


    public MapGenDungeonEmpty() {
        super(null);
    }

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {

    }

    @Override
    public synchronized boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
        return false;
    }
}
