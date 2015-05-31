package com.xmalloc.wordnote.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xmalloc.wordnote.common.activity.BaseAct;
import com.xmalloc.wordnote.word.WordAct;
import com.xmalloc.wordnote.word.WordPlanAct;

public class MainActivity extends BaseAct {

	Button button1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, WordPlanAct.class));
        finish();
    }
}
