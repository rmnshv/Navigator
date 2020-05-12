package icst.spbstu.ru.navigatoricst.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Objects;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.data.sqlite.NotificationDbController;
import icst.spbstu.ru.navigatoricst.notifications.MyFirebaseMessagingService;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class SplashActivity extends BaseActivity {

    private ImageView imageView;
    private Animation animation;
    private ConstraintLayout layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkNotification();

        layout = (ConstraintLayout)findViewById(R.id.splashLayout);
        imageView = (ImageView)findViewById(R.id.ivSplashIcon);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
    }

    private void checkNotification() {
        Intent intent = getIntent();
        if (intent != null){
            Bundle data = intent.getExtras();
            if (data != null){
                String title = data.getString("title");
                String messageBody = data.getString("message");
                String url = data.getString("url");
                if (title != null || messageBody != null || url != null){
                    NotificationDbController notificationDbController = new NotificationDbController(SplashActivity.this);
                    notificationDbController.insertData(title, messageBody, url);
                    Intent notifIntent = new Intent(AppConstants.NEW_NOTI);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(notifIntent);
                }

            }
        }
    }

    private void initFunctionality(){
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ActivityUtilities.getInstance().invokeNewActivity(SplashActivity.this, MainActivity.class, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }, AppConstants.SPLASH_DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }
}
