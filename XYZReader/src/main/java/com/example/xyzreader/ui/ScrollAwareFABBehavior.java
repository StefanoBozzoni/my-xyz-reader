package com.example.xyzreader.ui;
/*
 * Copyright 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) ||
                (dependency instanceof FloatingActionButton) ;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
               super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof FloatingActionButton) {
            if (dependency.getBottom() == 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
                return true;
            } else if (dependency.getBottom() == dependency.getHeight() && child.getVisibility() != View.VISIBLE) {
                // update the translation Y
                //child.setTranslationY(getFabTranslationYForSnackbar(parent, child));
                child.show();
                return true;
            }
            return false;
        } else {
            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                fab.setVisibility(View.INVISIBLE);
            }
        });

        if (dyConsumed > 0) {
            child.hide();
        } else if (dyConsumed < 0) {
            child.show();
        }
    }

}