package uk.co.keepawayfromfire.screens;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class PackagePickerFragment extends ListFragment {

    public static final String INTENT_EXTRA_PACKAGE = "pkg";

    AppInfoChangeListener listener;
    private boolean isTabletLayout;
    private int selectedItem;



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*PackageManager packageManager = getActivity().getPackageManager();

        ArrayList<ApplicationInfo> acceptablePackages = new ArrayList<>();
        List<PackageInfo> allPackages = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA);

        for (PackageInfo packageInfo : allPackages) {
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                acceptablePackages.add(packageInfo.applicationInfo);
            }
        }*/

        ArrayList<ApplicationSummary> acceptablePackages = new ArrayList<>();
        QueryResultIterable<ApplicationSummary> itr = null;
        try {
            itr = cupboard()
                    .withDatabase(ScreensApplication.getInstance().getAppDatabase().getWritableDatabase())
                    .query(ApplicationSummary.class).query();
            for (ApplicationSummary applicationSummary : itr) {
                acceptablePackages.add(applicationSummary);
            }
        } finally {
            if (itr != null) itr.close();
        }

        Collections.sort(acceptablePackages, new ApplicationInfoSorter());

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt("selectedItem", 0);
            getListView().setSelection(selectedItem);
        }

        isTabletLayout = getActivity().findViewById(R.id.nameEditText) != null;
        if (isTabletLayout)
            getListView().setSelector(R.drawable.package_list_selector);

        setListAdapter(new ApplicationInfoAdapter(this.getContext(), acceptablePackages));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        getListView().setItemChecked(position, true);

        ApplicationSummary applicationSummary = (ApplicationSummary) l.getItemAtPosition(position);

        if (isTabletLayout) {
            if (listener != null)
                listener.onAppInfoChanged(applicationSummary);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(INTENT_EXTRA_PACKAGE, applicationSummary);

            getActivity().setResult(getActivity().RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("selectedItem", selectedItem);
    }

    public void setApplicationInfoLisener(AppInfoChangeListener applicationInfoLisener) {
        this.listener = applicationInfoLisener;
    }

    public interface AppInfoChangeListener {
        void onAppInfoChanged(ApplicationSummary applicationSummary);
    }
}
