/*
 * Frozenland
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.frozenland.item;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.kegare.frozenland.api.IItemIceTool;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIcePickaxe extends ItemPickaxe implements IItemIceTool
{
	private final Random random = new Random();

	public ItemIcePickaxe(String name)
	{
		super(FrozenItems.ICE);
		this.setUnlocalizedName(name);
		this.setTextureName("frozenland:ice_pickaxe");
	}

	@Override
	public int getGrade(ItemStack itemstack)
	{
		if (itemstack == null || itemstack.getItem() != this || itemstack.getTagCompound() == null)
		{
			return 0;
		}

		return itemstack.getTagCompound().getInteger("Grade");
	}

	@Override
	public void setGrade(ItemStack itemstack, int grade)
	{
		if (itemstack == null || itemstack.getItem() != this)
		{
			return;
		}

		if (itemstack.getTagCompound() == null)
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}

		itemstack.getTagCompound().setInteger("Grade", grade);
	}

	@Override
	public void addGrade(ItemStack itemstack, int amount)
	{
		setGrade(itemstack, getGrade(itemstack) + amount);
	}

	@Override
	public int getMaxDamage(ItemStack itemstack)
	{
		int max = super.getMaxDamage(itemstack);

		return max + max / 2 * getGrade(itemstack);
	}

	@Override
	public int getHarvestLevel(ItemStack itemstack, String toolClass)
	{
		int level = super.getHarvestLevel(itemstack, toolClass);
		int grade = getGrade(itemstack);

		if (grade >= 30)
		{
			++level;
		}

		if (grade >= 300)
		{
			++level;
		}

		return level;
	}

	@Override
	public boolean func_150897_b(Block block)
	{
		return block.getMaterial() == Material.ice || super.func_150897_b(block);
	}

	@Override
	public float func_150893_a(ItemStack itemstack, Block block)
	{
		return block.getMaterial() == Material.ice ? efficiencyOnProperMaterial : super.func_150893_a(itemstack, block);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
	{
		if (!world.isRemote && (block == Blocks.ice || block == Blocks.packed_ice))
		{
			world.setBlockToAir(x, y, z);

			if (EnchantmentHelper.getSilkTouchModifier(entity) || random.nextInt(3) == 0)
			{
				EntityItem item = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, new ItemStack(block));
				item.delayBeforeCanPickup = 10;

				world.spawnEntityInWorld(item);
			}
		}

		return super.onBlockDestroyed(itemstack, world, block, x, y, z, entity);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity)
	{
		World world = entity.worldObj;

		if (!world.isRemote && entity.onGround && entity.ticksExisted % 120 == 0)
		{
			ItemStack itemstack = entity.getEntityItem();
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY) - 1;
			int z = MathHelper.floor_double(entity.posZ);
			BiomeGenBase biome = world.getBiomeGenForCoords(x, z);

			if (BiomeDictionary.isBiomeOfType(biome, Type.HOT))
			{
				if (itemstack.attemptDamageItem(1, random) && --itemstack.stackSize <= 0)
				{
					entity.setEntityItemStack(null);
					entity.setDead();

					return false;
				}
			}
			else if (itemstack.isItemDamaged() && BiomeDictionary.isBiomeOfType(biome, Type.COLD) && world.getBlock(x, y, z).getMaterial() == Material.ice)
			{
				itemstack.attemptDamageItem(-1, random);
			}
		}

		return super.onEntityItemUpdate(entity);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean advanced)
	{
		if (advanced || GuiConfig.isShiftKeyDown())
		{
			int grade = getGrade(itemstack);

			if (grade > 0 || GuiConfig.isShiftKeyDown())
			{
				list.add(I18n.format("item.toolIce.upgraded") + ": " + grade);
			}
		}
	}
}