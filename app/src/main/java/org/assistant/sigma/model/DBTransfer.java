package org.assistant.sigma.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import io.realm.Realm;

/**
 * Created by giovanni on 4/01/18.
 *
 */
public class DBTransfer {
    private static final String TAG = "DB_TRANSFER";
    private File EXTERNAL_PUBLIC_DOWNLOADS_PATH = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String DBName = "default.realm";

    public void backupDatabase(Context mContext) {
        Realm realm = Realm.getDefaultInstance();
        EXTERNAL_PUBLIC_DOWNLOADS_PATH.mkdirs();
        File backupFile = new File(EXTERNAL_PUBLIC_DOWNLOADS_PATH, DBName);
        backupFile.delete();

        realm.writeCopyTo(backupFile);
        realm.close();

        Log.d(TAG, "Realm exported to: " + backupFile.getPath());
        Toast.makeText(mContext, "Realm exported to: " + backupFile.getPath(), Toast.LENGTH_LONG).show();
    }
}
