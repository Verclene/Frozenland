/*
 * Frozenland
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.frozenland.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import cpw.mods.fml.common.registry.GameData;

public class ComponentVillageField2 extends ComponentVillage
{
	private Block cropTypeA;
	private Block cropTypeB;

	public ComponentVillageField2() {}

	protected ComponentVillageField2(ComponentVillageStartPiece villageStartPiece, int type, Random random, StructureBoundingBox structureBoundingBox, int coordBaseMode)
	{
		super(villageStartPiece, type);
		this.coordBaseMode = coordBaseMode;
		this.boundingBox = structureBoundingBox;
		this.cropTypeA = pickRandomCrop(random);
		this.cropTypeB = pickRandomCrop(random);
	}

	@Override
	protected void func_143012_a(NBTTagCompound nbtTagCompound)
	{
		super.func_143012_a(nbtTagCompound);
		nbtTagCompound.setString("CA", GameData.getBlockRegistry().getNameForObject(cropTypeA));
		nbtTagCompound.setString("CB", GameData.getBlockRegistry().getNameForObject(cropTypeB));
	}

	@Override
	protected void func_143011_b(NBTTagCompound nbtTagCompound)
	{
		super.func_143011_b(nbtTagCompound);
		cropTypeA = GameData.getBlockRegistry().getObject(nbtTagCompound.getString("CA"));
		cropTypeB = GameData.getBlockRegistry().getObject(nbtTagCompound.getString("CB"));
	}

	private Block pickRandomCrop(Random random)
	{
		switch (random.nextInt(5))
		{
			case 1:
				return Blocks.carrots;
			default:
				return Blocks.potatoes;
		}
	}

	protected static ComponentVillageField2 findValidPlacement(ComponentVillageStartPiece villageStartPiece, List list, Random random, int x, int y, int z, int coordBaseMode, int type)
	{
		StructureBoundingBox structureBoundingBox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 4, 9, coordBaseMode);

		return canVillageGoDeeper(structureBoundingBox) && StructureComponent.findIntersecting(list, structureBoundingBox) == null ? new ComponentVillageField2(villageStartPiece, type, random, structureBoundingBox, coordBaseMode) : null;
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox)
	{
		if (averageGroundLevel < 0)
		{
			averageGroundLevel = getAverageGroundLevel(world, structureBoundingBox);

			if (averageGroundLevel < 0)
			{
				return true;
			}

			boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 4 - 1, 0);
		}

		fillWithBlocks(world, structureBoundingBox, 0, 1, 0, 6, 4, 8, Blocks.air, Blocks.air, false);
		fillWithBlocks(world, structureBoundingBox, 1, 0, 1, 2, 0, 7, Blocks.farmland, Blocks.farmland, false);
		fillWithBlocks(world, structureBoundingBox, 4, 0, 1, 5, 0, 7, Blocks.farmland, Blocks.farmland, false);
		fillWithMetadataBlocks(world, structureBoundingBox, 0, 0, 0, 0, 0, 8, Blocks.log, 1, Blocks.log, 1, false);
		fillWithMetadataBlocks(world, structureBoundingBox, 6, 0, 0, 6, 0, 8, Blocks.log, 1, Blocks.log, 1, false);
		fillWithMetadataBlocks(world, structureBoundingBox, 1, 0, 0, 5, 0, 0, Blocks.log, 1, Blocks.log, 1, false);
		fillWithMetadataBlocks(world, structureBoundingBox, 1, 0, 8, 5, 0, 8, Blocks.log, 1, Blocks.log, 1, false);
		fillWithBlocks(world, structureBoundingBox, 3, 0, 1, 3, 0, 7, Blocks.flowing_water, Blocks.flowing_water, false);
		int i;

		for (i = 1; i <= 7; ++i)
		{
			placeBlockAtCurrentPosition(world, cropTypeA, MathHelper.getRandomIntegerInRange(random, 2, 7), 1, 1, i, structureBoundingBox);
			placeBlockAtCurrentPosition(world, cropTypeA, MathHelper.getRandomIntegerInRange(random, 2, 7), 2, 1, i, structureBoundingBox);
			placeBlockAtCurrentPosition(world, cropTypeB, MathHelper.getRandomIntegerInRange(random, 2, 7), 4, 1, i, structureBoundingBox);
			placeBlockAtCurrentPosition(world, cropTypeB, MathHelper.getRandomIntegerInRange(random, 2, 7), 5, 1, i, structureBoundingBox);
		}

		for (i = 0; i < 9; ++i)
		{
			for (int j = 0; j < 7; ++j)
			{
				clearCurrentPositionBlocksUpwards(world, j, 4, i, structureBoundingBox);
				func_151554_b(world, Blocks.dirt, 0, j, -1, i, structureBoundingBox);
			}
		}

		return true;
	}
}