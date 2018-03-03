package com.lszyhb.showcollectdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kkk8199 on 1/19/18.
 */

public class HorizontalScrollViewAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<String> mDatas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount()
    {
        return mDatas.size();
    }

    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.usermain_bottom, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.imageviewid);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.textviewid);
            viewHolder.mText.setText(mDatas.get(position));
            if(position==0){
                viewHolder.mText.setSelected(true);
                viewHolder.mImg.setVisibility(View.VISIBLE);
            }
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public ViewHolder getviewHolder(View convertView)
    {

        ViewHolder    viewHolder = (ViewHolder) convertView.getTag();
        return viewHolder;
    }

    public class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }
}
