package uk.co.keepawayfromfire.screens;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cj on 19/11/16.
 */
public class ApplicationInfoAdapter extends ArrayAdapter<ApplicationSummary> {

    private final Context context;
    private final PackageManager packageManager;
    private final ArrayList<ApplicationSummary> values;

    public ApplicationInfoAdapter(Context context, ArrayList<ApplicationSummary> values) {
        super(context, -1, values);

        this.context = context;
        this.packageManager = context.getPackageManager();
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View packageView = convertView;

        if (packageView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            packageView = inflater.inflate(R.layout.package_view, parent, false);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.iconImageView = (ImageView) packageView.findViewById(R.id.iconImageView);
            viewHolder.nameTextView = (TextView) packageView.findViewById(R.id.nameTextView);
            viewHolder.packageNameTextView = (TextView) packageView.findViewById(
                    R.id.packageNameTextView);

            packageView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) packageView.getTag();
        //TODO - fix the storage of icon
        //viewHolder.iconImageView.setImageDrawable(values.get(position).getIcon());
        viewHolder.nameTextView.setText(values.get(position).getLabel());
        viewHolder.packageNameTextView.setText(values.get(position).getPackageName());

        return packageView;
    }

    static class ViewHolder {
        public ImageView iconImageView;
        public TextView nameTextView;
        public TextView packageNameTextView;
    }
}
