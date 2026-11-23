package net.chixozhmix.chilib.events;

import net.chixozhmix.chilib.ChiLib;
import net.chixozhmix.chilib.client.entity.funny_cat.FunnyCatModel;
import net.chixozhmix.chilib.client.entity.funny_cat.FunyCatRenderer;
import net.chixozhmix.chilib.registers.CLEntityRegister;
import net.chixozhmix.chilib.registers.CLModelLayers;
import net.chixozhmix.chilib.utils.minecraft.IMinecraftInstanceHelper;
import net.chixozhmix.chilib.utils.minecraft.MInecraftInstanceHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = ChiLib.MODID, value = Dist.CLIENT)
public class CommonModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            MInecraftInstanceHelper.instance = new IMinecraftInstanceHelper() {
                @Override
                public @Nullable Player player() {
                    return Minecraft.getInstance().player;
                }
            };
        });

        EntityRenderers.register(CLEntityRegister.FUNNY_CAT.get(), FunyCatRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CLModelLayers.FUNNY_CAT_LAYER, FunnyCatModel::createBodyLayer);
    }
}
