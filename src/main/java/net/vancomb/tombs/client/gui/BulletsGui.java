package net.vancomb.tombs.client.gui;

import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.vancomb.tombs.Tombs;
import net.vancomb.tombs.registry.TombsItems;
import net.vancomb.tombs.world.capabilities.BulletsCapability;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Tombs.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class BulletsGui implements IGuiOverlay {
	@SubscribeEvent
	public static void registerOverlaysEvent(RegisterGuiOverlaysEvent event) {
		event.registerAbove(ResourceLocation.withDefaultNamespace("player_health"), "bullets", new BulletsGui());
	}
	
	private static final int UI_X_POSITION = 20;
	private static final int UI_Y_POSITION = 20;
	private static final int FONT_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
	private static final int BACKGROUND_COLOR = FastColor.ARGB32.color(100, 0, 0, 0);
	
	private void renderBulletsGui(GuiGraphics guiGraphics, Minecraft minecraft, Player player, BulletsCapability bullets) {
		minecraft.getProfiler().push("bullets");
		
		int pistols = 0;
		if (player.getMainHandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
		if (player.getOffhandItem().is(TombsItems.HK_USP9_PISTOL.get())) pistols++;
		
		String remainBullet = String.format("%d / %d", bullets.getRemainBullets(), BulletsCapability.getMaxBullets() * pistols);
		guiGraphics.drawString(minecraft.font, remainBullet, UI_X_POSITION + 10, UI_Y_POSITION + 11, FONT_COLOR);
		
		if (pistols == 1) {
			guiGraphics.pose().pushPose();
			guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(30.0F));
			guiGraphics.renderItem(TombsItems.HK_USP9_PISTOL.get().getDefaultInstance(), UI_X_POSITION + 60, UI_Y_POSITION - 40);
			guiGraphics.pose().popPose();
		} else if (pistols == 2) {
			guiGraphics.pose().pushPose();
			guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(30.0F));
			guiGraphics.renderItem(TombsItems.HK_USP9_PISTOL.get().getDefaultInstance(), UI_X_POSITION + 60, UI_Y_POSITION - 40);
			guiGraphics.renderItem(TombsItems.HK_USP9_PISTOL.get().getDefaultInstance(), UI_X_POSITION + 65, UI_Y_POSITION - 40);
			guiGraphics.pose().popPose();
		}
		
		guiGraphics.fill(UI_X_POSITION, UI_Y_POSITION, UI_X_POSITION + 84, UI_Y_POSITION + 30, BACKGROUND_COLOR);
		
		minecraft.getProfiler().pop();
	}
	
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
		if (!gui.getMinecraft().options.hideGui) {
			gui.setupOverlayRenderState(true, false);
			
			Minecraft minecraft = gui.getMinecraft();
			Player player = (Player)minecraft.getCameraEntity();
			
			BulletsCapability.getBullets(player).ifPresent(bullets -> {
				if (bullets.isHoldingCompatibleWeapon(player)) {
					this.renderBulletsGui(guiGraphics, minecraft, player, bullets);
				}
			});
		}
	}
}
