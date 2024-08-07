package net.abrasminceraft.modding.abrasadditions.enchantment;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AbrasAdditions.MODID);
    public static RegistryObject<Enchantment> HARVEST =
            ENCHANTMENTS.register("harvest", () -> new EnchantmentHarvest(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));
    public static void register(IEventBus bus){
        ENCHANTMENTS.register(bus);
    }
}
