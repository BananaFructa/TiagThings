package BananaFructa.Galacticraft;

import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.DungeonConfigurationVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.MapGenDungeonVenus;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class MapGenDungeonEmptyVenus extends MapGenDungeonVenus {
    public MapGenDungeonEmptyVenus() {
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
