package net.abrasminceraft.modding.abrasadditions.packets;

import net.abrasminceraft.modding.abrasadditions.utils.ClientSidedVariableStorage;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.abrasminceraft.modding.abrasadditions.utils.PackChecker;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientResourcePackUpdatePacket {
    public final List<String> message;
    public static final int PACKETID = 1;
    public ClientResourcePackUpdatePacket(List<String> msg){
        this.message = msg;
    }

    public void encode(FriendlyByteBuf buffer){
        byte[] encoded = UtilStringPackets.encode(this.message,PACKETID);
        buffer.writeByteArray(encoded);

    }

    public ClientResourcePackUpdatePacket(FriendlyByteBuf buffer){
        this(UtilStringPackets.readList(buffer.readByteArray(),PACKETID));
    }
    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            System.out.println("Client resource packet received");
            ClientSidedVariableStorage.allowedResourcePacks = this.message;
            ClientSidedVariableStorage.connected = true;

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

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
