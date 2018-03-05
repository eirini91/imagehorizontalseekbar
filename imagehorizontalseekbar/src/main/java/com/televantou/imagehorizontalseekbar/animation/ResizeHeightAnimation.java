package com.televantou.imagehorizontalseekbar.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by conta on 08/03/2017.
 */

public class ResizeHeightAnimation extends Animation {
    final int targetHeight;
    View view;
    int startHeight;
    int counter = 0;

    public ResizeHeightAnimation(View view, int targetHeight, int startHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = startHeight;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight = (int) (startHeight + targetHeight * interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
        if (counter == 0)
            view.setVisibility(View.VISIBLE);
        counter++;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}