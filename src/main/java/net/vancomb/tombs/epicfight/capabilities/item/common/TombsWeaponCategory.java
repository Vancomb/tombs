package net.vancomb.tombs.epicfight.capabilities.item.common;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum TombsWeaponCategory implements WeaponCategory {
	USP9;
	
	final int id;
	
	TombsWeaponCategory() {
		this.id = WeaponCategory.ENUM_MANAGER.assign(this);
	}
	
	@Override
	public int universalOrdinal() {
		return this.id;
	}
}
