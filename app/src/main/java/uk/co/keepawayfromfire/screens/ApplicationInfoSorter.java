package uk.co.keepawayfromfire.screens;

import java.util.Comparator;

public class ApplicationInfoSorter implements Comparator<ApplicationSummary> {

    @Override
    public int compare(ApplicationSummary a1, ApplicationSummary a2) {
        return a1.getLabel().toString().compareToIgnoreCase(
                a2.getLabel().toString());
    }
}
