package com.focusmedica.aqrshell;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.support.v7.widget.ActivityChooserView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;

public class Utility {
    public static Bitmap decodeSampledBitmapFromResource(int id, ImageView imageView) {
        int reqWidth = getImageWidth(imageView);
        int reqHeight = getImageHeight(imageView);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(imageView.getResources(), id);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(imageView.getResources(), id, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Config.RGB_565;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
            while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static int getImageWidth(ImageView imageView) {
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        int width = imageView.getLayoutParams().width;
        if (width <= 0) {
            width = getFieldValue(imageView, "mMaxWidth");
        }
        if (width <= 0) {
            return displayMetrics.widthPixels;
        }
        return width;
    }

    public static int getImageHeight(ImageView imageView) {
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        int height = imageView.getLayoutParams().height;
        if (height <= 0) {
            height = getFieldValue(imageView, "mMaxHeight");
        }
        if (height <= 0) {
            return displayMetrics.heightPixels;
        }
        return height;
    }

    public static int getFieldValue(Object object, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            if (fieldValue <= 0 || fieldValue >= ActivityChooserView.MEASURED_STATE_TOO_SMALL) {
                return 0;
            }
            return fieldValue;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDownLoadDestFilePath(Context context, String appName, String fileName) {
        File fullPath;
        String[] dirs = appName.split("/");
        File dirPtr = context.getDir(dirs[0], 0);
        if (!dirPtr.exists()) {
            dirPtr.mkdir();
        }
        if (dirs.length > 1) {
            fullPath = new File(dirPtr, dirs[1]);
            if (!fullPath.exists()) {
                fullPath.mkdir();
            }
        } else {
            fullPath = dirPtr;
        }
        if (fileName == null) {
            return fullPath.getAbsolutePath() + File.separator;
        }
        File filePtr = new File(fullPath, fileName);
        filePtr.setReadable(true, false);
        filePtr.setWritable(true, false);
        filePtr.setExecutable(true, false);
        return filePtr.getAbsolutePath();
    }

    public static void checkFiles(Context context, String appName) {
        File[] files = context.getDir(appName, 0).listFiles();
        if (files != null) {
            for (File file : files) {
                Log.d("CC", "File: " + file.getAbsolutePath());
            }
        }
    }

    public static String donwloadSizeFormatter(long totalsize, int bufferSize) {
        float number = ((float) totalsize) / ((float) bufferSize);
        return String.format(Locale.getDefault(), "%.2f", new Object[]{Float.valueOf(number)});
    }

    public static void deleteFile(String path) {
        File fileWithinMyDir = new File(path);
        if (fileWithinMyDir.exists()) {
            fileWithinMyDir.delete();
        }
    }

    @TargetApi(13)
    public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (VERSION.SDK_INT <= 13) {
            return display.getWidth();
        }
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @TargetApi(13)
    public static int getScreenHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (VERSION.SDK_INT <= 13) {
            return display.getHeight();
        }
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    @TargetApi(13)
    public static Point getScreenSize(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (VERSION.SDK_INT <= 13) {
            return new Point(display.getWidth(), display.getHeight());
        }
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static boolean isTablet(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (((double) Math.round(Math.sqrt(Math.pow((double) (((float) displayMetrics.widthPixels) / displayMetrics.xdpi), 2.0d) + Math.pow((double) (((float) displayMetrics.heightPixels) / displayMetrics.ydpi), 2.0d)))) >= 6.9d) {
            return true;
        }
        return false;
    }
}
