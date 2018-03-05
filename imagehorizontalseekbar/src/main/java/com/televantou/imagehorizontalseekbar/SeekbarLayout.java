package com.televantou.imagehorizontalseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.github.jimcoven.view.JCropImageView;

import java.util.ArrayList;

public class SeekbarLayout extends RelativeLayout {
    private RelativeLayout leftLayout;
    private RelativeLayout rightLayout;
    private ImageView backImage;
    private JCropImageView frontImage;
    private PointerLayout selectedLayout;
//    private TextView seekValue;
    VerticalSeekBar seekBar;
    private int backHeight;
    int progressValue = 0;
    private ArrayList<Pointer> pointers = new ArrayList<>();

    OnBottleLayoutValueChangedListener onBottleLayoutValueChangedListener;
    Drawable background, mask, thumb;
    public SeekbarLayout(Context context) {
        super(context);

        init(context);
    }

    public SeekbarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SeekbarLayout, 0, 0);
        try {

            background = ta.getDrawable(R.styleable.SeekbarLayout_backgroundImage);
            mask = ta.getDrawable(R.styleable.SeekbarLayout_maskImage);
            thumb = ta.getDrawable(R.styleable.SeekbarLayout_thumbImage);

        } finally {
            ta.recycle();
        }
        init(context);
    }

    public SeekbarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SeekbarLayout, 0, 0);
        try {

            background = ta.getDrawable(R.styleable.SeekbarLayout_backgroundImage);
            mask = ta.getDrawable(R.styleable.SeekbarLayout_maskImage);
            thumb = ta.getDrawable(R.styleable.SeekbarLayout_thumbImage);

        } finally {
            ta.recycle();
        }
    }

    public void setOnSeekbarLayoutValueChangedListener(OnBottleLayoutValueChangedListener onBottleLayoutValueChangedListener) {
        this.onBottleLayoutValueChangedListener = onBottleLayoutValueChangedListener;
    }

    public void setPointers(ArrayList<Pointer> pointers) {
        this.pointers = pointers;

        if (backHeight != 0) {
            addPointersInUi();
        }
    }

    public void setImages(Context context, Drawable background, Drawable mask, Drawable thumb){
        if(background!=null)
            this.background = background;
        if(mask!=null)
            this.mask = mask;
        if(thumb!=null)
            this.thumb = thumb;

        init(context);
    }

    private void addPointersInUi() {
        for (Pointer pointer : pointers) {
            PointerLayout pointerLayout = new PointerLayout(getContext());
            pointerLayout.setTitleText(pointer.getText());
            pointerLayout.setGravity(pointer.getGravity());
            pointerLayout.setSelected(pointer.getSelected(), false);
            if (pointer.getSelected())
                selectedLayout = pointerLayout;
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources()
                    .getDisplayMetrics());
            params.topMargin = (int) ((backHeight / 100.0f) * pointer
                    .getyPosition() - px);
            if (pointer.getGravity() == Gravity.LEFT) {
                leftLayout.addView(pointerLayout, params);
            } else {
                rightLayout.addView(pointerLayout, params);
            }
            pointer.setPointerLayout(pointerLayout);
        }

    }

    private void init(Context context) {
        View.inflate(context, R.layout.seekbar_layout, this);

        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);

        backImage = (ImageView) findViewById(R.id.back_image);
        frontImage = (JCropImageView) findViewById(R.id.image_mask);

        if(background!=null){
            backImage.setImageDrawable(background);
        }
        if(mask!=null){
            frontImage.setImageDrawable(mask);
        }
//        seekValue = (TextView) findViewById(R.id.seekValue);
        seekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        final ViewTreeObserver observer = backImage.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                backImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                Drawable drawable = backImage.getDrawable();
                if (drawable == null) {
                    backImage.getLayoutParams().width = 0;
                    backImage.getLayoutParams().height = 0;
                    frontImage.getLayoutParams().width = 0;
                    frontImage.getLayoutParams().height = 0;
                } else {
                    float imageSideRatio = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
                    float viewSideRatio = (float) MeasureSpec.getSize(backImage.getMeasuredWidth()) / (float)
                            MeasureSpec.getSize(backImage.getMeasuredHeight());
                    int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                            getResources()
                                    .getDisplayMetrics());

                    if (imageSideRatio >= viewSideRatio) {
                        // Image is wider than the display (ratio)
                        int width = MeasureSpec.getSize(backImage.getMeasuredWidth());
                        int height = (int) (width / imageSideRatio);
                        backImage.getLayoutParams().width = width;
//                        backImage.getLayoutParams().height = height;
                        frontImage.getLayoutParams().width = width + px;
//                        frontImage.getLayoutParams().height =height;
                    } else {
                        // Image is taller than the display (ratio)
                        int height = MeasureSpec.getSize(backImage.getMeasuredHeight());
                        int width = (int) (height * imageSideRatio);
                        backImage.getLayoutParams().width = width;
//                        backImage.getLayoutParams().height = height;
                        frontImage.getLayoutParams().width = width + px;
//                        frontImage.getLayoutParams().height =height;
                    }
                }
