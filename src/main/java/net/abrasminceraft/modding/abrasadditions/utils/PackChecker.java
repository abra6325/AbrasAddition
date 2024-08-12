package net.abrasminceraft.modding.abrasadditions.utils;

import com.google.gson.JsonElement;
import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.packets.SendByteLocationsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.repository.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PackChecker {
    public static Set<String> defaultPacks = new HashSet<>(Arrays.asList("vanilla","mod_resources"));
    public static boolean checkResourcePacks(List<Pack> packs) throws IOException {
        boolean legal = true;

        List<String> packNames = new ArrayList<>();
        for(Pack i:packs){
            packNames.add(i.getId());
        }
        List<String> legalPacks = ClientSidedVariableStorage.allowedResourcePacks;
//        for(JsonElement i:FileIOManager.getJSONFromFile("abrasconfigs.json").getAsJsonArray("resourcepacklist").asList()){
//            legalPacks.add(i.getAsString());
//        }
        Set<String> whitelist = new HashSet<>(legalPacks);
        Set<String> loaded = new HashSet<>(packNames);
        Set<String> loadedCopy = new HashSet<>(packNames);

        loaded.removeAll(whitelist);
        if(!loaded.isEmpty()){
            System.out.println("I got thi here");
            legal = false;
        }
        if(legal) {
            loadedCopy.removeAll(defaultPacks);
            for (String i : loadedCopy) {
                String tmp = i.split("/")[1];
                File f = new File(new File(System.getProperty("user.dir")).toString() + "\\"+"resourcepacks\\"+tmp);
                byte[] fbyte = Files.readAllBytes(f.toPath());
                Random e = new Random();
                int[] randomBytes = e.ints(32,0,fbyte.length).toArray();
                byte[] namebyte = tmp.getBytes();
                byte[] sendbytes = new byte[32 + namebyte.length];
                for(int j=0;j<randomBytes.length;j++) {

                    sendbytes[j] = fbyte[randomBytes[j]];
                }
                for(int j=0;j<namebyte.length;j++){
                    sendbytes[j+32] = namebyte[j];
                }
                PacketInit.INSTANCE.sendToServer(new SendByteLocationsPacket(randomBytes,sendbytes));

            }
        }
        return legal;
    }
    public static List<String> getResourcePacks(List<Pack> packs){
        List<String> packNames = new ArrayList<>();
        for(Pack i:packs){
            packNames.add(i.getId());
        }
        return packNames;
    }

}
