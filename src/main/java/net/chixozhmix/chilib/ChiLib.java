package net.chixozhmix.chilib;

import com.mojang.logging.LogUtils;
import net.chixozhmix.chilib.attributes.ChiAttributes;
import net.chixozhmix.chilib.registers.CLBrewingRecipeRegister;
import net.chixozhmix.chilib.registers.CLEntityRegister;
import net.chixozhmix.chilib.registers.CLItemRegister;
import net.chixozhmix.chilib.registers.CLSoundsRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ChiLib.MODID)
public class ChiLib
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "chilib";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChiLib(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ChiAttributes.ATTRIBUTES.register(modEventBus);
        CLItemRegister.register(modEventBus);
        CLEntityRegister.register(modEventBus);
        CLSoundsRegister.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        CLBrewingRecipeRegister.registerAll(builder);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    public static ResourceLocation id(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath("chilib", path);
    }
}
