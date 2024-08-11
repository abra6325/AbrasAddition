package net.abrasminceraft.modding.abrasadditions.init;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.packets.HandshakePacket;
import net.abrasminceraft.modding.abrasadditions.packets.ModCheckPacket;
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

    }

}
