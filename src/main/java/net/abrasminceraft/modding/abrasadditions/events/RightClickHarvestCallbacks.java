package net.abrasminceraft.modding.abrasadditions.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class RightClickHarvestCallbacks {
    /**
     * Called after right-click-harvesting.
     */
    public static class AfterHarvest extends Event {
        private final Player player;
        private final Block block;

        private AfterHarvest(Player player, Block block) {
            this.player = player;
            this.block = block;
        }

        protected static void post(Player player, Block block) {
            AfterHarvest event = new AfterHarvest(player, block);
            MinecraftForge.EVENT_BUS.post(event);
        }

        public Player getPlayer() {
            return this.player;
        }

        public Block getBlock() {
            return this.block;
        }
    }
}