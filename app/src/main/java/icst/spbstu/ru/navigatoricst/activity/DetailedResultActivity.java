package icst.spbstu.ru.navigatoricst.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.adapters.DirectionsAdapter;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class DetailedResultActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private RecyclerView rvDirContent;
    private TextView tvAreaName;
    private TextView tvAreaInfo;
    private ImageView ivAreaLogo;
    private ScrollView svDetResCont;

    private DirectionsAdapter adapter;

    private String mAreaName;
    private String mAreaDescription;
    
    private ArrayList<String> mICSTDirNames;
    private ArrayList<String> mICSTDirCodes;

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

        tvAreaName = (TextView) findViewById(R.id.tvAreaName);
        tvAreaInfo = (TextView) findViewById(R.id.tvAreaInfo);
        rvDirContent = (RecyclerView) findViewById(R.id.rvDirContent);
        svDetResCont = (ScrollView) findViewById(R.id.svDetResContainer);
        rvDirContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ivAreaLogo = (ImageView) findViewById(R.id.ivAreaLogo);

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
            mAreaName = jsonCurArea.getString(AppConstants.JSON_KEY_NAME);
            mAreaDescription = jsonCurArea.getString(AppConstants.JSON_KEY_DESCRIPTION);
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

        tvAreaName.setText(mAreaName);
        tvAreaInfo.setText(mAreaDescription);
        setAreaLogo();
        svDetResCont.scrollTo(0, 0);

        adapter = new DirectionsAdapter(mContext, mActivity, mICSTDirNames, mICSTDirCodes);
        rvDirContent.setAdapter(adapter);


    }

    private void setAreaLogo() {
        String filename = getResources().getString(R.string.assets_area_icon, currentId);
        InputStream inputStream = null;
        try{
            inputStream = mContext.getAssets().open(filename);
            Drawable d = Drawable.createFromStream(inputStream, null);
            ivAreaLogo.setImageDrawable(d);
            ivAreaLogo.setScaleType(ImageView.ScaleType.CENTER);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream != null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    private void initListeners() {
        adapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
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
