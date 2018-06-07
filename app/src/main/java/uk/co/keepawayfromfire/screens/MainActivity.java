package uk.co.keepawayfromfire.screens;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final int FIRST_APP_ICON = 1;
    private static final int SECOND_APP_ICON = 2;

    private boolean isTabletLayout;

    private ApplicationSummary package1;
    private ApplicationSummary package2;
    private ImageView quickPic1Button, quickPic2Button;
    private View icon1, icon2;

    private View.OnClickListener mPackagePickerApp1OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            launchPackagePicker(view, R.id.package1View);
        }
    };

    private View.OnClickListener mPackagePickerApp2OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            launchPackagePicker(view, R.id.package2View);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        isTabletLayout = findViewById(R.id.quickPic1Button) == null;

        if (savedInstanceState != null) {
            package1 = savedInstanceState.getParcelable("package1");
            package2 = savedInstanceState.getParcelable("package2");
            updateUi();
        }

        if (isTabletLayout) {
            FragmentManager fragmentManager = getFragmentManager();
            final PackagePickerFragment a = (PackagePickerFragment) fragmentManager.findFragmentById(R.id.pkg1);
            final PackagePickerFragment b = (PackagePickerFragment) fragmentManager.findFragmentById(R.id.pkg2);

            a.setApplicationInfoLisener(new PackagePickerFragment.AppInfoChangeListener() {
                @Override
                public void onAppInfoChanged(ApplicationSummary applicationSummary) {
                    package1 = applicationSummary;

                    updateUi();
                }
            });
            b.setApplicationInfoLisener(new PackagePickerFragment.AppInfoChangeListener() {
                @Override
                public void onAppInfoChanged(ApplicationSummary applicationSummary) {
                    package2 = applicationSummary;

                    updateUi();
                }
            });
        } else {
            quickPic1Button = (ImageView) findViewById(R.id.quickPic1Button);
            quickPic2Button = (ImageView) findViewById(R.id.quickPic2Button);
            icon1 = findViewById(R.id.package1View);
            icon2 = findViewById(R.id.package2View);

            quickPic1Button.setOnClickListener(mPackagePickerApp1OnClickListener);
            quickPic2Button.setOnClickListener(mPackagePickerApp2OnClickListener);
            icon1.setOnClickListener(mPackagePickerApp1OnClickListener);
            icon2.setOnClickListener(mPackagePickerApp2OnClickListener);
        }

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final Button createShortcutButton = (Button) findViewById(R.id.createShortcutButton);

        createShortcutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (package1 == null || package2 == null) {
                    Snackbar.make(view, "Please select two packages", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Intent installIntent = new Intent();
                installIntent.setAction(ACTION_INSTALL_SHORTCUT);

                installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                        nameEditText.getText().toString());
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(view.getContext(),
                                R.drawable.logo));
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                        ShortcutActivity.createShortcutIntent(view.getContext(),
                                package1.getPackageName(),
                                package2.getPackageName()
                        ));

                view.getContext().sendBroadcast(installIntent);

                Intent launcherIntent = new Intent();
                launcherIntent.setAction(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_HOME);

                startActivity(launcherIntent);
            }
        });

        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        ApplicationSummary applicationSummary = (ApplicationSummary) data.getParcelableExtra(PackagePickerFragment.INTENT_EXTRA_PACKAGE);

        if (requestCode == R.id.package1View) {
            setAddButtonVisibility(false, FIRST_APP_ICON);
            package1 = applicationSummary;
        } else {
            setAddButtonVisibility(false, SECOND_APP_ICON);
            package2 = applicationSummary;
        }

        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable("package1", package1);
        outState.putParcelable("package2", package2);
    }

    private void launchPackagePicker(View view, int package1View) {
        Intent intent = new Intent(view.getContext(), PackagePickerActivity.class);
        startActivityForResult(intent, package1View);
    }

    private void setAddButtonVisibility(boolean isVisible, int iconViewNumber) {
        if (iconViewNumber==FIRST_APP_ICON) {
            quickPic1Button.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            icon1.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        } else if (iconViewNumber==SECOND_APP_ICON) {
            quickPic2Button.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            icon2.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        }
    }

    public void updateUi() {
        if (isTabletLayout) {

        } else {
            updatePackageView(package1, (ImageView) findViewById(R.id.package1View));
            updatePackageView(package2, (ImageView) findViewById(R.id.package2View));
        }
    }

    private void updatePackageView(ApplicationSummary applicationSummary, ImageView packageView) {
        if (applicationSummary == null) {
            packageView.setImageDrawable(null);
        } else {
            try {
                ApplicationInfo applicationInfo = getPackageManager()
                        .getApplicationInfo(applicationSummary.getPackageName(), PackageManager.GET_META_DATA);
                int icon = applicationInfo.icon;
                Resources resources = getPackageManager().getResourcesForApplication(applicationInfo);
                packageView.setImageDrawable(resources.getDrawable(icon));
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
    }
}
