package net.chixozhmix.chilib.utils.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Optional;

//Утилиты предметов
public class ItemUtils {

    /**Провекрка полного сета брони
     * Желательно использовать это в armorTick, поскольку метод возвращает истину при любой надетой во все слоты брони.
     * @param player - целевой игрок
     * @return истину, если на игроке все элементы брони
     */
    public static boolean hasFullSet(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }

    //Получение названия предмета
    public static MutableComponent getItemName(Item item) {
        return Component.translatable(item.getDescriptionId());
    }

    //Добавление опциональных предметов в таблицу креатива
    public static void addOptionalItem(CreativeModeTab.Output output, Optional<DeferredItem<Item>> optionalItem) {
        optionalItem.ifPresent(regObj -> {
                output.accept(regObj.get());
        });
    }
}
