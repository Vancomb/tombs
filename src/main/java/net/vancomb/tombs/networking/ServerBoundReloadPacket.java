package net.vancomb.tombs.networking;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.vancomb.tombs.world.capabilities.item.BulletsCapability;

public class ServerBoundReloadPacket {
	private final int entityId;
	
	// Define a default class for handshake
	public ServerBoundReloadPacket() {
		this.entityId = -1;
	}
	
	// Initialize packet with player entity's id
	public ServerBoundReloadPacket(int entityId) {
		this.entityId = entityId;
	}
	
	// Decode packet
	public static ServerBoundReloadPacket fromBytes(FriendlyByteBuf buf) {
		return new ServerBoundReloadPacket(buf.readInt());
	}
	
	// Encode packet
	public static void toBytes(ServerBoundReloadPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.entityId);
	}
	
	public static void handle(ServerBoundReloadPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			// Get packet send player
			Player player = ctx.get().getSender();
			
			// Reload pistols in each hand
			player.getCapability(BulletsCapability.BULLETS_CAPABILITY).ifPresent(bulletsCapability -> {
				bulletsCapability.bulletReload(player);
			});
		});
		
		ctx.get().setPacketHandled(true);
	}
}
