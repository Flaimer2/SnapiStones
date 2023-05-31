package ru.mcsnapix.snapistones.plugin.modules.upgrade.settings;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.ConfKey;
import space.arim.dazzleconf.annote.SubSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UpgradeConfig {
    static Map<String, EffectOptions> defaultEffects() {
        Map<String, EffectOptions> effectOptionsMap = new HashMap<>();
        effectOptionsMap.put("Speed", EffectOptions.of("SPEED", 1, "Скорость"));
        return effectOptionsMap;
    }

    static Map<Integer, Integer> defaultLimitOwner() {
        Map<Integer, Integer> limitOptionsMap = new HashMap<>();
        limitOptionsMap.put(1, 2);
        limitOptionsMap.put(2, 3);
        limitOptionsMap.put(3, 4);
        limitOptionsMap.put(4, 5);
        limitOptionsMap.put(5, 6);
        return limitOptionsMap;
    }

    static Map<Integer, Integer> defaultLimitMember() {
        Map<Integer, Integer> limitOptionsMap = new HashMap<>();
        limitOptionsMap.put(1, 5);
        limitOptionsMap.put(2, 7);
        limitOptionsMap.put(3, 9);
        limitOptionsMap.put(4, 11);
        limitOptionsMap.put(5, 14);
        return limitOptionsMap;
    }

    @ConfDefault.DefaultObject("defaultEffects")
    Map<String, @SubSection EffectOptions> effects();

    @ConfKey("limit.owner")
    @ConfDefault.DefaultObject("defaultLimitOwner")
    Map<Integer, Integer> limitOwner();

    @ConfKey("limit.member")
    @ConfDefault.DefaultObject("defaultLimitMember")
    Map<Integer, Integer> limitMember();

    @SubSection
    MessageSection message();

    @SubSection
    ItemSection item();

    interface MessageSection {
        @SubSection
        LimitSection limit();

        interface LimitSection {
            @ConfDefault.DefaultString("Вы превышаете лимит по овнорам (%region_max_owners%)")
            String addOwner();

            @ConfDefault.DefaultString("Вы превышаете лимит по участник (%region_max_members%)")
            String addMember();
        }
    }

    interface ItemSection {
        @ConfDefault.DefaultString("Блок привата")
        String name();

        @ConfDefault.DefaultStrings({"Улучшения:", "%effectBought%", "%maxOwners%", "%maxMembers%"})
        List<String> lore();

        @ConfDefault.DefaultStrings({"Эффекты:", "%effects%"})
        List<String> effectBought();

        @ConfDefault.DefaultString("%name%")
        String formatListEffect();

        @ConfDefault.DefaultString("Уровень максимальное количество овнеров: %amount%")
        String maxOwners();

        @ConfDefault.DefaultString("Уровень максимальное количество мемберов: %amount%")
        String maxMembers();
    }
}
