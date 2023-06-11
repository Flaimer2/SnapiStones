package ru.mcsnapix.snapistones.plugin.modules.upgrades.config;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultStrings;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultObject;

public interface UpgradeConfig {
    @SuppressWarnings("unused") // used by DazzleConf
    static Map<String, EffectOptions> defaultEffects() {
        Map<String, EffectOptions> effectOptionsMap = new HashMap<>();
        effectOptionsMap.put("Speed", EffectOptions.of("SPEED", 1, "Скорость"));
        return effectOptionsMap;
    }

    @SuppressWarnings("unused") // used by DazzleConf
    static Map<Integer, Integer> defaultLimitOwner() {
        Map<Integer, Integer> limitOptionsMap = new HashMap<>();
        limitOptionsMap.put(1, 2);
        limitOptionsMap.put(2, 3);
        limitOptionsMap.put(3, 4);
        limitOptionsMap.put(4, 5);
        limitOptionsMap.put(5, 6);
        return limitOptionsMap;
    }

    @SuppressWarnings("unused") // used by DazzleConf
    static Map<Integer, Integer> defaultLimitMember() {
        Map<Integer, Integer> limitOptionsMap = new HashMap<>();
        limitOptionsMap.put(1, 5);
        limitOptionsMap.put(2, 7);
        limitOptionsMap.put(3, 9);
        limitOptionsMap.put(4, 11);
        limitOptionsMap.put(5, 14);
        return limitOptionsMap;
    }

    @DefaultObject("defaultEffects")
    Map<String, @SubSection EffectOptions> effects();

    @ConfKey("limit.owner")
    @DefaultObject("defaultLimitOwner")
    Map<Integer, Integer> limitOwner();

    @ConfKey("limit.member")
    @DefaultObject("defaultLimitMember")
    Map<Integer, Integer> limitMember();

    @SubSection
    MessageSection message();

    @SubSection
    ItemSection item();

    interface MessageSection {
        @SubSection
        LimitSection limit();

        interface LimitSection {
            @DefaultString("Вы превышаете лимит по овнорам (%region_max_owners%)")
            String addOwner();

            @DefaultString("Вы превышаете лимит по участник (%region_max_members%)")
            String addMember();
        }
    }

    interface ItemSection {
        @DefaultString("Блок привата")
        String name();

        @DefaultStrings({"Улучшения:", "%effectBought%", "%maxOwners%", "%maxMembers%"})
        List<String> lore();

        @DefaultStrings({"Эффекты:", "%effects%"})
        List<String> effectBought();

        @DefaultString("%name%")
        String formatListEffect();

        @DefaultString("Уровень максимальное количество овнеров: %amount%")
        String maxOwners();

        @DefaultString("Уровень максимальное количество мемберов: %amount%")
        String maxMembers();
    }
}
