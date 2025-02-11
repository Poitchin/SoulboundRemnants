package com.github.poitchin.common.event;

import com.github.poitchin.SoulboundRemnants;
import com.github.poitchin.common.DeathMessageHelper;
import com.github.poitchin.common.config.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = SoulboundRemnants.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    public static final TagKey<Item> KEEP_ON_DEATH = TagKey.create(Registries.ITEM, new ResourceLocation("soulboundremnants", "keep_on_death"));

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        // Only run for ServerPlayers (i.e. on the server) to avoid duplicate or client-side logic.
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        // Safety check: ensure we're on the server side.
        if (player.level().isClientSide()) {
            return;
        }
        // Process the main inventory:
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (!stack.isEmpty()) {
                // Check if the item has Curse of Vanishing
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, stack) > 0) {
                    // Remove it from the inventory without dropping it
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    continue; // Skip further processing for this slot
                }

                // If the item is not to be kept (per your shouldKeep logic), drop it
                if (!shouldKeep(stack)) {
                    // Copy the stack for dropping
                    ItemStack dropStack = stack.copy();
                    // Clear the slot
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    // Spawn it as an item drop in the world
                    ItemEntity itemEntity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), dropStack);
                    player.level().addFreshEntity(itemEntity);
                }
            }
        }
        // Send a chat message with the death coordinates.
        Component deathCoordMsg = DeathMessageHelper.buildDeathMessage(player);
        player.sendSystemMessage(deathCoordMsg);
    }

    /**
     * Determines if the item should be kept in the inventory on death.
     *
     * @param stack The item stack to test.
     * @return true if the item should be kept; false if it should be dropped.
     */
    private static boolean shouldKeep(ItemStack stack) {
        // Automatically keep items that are rare or epic.
        Rarity rarity = stack.getItem().getRarity(stack);
        if (rarity.ordinal() >= Rarity.RARE.ordinal()) {
            return true;
        }
        // Existing checks for known types, respecting config toggles.
        if (stack.getItem() instanceof ArmorItem && Config.keepArmorFlag) {
            return true;
        }
        if (stack.getItem() instanceof TieredItem && Config.keepTieredFlag) {
            return true;
        }
        if (stack.getItem() instanceof ProjectileWeaponItem && Config.keepBowsFlag) {
            return true;
        }
        if (stack.getItem() instanceof TridentItem && Config.keepTridentsFlag) {
            return true;
        }
        if (stack.getItem() instanceof ShieldItem && Config.keepShieldsFlag) {
            return true;
        }
        if (stack.getItem() instanceof ElytraItem && Config.keepElytraFlag) {
            return true;
        }
        return stack.is(KEEP_ON_DEATH) && Config.keepAdditionalItemsFlag;
    }

}
