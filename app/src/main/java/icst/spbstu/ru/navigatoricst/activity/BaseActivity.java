package icst.spbstu.ru.navigatoricst.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Objects;

import icst.spbstu.ru.navigatoricst.R;

public class BaseActivity extends AppCompatActivity {

    private Context context;
    private Activity activity;

    private Toolbar toolbar;
    private LinearLayout loadingView, noDataView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = BaseActivity.this;
        context = activity.getApplicationContext();
    }

    public void initToolbar(boolean isTitleEnabled){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(isTitleEnabled);
    }

    public void setToolbarTitle(String title){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    public void enableUpButton(){
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void initLoader(){
        loadingView = (LinearLayout) findViewById(R.id.loadingView);
        noDataView = (LinearLayout) findViewById(R.id.noDataView);
    }

    public void showLoader(){
        if (loadingView != null){
            loadingView.setVisibility(View.VISIBLE);
        }

        if (noDataView != null){
            noDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader(){
        if (loadingView != null){
            loadingView.setVisibility(View.GONE);
        }

        if (noDataView != null){
            noDataView.setVisibility(View.GONE);

        }
    }

    public void showEmptyView(){
        if (loadingView != null){
            loadingView.setVisibility(View.GONE);
        }

        if (noDataView != null){
            noDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
