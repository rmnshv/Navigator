package icst.spbstu.ru.navigatoricst.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Objects;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.data.preference.AppPreference;
import icst.spbstu.ru.navigatoricst.data.sqlite.NotificationDbController;
import icst.spbstu.ru.navigatoricst.models.NotificationModel;
import icst.spbstu.ru.navigatoricst.notifications.MyFirebaseMessagingService;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;
import icst.spbstu.ru.navigatoricst.utilities.AppUtilities;

public class MainActivity extends BaseActivity {

    private Activity activity;
    private Context context;

    Toolbar toolbar = null;

    private Drawer drawer = null;

    private Button btnStart;
    private RelativeLayout mNotificationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initNavigation(savedInstanceState);

        initListeners();



    }

    private void initNavigation(Bundle savedInstanceState) {
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header_background)
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withHasStableIds(true)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.about)
                                .withIcon(R.mipmap.app_logo)
                                .withIdentifier(10)
                                .withSelectable(false),

                        new SecondaryDrawerItem()
                                .withName(R.string.last_res)
                                .withIcon(R.drawable.prev_icon)
                                .withIdentifier(20)
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.info_about)
                                .withIcon(R.drawable.info_about_icon)
                                .withIdentifier(21)
                                .withSelectable(false),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.rate)
                                .withIcon(R.drawable.rate_icon)
                                .withIconColor(Color.WHITE)
                                .withIdentifier(30)
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.share)
                                .withIcon(R.drawable.share_icon)
                                .withIdentifier(31)
                                .withSelectable(false),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.support)
                                .withIcon(R.drawable.support_icon)
                                .withIdentifier(40)
                                .withSelectable(false),
                        new SecondaryDrawerItem()
                                .withName(R.string.exit)
                                .withIcon(R.drawable.exit_icon)
                                .withIdentifier(41)
                                .withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null){
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 10){
                                ActivityUtilities.getInstance().invokeNewActivity(activity,
                                        AboutActivity.class, false);

                            } else if (drawerItem.getIdentifier() == 20){
                                String res = AppPreference.getInstance(context).getString(AppConstants.LAST_RES);
                                if (res == null){
                                    AppUtilities.showToast(context, getResources().getString(R.string.no_last_res));
                                } else{
                                    ArrayList<Integer> mDirectionsScores = new ArrayList<>();
                                    for (String part : res.split("\\s")){
                                        mDirectionsScores.add(Integer.parseInt(part));
                                    }

                                    ActivityUtilities.getInstance().invokeScoreCardActivity(activity, ScoreCardActivity.class,
                                            mDirectionsScores, true);
                                }

                            } else if (drawerItem.getIdentifier() == 21){
                                ActivityUtilities.getInstance().invokeCustomUrlActivity(activity,
                                        CustomUrlActivity.class, getString(R.string.edu_title),
                                        getString(R.string.icst_edu_url), false);

                            } else if (drawerItem.getIdentifier() == 30){
                                AppUtilities.rateThisApp(activity);

                            } else if (drawerItem.getIdentifier() == 31){
                                AppUtilities.shareApp(activity);

                            } else if (drawerItem.getIdentifier() == 40){
                                ActivityUtilities.getInstance().invokeNewActivity(activity,
                                        SupportActivity.class, false);

                            } else if (drawerItem.getIdentifier() == 41){
                                finish();
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private void initVar() {
        activity = MainActivity.this;
        context = activity.getApplicationContext();
    }

    private void initView() {
        setContentView(R.layout.activity_main_1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnStart = (Button)findViewById(R.id.btnMainStart);
        mNotificationView = (RelativeLayout)findViewById(R.id.notificationView);
    }

    private void initListeners() {

        mNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.getInstance().invokeNewActivity(activity, NotificationListActivity.class, false);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.getInstance().invokeNewActivity(activity, PromptActivity.class, true);
            }
        });
    }

    // received new broadcast
    private BroadcastReceiver newNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initNotification();
        }
    };

    private void initNotification() {

        NotificationDbController notificationDbController = new NotificationDbController(context);
        TextView notificationCount = (TextView) findViewById(R.id.notificationCount);
        notificationCount.setVisibility(View.INVISIBLE);

        ArrayList<NotificationModel> notiArrayList = notificationDbController.getUnreadData();

        if (notiArrayList != null && !notiArrayList.isEmpty()) {
            int totalUnread = notiArrayList.size();
            if (totalUnread > 0) {
                notificationCount.setVisibility(View.VISIBLE);
                notificationCount.setText(String.valueOf(totalUnread));
            } else {
                notificationCount.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(AppConstants.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).registerReceiver(newNotificationReceiver, intentFilter);

        initNotification();

    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()){
            drawer.closeDrawer();
        } else{
            AppUtilities.tapPromptToExit(this);
        }
    }
}
