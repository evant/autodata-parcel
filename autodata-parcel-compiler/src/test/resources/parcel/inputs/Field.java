import android.os.Parcelable;
import me.tatarka.autodata.base.AutoData;
import me.tatarka.autodata.plugins.AutoParcel;

@AutoData(defaults = false)
@AutoParcel
public abstract class Field implements Parcelable {
    public abstract int test();
}
