package bibilmeshka.projects.aerialmenus.services.requirement.dependency;

import bibilmeshka.projects.aerialmenus.requirements.HasMoneyRequirement;
import net.milkbowl.vault.economy.Economy;

public class RequirementDependencySetService {

    private final Economy economy;

    public RequirementDependencySetService(final Economy economy) {
        this.economy = economy;
    }

    public void setInMoneyRequirement(HasMoneyRequirement moneyRequirement) {
        moneyRequirement.setEconomy(this.economy);
    }

}
