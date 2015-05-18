package com.xmalloc.wordnote.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ArrayAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mData;
    protected boolean mDataValid;
    protected Integer layoutResId;

    public ArrayAdapter(Context context, int layoutResId) {
        this(context);
        this.layoutResId = layoutResId;
    }
    
    public ArrayAdapter(Context context) {
    	mContext = context;
    	mDataValid = false;
    }
    
    /**
     * 添加数据 
     * @param data
     */
    public void addAll(List<T> data){
        if (data != null) {
            mDataValid = true;
            if(mData != null){
            	mData.addAll(data);
            }else{
            	mData = data;
            }
            notifyDataSetChanged();
        } else {
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }

    public void updateData(List<T> data) {
        if (data != null) {
            mDataValid = true;
            mData = data;
            notifyDataSetChanged();
        } else {
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        if (mDataValid && mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mDataValid && mData != null) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mData != null) {
            return position;
        } else {
            return 0;
        }
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the data is valid");
        }
        if (position < 0 || position >= mData.size()) {
            throw new IllegalStateException("couldn't get view at this position " + position);
        }
        T data = mData.get(position);
        View v;
        if (convertView == null) {
            v = newView(mContext, data, parent, getItemViewType(position));
        } else {
            v = convertView;
        }
        bindView(v, position, data);
        return v;
    }

    public  View newView(Context context, T data, ViewGroup parent, int type){
    	if(layoutResId != null){
    		View view = LayoutInflater.from(context).inflate(layoutResId, null);
    		return view;
    	}
    	return null;
    }

    public  void bindView(View view, int position, T data){
    	if (data == null)
            return;
    }
}
