package com.example.tammy.happypai2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by tammy on 17/6/12.
 */

public class NoScrollListView extends ListView {

    public NoScrollListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
