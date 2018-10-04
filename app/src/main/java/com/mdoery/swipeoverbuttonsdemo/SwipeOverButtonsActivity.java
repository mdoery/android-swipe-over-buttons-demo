package com.mdoery.swipeoverbuttonsdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * User needs to be able to swipe over a series of radio buttons to send you
 * to the next page. You could use "onFling" but let's use this old solution:
 * https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android
 */
public class SwipeOverButtonsActivity extends AppCompatActivity implements
        ViewTreeObserver.OnGlobalLayoutListener {
    private boolean DEBUG = false;
    private float x1, x2;
    private float MIN_DISTANCE = 0;
    private int init = 0;
    private int[] colors = { R.color.color1, R.color.color2, R.color.color3, R.color.color4 };
    private Context context;
    // e.g. top of surround_layout is 240 px. If ACTION_MOVE Y is above 240 px, then user
    // did not swipe over buttons, but swiped above them.
    private int TOP_Y = 0;
    // e.g. bottom of surround_layout is 690 px. If ACTION_MOVE Y is below 690 px, then user
    // did not swipe over buttons, but swiped below them.
    private int BOTTOM_Y = 0;
    private float yPosition = 0;
    private boolean overButtons = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getBaseContext();
        setContentView(R.layout.activity_swipe_over_buttons);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        // Swipe must be 95% of the width or more
        MIN_DISTANCE = widthPixels * 0.95f;

        Button lightestBtn = (Button) findViewById(R.id.lightestBtn);
        setUpButton(lightestBtn, 0);
        Button lightBtn = (Button) findViewById(R.id.lightBtn);
        setUpButton(lightBtn, 1);
        Button darkBtn = (Button) findViewById(R.id.darkBtn);
        setUpButton(darkBtn, 2);
        Button darkestBtn = (Button) findViewById(R.id.darkestBtn);
        setUpButton(darkestBtn, 3);

        View view = findViewById(R.id.surround_layout);
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    /**
     * For debugging.
     * @param str is a message to display as a Toast
     * @param view a View whose info will be displayed in the Toast.
     */
    private void showMe(String str, View view) {
        if (!DEBUG) return;
        int measuredWidth = view.getMeasuredWidth(); // view width
        int measuredHeight = view.getMeasuredHeight(); //view height
        float bottom = view.getBottom();
        float left = view.getLeft();
        float right = view.getRight();
        float top = view.getTop();
        float width = view.getWidth();
        float height = view.getHeight();
        toast(str + " bottom " + bottom + ", " +
                "left " + left + ", " +
                "right " + right + ", " +
                "top " + top+ ", " +
                "width " + width+ ", " +
                "height " + height);

    }

    /**
     * Adds an onClickListener to each radio button, and sets it up to switch
     * to a specific color.
     * @param b the radio button
     * @param colorIndex the index into our colors array
     */
    private void setUpButton(Button b, final int colorIndex) {
        /* check to see if swipe over button registers ontouch now */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackground(colorIndex);
            }
        });
    }

    /**
     * Changes the background to the color specified by the input
     * @param colorIndex index into our private colors array
     */
    private void changeBackground(int colorIndex) {
        try {
            int id = colors[colorIndex];
            int newColor = ContextCompat.getColor(getBaseContext(), id);
            setBackgroundColor(newColor);
        } catch (Exception e) {
            // Normally you will not Toast an error. This is a demo app, so it's okay to do this.
            Toast.makeText(this.getBaseContext(), " " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Change the background color of our layout to the color specified as the input id.
     * @param newColor
     */
    private void setBackgroundColor(int newColor) {
        LinearLayout l = (LinearLayout) findViewById(R.id.inner_layout);
        l.setBackgroundColor(newColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())  {
            case MotionEvent.ACTION_DOWN:
                overButtons = true;
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                boolean swipedLengthOfScreen = Math.abs(deltaX) > MIN_DISTANCE;

                if (swipedLengthOfScreen && overButtons) {
                    setBackgroundColor(Color.BLACK);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                yPosition = event.getY();
                if (yPosition < TOP_Y || yPosition > BOTTOM_Y) {
                    // Oops! You swiped outside the lines! You are not swiping correctly,
                    // so the color will not change.
                    overButtons = false;
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    protected void toast(String msg) {
        if (DEBUG) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Take action after the layout was inflated.
     * In this case, get the Rectangle surrounding the radio buttons, and set
     * upper and lower boundary.
     */
    @Override
    public void onGlobalLayout() {
        // Once the layout is inflated, get the Rectangle for the layout which
        // surrounds the radio buttons:
        View view = findViewById(R.id.surround_layout);
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        BOTTOM_Y = r.bottom; // e.g. 690 (px)
        TOP_Y = r.top; // e.g. 240 (px)
        toast("TOP_Y " + TOP_Y + " BOTTOM_Y " + BOTTOM_Y);
    }
}
