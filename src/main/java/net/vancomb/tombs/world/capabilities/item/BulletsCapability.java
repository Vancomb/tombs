package net.vancomb.tombs.world.capabilities.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.registry.TombsItems;
import yesman.epicfight.main.EpicFightMod;

/**
 * A capability object that counts left bullets
 */
@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BulletsCapability {
	
	// Max bullets when reload
	private static final int MAX_BULLETS = 20;
	
	public static int getMaxBullets() {
		return MAX_BULLETS;
	}
	
	// A capability key class to get an instance of bullets capability from item stack
	public static final Capability<BulletsCapability> BULLETS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    
	// Register capability class
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(BulletsCapability.class);
	}
	
	// A helper method gets bullet capability from itemstack
	public static LazyOptional<BulletsCapability> getBullets(ItemStack itemstack) {
		return itemstack.getCapability(BULLETS_CAPABILITY);
	}
	
	private int bulletsLeft;
	
	public BulletsCapability(int maxBullets) {
		this.bulletsLeft = maxBullets;
	}
	
	public int getBulletsLeft() {
		return this.bulletsLeft;
	}
	
	public boolean canShot() {
		return this.bulletsLeft > 0;
	}
	
	public void setBulletsLeft(int bullets) {
		this.bulletsLeft = bullets;
	}
	
	public void consume() {
		--this.bulletsLeft;
	}
	
	/**
	 * A capability provider class, implements {@link INBTSerializable} to save the left bullet count
	 */
	public static class Provider implements ICapabilityProvider, INBTSerializable<IntTag> {
		BulletsCapability bulletsCapability = new BulletsCapability(MAX_BULLETS);
		LazyOptional<BulletsCapability> lazyOptional = LazyOptional.of(() -> this.bulletsCapability);

		/**
		 * Return a bullet capability if the given key is {@link BulletsCapability#BULLETS_CAPABILITY}}
		 */
		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capabilityKey, @Nullable Direction side) {
			return capabilityKey == BulletsCapability.BULLETS_CAPABILITY ? this.lazyOptional.cast() : LazyOptional.empty();
		}
		
		/**
		 * Serialize to Tag to save data
		 */
		@Override
		public IntTag serializeNBT() {
			return IntTag.valueOf(this.bulletsCapability.getBulletsLeft());
		}
		
		/**
		 * Read data from Tag
		 */
		@Override
		public void deserializeNBT(IntTag nbt) {
			this.bulletsCapability.setBulletsLeft(nbt.getAsInt());
		}
	}
	
	/**
	 * An inner class to register capability attach event
	 */
	@Mod.EventBusSubscriber(modid = Tombs.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class Event {
		// Capability key
		private static final ResourceLocation BULLETS = ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "bullets");
		
		@SubscribeEvent
		public static void attachItemCapabilityEvent(AttachCapabilitiesEvent<ItemStack> event) {
			
			/**
			 * Attach the bullet capability if ItemStack holds {@link USP9PistolItem}
			 */
			if (TombsItems.HK_USP9_PISTOL.get().equals(event.getObject().getItem()) && !event.getCapabilities().containsKey(BULLETS)) {
				event.addCapability(BULLETS, new BulletsCapability.Provider());
			}
		}
	}
}
