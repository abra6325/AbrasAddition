package net.abrasminceraft.modding.abrasadditions.events;

import com.google.gson.JsonElement;
import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.packets.ClientResourcePackUpdatePacket;
import net.abrasminceraft.modding.abrasadditions.utils.FileIOManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AbrasAdditions.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value= Dist.DEDICATED_SERVER)
public class ServerEvents {


}
