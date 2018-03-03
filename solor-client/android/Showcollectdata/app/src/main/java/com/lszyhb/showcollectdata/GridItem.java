package com.lszyhb.showcollectdata;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by kkk8199 on 2018/1/27.
 */

public class GridItem extends RelativeLayout {

        private Context mContext;
        private boolean mChecked;//判断该选项是否被选上的标志量
        private TextView mTextView = null;
        private ImageView mSecletView = null;
        private  CheckBox mcheckbox=null;

    public GridItem(Context context) {
            this(context, null, 0);
        }

        public GridItem(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public GridItem(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
            mContext = context;
            LayoutInflater.from(mContext).inflate(R.layout.grid_item, this);
             mcheckbox = findViewById(R.id.grid_checkbox);
        //    mSecletView = (ImageView) findViewById(R.id.grid_imageview);
         //   Log.i("kkk8199","mSecletView="+mSecletView);
         //   mTextView = (TextView) findViewById(R.id.grid_textview);
        }
}
