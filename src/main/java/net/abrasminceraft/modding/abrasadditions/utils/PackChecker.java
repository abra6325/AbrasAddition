package net.abrasminceraft.modding.abrasadditions.utils;

import net.minecraft.server.packs.repository.Pack;

import java.util.ArrayList;
import java.util.List;

public class PackChecker {
    public static boolean checkResourcePacks(List<Pack> packs){
        boolean legal = true;
        if(packs.size() > 2) legal = false;
        List<String> packNames = new ArrayList<>();
        for(Pack i:packs){
            packNames.add(i.getId());
        }
        if(!(packNames.contains("vanilla") && packNames.contains("mod_resources"))){
            legal = false;
        }
        return legal;
    }
}
