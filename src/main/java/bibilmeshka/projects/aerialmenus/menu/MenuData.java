package bibilmeshka.projects.aerialmenus.menu;

import bibilmeshka.projects.aerialmenus.requirements.RequirementsData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuData {

    private File menuFile;
    private String path;
    private int updateInterval;
    private int taskId;
    private int size;
    private boolean needRegister;
    private boolean clearInventory;
    private String guiName;
    private String argsUsageMessage;
    private InventoryType type;
    private MenuItem[] items;
    private List<String> openCommands;
    private List<String> commands = new ArrayList<>();
    private List<String> closeCommands;
    private List<String> args;
    private RequirementsData openRequirements;

}