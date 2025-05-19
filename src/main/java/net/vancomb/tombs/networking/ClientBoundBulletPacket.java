package net.vancomb.tombs.networking;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.vancomb.tombs.world.capabilities.BulletsCapability;

public class ClientBoundBulletPacket {
	private final int remainBullet;
	
	// Define a default class for handshake
	public ClientBoundBulletPacket() {
		this.remainBullet = -1;
	}
	
	// Initialize packet with remaining bullets
	public ClientBoundBulletPacket(int remainBullets) {
		this.remainBullet = remainBullets;
	}
	
	// Decode packet
	public static ClientBoundBulletPacket fromBytes(FriendlyByteBuf buf) {
		return new ClientBoundBulletPacket(buf.readInt());
	}
	
	// Encode packet
	public static void toBytes(ClientBoundBulletPacket msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.remainBullet);
	}
	
	public static void handle(ClientBoundBulletPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			BulletsCapability.getBullets(ctx.get().getSender()).ifPresent(bulletsCapability -> {
				bulletsCapability.setRemainBullet(msg.remainBullet);
			});
		});
		ctx.get().setPacketHandled(true);
	}
}
