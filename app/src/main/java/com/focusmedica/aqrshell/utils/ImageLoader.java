package com.focusmedica.aqrshell.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

public class ImageLoader {

    private static Activity dummyActivity;

    public static BitmapDrawable loadDrawable(final Activity callingActivity, final int imageId) {

        Bitmap bitmap = null;

        dummyActivity = callingActivity;

        final Resources res = dummyActivity.getResources();

        final int screenWidth = dummyActivity.getResources().getDisplayMetrics().widthPixels;

        final int screenHeight = dummyActivity.getResources().getDisplayMetrics().heightPixels;

        bitmap	= BitmapFactory.decodeResource(res, imageId);

        Bitmap splashBitmapPort = Bitmap.createScaledBitmap(bitmap, screenHeight, screenWidth, true);

        return (new BitmapDrawable(res,splashBitmapPort));
    }

    public static BitmapDrawable loadDrawable(final Activity callingActivity, final int imageId, final int customWidth, final int customHeight, final boolean isInDip) {

        Bitmap splashBitmapPort = null;

        dummyActivity = callingActivity;

        final Resources res = dummyActivity.getResources();

        final Bitmap bitmap	= BitmapFactory.decodeResource(res, imageId);

        if(isInDip) {

            splashBitmapPort = Bitmap.createScaledBitmap(bitmap, customHeight, customWidth, true);
        }
        else {
            final DisplayMetrics metrics = new DisplayMetrics();
            dummyActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final float logicalDensity = metrics.density;

            final int dipWidth = (int) Math.ceil(customWidth * logicalDensity);
            final int dipHeight = (int) Math.ceil(customHeight * logicalDensity);

            splashBitmapPort = Bitmap.createScaledBitmap(bitmap, dipHeight, dipWidth, true);
        }

        return (new BitmapDrawable(res,splashBitmapPort));
    }
}
