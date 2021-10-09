package thelonebarkeeper.mgame.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ClassType {
    FLASH(new ItemStack(Material.SUGAR), "Флэш", "Зелье Скорости I"),
    MINER(new ItemStack(Material.STONE_PICKAXE), "Шахтёр", "Каменная Кирка - Эффективность I"),
    FARMER(new ItemStack(Material.WHEAT), "Фермер", "Растения растут БЫСТРЕЕ. Мотыга, Семена Картофеля x9, Морковь x9, Семена Пшеницы x9"),
    ARCHER(new ItemStack(Material.BOW), "Лучник", "Лук, Стрела x4"),
    SNOWMAN(new ItemStack(Material.SNOW_BLOCK), "Снеговик", "Снежок x12"),
    ALCHEMIST(new ItemStack(Material.BREWING_STAND_ITEM), "Алхимик", "Зелье Замедления I, Зелье Отравления I"),
    KNIGHT(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "Рыцарь", "Кольчужный Нагрудник, Кольчужные Штаны"),
    BUILDER(new ItemStack(Material.BRICK), "Строитель", "Камень x16, Кирпичи x16, Доски x16")
    ;

    public final ItemStack icon;
    public final String name;
    public final String lore;

    ClassType(ItemStack icon, String name, String lore) {
        this.icon = icon;
        this.name = name;
        this.lore = lore;
    }
}
