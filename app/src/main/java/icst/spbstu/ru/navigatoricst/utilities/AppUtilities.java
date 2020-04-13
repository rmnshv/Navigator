package icst.spbstu.ru.navigatoricst.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import icst.spbstu.ru.navigatoricst.R;

public class AppUtilities {

    private static long backPressed = 0;
    

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void tapPromptToExit(Activity activity){
        if (backPressed + 2500 > System.currentTimeMillis()){
            activity.finish();
        } else{
            showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.tap_again));
        }
        backPressed = System.currentTimeMillis();
    }

    private static void updateLink(Activity activity, String text){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null){
            activity.startActivity(intent);
        }
    }

    public static void shareApp(Activity activity) {
        try{
            final String appPackageName = activity.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_msg) +
                    " https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void rateThisApp (Activity activity){
        try{
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
