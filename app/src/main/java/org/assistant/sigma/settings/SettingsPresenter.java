package org.assistant.sigma.settings;

import org.assistant.sigma.model.entities.Settings;
import org.assistant.sigma.model.repositories.SettingsRepository;

/**
 *
 * Created by giovanni on 7/05/17.
 */
public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mSettingsView;
    private SettingsRepository settingsRepository;

    public SettingsPresenter(SettingsContract.View mSettingsView) {
        this.mSettingsView = mSettingsView;
        this.mSettingsView.setPresenter(this);

        settingsRepository = new SettingsRepository();
    }

    @Override
    public void onDestroy() {
        settingsRepository.onDestroy();
    }

    @Override
    public void loadSettings() {
        Settings settings = settingsRepository.activeUserSettings();
        mSettingsView.showSettings(settings);
    }

    @Override
    public void saveSettings(Settings settings) {
        settingsRepository.saveSettings(settings);
    }
}
