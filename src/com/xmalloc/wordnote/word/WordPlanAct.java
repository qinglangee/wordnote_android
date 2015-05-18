package com.xmalloc.wordnote.word;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.common.base.Strings;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.constant.PintimesApi;
import com.xmalloc.wordnote.constant.TempResp;
import com.xmalloc.wordnote.constant.WordApi;
import com.xmalloc.wordnote.util.LL;
import com.xmalloc.wordnote.util.TimeUtil;
import com.xmalloc.wordnote.util.net.GsonRequest;
import com.xmalloc.wordnote.util.net.NetUtil;
import com.xmalloc.wordnote.util.net.VolleyWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhch on 2015/5/18.
 */
public class WordPlanAct extends BaseAct {

    EditText mAct;
    EditText mPlan;
    
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_plan_act);
        mAct = (EditText)findViewById(R.id.act);
        mPlan = (EditText)findViewById(R.id.plan);
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
        content= TempResp.actList;
        setListResp(content);
    }


    private Response.Listener<ActResp> getListResponseListener() {
        return new Response.Listener<ActResp>() {
            @Override
            public void onResponse(ActResp res) {
                mAct.setText(res.content);
            }
        };
    }
    private void setListResp(String content){
        mAct.setText(content);
        calculate();
    }

    private void calculate() {
        try {
            String result = calculateTime();
            mPlan.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String calculateTime() throws IOException {
        List<String> lines = new ArrayList<String>();
        String[] lineStrs = content.split("\\s");
        for(String s : lineStrs){
            lines.add(s);
        }
        List<Record> records = new ArrayList<Record>();
        for(String line : lines){
            if(Strings.isNullOrEmpty(line) || !line.contains("|") || line.startsWith("#")){
                continue;
            }
            String[] fields = line.split("\\|");
            Record r = new Record(fields[0],fields[1],fields[2]);
            records.add(r);
        }

        Map<String, List<Record>> map = new HashMap<String, List<Record>>();

        int[] reviewTime = new int[]{1,2,4,7,15,25,70}; // 复习的间隔时间

        for(Record r : records){
            Date date = TimeUtil.parseDate(r.time, "yyyy-MM-dd");
            Calendar c = Calendar.getInstance();

            for(int day : reviewTime){
                c.setTime(date);
                c.add(Calendar.DAY_OF_YEAR, day);
                String reviewDay = TimeUtil.format(c.getTime(), "yyyy-MM-dd");

                if(map.get(reviewDay) != null){
                    map.get(reviewDay).add(r);
                }else{
                    List<Record> tasks = new ArrayList<Record>();
                    tasks.add(r);
                    map.put(reviewDay, tasks);
                }
            }

        }

        return printResult(map);
    }

    /**
     * 打印结果
     * @param map
     */
    private String printResult(Map<String, List<Record>> map) {
        List<String> keys =new ArrayList<String>(map.keySet());
        Collections.sort(keys); // 日期排序

        // 计算所有的天数, 打印
        Date first = TimeUtil.parseDate(keys.get(0), "yyyy-MM-dd");
        Date last = TimeUtil.parseDate(keys.get(keys.size() - 1), "yyyy-MM-dd");
        long days = (last.getTime() - first.getTime())/1000/60/60/24;

        Calendar c = Calendar.getInstance();
        c.setTime(first);
        c.add(Calendar.DAY_OF_YEAR, -1);

        StringBuilder result = new StringBuilder();
        for(int i=0;i<=days;i++){
            c.add(Calendar.DAY_OF_YEAR, 1);

            // 周一就打印个空行
            if(c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                System.out.println();
            }

            String key = TimeUtil.format(c.getTime(), "yyyy-MM-dd");
            StringBuilder sb = new StringBuilder();
            sb.append(key);

            List<Record> records = map.get(key);
            if(records != null){
                Collections.sort(records);
                for(Record r : records){
                    sb.append("  ").append(r.name); //.append("(").append(r.comment).append(")");
                }
            }
            result.append(sb).append("\n");
        }
        return result.toString();
    }
}
