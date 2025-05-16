package net.vancomb.tombs.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.world.item.ExcaliburItem;
import net.vancomb.tombs.world.item.HKUSP9PistolItem;
import net.vancomb.tombs.world.item.SurvivorPickaxeItem;
import net.vancomb.tombs.world.item.VintageSpecsItem;

public abstract class TombsItems {
	// To register modded items, create a deferred reigster with mod id
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Tombs.MODID);
	
	// Register new items inside supplier to get an actual item object later (this is the reason why it's called deferred register)
	
	// WEAPONS
	public static final RegistryObject<Item> HK_USP9_PISTOL = ITEMS.register("hk_usp9_pistol", () ->
		new HKUSP9PistolItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC))
	);
	
	public static final RegistryObject<Item> SURVIVOR_PICKAXE = ITEMS.register("survivor_pickaxe", () ->
		new SurvivorPickaxeItem(Tiers.IRON, new Item.Properties().rarity(Rarity.EPIC))
	);
	
	public static final RegistryObject<Item> EXCALIBUR = ITEMS.register("excalibur", () ->
		new ExcaliburItem(Tiers.NETHERITE, new Item.Properties().rarity(Rarity.EPIC))
	);
	
	public static final RegistryObject<Item> VINTAGE_SPECTACLES = ITEMS.register("vintage_spectacles", () ->
		new VintageSpecsItem(new Item.Properties())
	);
}
