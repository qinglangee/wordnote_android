package com.xmalloc.wordnote.common.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zhch on 2015/5/16.
 */
public class CustomArrayAdapter<T> extends ArrayAdapter<T> {
    private int mResource;
    private LayoutInflater mInflater;
    private String mExtraParam;

    public CustomArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        init(context, resource);
    }

    public CustomArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
        init(context, resource);
    }
    public CustomArrayAdapter(Context context, int resource) {
        super(context, resource);
        init(context, resource);
    }

    private void init(Context context, int resource) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
    }

    public void updateData(List<T> objects){
        clear();
        addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }
        CustomItem customView;
        try {
            customView = (CustomItem)view;
        } catch (ClassCastException e) {
            Log.e("CustomArrayAdapter", "You must supply a resource ID for a CustomItem");
            throw new IllegalStateException(
                    "CustomArrayAdapter requires the resource ID to be a CustomItem", e);
        }

        T item = getItem(position);
        customView.initData(item, mExtraParam);

        return view;
    }

    public void setExtraParam(String param){
        mExtraParam = param;
    }

    public interface CustomItem<T> {
        /**
         * 设置 item 数据
         * @param data
         * @param extraParam 附加参数， 有需要的用， 不需要就不用
         */
        public void initData(T data, String extraParam);
    }
}
