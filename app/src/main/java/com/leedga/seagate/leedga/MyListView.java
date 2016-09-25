package com.leedga.seagate.leedga;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Muhammad Workstation on 21/09/2016.
 */
public class MyListView extends ListView {

    boolean enabled;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction()==MotionEvent.ACTION_MOVE) return true;
        return super.dispatchTouchEvent(ev);

    }

    public void setEnabled(boolean enabled){
        this.enabled=enabled;
    }
}
