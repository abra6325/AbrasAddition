package net.abrasminceraft.modding.abrasadditions;

import com.mojang.logging.LogUtils;
import net.abrasminceraft.modding.abrasadditions.enchantment.ModEnchantments;
import net.abrasminceraft.modding.abrasadditions.events.EventImbueHarvest;

import net.abrasminceraft.modding.abrasadditions.events.EventPlayerWakeUp;
import net.abrasminceraft.modding.abrasadditions.events.EventsGeneral;
import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.item.ModItems;
import net.abrasminceraft.modding.abrasadditions.utils.FileIOManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.resource.ResourcePackLoader;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AbrasAdditions.MODID)
public class AbrasAdditions {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "abrasadditions";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void log(String s){LOGGER.info(s);}
    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,AbrasAdditions.MODID
    );
    public static final RegistryObject<CreativeModeTab> ABRAS_TAB = TAB_REGISTER.register("abrastab",() -> CreativeModeTab.builder()
            .title(Component.nullToEmpty("Abra's Additions"))
            .icon(() -> new ItemStack(ModItems.GENSOUKYO_FRAG.get()))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.GENSOUKYO_FRAG.get());
                pOutput.accept(ModItems.TELEPORTATION_TICKET.get());
                pOutput.accept(ModItems.LIQUID_DIAMOND.get());
                pOutput.accept(ModItems.DIAMOND_DUST.get());
            })
            .build()
    );
    public AbrasAdditions() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AbrasAdditions.TAB_REGISTER.register(modEventBus);
        ModItems.register(modEventBus);
        ModEnchantments.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);


        //MY EVENTS
//        MinecraftForge.EVENT_BUS.register(new EventPlayerWakeUp());
        MinecraftForge.EVENT_BUS.register(new EventImbueHarvest());
        MinecraftForge.EVENT_BUS.register(new EventsGeneral());

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(PacketInit::init);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
//            event.accept(ModItems.GENSOUKYO_FRAG);
//        }
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        log(FileIOManager.getJSONFromFile("abrasconfigs.json").toString());

    }

}
