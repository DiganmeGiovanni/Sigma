package org.assistant.sigma;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.assistant.sigma.model.migrations.Migration;
import org.assistant.sigma.utils.services.DBInitializer;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by giovanni on 4/05/17.
 *
 */
public class SigmaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
//                .assetFile("default.realm")
                .schemaVersion(5)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(config);
        DBInitializer.init(this); // Init/Verify database

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build()
        );

        Iconify.with(new MaterialModule());
    }
}
