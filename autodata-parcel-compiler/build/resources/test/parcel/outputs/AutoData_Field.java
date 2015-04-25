import android.os.Parcel;
import android.os.Parcelable;

final class AutoData_Field extends Field {
    public static final Parcelable.Creator<AutoData_Field> CREATOR = new Parcelable.Creator<AutoData_Field>() {
        @Override
        public AutoData_Field createFromParcel(Parcel in) {
            return new AutoData_Field(in);
        }

        @Override
        public AutoData_Field[] newArray(int size) {
            return new AutoData_Field[size];
        }
    };

    private final int test;

    AutoData_Field(int test) {
        this.test = test;
    }

    private AutoData_Field(Parcel in) {
        this((int) in.readValue(AutoData_Field.class.getClassLoader()));
    }

    @Override
    public int test() {
        return test;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.test);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}