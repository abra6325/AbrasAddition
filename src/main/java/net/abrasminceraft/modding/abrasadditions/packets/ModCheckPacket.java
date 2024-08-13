package net.abrasminceraft.modding.abrasadditions.packets;

import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ModCheckPacket {
    //Separated by
    public final List<String> message;
    public static final int PACKETID = 1;
    public ModCheckPacket(List<String> msg){
        this.message = msg;
    }

    public void encode(FriendlyByteBuf buffer){
        byte[] encoded = UtilStringPackets.encode(this.message,PACKETID);
        buffer.writeByteArray(encoded);

    }

    public ModCheckPacket(FriendlyByteBuf buffer){
        this(UtilStringPackets.readList(buffer.readByteArray(),PACKETID));
    }
    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            Set<String> myMods=  new HashSet<>();
            for(IModInfo i:ModList.get().getMods()){
                myMods.add(i.getModId());
            }


            Set<String> serverMods = new HashSet<>(this.message);
            List<String> mods2 = new ArrayList<>(myMods);


            myMods.removeAll(serverMods);
            Logger.log("Infinite Tsugiyomi "+myMods);
            if(!myMods.isEmpty()){
//                PacketInit.INSTANCE.sendToServer(new RoastPacket());
                Minecraft.crash(new CrashReport("You have fucking cheats. Extra mods are:\n "+ myMods,new Exception()));

            }else{
                Logger.log("Mod check completed. no illegal mods found.");
                Logger.log("Found mods" + this.message.toString() +" \nCompared with " + mods2.toString());
                success.set(true);
            }

        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }

}
