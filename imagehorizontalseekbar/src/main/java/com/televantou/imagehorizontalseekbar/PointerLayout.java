package com.televantou.imagehorizontalseekbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.televantou.imagehorizontalseekbar.animation.ResizeHeightAnimation;
import com.televantou.imagehorizontalseekbar.animation.ResizeWidthAnimation;

/**
 * Created by conta on 08/03/2017.
 */

public class PointerLayout extends RelativeLayout {
    TextView title;
    ImageView line;
    public PointerLayout(Context context) {
        super(context);
        init(context);
    }

    public PointerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PointerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_pointer, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        title = (TextView) findViewById(R.id.text);
        line = (ImageView) findViewById(R.id.dotted_line);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                startAnimations();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

            }
        });
    }

    public void startAnimations() {
        title.setVisibility(GONE);

        Resources r = getResources();


        ResizeWidthAnimation anim = new ResizeWidthAnimation(line, getWidth());
        anim.setDuration(350);
        line.startAnimation(anim);

        final float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r
                .getDisplayMetrics());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ResizeHeightAnimation resizeAnimation = new ResizeHeightAnimation(
                        title,
                        (int) px2,
                        0
                );
                resizeAnimation.setDuration(350);
                title.startAnimation(resizeAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }




    public  void setTitleText(String text){
        title.setText(text);

    }

    public void setGravity(int gravity) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());
        float pxh = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r
                .getDisplayMetrics());

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        LayoutParams layoutParams = new LayoutParams((int) px, (int) pxh);

        if (gravity == Gravity.RIGHT) {

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);

            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        params.addRule(RelativeLayout.BELOW, R.id.dotted_line);
        title.setLayoutParams(params);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.fake);
        line.setLayoutParams(layoutParams);
    }

    public void setSelected(boolean selected, boolean animate) {
        final float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
                .getDisplayMetrics());
        if (selected) {
            line.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dotted_blue));
            title.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright));
            if (animate) {
                ObjectAnimator translateY= ObjectAnimator.ofFloat(title, "translationY", -(title
                        .getHeight()+px2));
                translateY.setDuration(300);

                translateY.start();
//                Animation animation = new TranslateAnimation(0, 0 ,title.getHeight(), 0);
//                animation.setDuration(350);
//                animation.setFillAfter(true);
//                title.startAnimation(animation);

            }
        } else {
            title.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            line.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dotted_grey));

            if (animate) {
                ObjectAnimator translateY= ObjectAnimator.ofFloat(title, "translationY", 0);
                translateY.setDuration(300);

                translateY.start();

//                Animation animation = new TranslateAnimation(0,0, 0,  title.getHeight());
//                animation.setDuration(350);
//                animation.setFillAfter(true);
//                title.startAnimation(animation);

            }
        }

    }

    public float getLineX() {

        return line.getX();

    }
}
