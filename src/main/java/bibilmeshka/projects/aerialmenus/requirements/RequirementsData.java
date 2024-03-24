package bibilmeshka.projects.aerialmenus.requirements;

import java.util.List;
public record RequirementsData(int minimumRequirements, boolean stopAtSuccess, List<Requirement> requirements) {}