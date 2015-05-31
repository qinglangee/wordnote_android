package com.xmalloc.wordnote.word;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.constant.WordApi;
import com.xmalloc.wordnote.util.PreferencesUtils;
import com.xmalloc.wordnote.util.net.GsonRequest;
import com.xmalloc.wordnote.util.net.NetUtil;
import com.xmalloc.wordnote.util.net.VolleyWrapper;
import com.xmalloc.wordnote.word.vo.ActResp;
import com.xmalloc.wordnote.word.vo.CalculateResult;

/**
 * Created by zhch on 2015/5/18.
 */
public class WordPlanAct extends BaseAct {

    EditText mAct;
    EditText mPlan;
    EditText mTodayDays;
    RadioButton mShowAll;

    WordLogic logic;
    String content;
    List<String> reviewDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_plan_act);
        mAct = (EditText)findViewById(R.id.act);
        mPlan = (EditText)findViewById(R.id.plan);
        mTodayDays = (EditText)findViewById(R.id.today_days);
        mShowAll = (RadioButton)findViewById(R.id.show_all);
        logic = new WordLogic(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String dateString = PreferencesUtils.getString(WordPlanAct.this, logic.DATE_STRING);
        String reviewDays = PreferencesUtils.getString(this, WordLogic.REVIEW_DAYS);
        String scheduleString = PreferencesUtils.getString(WordPlanAct.this, logic.SCHEDULE_STRING);
        if(!TextUtils.isEmpty(dateString)){
            setContent(dateString);
        }
        if(!TextUtils.isEmpty(reviewDays)){
            mTodayDays.setText(reviewDays);
        }
        if(!TextUtils.isEmpty(scheduleString)){
            mPlan.setText(scheduleString);
        }
    }

    public void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                getList();
                break;
            case R.id.calculate:
                calculateAndDisplay();
                break;
            case R.id.next:
                startActivity(new Intent(this, WordAct.class));
                break;
        }
    }

    /**
     * 从服务器取 DATE_STRING 的值
     */
    private void getList() {
        GsonRequest<ActResp> listRequest = new GsonRequest<ActResp>(WordApi.PLAN_LIST,
                ActResp.class, null, getListResponseListener(),
                NetUtil.getErrorListener(this));
        VolleyWrapper.getInstance(this).addToRequestQueue(listRequest);
    }

    /**
     * 取回DATE_STRING的值, 保存并计算 SCHEDULE
     * @return
     */
    private Response.Listener<ActResp> getListResponseListener() {
        return new Response.Listener<ActResp>() {
            @Override
            public void onResponse(ActResp res) {
                setContent(res.content);
                PreferencesUtils.putString(WordPlanAct.this, logic.DATE_STRING, content);
                calculateAndDisplay();
            }
        };
    }

    /**
     * DATE_STRING 要设置两个地方
     * @param content
     */
    public void setContent(String content){
        this.content = content;
        mAct.setText(content);
    }

    /**
     * 计算SCHEDULE, 如果界面 today_days 有值就不计算，用已有的值
     */
    private void calculateAndDisplay() {
        calculate();
        showCalculateResult();
    }
    private void calculate() {
        String oldReviewDays = mTodayDays.getText().toString();

        // 如果界面 today_days 有值就不计算，用已有的值
        if(!TextUtils.isEmpty(oldReviewDays.trim())){
            reviewDays = Arrays.asList(oldReviewDays.trim().split("\\s+"));
            return;
        }

        boolean showAll = mShowAll.isChecked();
        CalculateResult result = logic.calculateTime(content, showAll);

        // 显示今天要背的
        reviewDays = Arrays.asList(result.reviewDays.trim().split("\\s+"));

        // 显示 schedule
        String scheduleString = WordLogic.printSchedule(result.scheduleList, showAll);
        mPlan.setText(scheduleString);
        PreferencesUtils.putString(WordPlanAct.this, logic.SCHEDULE_STRING, scheduleString);
    }
    private void showCalculateResult(){
        // 显示今天要背的
        String reviewDaysStr = TextUtils.join(" ", reviewDays);
        mTodayDays.setText(reviewDaysStr);
        PreferencesUtils.putString(this, WordLogic.REVIEW_DAYS, reviewDaysStr);
    }
}
