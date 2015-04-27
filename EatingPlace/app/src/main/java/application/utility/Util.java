package application.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

/**
 * Created by Uuser on 2015/4/27.
 */
public class Util {

    private static ProgressDialog progressDialog;

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
