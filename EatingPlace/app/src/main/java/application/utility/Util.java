package application.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Uuser on 2015/4/27.
 */
public class Util {

    private static ProgressDialog progressDialog;

    public static void pushGeneralDialog(Context context, String title, String msg, String positiveMsg, String negativeMsg,
                                         final IGenericDialogUtil.IMaterialBtnClick target) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(context);
        mMaterialDialog.setTitle(title).setMessage(msg)
                .setPositiveButton(positiveMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        target.PositiveMethod(v);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton(negativeMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        target.NegativeMethod(v);
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    public static void showProgressDialog(Activity activity, String title,
                                          String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
