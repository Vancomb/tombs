package net.vancomb.tombs.world.item;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

public class HKUSP9PistolItem extends ProjectileWeaponItem {
	public static final Predicate<ItemStack> NO_ITMES = (p_43017_) -> {
		return false;
	};
	
	public HKUSP9PistolItem(Properties pProperties) {
		super(pProperties);
	}
	
	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return NO_ITMES;
	}
	
	@Override
	public int getDefaultProjectileRange() {
		return 25;
	}
}