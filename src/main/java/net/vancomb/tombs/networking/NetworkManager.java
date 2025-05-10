package net.vancomb.tombs.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.vancomb.tombs.Tombs;

public class NetworkManager {
	private static final String PROTOCOL_VERSION = "1";
	
	// Define a communication channel for the mod
	public static final SimpleChannel INSTANCE =
		NetworkRegistry.newSimpleChannel(
			ResourceLocation.fromNamespaceAndPath(Tombs.MODID, "network_manager"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		);
	
	// Register client-server communication packet
	public static void registerPackets() {
		int packetId = 0;
		
		INSTANCE.registerMessage(packetId++, ServerBoundReloadPacket.class, ServerBoundReloadPacket::toBytes, ServerBoundReloadPacket::fromBytes, ServerBoundReloadPacket::handle);
	}
}
