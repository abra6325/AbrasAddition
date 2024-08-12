package net.abrasminceraft.modding.abrasadditions.init;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketInit {
    private PacketInit(){

    }
    private static final String PROTOCOL_VERSOIN = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(AbrasAdditions.MODID,"main"),
            () -> PROTOCOL_VERSOIN,PROTOCOL_VERSOIN::equals,PROTOCOL_VERSOIN::equals);
    public static void init(){
        int index = 0;
        INSTANCE.messageBuilder(HandshakePacket.class,index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HandshakePacket::encode)
                .decoder(HandshakePacket::new)
                .consumerMainThread(HandshakePacket::handle)
                .add();
        INSTANCE.messageBuilder(ModCheckPacket.class,index++,NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ModCheckPacket::encode)
                .decoder(ModCheckPacket::new)
                .consumerMainThread(ModCheckPacket::handle)
                .add();
//        INSTANCE.messageBuilder(RoastPacket.class,index++,NetworkDirection.PLAY_TO_SERVER)
//                .encoder(RoastPacket::encode)
//                .decoder(RoastPacket::new)
//                .consumerMainThread(RoastPacket::handle)
//                .add();
        INSTANCE.messageBuilder(SendByteLocationsPacket.class,index++,NetworkDirection.PLAY_TO_SERVER)
                .encoder(SendByteLocationsPacket::encode)
                .decoder(SendByteLocationsPacket::new)
                .consumerMainThread(SendByteLocationsPacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientCrashPacket.class,index++,NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientCrashPacket::encode)
                .decoder(ClientCrashPacket::new)
                .consumerMainThread(ClientCrashPacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientResourcePackUpdatePacket.class,index++,NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientResourcePackUpdatePacket::encode)
                .decoder(ClientResourcePackUpdatePacket::new)
                .consumerMainThread(ClientResourcePackUpdatePacket::handle)
                .add();


    }

}
