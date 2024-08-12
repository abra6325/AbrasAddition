package net.abrasminceraft.modding.abrasadditions.packets;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientCrashPacket {
    public final String msg;
    public ClientCrashPacket(String msg){
        this.msg = msg;
    }
    public void encode(FriendlyByteBuf buffer){
        buffer.writeByteArray(this.msg.getBytes());
    }

    public ClientCrashPacket(FriendlyByteBuf buffer){
        this(new String(buffer.readByteArray()));
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {

            success.set(true);
        });
        ctx.get().setPacketHandled(true);
        if(success.get()){
            Minecraft.crash(new CrashReport(this.msg,new Exception()));
        }
        return success.get();
    }
}
