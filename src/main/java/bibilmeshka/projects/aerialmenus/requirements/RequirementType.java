package bibilmeshka.projects.aerialmenus.requirements;

import java.util.ArrayList;
import java.util.List;

public enum RequirementType {

    HAS_PERMISSION(false),
    NOT_HAS_PERMISSION(true),
    HAS_MONEY(false),
    NOT_HAS_MONEY(true),
    HAS_ITEM(false),
    NOT_HAS_ITEM(true),
    HAS_META(false),
    NOT_HAS_META(true),
    HAS_EXP(false),
    NOT_HAS_EXP(true),
    IS_NEAR(false),
    NOT_IS_NEAR(true),
    JAVASCRIPT(false),
    STRING_EQUALS(false),
    NOT_STRING_EQUALS(true),
    STRING_EQUALS_IGNORE_CASE(false),
    NOT_STRING_EQUALS_IGNORE_CASE(true),
    STRING_CONTAINS(false),
    NOT_STRING_CONTAINS(true),
    REGEX_MATCHES(false),
    NOT_REGEX_MATCHES(true),
    COMPARATORS(false);

    private boolean negative;

    RequirementType(boolean negative) {
        this.negative = negative;
    }

    public  boolean isNegative() {
        return negative;
    }
}