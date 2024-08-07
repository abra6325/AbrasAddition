package net.abrasminceraft.modding.abrasadditions.events;

import net.abrasminceraft.modding.abrasadditions.AbrasAdditions;
import net.abrasminceraft.modding.abrasadditions.item.ModItems;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerMain;

public class EventPlayerWakeUp {
    @SubscribeEvent
    public void wakeupEvent(final PlayerWakeUpEvent event){
        Player p = event.getEntity();

        if(p.level().getDayTime() == 24000){
            p.getInventory().add(new ItemStack(ModItems.GENSOUKYO_FRAG.get()));
            p.sendSystemMessage(Component.nullToEmpty(ChatFormatting.RED + "You have gained 1 Fragment of Phantasm."));
            Logger.log("Given "+p.getName().toString()+" 1 GENSOU_FRAG");
        }
    }
}
