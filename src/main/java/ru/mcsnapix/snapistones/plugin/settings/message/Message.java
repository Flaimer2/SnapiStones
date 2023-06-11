package ru.mcsnapix.snapistones.plugin.settings.message;

import space.arim.dazzleconf.annote.SubSection;

import java.util.List;

import static space.arim.dazzleconf.annote.ConfDefault.DefaultString;
import static space.arim.dazzleconf.annote.ConfDefault.DefaultStrings;

public interface Message {
    @DefaultStrings({
            "",
            "  §e§lКоманды приватов на Анархии",
            "",
            "  §fИнформация о регионе §7– §a/rg info регион",
            "  §fДобавить владельца §7– §a/rg addowner регион игрок",
            "  §fДобавить участника §7– §a/rg addmember регион игрок",
            "  §fИсключить игрока §7– §a/rg remove регион игрок",
            "  §fУстановить точку дома §7– §a/rg sethome",
            "  §fТелепортироваться домой §7– §a/rg home регион",
            "  §fСписок регионов §7– §a/rg list",
            "  §fОткрыть меню блока привата §7– §a/rg menu регион",
            ""
    })
    List<String> help();

    @DefaultString("§cНет §fрегиона с названием §a%region_id%")
    String regionNotExist();

    @DefaultString("§fВы §cне являетесь §fвладельцем региона §a%region_id%")
    String notOwner();

    @DefaultString("§fВы §cне можете §fзащитить эту область")
    String cannotProtectArea();

    @DefaultString("§fЭта территория теперь §aзащищена§f!")
    String protectedArea();

    @DefaultString("§fВы §cне можете §fустановить блок привата здесь! Ваш регион §bпересекается §fс другим")
    String cannotPlaceProtectedBlock();

    @DefaultString("§fВы §cсломали §fблок привата! Теперь ваша территория §cне защищена")
    String protectedBlockBroken();

    @SubSection
    RegionInfo regionInfo();

    interface RegionInfo {
        @DefaultString("§fИспользуйте: §a/rg info регион")
        String usage();

        @DefaultStrings({
                "",
                "  §e§lИнформация о регионе:",
                "",
                "  §fНазвание региона: §a%region_id%",
                "  §fРазмер региона: §a%region_size%",
                "  §fВладельцы: §a%region_owners%",
                "  §fУчастники: §a%region_members%",
                ""
        })
        List<String> message();
    }

    @SubSection
    RegionList regionList();

    interface RegionList {
        @DefaultString("<newline>  <aqua><b>Список регионов</b></aqua><newline>       ")
        String header();

        @DefaultString("  <white><count>. <green><hover:show_text:'<green>Нажмите, чтобы открыть меню региона'><click:run_command:/rg menu <region_id>><region_id></click></hover> <gray>– <white><location>")
        String region();

        @DefaultString("  <white>У вас <red>нет <white>регионов")
        String noRegion();
    }

    @SubSection
    Command command();

    interface Command {
        @DefaultString("§fИгрока с таким ником §cне существует")
        String playerNotFound();

        @DefaultString("§fИгрок, с данным ником, §cне состоит §fв вашем регионе")
        String playerNotInRegion();

        @DefaultString("§fИгрок, с данным ником, §aуже есть §fв вашем регионе")
        String playerAlreadyInRegion();

        @SubSection
        CommandRemove remove();

        interface CommandRemove {
            @DefaultString("§fИспользуйте: §a/rg remove регион игрок")
            String usage();

            @DefaultString("§fИспользуйте: §a/rg remove %region_id% игрок")
            String usageAlt();

            @DefaultString("§fВы §cне можете §fудалить себя")
            String cannotRemoveSelf();

            @DefaultString("§fВы успешно §cудалили §fигрока &a%another_player_name%")
            String success();
        }

        @SubSection
        CommandAddMember addMember();

        interface CommandAddMember {
            @DefaultString("§fИспользуйте: §a/rg addmember регион игрок")
            String usage();

            @DefaultString("§fИспользуйте: §a/rg addmember %region_id% игрок")
            String usageAlt();

            @DefaultString("§fВы §cне можете §fдобавить себя")
            String cannotAddSelf();

            @DefaultString("§fВы успешно §aдобавили §fигрока &a%another_player_name%")
            String success();
        }

        @SubSection
        CommandAddOwner addOwner();

        interface CommandAddOwner {
            @DefaultString("§fИспользуйте: §a/rg addowner регион игрок")
            String usage();

            @DefaultString("§fИспользуйте: §a/rg addowner %region_id% игрок")
            String usageAlt();

            @DefaultString("§fВы §cне можете §fдобавить себя")
            String cannotAddSelf();

            @DefaultString("§fВы успешно §aдобавили §fигрока &a%another_player_name%")
            String success();
        }
    }
}