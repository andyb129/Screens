package uk.co.keepawayfromfire.screens;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ScreensApplication extends Application {

    private CupboardSQLiteOpenHelper appDatabase;
    private static ScreensApplication self;

    public static ScreensApplication getInstance() {
        if (self==null) {
            self = new ScreensApplication();
        }
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = new CupboardSQLiteOpenHelper(this);

        populateAppsInDb();
    }

    public CupboardSQLiteOpenHelper getAppDatabase() {
        return appDatabase;
    }

    private void populateAppsInDb() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = getApplicationContext().getPackageManager();

                //ArrayList<ApplicationInfo> acceptablePackages = new ArrayList<>();
                List<PackageInfo> allPackages = packageManager.getInstalledPackages(
                        PackageManager.GET_META_DATA);

                for (PackageInfo packageInfo : allPackages) {
                    if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                        //acceptablePackages.add(packageInfo.applicationInfo);

                        //TODO - sort out icon below
                        ApplicationSummary applicationSummary = new ApplicationSummary(packageInfo.applicationInfo.loadLabel(packageManager),
                                /*packageInfo.applicationInfo.loadIcon(packageManager)*/0,
                                packageInfo.applicationInfo.packageName);
                        cupboard().withDatabase(appDatabase.getWritableDatabase()).put(applicationSummary);
                    }
                }

                //Collections.sort(acceptablePackages, new ApplicationInfoSorter(packageManager));
            }
        }).start();
    }
}
