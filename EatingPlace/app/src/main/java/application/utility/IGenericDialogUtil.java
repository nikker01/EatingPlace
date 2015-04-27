package application.utility;

import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Uuser on 2015/4/27.
 */
public class IGenericDialogUtil {

    public interface IMaterialBtnClick {
        public abstract void PositiveMethod(View v);
        public abstract void NegativeMethod(View v);
    }

    public interface IGenericBtnClickListener {
        public abstract void PositiveMethod(DialogInterface dialog, int id);
        public abstract void NegativeMethod(DialogInterface dialog, int id);
    }

    public interface PositiveBtnClickListener {
        public abstract void PositiveMethod(DialogInterface dialog, int id);
    }
}
