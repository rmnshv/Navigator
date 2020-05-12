package icst.spbstu.ru.navigatoricst.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.adapters.ResultAdapter;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;

public class ScoreCardActivity extends BaseActivity implements OnChartValueSelectedListener {

    private Activity mActivity;
    private Context mContext;

    private RecyclerView rvContent;
    private PieChart mPieChart;
    private ImageView ivScoreShare;
    private ScrollView svScoreCont;

    private ResultAdapter adapter;


    private ArrayList<Integer> mIds = null;
    private ArrayList<Integer> mValues = null;
    private ArrayList<String> mDirNames = null;
    private ArrayList<Double> mPercents = null;
    private ArrayList<String> mResDirNames = null;

    private int[] mColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        loadData();
        initFunctionality();
        initListener();
        Log.d("myLogs", "ScoreCard: onCreate");
    }

    private void initListener() {

        ivScoreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = mActivity.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mActivity.getString(R.string.share_res_msg) +
                        " https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.send)));
            }
        });

        adapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                int itemId = mIds.get(position);
                ActivityUtilities.getInstance().invokeDetailedResultActivity(mActivity, DetailedResultActivity.class,
                        itemId, false);
            }
        });
    }

    private void initVar() {
        mActivity = ScoreCardActivity.this;
        mContext = mActivity.getApplicationContext();

        ArrayList<Integer> mDirectionsScores = null;

        mIds = new ArrayList<>();
        mValues = new ArrayList<>();
        mPercents = new ArrayList<>();
        mDirNames = new ArrayList<String>();
        mResDirNames = new ArrayList<String>();
        mColors = new int[]{getResources().getColor(R.color.diag_1), getResources().getColor(R.color.diag_2),
                getResources().getColor(R.color.diag_3), getResources().getColor(R.color.diag_4)};
        Intent intent = getIntent();
        if (intent != null){
            mDirectionsScores = intent.getIntegerArrayListExtra(AppConstants.BUNDLE_KEY_DIRECTIONS_SCORES);
        }

        getMaxScores(mDirectionsScores);
    }

    private void getMaxScores(ArrayList<Integer> mDirectionsScores){

        boolean[] used = {false, false, false, false, false, false, false, false};

        for (int k = 0; k < AppConstants.INFO_DIRECTIONS_COUNT; ++k){
            int val = 0;
            int id = -1;
            for (int i = 0; i < mDirectionsScores.size(); ++i){
                if (mDirectionsScores.get(i) > val && !used[i]){
                    val = mDirectionsScores.get(i);
                    id = i;
                }
            }
            mIds.add(id);
            mValues.add(val);
            used[id] = true;
        }
        int val = 0;
        for (int i = 0; i < mDirectionsScores.size(); ++i){
            if (mDirectionsScores.get(i) > val && !used[i]){
                val = mDirectionsScores.get(i);
            }
        }
        for (int i = 0; i < mValues.size(); ++i) mValues.set(i, mValues.get(i) - val + 1);
        double sum = 0;
        for (int i = 0; i < mValues.size(); ++i) sum += mValues.get(i);
        for (int i = 0; i < mValues.size(); ++i){
            mPercents.add((double)mValues.get(i) * 100. / sum);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_score_card);

        rvContent = (RecyclerView)findViewById(R.id.rvContent);
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ivScoreShare = (ImageView)findViewById(R.id.ivScoreShare);
        svScoreCont = (ScrollView)findViewById(R.id.svScoreContainer);

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
            JSONArray jsonArray = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_AREAS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String name = jsonObj.getString(AppConstants.JSON_KEY_NAME);
                mDirNames.add(name);
            }

            for (int i = 0; i < AppConstants.INFO_DIRECTIONS_COUNT; ++i){
                mResDirNames.add(mDirNames.get(mIds.get(i)));
            }

            hideLoader();
        } catch (JSONException e) {
            e.printStackTrace();
            showEmptyView();
        }
    }

    private void initFunctionality() {
        svScoreCont.scrollTo(0, 0);
        showPieChart();

        adapter = new ResultAdapter(mContext, mActivity, mResDirNames, mPercents, mColors);
        rvContent.setAdapter(adapter);
    }

    private void showPieChart() {

        mPieChart = (PieChart)findViewById(R.id.piechart);

        mPieChart.setUsePercentValues(true);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setOnChartValueSelectedListener(this);
        mPieChart.setTransparentCircleRadius(AppConstants.TRANSPARENT_CIRCLE_RADIUS);
        mPieChart.setHoleRadius(AppConstants.TRANSPARENT_CIRCLE_RADIUS);
        mPieChart.animateXY(AppConstants.ANIMATION_VALUE, AppConstants.ANIMATION_VALUE);
        mPieChart.setDrawEntryLabels(false);

        Description desc = mPieChart.getDescription();
        desc.setEnabled(false);

        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);


        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < AppConstants.INFO_DIRECTIONS_COUNT; ++i){
            int val = mValues.get(i);
            int id = mIds.get(i);
            entries.add(new PieEntry(val, id));
        }

        PieDataSet dataSet = new PieDataSet(entries, AppConstants.EMPTY_STRING);
        dataSet.setColors(mColors);
        dataSet.setValueTextSize(15);
        dataSet.setValueTextColor(getResources().getColor(R.color.white));

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter(mPieChart));
        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int itemId = (Integer)e.getData();
        ActivityUtilities.getInstance().invokeDetailedResultActivity(mActivity, DetailedResultActivity.class,
                itemId, false);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
    }

}
