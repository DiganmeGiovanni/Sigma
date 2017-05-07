package org.assistant.sigma.settings;

import org.assistant.sigma.BasePresenter;
import org.assistant.sigma.BaseView;
import org.assistant.sigma.model.entities.Settings;

/**
 *
 * Created by giovanni on 7/05/17.
 */
interface SettingsContract {

    interface Presenter extends BasePresenter {

        void loadSettings();

        void saveSettings(Settings settings);
    }

    interface View extends BaseView<Presenter> {

        Settings createSettingsFromForm();

        void setupSaveBtn();

        void showSettings(Settings settings);

        boolean validateSettings();
    }
}
