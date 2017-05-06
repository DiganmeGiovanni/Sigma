package org.assistant.sigma.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 *
 * Created by giovanni on 20/04/17.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id
     * {@code frameId}. The operation is performed by the {@code fragmentManager}.
     *
     * @param fragmentManager Fragment manager to perform operation
     * @param fragment Fragment to add to {@code frameId}
     * @param frameId Container where to put fragment
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * The {@code fragment} is added to the container view with id
     * {@code frameId}. The operation is performed by the {@code fragmentManager}.
     * and {@code fragment} is added to back stack with given tag
     *
     * @param fragmentManager Fragment manager to perform operation
     * @param fragment Fragment to add to {@code frameId}
     * @param frameId Container where to put fragment
     */
    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
