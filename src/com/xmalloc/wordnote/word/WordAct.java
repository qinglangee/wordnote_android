package com.xmalloc.wordnote.word;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.constant.WordApi;
import com.xmalloc.wordnote.util.GsonUtils;
import com.xmalloc.wordnote.util.PreferencesUtils;
import com.xmalloc.wordnote.util.ToastUtil;
import com.xmalloc.wordnote.util.ViewTextUtil;
import com.xmalloc.wordnote.util.net.GsonRequest;
import com.xmalloc.wordnote.util.net.NetUtil;
import com.xmalloc.wordnote.util.net.VolleyWrapper;
import com.xmalloc.wordnote.word.vo.BaseResp;
import com.xmalloc.wordnote.word.vo.ForgetResp;
import com.xmalloc.wordnote.word.vo.Word;
import com.xmalloc.wordnote.word.vo.WordListResp;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by zhch on 2015/5/18.
 */
public class WordAct extends BaseAct {
    TextView mInfo;
    TextView mReviewInfo;
    TextView mIndexInfo;
    EditText mTranslate;
    TextView mWord01;
    TextView mWord02;
    TextView mWord03;

    WordLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_act);

        mInfo = (TextView) findViewById(R.id.info);
        mReviewInfo = (TextView) findViewById(R.id.review_info);
        mIndexInfo = (TextView) findViewById(R.id.index_info);
        mTranslate = (EditText) findViewById(R.id.translate);
        mWord01 = (TextView) findViewById(R.id.word01);
        mWord02 = (TextView) findViewById(R.id.word02);
        mWord03 = (TextView) findViewById(R.id.word03);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logic = new WordLogic(this);
        logic.setReviewDays(PreferencesUtils.getString(this, WordLogic.REVIEW_DAYS));
        String reviewWordsJson = PreferencesUtils.getString(this, WordLogic.REVIEW_WORDS);
        if (Strings.isNullOrEmpty(reviewWordsJson)) {
            logic.setupWords();
        } else {
            logic.setReviewWordList(reviewWordsJson);
            logic.setRandomWordList(PreferencesUtils.getString(this, WordLogic.RANDOM_WORDS));
            String forgetWordsJson = PreferencesUtils.getString(this, WordLogic.FORGET_WORDS);
            if(!TextUtils.isEmpty(forgetWordsJson)){
                logic.setForgetWordList(forgetWordsJson);
            }else{
                logic.forgetSet = new HashSet<>();
            }
        }
        mInfo.setText(logic.reviewPreInfo());
    }

    public void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.next:
                logic.next();
                break;
            case R.id.pre:
                logic.pre();
                break;
            case R.id.show:
                mTranslate.setText(logic.forgetWords());
                return;
            case R.id.forget:
                logic.forget();
                break;
            case R.id.pass:
                logic.pass();
                break;
        }
        setDisplayWord();
    }

    public void setDisplayWord() {
        setWord(logic.getPre(), mWord01);
        setWord(logic.getCurrent(), mWord02);
        setWord(logic.getNext(), mWord03);
        mReviewInfo.setText(logic.reviewInfo());
        mTranslate.setText(logic.getPreTranslate());
        mIndexInfo.setText("index: " + logic.reviewIndex);
    }

    public void setWord(Word word, TextView view) {
        if (word != null) {
            ViewTextUtil.setText(view, word.text);
        }else{
            ViewTextUtil.setText(view, "");
        }
    }

    public void postForget(View v) {
        String forgetWords = logic.forgetWords();

        Map<String, String> params = NetUtil.createParams("content", forgetWords);
        GsonRequest<BaseResp> listRequest = new GsonRequest<BaseResp>(WordApi.SAVE_FORGET,
                BaseResp.class, params, new Response.Listener<BaseResp>() {
            @Override
            public void onResponse(BaseResp res) {
                if (res.err == 0) {
                    ToastUtil.show(WordAct.this, res.msg);
                }
            }
        },
                NetUtil.getErrorListener(this));
        VolleyWrapper.getInstance(this).addToRequestQueue(listRequest);
    }

    public void requestForget(View v) {

        GsonRequest<ForgetResp> listRequest = new GsonRequest<ForgetResp>(WordApi.VIEW_FORGET,
                ForgetResp.class, null, new Response.Listener<ForgetResp>() {
            @Override
            public void onResponse(ForgetResp res) {
                if (res.err == 0) {
                    mTranslate.setText(res.content);
                }else{
                    ToastUtil.show(WordAct.this, res.msg);
                }
            }
        },
                NetUtil.getErrorListener(this));
        VolleyWrapper.getInstance(this).addToRequestQueue(listRequest);
    }
}
