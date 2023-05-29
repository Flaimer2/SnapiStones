package ru.mcsnapix.snapistones.plugin.settings.message;

import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.List;

public interface Message {
    @ConfDefault.DefaultStrings({
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

    @ConfDefault.DefaultString("§cНет §fрегиона с названием §a%region_id%")
    String regionNotExist();

    @ConfDefault.DefaultString("§fВы §cне являетесь §fвладельцем региона §a%region_id%")
    String notOwner();

    @ConfDefault.DefaultString("§fВы §cне можете §fзащитить эту область")
    String cannotProtectArea();

    @ConfDefault.DefaultString("§fЭта территория теперь §aзащищена§f!")
    String protectedArea();

    @ConfDefault.DefaultString("§fВы §cне можете §fустановить блок привата здесь! Ваш регион §bпересекается §fс другим")
    String cannotPlaceProtectedBlock();

    @ConfDefault.DefaultString("§fВы §cсломали §fблок привата! Теперь ваша территория §cне защищена")
    String protectedBlockBroken();

    @SubSection
    RegionInfoSection regionInfo();

    @SubSection
    RegionListSection regionList();

    ElementOnPlayersSection elementOnPlayers();

    @SubSection
    CommandSection command();

    interface RegionInfoSection {
        @ConfDefault.DefaultString("§fИспользуйте: §a/rg info регион")
        String usage();

        @ConfDefault.DefaultStrings({
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

    interface RegionListSection {
        @ConfDefault.DefaultString("<newline>  <aqua><b>Список регионов</b></aqua><newline>       ")
        String header();

        @ConfDefault.DefaultString("  <white><count>. <green><hover:show_text:'<green>Нажмите, чтобы открыть меню региона'><click:run_command:/rg menu <region_id>><region_id></click></hover> <gray>– <white><location>")
        String region();

        @ConfDefault.DefaultString("  <white>У вас <red>нет <white>регионов")
        String noRegion();
    }

    interface ElementOnPlayersSection {
        @ConfDefault.DefaultString("%another_vault_prefix%%another_player_name%")
        String name();

        @ConfDefault.DefaultStrings({
                "",
                "§aНажмите, чтобы добавить игрока",
        })
        List<String> lore();
    }

    interface CommandSection {
        @ConfDefault.DefaultString("§fИгрока с таким ником §cне существует")
        String playerNotFound();

        @ConfDefault.DefaultString("§fИгрок, с данным ником, §cне состоит §fв вашем регионе")
        String playerNotInRegion();

        @ConfDefault.DefaultString("§fИгрок, с данным ником, §aуже есть §fв вашем регионе")
        String playerAlreadyInRegion();

        @SubSection
        CommandRemoveSection remove();

        @SubSection
        CommandAddMemberSection addMember();

        @SubSection
        CommandAddOwnerSection addOwner();

        interface CommandRemoveSection {
            @ConfDefault.DefaultString("§fИспользуйте: §a/rg remove регион игрок")
            String usage();

            @ConfDefault.DefaultString("§fИспользуйте: §a/rg remove %region_id% игрок")
            String usageAlt();

            @ConfDefault.DefaultString("§fВы §cне можете §fудалить себя")
            String cannotRemoveSelf();

            @ConfDefault.DefaultString("§fВы успешно §cудалили §fигрока &a%another_player_name%")
            String success();
        }

        interface CommandAddMemberSection {
            @ConfDefault.DefaultString("§fИспользуйте: §a/rg addmember регион игрок")
            String usage();

            @ConfDefault.DefaultString("§fИспользуйте: §a/rg addmember %region_id% игрок")
            String usageAlt();

            @ConfDefault.DefaultString("§fВы §cне можете §fдобавить себя")
            String cannotAddSelf();

            @ConfDefault.DefaultString("§fВы успешно §aдобавили §fигрока &a%another_player_name%")
            String success();
        }

        interface CommandAddOwnerSection {
            @ConfDefault.DefaultString("§fИспользуйте: §a/rg addowner регион игрок")
            String usage();

            @ConfDefault.DefaultString("§fИспользуйте: §a/rg addowner %region_id% игрок")
            String usageAlt();

            @ConfDefault.DefaultString("§fВы §cне можете §fдобавить себя")
            String cannotAddSelf();

            @ConfDefault.DefaultString("§fВы успешно §aдобавили §fигрока &a%another_player_name%")
            String success();
        }
    }
}