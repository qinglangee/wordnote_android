package com.xmalloc.wordnote.word;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.xmalloc.wordnote.R;
import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.util.GsonUtils;
import com.xmalloc.wordnote.util.PreferencesUtils;
import com.xmalloc.wordnote.word.vo.Word;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhch on 2015/5/18.
 */
public class WordAct extends BaseAct {
    TextView mInfo;

    WordLogic logic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_act);

        mInfo = (TextView)findViewById(R.id.info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logic = new WordLogic(this);
        logic.reviewDays = PreferencesUtils.getString(this, WordLogic.REVIEW_DAYS);
        String reviewWordsJson = PreferencesUtils.getString(this, WordLogic.REVIEW_WORDS);
        if(Strings.isNullOrEmpty(reviewWordsJson)){
            logic.setupWords();
        }else{
            Type type = new TypeToken<List<Word>>(){}.getType();
            logic.reviewWordList = GsonUtils.fromJson(reviewWordsJson, type);
        }
        mInfo.setText("天数：" + logic.dayCount + " 单词数：" + logic.reviewWordList.size());
    }

    public void clickBtn(View v){

    }
}
