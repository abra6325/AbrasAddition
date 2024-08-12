package net.abrasminceraft.modding.abrasadditions.events;

import net.abrasminceraft.modding.abrasadditions.enchantment.EnchantmentHarvest;
import net.abrasminceraft.modding.abrasadditions.enchantment.ModEnchantments;
import net.abrasminceraft.modding.abrasadditions.item.ModItems;
import net.abrasminceraft.modding.abrasadditions.utils.FileIOManager;
import net.abrasminceraft.modding.abrasadditions.utils.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.net.InetSocketAddress;
import java.util.List;

public class EventImbueHarvest {
    @SubscribeEvent
    public void rightClickEvent(PlayerInteractEvent e){

        if(e instanceof PlayerInteractEvent.RightClickBlock || e instanceof PlayerInteractEvent.RightClickItem| e instanceof PlayerInteractEvent.EntityInteract){
            Player p = e.getEntity();
            if(p.getOffhandItem().getItem() == ModItems.GENSOUKYO_FRAG.get()){
                ItemStack tmp1 = p.getMainHandItem();
                if(ForgeRegistries.ITEMS.tags().getTag(ItemTags.HOES).contains(tmp1.getItem()) && tmp1.getEnchantmentLevel(ModEnchantments.HARVEST.get()) == 0){
                    tmp1.enchant(ModEnchantments.HARVEST.get(),1);
                    p.getOffhandItem().setCount(p.getOffhandItem().getCount()-1);
                }
            }
        }

    }
    @SubscribeEvent
    public void onBlockUse(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult res = onBlockUse(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec(),
                true);

        if (res != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(res);
        }
    }

    private InteractionResult onBlockUse(Player player, Level world, InteractionHand hand,
                                         net.minecraft.world.phys.BlockHitResult hitResult,
                                         boolean initialCall) {
        if (player.isSpectator() || hand == InteractionHand.OFF_HAND) {
            return InteractionResult.PASS;
        }

        BlockState state = world.getBlockState(hitResult.getBlockPos());
        Block originalBlock = state.getBlock();
        ItemStack stack = player.getItemInHand(hand);

        if(stack.getEnchantmentLevel(ModEnchantments.HARVEST.get())== 0) return InteractionResult.PASS;


        if (state.getBlock() instanceof CocoaBlock || state.getBlock() instanceof CropBlock
                || state.getBlock() instanceof NetherWartBlock) {
            if (isMature(state)) {
                if (!world.isClientSide) {
                    Logger.log(FileIOManager.getJSONFromFile("abrasconfigs.json").toString());
                    BlockEvent.BreakEvent breakEv = new BlockEvent.BreakEvent(world, hitResult.getBlockPos(), state, player);
                    if (MinecraftForge.EVENT_BUS.post(breakEv)) return InteractionResult.FAIL;
                    BlockState replantState = getReplantState(state);
                    BlockEvent.EntityPlaceEvent placeEv = new BlockEvent.EntityPlaceEvent(
                            BlockSnapshot.create(world.dimension(), world, hitResult.getBlockPos()),
                            world.getBlockState(hitResult.getBlockPos().below()),
                            player
                    );
                    if (MinecraftForge.EVENT_BUS.post(placeEv)) return InteractionResult.FAIL;
                    world.setBlockAndUpdate(hitResult.getBlockPos(), replantState);
                    dropStacks(state, (ServerLevel) world, hitResult.getBlockPos(), player,
                            player.getItemInHand(hand));

                    player.causeFoodExhaustion(0.008f);

                } else {
                    player.playSound(
                            state.getBlock() instanceof NetherWartBlock ? SoundEvents.NETHER_WART_PLANTED
                                    : SoundEvents.CROP_PLANTED,
                            1.0f, 1.0f);
                }

                RightClickHarvestCallbacks.AfterHarvest.post(player, originalBlock);
                return InteractionResult.SUCCESS;
            }
        } else if (state.getBlock() instanceof SugarCaneBlock) {
            if (hitResult.getDirection() == Direction.UP && stack.getItem() == Items.SUGAR_CANE) {
                return InteractionResult.PASS;
            }

            int count = 1;

            BlockPos bottom = hitResult.getBlockPos().below();
            while (world.getBlockState(bottom).is(Blocks.SUGAR_CANE)) {
                count++;
                bottom = bottom.below();
            }

            if (count == 1 && !world.getBlockState(hitResult.getBlockPos().above()).is(Blocks.SUGAR_CANE)) {
                return InteractionResult.PASS;
            }

            if (!world.isClientSide) {
                world.destroyBlock(bottom.above(2), true);

                // Regular block breaking causes 0.005f exhaustion
                player.causeFoodExhaustion(0.008f);
            } else {
                player.playSound(SoundEvents.CROP_PLANTED, 1.0f, 1.0f);
            }

            RightClickHarvestCallbacks.AfterHarvest.post(player, originalBlock);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static boolean isMature(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) {
            return state.getValue(CocoaBlock.AGE) >= CocoaBlock.MAX_AGE;
        } else if (state.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.isMaxAge(state);
        } else if (state.getBlock() instanceof NetherWartBlock) {
            return state.getValue(NetherWartBlock.AGE) >= NetherWartBlock.MAX_AGE;
        }

        return false;
    }

    private static BlockState getReplantState(BlockState state) {
        if (state.getBlock() instanceof CocoaBlock) {
            return state.setValue(CocoaBlock.AGE, 0);
        } else if (state.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.getStateForAge(0);
        } else if (state.getBlock() instanceof NetherWartBlock) {
            return state.setValue(NetherWartBlock.AGE, 0);
        }

        return state;
    }

    private static void dropStacks(BlockState state, ServerLevel world, BlockPos pos, Entity entity,
                                   ItemStack toolStack) {
        Item replant = state.getBlock().getCloneItemStack(world, pos, state).getItem();
        final boolean[] removedReplant = {false};

        Block.getDrops(state, world, pos, null, entity, toolStack).forEach(stack -> {
            if (!removedReplant[0] && stack.getItem() == replant) {
                stack.setCount(stack.getCount() - 1);
                removedReplant[0] = true;
            }

            Block.popResource(world, pos, stack);
        });

        state.spawnAfterBreak(world, pos, toolStack, true);
    }

}
