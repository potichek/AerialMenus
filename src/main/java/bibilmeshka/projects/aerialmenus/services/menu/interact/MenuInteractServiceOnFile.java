package bibilmeshka.projects.aerialmenus.services.menu.interact;

import bibilmeshka.projects.aerialmenus.menu.Menu;
import bibilmeshka.projects.aerialmenus.services.cutter.StringCutterService;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import bibilmeshka.projects.aerialmenus.services.menu.convert.MenuConvertServiceConsumer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuInteractServiceOnFile implements MenuInteractService {

    private final JavaPlugin plugin;
    private final MenuConvertServiceConsumer convertService;
    private final DebugService debugService;
    private File menusFolder;
    public MenuInteractServiceOnFile(final JavaPlugin plugin, final MenuConvertServiceConsumer convertService, final DebugService debugService) {
        this.plugin = plugin;
        this.convertService = convertService;
        this.debugService = debugService;
    }

    private void createMenuFolder() {
        this.menusFolder = new File(this.plugin.getDataFolder().getAbsolutePath() + "/Menus");
        if (this.menusFolder.exists()) return;
        this.menusFolder.mkdir();
    }

    private void createExampleMenu() {
        try {
            if (!(this.plugin.getConfig().getBoolean("create_example_menu"))) return;
            final var exampleMenuFile = new File(this.menusFolder.getAbsolutePath() + "/example.yml");
            if (exampleMenuFile.exists()) return;

            final var inputStream = this.plugin.getResource("example.yml");
            final var buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            final var outputStream = new FileOutputStream(exampleMenuFile);
            outputStream.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void startUp() {
        createMenuFolder();
        createExampleMenu();
    }

    private List<File> getAllFiles() {
        final var files = new ArrayList<File>();
        this.plugin.reloadConfig();
        final var config =  this.plugin.getConfig();
        for (final var name : config.getConfigurationSection("menus").getKeys(false)) {
            final var menuFile = new File(this.plugin.getDataFolder().getAbsolutePath() +
                    "/Menus" + "/" + config.getString("menus" + "." + name + "." + "file"));
            try {
                menuFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            files.add(menuFile);
        }
        return files;
    }

    @Nullable
    public Menu getMenuByName(final String menuName) {
        for (final var menuFile : getAllFiles()) {
            if (!(menuFile.getName().equals(menuName))) continue;
            return this.convertService.convert(menuFile);
        }
        return null;
    }

    @Override
    public List<Menu> getAllMenu() {
        return this.convertService.convertList(getAllFiles());
    }

    @Override
    public void saveMenu(File menuFile) {
        if (!menuFile.exists()) {
            plugin.saveResource("Menus/" + menuFile.getName(), false);
        }
    }

    @Override
    public void saveAllMenus() {
        for (var menuFile : getAllFiles()) {
            saveMenu(menuFile);
        }
    }
}
