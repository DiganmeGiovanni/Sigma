package org.assistant.sigma;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.assistant.sigma.utils.services.DBInitializer;

import io.realm.Realm;

/**
 * Created by giovanni on 4/05/17.
 *
 */
public class SigmaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build()
        );

        Iconify.with(new MaterialModule());

        // Init/Verify database
        DBInitializer.init(this);
    }
}
