package icst.spbstu.ru.navigatoricst.activity;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import icst.spbstu.ru.navigatoricst.R;
import icst.spbstu.ru.navigatoricst.data.preference.AppPreference;
import icst.spbstu.ru.navigatoricst.models.TestModel;
import icst.spbstu.ru.navigatoricst.adapters.TestingAdapter;
import icst.spbstu.ru.navigatoricst.constants.AppConstants;
import icst.spbstu.ru.navigatoricst.listeners.ListItemClickListener;
import icst.spbstu.ru.navigatoricst.utilities.ActivityUtilities;
import icst.spbstu.ru.navigatoricst.utilities.DialogUtilities;

public class TestingActivity extends BaseActivity implements DialogUtilities.OnCompleteListener {

    private Activity mActivity;
    private Context mContext;

    private RecyclerView mRecyclerTest;
    private TextView tvQuestionText;
    private TextView tvQuestionTitle;
    private ProgressBar pbTestingProgress;

    private TestingAdapter mAdapter = null;

    private List<TestModel> mItemList;
    ArrayList<String> mOptionList;

    private int mQuestionPosition = 0;
    private int mQuestionsCount = 0;

    private ArrayList<Integer> mDirectionsScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLogs", "Testing: onCreate");
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        loadData();
        initListener();
    }

    private void initVar() {
        mActivity = TestingActivity.this;
        mContext = mActivity.getApplicationContext();

        mItemList = new ArrayList<>();
        mOptionList = new ArrayList<>();
        mDirectionsScores = new ArrayList<>();
        for (int i = 0; i < AppConstants.DIRECTIONS_COUNT; ++i){
            mDirectionsScores.add(0);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_testing);

        tvQuestionText = (TextView) findViewById(R.id.tvQuestionText);
        tvQuestionTitle = (TextView) findViewById(R.id.tvQuestionTitle);

        pbTestingProgress = (ProgressBar)findViewById(R.id.pbTestingProgress);
        pbTestingProgress.setProgress(0);

        mRecyclerTest = (RecyclerView) findViewById(R.id.rvTest);
        mRecyclerTest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));

        mAdapter = new TestingAdapter(mContext, mActivity, mOptionList);
        mRecyclerTest.setAdapter(mAdapter);

        initToolbar(true);
        setToolbarTitle(getString(R.string.testing));
        enableUpButton();
        initLoader();
    }

    private void loadData() {
        showLoader();
        loadJson();
    }

    public void initListener() {

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                pbTestingProgress.incrementProgressBy(1);
                updateDirectionsScores(mItemList.get(mQuestionPosition).getScores(position));
                setNextQuestion();
            }
        });

    }

    private void updateDirectionsScores(ArrayList<Integer> scores){
        for (int i = 0; i < AppConstants.DIRECTIONS_COUNT; ++i){
            mDirectionsScores.set(i, mDirectionsScores.get(i) + scores.get(i));
        }
    }

    public void setNextQuestion() {
        if (mQuestionPosition < mQuestionsCount - 1) {
            mQuestionPosition++;
            updateQuestionsAndAnswers();
        }  else {
            AppPreference.getInstance(mContext).setString(AppConstants.LAST_RES, getDirectionsString());
            ActivityUtilities.getInstance().invokeScoreCardActivity(mActivity, ScoreCardActivity.class,
                    mDirectionsScores, true);
        }
    }

    private String getDirectionsString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mDirectionsScores.size(); ++i){
            sb.append(mDirectionsScores.get(i));
            sb.append(' ');
        }
        return sb.toString();
    }

    public void updateQuestionsAndAnswers() {
        mOptionList.clear();
        (Objects.requireNonNull(mRecyclerTest.getLayoutManager())).scrollToPosition(AppConstants.BUNDLE_KEY_ZERO_INDEX);

        mOptionList.addAll(mItemList.get(mQuestionPosition).getAnswers());
        mAdapter.notifyDataSetChanged();

        String mQuestionText = mItemList.get(mQuestionPosition).getQuestion();

        tvQuestionText.setText(Html.fromHtml(mQuestionText));
        tvQuestionTitle.setText(getString(R.string.test_question_title,
                mQuestionPosition + 1, mQuestionsCount));
    }

    public void testingActivityClosePrompt() {
        FragmentManager manager = getSupportFragmentManager();
        DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.exit), getString(R.string.cancel_prompt),
                getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_CLOSE_OPTION);
        dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
    }

    private void loadJson() {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.QUESTION_FILE)));
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

    public void parseJson(String jsonData) {
        try {
            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_QUESTIONNAIRY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String question = jsonObj.getString(AppConstants.JSON_KEY_QUESTION);

                JSONArray jsonArrayAns = jsonObj.getJSONArray(AppConstants.JSON_KEY_ANSWERS);
                ArrayList<String> contents = new ArrayList<>();
                for (int j = 0; j < jsonArrayAns.length(); j++) {
                    String itemTitle = jsonArrayAns.get(j).toString();
                    contents.add(itemTitle);
                }

                JSONArray jsonArrayScores = jsonObj.getJSONArray(AppConstants.JSON_KEY_SCORES);
                ArrayList<ArrayList<Integer>> scores = new ArrayList<>();
                for (int j = 0; j < jsonArrayScores.length(); ++j){
                    ArrayList<Integer> score = new ArrayList<>();
                    JSONArray jsonScore = jsonArrayScores.getJSONObject(j).getJSONArray(AppConstants.JSON_KEY_SCORE_SET);
                    for (int k = 0; k < jsonScore.length(); ++k){
                        score.add(jsonScore.getInt(k));
                    }
                    scores.add(score);
                }
                mItemList.add(new TestModel(question, contents, scores));
            }

            mQuestionsCount = mItemList.size();
            pbTestingProgress.setMax(mQuestionsCount);
            Collections.shuffle(mItemList);

            hideLoader();
            updateQuestionsAndAnswers();

        } catch (JSONException e) {
            e.printStackTrace();
            showEmptyView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            testingActivityClosePrompt();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        testingActivityClosePrompt();
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            switch (viewIdText) {
                case AppConstants.BUNDLE_KEY_CLOSE_OPTION:
                    ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
                    break;
                case AppConstants.BUNDLE_KEY_SKIP_OPTION:

                    //TODO: mIsSkipped = true;

                    //TODO: updateResultSet();
                    setNextQuestion();
                    break;
                case AppConstants.BUNDLE_KEY_REWARD_OPTION:
                    //TODO:  mRewardedVideoAd.show();
                    break;
            }
        } else if (viewIdText.equals(AppConstants.BUNDLE_KEY_REWARD_OPTION)) {
            //TODO: invoke ScoreCardActivity
        }
    }

    @Override
    protected void onDestroy() {

        Log.d("myLogs", "Testing: onDestroy");
        super.onDestroy();
    }
}
