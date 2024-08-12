package net.abrasminceraft.modding.abrasadditions.events;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.init.PacketInit;
import net.abrasminceraft.modding.abrasadditions.packets.RoastPacket;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.abrasminceraft.modding.abrasadditions.utils.PackChecker;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AbrasAdditions.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value= Dist.CLIENT)
public class ClientEvents {

}
