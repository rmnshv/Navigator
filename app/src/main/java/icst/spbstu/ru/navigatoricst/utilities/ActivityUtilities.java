package icst.spbstu.ru.navigatoricst.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import icst.spbstu.ru.navigatoricst.constants.AppConstants;

public class ActivityUtilities {

    private static ActivityUtilities activityUtilities = null;

    public static ActivityUtilities getInstance() {
        if (activityUtilities == null){
            activityUtilities = new ActivityUtilities();
        }
        return activityUtilities;
    }

    public void invokeNewActivity(Activity activity, Class<?> tClass, boolean shouldFinish){
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish){
            activity.finish();
        }
    }

    public void invokeCustomUrlActivity(Activity activity, Class<?> tClass, String pageTitle,
                                        String pageUrl, boolean shouldFinish){
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstants.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstants.BUNDLE_KEY_URL, pageUrl);

        activity.startActivity(intent);

        if (shouldFinish){
            activity.finish();
        }
    }

    public void invokeScoreCardActivity(Activity activity, Class<?> tClass, ArrayList<Integer> mDirectionsScores,
                                        boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstants.BUNDLE_KEY_DIRECTIONS_SCORES, mDirectionsScores);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeDetailedResultActivity(Activity activity, Class<?> tClass, int id,
                                        boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstants.BUNDLE_KEY_ID, id);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

}
