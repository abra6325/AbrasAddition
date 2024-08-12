package net.abrasminceraft.modding.abrasadditions.packets;

import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class RoastPacket {
    public RoastPacket(){

    }
    public RoastPacket(FriendlyByteBuf buffer){

    }
    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(1);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            ServerPlayer p = ctx.get().getSender();
            assert p != null;
            p.getServer().sendSystemMessage(Component.nullToEmpty(ChatFormatting.RED +
                    "SAY YOUR L's TO "+p.getDisplayName() +" FOR HAVING FUCKING CHEATS!!!"));
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

}
