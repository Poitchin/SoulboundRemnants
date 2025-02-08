package com.github.poitchin.common.config;

import com.github.poitchin.SoulboundRemnants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = SoulboundRemnants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    // Create the config builder.
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Define boolean toggles for different item types.
    public static final ForgeConfigSpec.BooleanValue KEEP_ARMOR = BUILDER
            .comment("Keep armor items on death")
            .define("keepArmor", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_TIERED = BUILDER
            .comment("Keep tiered items (tools, weapons, etc.) on death")
            .define("keepTiered", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_BOWS = BUILDER
            .comment("Keep bows on death")
            .define("keepBows", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_CROSSBOWS = BUILDER
            .comment("Keep crossbows on death")
            .define("keepCrossbows", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_TRIDENTS = BUILDER
            .comment("Keep tridents on death")
            .define("keepTridents", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_SHIELDS = BUILDER
            .comment("Keep shields on death")
            .define("keepShields", true);

    public static final ForgeConfigSpec.BooleanValue KEEP_ELYTRA = BUILDER
            .comment("Keep elytra on death")
            .define("keepElytra", true);

    // New Boolean option to enable/disable the additional items list.
    public static final ForgeConfigSpec.BooleanValue USE_ADDITIONAL_ITEMS_LIST = BUILDER
            .comment("If true, the additional items list will be used to keep items on death")
            .define("useAdditionalItemsList", false);

    // Optionally, a list of additional items (by resource location) to keep.
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> KEEP_ITEMS_LIST = BUILDER
            .comment("List of additional items to keep on death (as resource locations, e.g. 'minecraft:diamond')")
            .defineListAllowEmpty("keepItemsList", List.of(), Config::validateItemName);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // These public static fields will be populated when the config loads.
    public static boolean keepArmorFlag;
    public static boolean keepTieredFlag;
    public static boolean keepBowsFlag;
    public static boolean keepCrossbowsFlag;
    public static boolean keepTridentsFlag;
    public static boolean keepShieldsFlag;
    public static boolean keepElytraFlag;
    public static boolean useAdditionalItemsListFlag;
    public static Set<Item> additionalKeepItems;

    // A helper method to validate that an entry in the list is a valid item resource location.
    private static boolean validateItemName(final Object obj) {
        return obj instanceof String str && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(str));
    }

    // Listen to config load events to update the values.
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        keepArmorFlag = KEEP_ARMOR.get();
        keepTieredFlag = KEEP_TIERED.get();
        keepBowsFlag = KEEP_BOWS.get();
        keepCrossbowsFlag = KEEP_CROSSBOWS.get();
        keepTridentsFlag = KEEP_TRIDENTS.get();
        keepShieldsFlag = KEEP_SHIELDS.get();
        keepElytraFlag = KEEP_ELYTRA.get();
        useAdditionalItemsListFlag = USE_ADDITIONAL_ITEMS_LIST.get();
        additionalKeepItems = KEEP_ITEMS_LIST.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
