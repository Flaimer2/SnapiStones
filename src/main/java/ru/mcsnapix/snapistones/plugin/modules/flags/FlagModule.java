package ru.mcsnapix.snapistones.plugin.modules.flags;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.mcsnapix.snapistones.plugin.modules.Module;
import ru.mcsnapix.snapistones.plugin.modules.interfaces.IModule;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class FlagModule implements IModule {
    @NonNull
    private final Module module;

    @Override
    public void load() {

    }

    @Override
    public void reload() {

    }
}
