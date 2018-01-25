package org.assistant.sigma.ui.drawer;

import org.assistant.sigma.AbstractPresenter;
import org.assistant.sigma.model.dao.UserDao;
import org.assistant.sigma.model.entities.User;

/**
 * Created by giovanni on 24/01/18.
 *
 */
public class DrawerPresenter implements AbstractPresenter {
    private UserDao userDao;

    public DrawerPresenter() {
        userDao = new UserDao();
    }

    @Override
    public void onCreate() {
        userDao.onCreate();
    }

    @Override
    public void onDestroy() {
        userDao.onDestroy();
    }

    public User getActiveUser() {
        return userDao.findActive();
    }
}
