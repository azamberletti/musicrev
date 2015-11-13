package com.example.andrea.musicreview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Created by luca-campana on 13/11/15.
 */
public class NonScrollableGridView extends GridView{

    public NonScrollableGridView(Context context) {
        super(context);
        AvoidScrolling();
    }

    public NonScrollableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AvoidScrolling();
    }

    public NonScrollableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AvoidScrolling();
    }

    private void AvoidScrolling(){
        setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }
        });
    }


}
