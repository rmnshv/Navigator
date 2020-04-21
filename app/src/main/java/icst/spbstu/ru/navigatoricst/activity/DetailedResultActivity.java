package icst.spbstu.ru.navigatoricst.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class DetailedResultActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private ListView lvResDirs;
    private TextView tvDirName;

    private String mDirName;

    private List<String> mICSTDirNames;
    private List<String> mICSTDirCodes;

    private List<Integer> mICSTDirIds;

    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        loadData();
        initFunctionality();
        initListeners();

    }

    private void initVar() {
        mActivity = DetailedResultActivity.this;
        mContext = mActivity.getApplicationContext();

        mICSTDirNames = new ArrayList<>();
        mICSTDirIds = new ArrayList<>();
        mICSTDirCodes = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null){
            currentId = intent.getIntExtra(AppConstants.BUNDLE_KEY_ID, 0);
        }

    }

    private void initView() {
        setContentView(R.layout.activity_detailed_result);

        tvDirName = (TextView) findViewById(R.id.tvDirName);
        lvResDirs = (ListView) findViewById(R.id.lvResDirs);

        initToolbar(true);
        setToolbarTitle(getResources().getString(R.string.result));
        enableUpButton();
    }

    private void loadData() {
        showLoader();
        loadJson();
    }

    private void loadJson() {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.INFO_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert br != null;
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseJson(sb.toString());
    }

    private void parseJson(String jsonData) {
        try {
            JSONObject jsonObjMain = new JSONObject(jsonData);

            JSONArray jsonArrayAreas = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_AREAS);
            JSONArray jsonArrayDirs = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_DIRECTIONS);

            JSONObject jsonCurArea = jsonArrayAreas.getJSONObject(currentId);
            mDirName = jsonCurArea.getString(AppConstants.JSON_KEY_NAME);
            JSONArray jsonICSTdirsIds = jsonCurArea.getJSONArray(AppConstants.JSON_KEY_DIRS);
            for (int i = 0; i < jsonICSTdirsIds.length(); ++i){
                mICSTDirIds.add(jsonICSTdirsIds.getInt(i));
            }

            for (int i = 0; i < mICSTDirIds.size(); ++i){
                mICSTDirNames.add (jsonArrayDirs.getJSONObject(mICSTDirIds.get(i)).getString(AppConstants.JSON_KEY_NAME));
                mICSTDirCodes.add (jsonArrayDirs.getJSONObject(mICSTDirIds.get(i)).getString(AppConstants.JSON_KEY_CODES));
            }

            hideLoader();
        } catch (JSONException e) {
            e.printStackTrace();
            showEmptyView();
        }
    }

    private void initFunctionality() {

        tvDirName.setText(mDirName);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, mICSTDirNames);

        lvResDirs.setAdapter(adapter);

    }

    private void initListeners() {
        lvResDirs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = getString(R.string.icst_edu_url) + mICSTDirCodes.get(position) + "/";
                ActivityUtilities.getInstance().invokeCustomUrlActivity(mActivity, CustomUrlActivity.class,
                        mICSTDirNames.get(position), url, false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mActivity.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
