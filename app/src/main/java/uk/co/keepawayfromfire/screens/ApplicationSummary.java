package uk.co.keepawayfromfire.screens;


import android.os.Parcel;
import android.os.Parcelable;

public class ApplicationSummary implements Parcelable {

    private long _id;
    private String packageName;
    private String label;
    private int icon;

    public ApplicationSummary(CharSequence label, int icon, String packageName) {
        this.label = label.toString();
        this.icon = icon;
        this.packageName = packageName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.packageName);
        dest.writeString(this.label);
        dest.writeInt(this.icon);
    }

    protected ApplicationSummary(Parcel in) {
        this._id = in.readLong();
        this.packageName = in.readString();
        this.label = in.readString();
        this.icon = in.readInt();
    }

    public static final Parcelable.Creator<ApplicationSummary> CREATOR = new Parcelable.Creator<ApplicationSummary>() {
        public ApplicationSummary createFromParcel(Parcel source) {
            return new ApplicationSummary(source);
        }

        public ApplicationSummary[] newArray(int size) {
            return new ApplicationSummary[size];
        }
    };
}
