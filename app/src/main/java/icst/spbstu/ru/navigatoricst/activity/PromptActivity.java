package icst.spbstu.ru.navigatoricst.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;
import icst.spbstu.ru.navigatoricst.utilities.AppUtilities;

public class PromptActivity extends BaseActivity {

    private ConstraintLayout promptLayout;
    private LinearLayout llPrompt1, llPrompt2, llPrompt3;
    private Animation anim1, anim2;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        initToolbar(true);
        setToolbarTitle(getString(R.string.testing));
        enableUpButton();

        activity = PromptActivity.this;

        promptLayout = (ConstraintLayout)findViewById(R.id.promptLayout);

        llPrompt1 = (LinearLayout) findViewById(R.id.llPrompt1);
        llPrompt2 = (LinearLayout) findViewById(R.id.llPrompt2);
        llPrompt3 = (LinearLayout) findViewById(R.id.llPrompt3);

        llPrompt1.setVisibility(View.INVISIBLE);
        llPrompt2.setVisibility(View.INVISIBLE);
        llPrompt3.setVisibility(View.INVISIBLE);

        anim1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.prompt_anim_1);
        anim2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.prompt_anim_2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityUtilities.getInstance().invokeNewActivity(activity, MainActivity.class, true);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFunctionality(){
        promptLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                llPrompt1.startAnimation(anim1);
                llPrompt2.startAnimation(anim2);
                llPrompt3.startAnimation(anim1);
                Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        llPrompt1.setVisibility(View.VISIBLE);
                        llPrompt2.setVisibility(View.VISIBLE);
                        llPrompt3.setVisibility(View.VISIBLE);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ActivityUtilities.getInstance().invokeNewActivity(activity, TestingActivity.class, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };
                anim1.setAnimationListener(animationListener);
            }
        }, AppConstants.PROMPT_DURATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunctionality();
    }

    @Override
    public void onBackPressed() {
        ActivityUtilities.getInstance().invokeNewActivity(activity, MainActivity.class, true);
    }
}
