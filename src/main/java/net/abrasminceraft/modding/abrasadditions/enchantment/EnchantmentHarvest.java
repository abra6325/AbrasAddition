package net.abrasminceraft.modding.abrasadditions.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentHarvest extends Enchantment {
    protected EnchantmentHarvest(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }
    @Override
    public boolean isDiscoverable(){
        return false;
    }

}
