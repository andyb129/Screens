package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class ShortcutActivity extends Activity {

    public static final String INTENT_EXTRA_PACKAGE_1 = "pkg1";
    public static final String INTENT_EXTRA_PACKAGE_2 = "pkg2";

    public static Intent createShortcutIntent(Context context, String package1, String package2) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setClass(context, ShortcutActivity.class);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_1, package1);
        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_2, package2);

        /* shortcutIntent.putExtra("pkg", new String[]{
                        package1,
                        package2}); */

        return shortcutIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isInMultiWindowMode()) {
            thunderbirdsAreGo();
        } else {
            startService(new Intent(this, SplitScreenService.class));

            //Not in multi window mode and hasn't got accessibility enabled
            setContentView(R.layout.activity_shortcut);

            Button accessibilityOptionsButton = (Button) findViewById(R.id.accessibilityOptionsButton);
            accessibilityOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent accessibilityIntent = new Intent();
                    accessibilityIntent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);

                    startActivity(accessibilityIntent);
                }
            });
        }
    }

    public void thunderbirdsAreGo() {
        //Seems to be an issue with passing arrays?

        /* if (!getIntent().hasExtra("pkg")) {
            Toast.makeText(this, "No Packages", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent[] intents = new Intent[getIntent().getStringArrayListExtra("pkg").size()];
        int i = 0;

        for (String pkg : getIntent().getStringArrayListExtra("pkg")) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);

            intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK);

            intents[i] = intent;
            i++;
        }

        startActivities((intents); */

        Intent primaryIntent = getPackageManager().getLaunchIntentForPackage(getIntent().
                getStringExtra(INTENT_EXTRA_PACKAGE_1));
        primaryIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        primaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent secondaryIntent = getPackageManager().getLaunchIntentForPackage(getIntent().
                getStringExtra(INTENT_EXTRA_PACKAGE_2));
        secondaryIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivities(new Intent[]{primaryIntent, secondaryIntent});
        finish();
    }
}
