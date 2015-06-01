package com.xmalloc.wordnote.common.fragment;

import android.app.Fragment;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhch on 2015/5/12.
 */
public class BaseFrag extends Fragment {
    private Toast toast;
    public Context getContext(){
        return getActivity();
    }

    /**
     * 显示 toast, Fragment范围内唯一
     * @param text
     */
    protected void showToast(CharSequence text){
        if(toast == null){
            toast = createToast();
        }
        toast.setText(text);
        toast.show();
    }

    protected void showToast(int textRes){
        if(toast == null){
            toast = createToast();
        }
        toast.setText(getString(textRes));
        toast.show();
    }

    private Toast createToast(){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getActivity(), "", duration);
        return toast;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(toast != null){
            toast.cancel();
        }
    }
}
