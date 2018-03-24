package com.lszyhb.showcollectdata;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kkk8199 on 1/19/18.
 */

public class BottomHorizontalScrollView extends HorizontalScrollView implements View.OnClickListener {
    private int  recordscrollX;
    private View firstview;
    /**
     * 条目点击时的回调
     */
    public interface OnItemClickListener
    {
        void onClick(View view, int pos);
    }

    private OnItemClickListener mOnClickListener;

    /**
     * HorizontalListView中的LinearLayout
     */
    private LinearLayout mContainer;

    /**
     * 子元素的宽度
     */
    private int mChildWidth;
    /**
     * 子元素的高度
     */
    private int mChildHeight;
    /**
     * 数据适配器
     */
    private HorizontalScrollViewAdapter mAdapter;

    public BottomHorizontalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    /**
     * 初始化数据，设置数据适配器
     *
     * @param mAdapter
     */
    public void initDatas(HorizontalScrollViewAdapter mAdapter,LinearLayout Horizontallinearlayout)
    {
        this.mAdapter = mAdapter;
        mContainer =Horizontallinearlayout;
        // 获得适配器中第一个View
        final View view = mAdapter.getView(0, null, mContainer);
        mContainer.addView(view);

        // 强制计算当前View的宽和高
        if (mChildWidth == 0 && mChildHeight == 0)
        {
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            mChildHeight = view.getMeasuredHeight();
            mChildWidth = view.getMeasuredWidth();
        }
        //初始化屏幕的元素
        initScreenChildren();
    }

    /**
     * 加载View
     *
     *
     */
    public void initScreenChildren()
    {
        mContainer.removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++)
        {
            View view = mAdapter.getView(i, null, mContainer);
            if(i==0)
                firstview = view;
            view.setOnClickListener(this);
            view.setTag(R.id.item_position,i);
            Log.i("kkk8199","view="+view);
            mContainer.addView(view);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
       switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
               int scrollX = getScrollX();
               int x=0;
               if(scrollX>recordscrollX) {//右边移动
                   x= mChildWidth-scrollX%mChildWidth;
               }
               else {//左边移动
                   x = - scrollX % mChildWidth;
               }
          /*    Log.i("kkk8199", "scrollX11=" + scrollX + "mChildWidth11=" + mChildWidth);
               Log.i("kkk8199", "x=" + x);*/
               smoothScrollBy(x,0);
             return true;
            case MotionEvent.ACTION_MOVE:
                recordscrollX = getScrollX();
              // Log.i("kkk8199","scrollX="+scrollX1+"mChildWidth="+mChildWidth);
        }
        return super.onTouchEvent(ev);
    }
    @Override
    public void onClick(View v)
    {
       // Log.i("kkk8199","v.tag="+v.getTag(R.id.item_position));
        if (mOnClickListener != null)
        {
            Log.i("kkk8199","v="+v);
            int postion=(int)v.getTag(R.id.item_position);
            for (int i = 0; i < mContainer.getChildCount(); i++)
            {
               View ConvermconvertView= mContainer.getChildAt(i);
                HorizontalScrollViewAdapter.ViewHolder view = mAdapter.getviewHolder(ConvermconvertView);
                if(i==postion){
                    view.mImg.setVisibility(VISIBLE);
                    view.mText.setSelected(true);
                }
                else {
                    view.mImg.setVisibility(INVISIBLE);
                    view.mText.setSelected(false);
                }
            }
            mOnClickListener.onClick(v, postion);
        }
    }

    public void setOnItemClickListener(OnItemClickListener mOnClickListener)
    {
        this.mOnClickListener = mOnClickListener;
    }

    public View getfirstview(){
        return firstview;
    }
}
