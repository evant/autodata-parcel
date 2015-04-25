import android.os.Parcel;
import android.os.Parcelable;

final class AutoData_Empty extends Empty {
    public static final Parcelable.Creator<AutoData_Empty> CREATOR = new Parcelable.Creator<AutoData_Empty>() {
        @Override
        public AutoData_Empty createFromParcel(Parcel in) {
            return new AutoData_Empty();
        }

        @Override
        public AutoData_Empty[] newArray(int size) {
            return new AutoData_Empty[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }
}