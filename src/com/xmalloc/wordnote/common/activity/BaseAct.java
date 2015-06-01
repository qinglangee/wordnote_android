package com.xmalloc.wordnote.common.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Toast;

import com.xmalloc.wordnote.R;

/**
 * Created by Administrator on 2015/5/12.
 */
public class BaseAct extends Activity {

    private Toast toast;

    /**
     * 切换 fragment, 并记入back stack
     * @param frag
     */
    public void changeFragment(Fragment frag, int replaceId) {
        changeFragment(frag, true,replaceId);
    }

    /**
     * @param frag
     * @param addToBackStack 是否记入back stack
     */
    public void changeFragment(Fragment frag, boolean addToBackStack, int replaceId) {
        if (frag == null || replaceId < 1) {
            return;
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(replaceId, frag);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }


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
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "", duration);
        return toast;
    }
}
