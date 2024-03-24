package bibilmeshka.projects.aerialmenus.services.cutter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringCutterService {

    public int getCountOfChar(final String string, final char sign) {
        var charCount = 0;
        for (final var symbol : string.toCharArray()) {
            if (symbol == sign) charCount++;
        }
        return charCount;
    }

    public List<Object> cutArgumentsFromString(String str, char sign, Class<? extends Number> numericType) {
        final var arguments = new ArrayList<Object>();
        if (str.isEmpty()) return arguments;
        var signCount = getCountOfChar(str, sign) - 1;

        for (int i = 0; signCount >= i; i++) {
            var subStr = str.substring(0, str.indexOf(sign) + 1);
            str = str.replaceFirst(subStr, "").strip();
            subStr = subStr.replace(String.valueOf(sign), "").strip();

            arguments.add(processString(subStr, numericType));
        }
        arguments.add(processString(str.strip(), numericType));
        return arguments;
    }

    public List<String> cutArgumentsFromString(String str, char sign) {
        final var arguments = new ArrayList<String>();
        if (str.isEmpty()) return arguments;
        var signCount = -1;

        for (var symbol : str.toCharArray()) {
            if (symbol == sign) signCount++;
        }

        for (int i = 0; signCount >= i; i++) {
            var subStr = str.substring(0, str.indexOf(sign) + 1);
            str = str.replaceFirst(subStr, "").strip();
            subStr = subStr.replace(String.valueOf(sign), "").strip();

            arguments.add(subStr);
        }
        arguments.add(str.strip());
        return arguments;
    }

    private Object processString(String string, Class<? extends Number> numericType) {

        if (string.equalsIgnoreCase("true")) {
            return true;
        } else if (string.equalsIgnoreCase("false")) {
            return false;
        }

        try {
            return numericType.getConstructor(String.class).newInstance(string);
        } catch (Exception e) {
            return string;
        }
    }
}
