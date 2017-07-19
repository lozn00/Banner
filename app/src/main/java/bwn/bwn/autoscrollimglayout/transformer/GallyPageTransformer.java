package bwn.bwn.autoscrollimglayout.transformer;

import android.view.View;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/5/3.
 */

public class GallyPageTransformer extends ABaseTransformer {

    private static final float min_scale = 0.85f;

    @Override
    protected void onTransform(View page, float position) {
        float scaleFactor = Math.max(min_scale, 1 - Math.abs(position));
        float rotate = 20 * Math.abs(position);
        if (position < -1) {

        } else if (position < 0) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(rotate);
        } else if (position >= 0 && position < 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        } else if (position >= 1) {
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setRotationY(-rotate);
        }
    }
}
