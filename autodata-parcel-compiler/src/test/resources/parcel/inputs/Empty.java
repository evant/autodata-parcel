import android.os.Parcelable;
import me.tatarka.autodata.base.AutoData;
import me.tatarka.autodata.plugins.AutoParcel;

@AutoData(defaults = false)
@AutoParcel
public abstract class Empty implements Parcelable {}
