package net.vancomb.tombs.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.vancomb.tombs.Tombs;

import javax.annotation.Nullable;
import java.util.List;

public class ExcaliburItem extends SwordItem {
	private static final int DAMAGE_MODIFIER = 11;
	private static final float ATTACK_SPEED_MODIFIER = -2.85F;
	
    public ExcaliburItem(Tier tier, Properties properties) {
        super(tier, DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, properties);
    }
    
    // Append tooltip when hovers
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip." + Tombs.MODID + ".excalibur.tooltip"));
        
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}