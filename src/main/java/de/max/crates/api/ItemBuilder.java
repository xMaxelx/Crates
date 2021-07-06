package de.max.crates.api;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material, short subID) {
        item = new ItemStack(material, 1, subID);
        itemMeta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, (short)0);
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayname(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder addEntchantment(Enchantment e, Integer strenght, boolean b) {
        this.itemMeta.addEnchant(e, strenght, b);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        itemMeta.setLore(lore);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setUnbreakable(boolean b){
        itemMeta.setUnbreakable(b);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... itemFlags){
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemStack createPlayerHead(Player p, String displayname) {
        ItemStack is = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta im = (SkullMeta)is.getItemMeta();
        im.setDisplayName(displayname);
        im.setOwner(p.getName());

        item.setItemMeta(im);
        return item;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }
}
