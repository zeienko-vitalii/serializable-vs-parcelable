package com.zeenko.serializablevsparcelable.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeenko.serializablevsparcelable.R;

public class CustomView extends FrameLayout {

    //init properties
    protected boolean toggle;
    protected Drawable lessArrow = getResources().getDrawable(R.drawable.ic_launcher_background);
    protected Drawable moreArrow = getResources().getDrawable(R.drawable.ic_launcher_background);
    protected View topBar;
    protected ViewGroup bottomBar;
    protected ImageView toggleBottom;
    Paint paint;
    private TextView topBarText, bottomBarText;

    public CustomView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
////        super.onDraw(canvas);
//        canvas.drawColor(Color.RED);
////        int x = getWidth() / 2;
////        int y = getHeight() / 2;
////        int radius = 100;
////        Log.d("duck", "onDraw: ");
////        paint.setColor(Color.parseColor("#CD5C5C"));
////        canvas.drawCircle(x, y, radius, paint);
//    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        paint = new Paint();
        inflate(context, R.layout.qwe, this);
        topBar = findViewById(R.id.topBar);
        bottomBar = findViewById(R.id.bottomBar);
        toggleBottom = findViewById(R.id.toggle_button);
        topBarText = findViewById(R.id.top_bar_title);
        bottomBarText = findViewById(R.id.bottom_bar_text);
        topBar.setOnClickListener(setToggle(toggleBottom, bottomBarText));
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
            try {
                toggle = typedArray.getBoolean(R.styleable.CustomView_showMessage, false);
                bottomBar.setVisibility(toggle ? View.VISIBLE : View.GONE);
                toggleBottom.setImageDrawable(toggle ? lessArrow : moreArrow);
                if (typedArray.hasValue(R.styleable.CustomView_barTitle)) {
                    topBarText.setText(typedArray.getString(R.styleable.CustomView_barTitle));
                }
                if (typedArray.hasValue(R.styleable.CustomView_colorBar)) {
                    topBar.setBackgroundColor(typedArray.getColor(R.styleable.CustomView_colorBar, getResources().getColor(R.color.colorPrimary)));
                }
                if (typedArray.hasValue(R.styleable.CustomView_contentBar)) {
                    bottomBar.setBackgroundColor(typedArray.getColor(R.styleable.CustomView_contentBar, getResources().getColor(R.color.colorPrimaryDark)));
                }
                if (typedArray.hasValue(R.styleable.CustomView_message)) {
                    bottomBarText.setText(typedArray.getString(R.styleable.CustomView_message));
                }
            } finally {
                typedArray.recycle();
            }
        }

    }

    public void setTitle(String title) {
        topBarText.setText(title);
    }

    public void setMessage(String message) {
        bottomBarText.setText(message);
    }

    public void setTitleBarColor(int color) {
        topBar.setBackgroundColor(getResources().getColor(color));
    }

    public void setBottomBarColor(int color) {
        bottomBar.setBackgroundColor(getResources().getColor(color));
    }

    protected OnClickListener setToggle(final ImageView toggleButton, final View bottomBar) {
        return v -> {
            Drawable drawable = lessArrow;
            int visibility = View.VISIBLE;
            if (toggle) {
                drawable = moreArrow;
                visibility = View.GONE;
                toggle = false;
            } else {
                toggle = true;
            }
            toggleButton.setImageDrawable(drawable);
            bottomBar.setVisibility(visibility);
        };

    }
}
