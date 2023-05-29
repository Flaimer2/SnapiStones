package ru.mcsnapix.snapistones.plugin;

import java.util.ArrayList;
import java.util.List;

public class ReplacedList extends ArrayList<String> {
    public ReplacedList(List<String> strings) {
        super(strings);
    }

    public ReplacedList replace(String id, String replacement) {
        ReplacedList newStrings = new ReplacedList(new ArrayList<>());
        for (String s : this) {
            if (s.equals(id)) {
                if (replacement == null) {
                    continue;
                }

                newStrings.add(s.replace(id, replacement));
            } else {
                newStrings.add(s);
            }
        }
        return newStrings;
    }

    public ReplacedList replace(String id, List<String> replacement) {
        ReplacedList newStrings = new ReplacedList(new ArrayList<>());
        for (String s : this) {
            if (s.equals(id)) {
                if (replacement == null) {
                    continue;
                }
                newStrings.addAll(replacement);
            } else {
                newStrings.add(s);
            }
        }
        return newStrings;
    }
}
