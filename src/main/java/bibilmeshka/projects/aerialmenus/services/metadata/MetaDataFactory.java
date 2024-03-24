package bibilmeshka.projects.aerialmenus.services.metadata;

import bibilmeshka.projects.aerialmenus.AerialMenus;
import bibilmeshka.projects.aerialmenus.services.debug.DebugService;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;


public class MetaDataFactory {

    private final AerialMenus plugin;
    private final DebugService debugService;

    public MetaDataFactory(final AerialMenus plugin, final DebugService debugService) {
        this.plugin = plugin;
        this.debugService = debugService;
    }

    public @NotNull FixedMetadataValue create(final MetaValueType type, final Object value) {
        FixedMetadataValue metaDataValue = null;
        if (type == MetaValueType.STRING) {
            metaDataValue = new FixedMetadataValue(this.plugin, (String) value);
        } else if (type == MetaValueType.BOOLEAN) {
            metaDataValue = new FixedMetadataValue(this.plugin, (Boolean) value);
        } else if (type == MetaValueType.DOUBLE) {
            metaDataValue = new FixedMetadataValue(this.plugin, (Double) value);
        } else if (type == MetaValueType.LONG) {
            metaDataValue = new FixedMetadataValue(this.plugin, new Long(((Number) value).longValue()));
        } else if (type == MetaValueType.INTEGER) {
            metaDataValue = new FixedMetadataValue(this.plugin, new Integer(((Number) value).intValue()));
        }
        return metaDataValue;
    }

}
