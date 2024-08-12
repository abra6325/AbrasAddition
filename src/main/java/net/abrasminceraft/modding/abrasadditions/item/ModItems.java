package net.abrasminceraft.modding.abrasadditions.item;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, AbrasAdditions.MODID
    );
    public static final RegistryObject<Item> GENSOUKYO_FRAG = ITEMS.register("gensoukyo_fragment",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
                    .fireResistant()));
    public static final RegistryObject<Item> TELEPORTATION_TICKET = ITEMS.register("teleport_ticket",
            () -> new Item(new Item.Properties()
                    .stacksTo(16)
                    ));
    public static final RegistryObject<Item> LIQUID_DIAMOND = ITEMS.register("liquid_diamond",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
                    .fireResistant()));
    public static final RegistryObject<Item> DIAMOND_DUST = ITEMS.register("diamond_dust",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);

    }

}
