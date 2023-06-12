package ru.mcsnapix.snapistones.plugin.util;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Util {
    public boolean ignoreCaseContains(List<String> list, String searchStr) {
        for (String s : list) {
            if (searchStr.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
