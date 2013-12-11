package kegare.frozenland.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureVillagePieceWeight;

import com.google.common.collect.Lists;

public class ComponentVillageStartPiece extends ComponentVillageWell
{
	protected WorldChunkManager worldChunkMgr;
	protected BiomeGenBase biome;
	protected int size;

	protected StructureVillagePieceWeight villagePieceWeight;

	protected List villageWeightedPieceList;
	protected final List villageStructureList = Lists.newArrayList();
	protected final List villageComponentList = Lists.newArrayList();

	public ComponentVillageStartPiece() {}

	protected ComponentVillageStartPiece(WorldChunkManager worldChunkManager, Random random, int x, int z, List list, int size)
	{
		super((ComponentVillageStartPiece)null, 0, random, x, z);
		this.worldChunkMgr = worldChunkManager;
		this.biome = worldChunkManager.getBiomeGenAt(x, z);
		this.size = size;
		this.villageWeightedPieceList = list;
	}
}