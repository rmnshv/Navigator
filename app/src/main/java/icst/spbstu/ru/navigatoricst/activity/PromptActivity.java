package icst.spbstu.ru.navigatoricst.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class PromptActivity extends BaseActivity {

    private ImageView ivPrompt;
    private ConstraintLayout layout;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        initToolbar(true);
        setToolbarTitle(getString(R.string.testing));
        enableUpButton();

        layout = (ConstraintLayout)findViewById(R.id.promptLayout);
        ivPrompt = (ImageView)findViewById(R.id.ivPrompt);
        ivPrompt.setVisibility(View.INVISIBLE);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFunctionality(){
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivPrompt.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivPrompt.setVisibility(View.VISIBLE);
                        // TODO invoke question activity
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
