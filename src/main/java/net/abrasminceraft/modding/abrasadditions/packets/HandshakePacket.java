package net.abrasminceraft.modding.abrasadditions.packets;

import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

//0x69 0x42 0x00 0x69
public class HandshakePacket {
    public final String msg;
    public static final int PACKETID = 0;
    public static final byte[] STARTING_SEQUENCE = {(byte) 0x69, (byte) 0x42, (byte) 0x00, (byte) 0x69};
    public HandshakePacket(String message){
        this.msg = message;

    }
    public void encode(FriendlyByteBuf buffer){
        byte[] msg = this.msg.getBytes();
        byte[] encoded_msg = new byte[msg.length+5];
        for(int i=0;i<4;i++) encoded_msg[i] = STARTING_SEQUENCE[i];
        encoded_msg[4] = (byte) PACKETID;
        for(int i=0;i<msg.length;i++) encoded_msg[i+5] = msg[i];
        buffer.writeByteArray(encoded_msg);
    }
    public static boolean checkStartingSequence(byte[] x){
        boolean ret = true;
        for(int i=0;i<4;i++) ret = (x[i] == STARTING_SEQUENCE[i]);
        return ret;
    }

    public HandshakePacket(FriendlyByteBuf buffer){
        this(read(buffer));
    }
    public static String read(FriendlyByteBuf buffer){
        byte[] tmp = buffer.readByteArray();
        assert(tmp.length >5);
        assert(checkStartingSequence(tmp));
        assert(tmp[4] == (byte) PACKETID);
        byte[] ret = new byte[tmp.length-5];
        int l = tmp.length-5;
        for(int i=0;i<l;i++){
            ret[i] = tmp[i+5];
        }
        return new String(ret);
    }
    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() ->{
            Logger.log("Handshake Success!");

            Logger.log(this.msg);
            System.out.println("CLIENT SIDE "+System.getProperty("user.dir"));

            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
