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

    public static final ForgeConfigSpec.BooleanValue KEEP_ADDITIONAL_ITEMS = BUILDER
            .comment("If true, the additional items list will be used to keep items on death")
            .define("keepAdditionalItems", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // These public static fields will be populated when the config loads.
    public static boolean keepArmorFlag;
    public static boolean keepTieredFlag;
    public static boolean keepBowsFlag;
    public static boolean keepCrossbowsFlag;
    public static boolean keepTridentsFlag;
    public static boolean keepShieldsFlag;
    public static boolean keepElytraFlag;
    public static boolean keepAdditionalItemsFlag;


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
        keepAdditionalItemsFlag = KEEP_ADDITIONAL_ITEMS.get();
    }
}
