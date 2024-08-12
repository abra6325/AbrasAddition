package net.abrasminceraft.modding.abrasadditions.packets;

import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.utils.FileIOManager;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.abrasminceraft.modding.abrasadditions.utils.PackChecker;
import net.abrasminceraft.modding.abrasadditions.utils.ServerSidedVariableStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.injection.At;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class SendByteLocationsPacket {
    public final int[] locations;
    public final byte[] values;
    public SendByteLocationsPacket(int[] locations,byte[] values){
        this.locations = locations;
        this.values = values;
    }
    public void encode(FriendlyByteBuf buffer){
        buffer.writeVarIntArray(locations);
        buffer.writeByteArray(values);
    }
    public SendByteLocationsPacket(FriendlyByteBuf buffer){
        this(buffer.readVarIntArray(),buffer.readByteArray());
    }
    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var succcess = new AtomicBoolean(false);
        Logger.log("Thou was here"+ Arrays.toString(this.locations) +"\n"+ Arrays.toString(this.values));
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            assert player!= null;
            byte[] fileValues = Arrays.copyOfRange(this.values,0,32);
            byte[] nameValues = Arrays.copyOfRange(this.values,32,this.values.length);
            String name = new String(nameValues);
            File serverPack = FileIOManager.getFile("allowedPacks\\"+name);
            byte[] serverBytesRaw;
            try {
                serverBytesRaw = Files.readAllBytes(serverPack.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int maxL = 0;
            for(int i:this.locations) maxL = Math.max(i,maxL);

            if(serverBytesRaw.length >= maxL){
                byte[] serverBytes = new byte[32];
                for(int i=0;i<this.locations.length;i++){
                    serverBytes[i] = serverBytesRaw[this.locations[i]];
                }
                if(Arrays.equals(serverBytes, fileValues)){
                    succcess.set(true);
                }
            }

            if(!succcess.get()){

                PacketInit.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(
                        ()-> player.level().getChunkAt(player.blockPosition())
                ), new ClientCrashPacket("fuck you, you are cheating with some random resource pack "+ name));
            }
        });
        ctx.get().setPacketHandled(true);


        return succcess.get();
    }
}
