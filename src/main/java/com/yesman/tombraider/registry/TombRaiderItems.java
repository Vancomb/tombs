package com.yesman.tombraider.registry;

import com.yesman.tombraider.TombRaider;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TombRaiderItems {
	// To register modded items, create a deferred reigster with mod id
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TombRaider.MODID);
	
	// Register new item inside supplier to get an actual item object later (this is the reason why it's called deferred register)
	public static final RegistryObject<Item> HK_USP_PISTOL = ITEMS.register("hk_usp9_pistol", () ->
		new Item(new Item.Properties())
	);
}
