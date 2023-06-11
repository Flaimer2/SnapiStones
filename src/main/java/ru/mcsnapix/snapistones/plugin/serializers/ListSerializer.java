package ru.mcsnapix.snapistones.plugin.serializers;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class ListSerializer {
    public String serialise(List<String> list) {
        return String.join(";", list);
    }

    public List<String> deserialise(String s) {
        return Arrays.asList(s.split(";"));
    }
}
