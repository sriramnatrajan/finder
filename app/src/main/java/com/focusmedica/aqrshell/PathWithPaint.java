package com.focusmedica.aqrshell;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Lokesh on 18-May-16.
 */
public class PathWithPaint {

    private Path path;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    Paint mPaint;

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

}
