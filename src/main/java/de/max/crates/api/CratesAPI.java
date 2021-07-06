package de.max.crates.api;

import de.max.crates.Crates;
import de.max.crates.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CratesAPI implements Listener {

    private List<Inventory> invs = new ArrayList<>();
    public static ItemStack[] contents = new ItemStack[]{

            new ItemStack(Material.BEACON),
    };

    private int itemIndex = 0;

    public final String TITLE = "§aCrate";

    public void spin(Player p) {

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        verteilen(inv);
        this.invs.add(inv);
        p.openInventory(inv);
        Random r = new Random();
        final double sekunden = 7D + 5D * r.nextDouble();


        new BukkitRunnable() {

            boolean finished = false;
            double delay = 0.0D;
            int ticks = 0;

            @Override
            public void run() {
                if (finished) {
                    return;
                }
                this.ticks++;
                this.delay += 1.0D / (20D * sekunden);
                if (this.ticks > this.delay * 10) {
                    this.ticks = 0;
                    for (int is = 9; is < 18; is++) {
                        inv.setItem(is, CratesAPI.contents[((is + CratesAPI.this.itemIndex) % CratesAPI.contents.length)]);
                    }
                    CratesAPI.this.itemIndex += 1;
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                    if (this.delay >= 0.5D) {
                        finished = true;
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                try {
                                    ItemStack item = inv.getItem(13);
                                    p.getInventory().addItem(item);
                                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 1, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    cancel();
                                }
                                p.updateInventory();
                                p.closeInventory();
                                cancel();
                            }
                        }.runTaskLater(Crates.getInstance(), 50L);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(Crates.getInstance(), 0L, 1L);

    }


    public void verteilen(Inventory inv) {
        ItemStack rand = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta randMeta = rand.getItemMeta();
        randMeta.setDisplayName("§0");
        rand.setItemMeta(randMeta);

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, rand);
        }

        ItemStack price = new ItemStack(Material.HOPPER);
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName("§aDein Gewinn");
        price.setItemMeta(priceMeta);

        inv.setItem(4, price);
        inv.setItem(22, price);

        int startIndex = new Random().nextInt(contents.length);
        for (int index = 0; index < startIndex; index++) {
            for (int is = 9; is < 18; is++) {
                inv.setItem(is, contents[((is + this.itemIndex) % contents.length)]);
            }
            this.itemIndex += 1;
        }
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if(e.getView().getTitle().equalsIgnoreCase(TITLE)){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(Objects.requireNonNull(b).getLocation().equals(CratesAPI.getCrate()) &&
                    b.getType() == Material.ENDER_CHEST){
                    if (p.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK && Objects.requireNonNull(Objects.requireNonNull(p.getInventory().getItemInMainHand().getItemMeta()).getDisplayName().equalsIgnoreCase("§aCrate Key"))) {
                        p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                        e.setCancelled(true);
                        new CratesAPI().spin(p);
                    } else {
                        p.sendMessage(Config.cfg.getString("Prefix")+ "§cDu hast keine Keys mehr!");
                        e.setCancelled(true);
                    }
                }
        }
    }

    public static ItemStack getKeyItem(){
        return new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayname("§aCrate Key").setLore("§bRechtsklick auf die Crate").build();
    }

    public static ItemStack getCrateItem(){
        return new ItemBuilder(Material.ENDER_CHEST).setDisplayname("§6Crate").build();
    }

    public static void setCrate(Location loc){
        File file = new File("plugins/Crates/CratesData.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        data.set("Location", loc);
        try {data.save(file);}catch(IOException ignored){}
    }

    public static Location getCrate(){
        File file = new File("plugins/Crates/CratesData.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        return data.getLocation("Location");
    }

    @EventHandler
    public void on(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();
        if(b.getType() == Material.ENDER_CHEST){
            if(p.isOp()){
                if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(CratesAPI.getCrateItem().getItemMeta().getDisplayName())){
                    CratesAPI.setCrate(b.getLocation());
                }
            }
        }
    }
}
