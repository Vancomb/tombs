package net.vancomb.tombs.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vancomb.tombs.Tombs;

public abstract class TombsSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Tombs.MODID);
	
    public static final RegistryObject<SoundEvent> TOMBRAIDER_OST = registerSoundEvents("tombraider_ost");
    
    public static final RegistryObject<SoundEvent> VENICE_VIOLINS = registerSoundEvents("venice_violins");
    
    public static final RegistryObject<SoundEvent> PISTOL_SHOT = registerSoundEvents("pistol_shot");
    
    public static final RegistryObject<SoundEvent> PISTOL_EMPTY = registerSoundEvents("pistol_empty");
    
    public static final RegistryObject<SoundEvent> PISTOL_RELOAD = registerSoundEvents("pistol_reload");
    
    public static final RegistryObject<SoundEvent> EXCALIBUR_SWING = registerSoundEvents("excalibur_swing");
    
    //Helper Method
    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Tombs.MODID, name)));
    }
    
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
