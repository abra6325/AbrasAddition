package net.abrasminceraft.modding.abrasadditions.events;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.utils.PackChecker;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AbrasAdditions.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value= Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onClientUpdate(TickEvent.ClientTickEvent e){
        List<Pack> packs =  Minecraft.getInstance().getResourcePackRepository().getSelectedPacks().stream().toList();
        if(!PackChecker.checkResourcePacks(packs)){
            Minecraft.crash(new CrashReport("fuck you, you are cheating with some random resource pack.",new Exception()));

        }
    }




}
