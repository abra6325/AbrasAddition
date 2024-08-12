package net.abrasminceraft.modding.abrasadditions.events;

import net.abrasminceraft.modding.abrasadditions.utils.ClientSidedVariableStorage;
import net.abrasminceraft.modding.abrasadditions.utils.PackChecker;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;
import java.util.List;

import static net.abrasminceraft.modding.abrasadditions.AbrasAdditions.MODID;
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {

    }
    @SubscribeEvent
    public static void onClientReloadResources(RegisterClientReloadListenersEvent event){
        net.abrasminceraft.modding.abrasadditions.utils.Logger.log("hi, i have rleoad");
        event.registerReloadListener(new SimplePreparableReloadListener<Unit>() {
            @Override
            protected Unit prepare(ResourceManager resourceManager, ProfilerFiller filler){
                net.abrasminceraft.modding.abrasadditions.utils.Logger.log("------> PREPARE");
                return Unit.INSTANCE;
            }
            @Override
            protected void apply(Unit ignored, ResourceManager manager,ProfilerFiller filler){
                net.abrasminceraft.modding.abrasadditions.utils.Logger.log("APPLY");
                if(ClientSidedVariableStorage.connected) {
                    System.out.println(ClientSidedVariableStorage.allowedResourcePacks);
                    List<Pack> packs = Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().toList();
                    try {
                        if (!PackChecker.checkResourcePacks(packs)) {
                            Minecraft.crash(new CrashReport("fuck you, you are cheating with some random resource pack." + PackChecker.getResourcePacks(packs), new Exception()));

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
    }

}

