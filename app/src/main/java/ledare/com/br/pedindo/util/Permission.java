package ledare.com.br.pedindo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class Permission {
    public static boolean hasStoragePermission(Context context){
        return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestStoragePermission(Activity activity, int requestCode){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                requestCode);
    }
}
