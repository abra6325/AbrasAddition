package net.abrasminceraft.modding.abrasadditions.events;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.abrasminceraft.modding.abrasadditions.constants.Store;
import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.packets.HandshakePacket;
import net.abrasminceraft.modding.abrasadditions.packets.ModCheckPacket;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.resource.ResourcePackLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventsGeneral {
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent e){
        if(!e.getEntity().level().isClientSide){
            Logger.log("detected playter log in");


            Player tmp1 = (Player) e.getEntity();

            PacketInit.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> e.getEntity().level().getChunkAt(e.getEntity().blockPosition())),new HandshakePacket("Hi, handshake done!"));
            System.out.println("SERVER SIDE "+System.getProperty("user.dir"));
            Set<String> mods = new HashSet<>();
            for(IModInfo i: ModList.get().getMods()){
                mods.add(i.getModId());
            }
            mods.addAll(Store.allowedModIDs);
            List<String> mods2 = new ArrayList<>(mods);
            PacketInit.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> e.getEntity().level().getChunkAt(e.getEntity().blockPosition())),
                    new ModCheckPacket(mods2));

        }

    }


}
