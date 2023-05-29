package ru.mcsnapix.snapistones.plugin.modules.upgrade;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder(access = AccessLevel.PACKAGE)
@Accessors(fluent = true)
@Getter
public class UpgradeBlock {
    private final int levelMaxOwners;
    private final int levelMaxMembers;
    private final String effectBought;
}
