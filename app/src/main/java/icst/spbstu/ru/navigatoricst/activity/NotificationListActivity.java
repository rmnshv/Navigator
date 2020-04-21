package icst.spbstu.ru.navigatoricst.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.adapters.NotificationAdapter;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.data.sqlite.NotificationDbController;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;
import icst.spbstu.ru.navigatoricst.models.NotificationModel;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;
import icst.spbstu.ru.navigatoricst.utilities.DialogUtilities;

public class NotificationListActivity extends BaseActivity implements DialogUtilities.OnCompleteListener {

    private Context mContext;
    private Activity mActivity;

    private RecyclerView mRecycler;
    private NotificationAdapter mNotificationAdapter;
    private ArrayList<NotificationModel> mNotificationList;
    private MenuItem mMenuItemDeleteAll;
    private NotificationDbController mNotificationDbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = NotificationListActivity.this;
        mContext = mActivity.getApplicationContext();

        initVar();
        initView();
        initListener();
    }

    private void initVar() {
        mNotificationList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_notification);

        mRecycler = (RecyclerView) findViewById(R.id.rv_recycler);
        mNotificationAdapter = new NotificationAdapter(mActivity, mNotificationList);
        mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecycler.setAdapter(mNotificationAdapter);

        initLoader();
        initToolbar(true);
        setToolbarTitle(getString(R.string.notifications));
        enableUpButton();
    }

    private void updateUI() {
        showLoader();

        if (mNotificationDbController == null) {
            mNotificationDbController = new NotificationDbController(mContext);
        }
        mNotificationList.clear();
        mNotificationList.addAll(mNotificationDbController.getAllData());
        mNotificationAdapter.notifyDataSetChanged();

        hideLoader();

        if (mNotificationList.size() == 0) {
            showEmptyView();
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(false);
            }
        } else {
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(true);
            }
        }
    }

    private void initListener() {
        // recycler list item click listener
        mNotificationAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mNotificationDbController.updateStatus(mNotificationList.get(position).getId(), true);

                ActivityUtilities.getInstance().invokeCustomUrlActivity(mActivity, CustomUrlActivity.class,
                        mNotificationList.get(position).getTitle(),
                        mNotificationList.get(position).getUrl(),
                        false);


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menus_delete_all:
                FragmentManager manager = getSupportFragmentManager();
                DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.notifications),
                        getString(R.string.delete_all_notification), getString(R.string.yes), getString(R.string.no),
                        AppConstants.BUNDLE_KEY_DELETE_ALL_NOT);

                dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_all, menu);
        mMenuItemDeleteAll = menu.findItem(R.id.menus_delete_all);

        updateUI();

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNotificationAdapter != null) {
            updateUI();
        }
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            if (viewIdText.equals(AppConstants.BUNDLE_KEY_DELETE_ALL_NOT)) {
                mNotificationDbController.deleteAllNot();
                updateUI();
            }
        }
    }

}
