package com.mira.furnitureengine.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    /**
     * This file defines colors,
     * and gradients for item names.
     */

    public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;

    public static String format(String input){
        return translateHexColorCodes("&#",ChatColor.translateAlternateColorCodes('&', input));
    }

    public static List<String> format(List<String> input){
        List<String> output = new ArrayList<>();
        for(String string : input){
            output.add(format(string));
        }
        return output;
    }

    // Custom Hex Colors
    public static String translateHexColorCodes(String startTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
