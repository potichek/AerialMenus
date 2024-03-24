package bibilmeshka.projects.aerialmenus.services.debug;

public enum DebugLevel {

    LOWEST(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    HIGHEST(4);

    private final int priority;

    DebugLevel(final int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

}
