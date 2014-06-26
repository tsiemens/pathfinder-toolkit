package com.lateensoft.pathfinder.toolkit.inject;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.AndroidRuntimeException;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.lateensoft.pathfinder.toolkit.pref.AppPreferences;
import com.lateensoft.pathfinder.toolkit.pref.Preferences;

public class ApplicationModule extends AbstractModule {
    private final Application application;
    private final Provider<PackageInfo> packageInfoProvider;
    private final Preferences preferences;

    public ApplicationModule(Application application, Provider<PackageInfo> packageInfoProvider, Preferences preferences) {
        this.application = application;
        this.packageInfoProvider = packageInfoProvider;
        this.preferences = preferences;
    }

    public ApplicationModule(Application application, Preferences preferences) {
        this(application, null, preferences);
    }

    @Override
    protected void configure() {
        bind(Preferences.class).toInstance(preferences);

        Provider<PackageInfo> infoProvider = packageInfoProvider == null ? new DefaultPackageInfoProvider() : packageInfoProvider;
        bind(PackageInfo.class).toProvider(infoProvider);
    }

    private class DefaultPackageInfoProvider implements Provider<PackageInfo> {

        @Override
        public PackageInfo get() {
            try {
                PackageManager pm = application.getPackageManager();
                if (pm != null) {
                    return pm.getPackageInfo(application.getPackageName(), 0);
                } else {
                    throw new AndroidRuntimeException("No package manager found for application");
                }
            } catch (PackageManager.NameNotFoundException e) {
                throw new AndroidRuntimeException("Failed to get PackageInfo for " + application.getPackageName(), e);
            }
        }
    }
}
