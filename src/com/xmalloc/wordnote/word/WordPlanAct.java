package com.xmalloc.wordnote.word;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.android.volley.Response;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.constant.WordApi;
import com.xmalloc.wordnote.util.PreferencesUtils;
import com.xmalloc.wordnote.util.TimeUtil;
import com.xmalloc.wordnote.util.net.GsonRequest;
import com.xmalloc.wordnote.util.net.NetUtil;
import com.xmalloc.wordnote.util.net.VolleyWrapper;
import com.xmalloc.wordnote.word.vo.ActResp;
import com.xmalloc.wordnote.word.vo.Record;
import com.xmalloc.wordnote.word.vo.Schedule;

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
    List<Record> reviewDays;
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

    public void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                getList();
                break;
            case R.id.calculate:
                calculate();
                break;
            case R.id.next:
                startActivity(new Intent(this, WordAct.class));
                break;
        }
    }


    private void getList() {

        GsonRequest<ActResp> listRequest = new GsonRequest<ActResp>(WordApi.PLAN_LIST,
                ActResp.class, null, getListResponseListener(),
                NetUtil.getErrorListener(this));
        VolleyWrapper.getInstance(this).addToRequestQueue(listRequest);
    }


    private Response.Listener<ActResp> getListResponseListener() {
        return new Response.Listener<ActResp>() {
            @Override
            public void onResponse(ActResp res) {
                content = res.content;
                mAct.setText(content);
                calculate();
            }
        };
    }

    private void calculate() {
        List<Schedule> result = logic.calculateTime(content);
        String today = TimeUtil.format(new Date(), "yyyy-MM-dd");

        // 显示今天要背的
        for(Schedule s: result){
            if(today.equals(s.date)){
                mTodayDays.setText(s.words());
                PreferencesUtils.putString(this, WordLogic.REVIEW_DAYS, s.words());
                reviewDays = s.wordDays;
                break;
            }
        }
        boolean showAll = mShowAll.isChecked();
        mPlan.setText(WordLogic.printSchedule(result, showAll));
    }

    private void setupWords() {
        for(Record r : reviewDays){
            String wordJson = PreferencesUtils.getString(this, r.name);
        }
    }
}
