package com.kawika.smart_survey.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.kawika.smart_survey.R;
import com.kawika.smart_survey.models.Catalog;
import com.kawika.smart_survey.models.Categories;

import java.util.ArrayList;
import java.util.List;

/*k
 * Created by senthiljs on 12/02/18.
 */

public class RevealEffect {

    public static int currentType;
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int CENTER = 3;
    public static final int BOTTOM_LEFT = 4;
    public static final int BOTTOM_RIGHT = 5;
    public static int positionArray[] = new int[2];

    public static AnimatorSet getViewToViewScalingAnimator(final RelativeLayout parentView,
                                                           final View viewToAnimate,
                                                           final Rect fromViewRect,
                                                           final Rect toViewRect,
                                                           final long duration,
                                                           final long startDelay) {
        // get all coordinates at once
        final Rect parentViewRect = new Rect(), viewToAnimateRect = new Rect();
        parentView.getGlobalVisibleRect(parentViewRect);
        viewToAnimate.getGlobalVisibleRect(viewToAnimateRect);

        viewToAnimate.setScaleX(1f);
        viewToAnimate.setScaleY(1f);

        // rescaling of the object on X-axis
        final ValueAnimator valueAnimatorWidth = ValueAnimator.ofInt(fromViewRect.width(), toViewRect.width());
        valueAnimatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newWidth = (int) valueAnimatorWidth.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();

                lp.width = newWidth;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // rescaling of the object on Y-axis
        final ValueAnimator valueAnimatorHeight = ValueAnimator.ofInt(fromViewRect.height(), toViewRect.height());
        valueAnimatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get animated width value update
                int newHeight = (int) valueAnimatorHeight.getAnimatedValue();

                // Get and update LayoutParams of the animated view
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewToAnimate.getLayoutParams();
                lp.height = newHeight;
                viewToAnimate.setLayoutParams(lp);
            }
        });

        // moving of the object on X-axis
        ObjectAnimator translateAnimatorX = ObjectAnimator.ofFloat(viewToAnimate, "X", fromViewRect.left - parentViewRect.left, toViewRect.left - parentViewRect.left);

        // moving of the object on Y-axis
        ObjectAnimator translateAnimatorY = ObjectAnimator.ofFloat(viewToAnimate, "Y", fromViewRect.top - parentViewRect.top, toViewRect.top - parentViewRect.top);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1f));
        animatorSet.setDuration(duration); // can be decoupled for each animator separately
        animatorSet.setStartDelay(startDelay); // can be decoupled for each animator separately
        animatorSet.playTogether(valueAnimatorWidth, valueAnimatorHeight, translateAnimatorX, translateAnimatorY);

        return animatorSet;
    }

    public static void showWithCircularRevealAnimation(View view, int center, int animationType, int duration) {
        currentType = animationType;

        int positionArray[] = getPositions(view, animationType);

        int cx = positionArray[0];
        int cy = positionArray[1];

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

        Log.e("finalRadius:", "" + finalRadius);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(duration);

        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    private static int[] getPositions(View view, int animationType) {
        int cx = 0, cy = 0;

        if (animationType == TOP_RIGHT) {
            cx = view.getWidth();
        } else if (animationType == CENTER) {
            cx = view.getWidth() / 2;
            cy = view.getHeight() / 2;
        } else if (animationType == BOTTOM_LEFT) {
            cy = view.getHeight();
        } else if (animationType == BOTTOM_RIGHT) {
            cx = view.getWidth();
            cy = view.getHeight();
        }

        positionArray[0] = cx;
        positionArray[1] = cy;

        return positionArray;
    }

}
