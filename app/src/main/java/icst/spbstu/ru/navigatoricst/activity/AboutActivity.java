package icst.spbstu.ru.navigatoricst.activity;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class AboutActivity extends BaseActivity {

    private ImageView ivIcstLogo;
    private TextView tvAboutInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ivIcstLogo = (ImageView)findViewById(R.id.ivAboutLogoIcst);
        tvAboutInfo = (TextView)findViewById(R.id.tvAboutInfo);

        initToolbar(true);
        setToolbarTitle(getString(R.string.about));
        enableUpButton();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickView (View view){
        ActivityUtilities.getInstance().invokeCustomUrlActivity(AboutActivity.this,
                CustomUrlActivity.class, getString(R.string.icst_site), getString(R.string.icst_url), false) ;
    }
}
