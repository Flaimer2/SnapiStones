package ru.mcsnapix.snapistones.plugin.modules.home.config;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

public interface HomeConfig {
    @SubSection
    CommandsSection commands();

    interface CommandsSection {
        @SubSection
        HomeSection home();

        @SubSection
        SetHomeSection setHome();

        interface HomeSection {
            @ConfDefault.DefaultString("§fНапишите название регион, чтобы туда §aтелепортироваться")
            String writeRegionName();

            @ConfDefault.DefaultString("§fВы §cне являетесь §fучастником региона")
            String noMember();

            @ConfDefault.DefaultString("§fВ регионе §cнет §fточки дома")
            String noHomeInRegion();

            @ConfDefault.DefaultString("§fВы были §aуспешно §fтелепортированы домой")
            String success();
        }

        interface SetHomeSection {
            @ConfDefault.DefaultString("§fВы сейчас находитесь §cне в регионе§f! Чтобы создать точку дома, вы должны §aстоять §fв вашем регионе")
            String notInRegion();

            @ConfDefault.DefaultString("§fВы §cне являетесь §fвладелецем региона")
            String notOwner();

            @ConfDefault.DefaultString("§fВы успешно §aустановили §fточку дома")
            String success();
        }
    }
}