//                frontImage.getLayoutParams().width = backImage.getMeasuredWidth();
                frontImage.requestLayout();
                backImage.requestLayout();
                backHeight = backImage.getHeight();

                addPointersInUi();
                setUpUi();
            }
        });

    }

    public void animateEntrance() {
        for (Pointer pointer : pointers) {
            pointer.getPointerLayout().startAnimations();

            if (pointer.getSelected()) {
                selectedLayout = pointer.getPointerLayout();
            }
        }
    }

    public void setProgress(int progress) {
        progressValue = progress;
        if (backHeight != 0) {
            int value = (progress * backHeight) / 24;
            seekBar.setProgress(value);
//            seekValue.setText(value + "");
            ViewGroup.LayoutParams LP = frontImage.getLayoutParams();
            LP.height = value;
            frontImage.setLayoutParams(LP);
            updateNearestPointer(value);
        }
    }

    public void setUpUi() {
        int value = (progressValue * backHeight) / 24;

        seekBar.setMax(backHeight);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seekValue.setText(progress + "");
                ViewGroup.LayoutParams LP = frontImage.getLayoutParams();
                LP.height = progress;
                frontImage.setLayoutParams(LP);
                updateNearestPointer(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = (24 * seekBar.getProgress()) / backHeight;
                if (onBottleLayoutValueChangedListener != null)
                    onBottleLayoutValueChangedListener.onValueChanged(value);
            }
        });

        seekBar.setProgress(value);
        seekBar.setPadding((int) getResources().getDimension(R.dimen.required_seekbar_padding), 0,
                (int) getResources().getDimension(R.dimen.required_seekbar_padding), 0);

//        seekValue.setText(value + "");
        ViewGroup.LayoutParams LP = frontImage.getLayoutParams();
        LP.height = value;
        frontImage.setLayoutParams(LP);
        updateNearestPointer(value);

    }

    public void updateNearestPointer(int pos) {
        int rpos = reverseNumber(pos, 0, backHeight);
        float tempMax = 90000000;
        PointerLayout pointerLayout = null;
        for (Pointer pointer : pointers) {


            float y = ((backHeight / 100.0f) * pointer
                    .getyPosition() );
//            float y = pointer.getPointerLayout().getY() ;
            float difference = Math.abs(rpos - y);
            if (difference < tempMax) {
                tempMax = difference;
                pointerLayout = pointer.getPointerLayout();
            }

        }
        if (selectedLayout != pointerLayout) {
            if (selectedLayout != null)
                selectedLayout.setSelected(false, true);

            selectedLayout = pointerLayout;
            selectedLayout.setSelected(true, true);

        }
    }

    public int reverseNumber(int num, int min, int max) {
        return (max + min) - num;
    }

    public Pointer getPointerAtPosition(int position) {
        return pointers.get(position);
    }

    public static class Pointer {
        int gravity;
        int yPositionPercent;
        String text;
        boolean selected;
        PointerLayout pointerLayout;

        public Pointer() {

        }

        public Pointer(int gravity, int yPositionPercent, String text, boolean selected) {
            this.selected = selected;
            this.gravity = gravity;
            this.yPositionPercent = yPositionPercent;
            this.text = text;
        }

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public float getyPosition() {
            return yPositionPercent;
        }

        public void setyPosition(int yPositionPercent) {
            this.yPositionPercent = yPositionPercent;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setPointerLayout(PointerLayout pointerLayout) {
            this.pointerLayout = pointerLayout;
        }

        public PointerLayout getPointerLayout() {
            return pointerLayout;
        }

        @Override
        public String toString() {
            return "Pointer{" +
                    "gravity=" + gravity +
                    ", yPositionPercent=" + yPositionPercent +
                    ", text='" + text + '\'' +
                    ", selected=" + selected +
                    ", pointerLayout=" + pointerLayout +
                    '}';
        }
    }

    public interface OnBottleLayoutValueChangedListener {
        void onValueChanged(int value);
    }

}
