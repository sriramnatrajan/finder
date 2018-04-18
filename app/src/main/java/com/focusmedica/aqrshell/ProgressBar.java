package com.focusmedica.aqrshell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ProgressBar extends View {
    private float endAngle;
    private float lineLength;
    private Paint paintBg;
    private Paint paintProgress;
    private float progress;
    private RectF rectF;
    private float startAngle;
    private float strokeLineWidth;
    private float strokeProgressWidth;
    private int viewSize;

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paintBg = null;
        this.paintProgress = null;
        this.rectF = null;
        this.startAngle = 270.0f;
        this.endAngle = 360.0f;
        this.progress = 0.0f;
        this.strokeProgressWidth = 0.0f;
        this.strokeLineWidth = 0.0f;
        this.lineLength = 0.0f;
        this.viewSize = 200;
        initUI(context);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paintBg = null;
        this.paintProgress = null;
        this.rectF = null;
        this.startAngle = 270.0f;
        this.endAngle = 360.0f;
        this.progress = 0.0f;
        this.strokeProgressWidth = 0.0f;
        this.strokeLineWidth = 0.0f;
        this.lineLength = 0.0f;
        this.viewSize = 200;
        initUI(context);
    }

    public ProgressBar(Context context) {
        super(context);
        this.paintBg = null;
        this.paintProgress = null;
        this.rectF = null;
        this.startAngle = 270.0f;
        this.endAngle = 360.0f;
        this.progress = 0.0f;
        this.strokeProgressWidth = 0.0f;
        this.strokeLineWidth = 0.0f;
        this.lineLength = 0.0f;
        this.viewSize = 200;
        initUI(context);
    }

    private void initUI(Context context) {
        if (isInEditMode()) {
            this.viewSize = 200;
        } else {
            this.viewSize = (int) (((double) Utility.getScreenHeight(context)) * 0.1d);
        }
        this.strokeProgressWidth = (float) (((double) this.viewSize) * 0.1d);
        this.strokeLineWidth = (float) (((double) this.strokeProgressWidth) * 0.2d);
        this.paintBg = new Paint();
        this.paintBg.setAntiAlias(true);
        this.paintBg.setStyle(Style.STROKE);
        this.paintBg.setStrokeWidth(this.strokeProgressWidth);
        this.paintBg.setStrokeCap(Cap.ROUND);
        this.paintBg.setColor(-11184811);
        this.paintProgress = new Paint();
        this.paintProgress.setAntiAlias(true);
        this.paintProgress.setStyle(Style.STROKE);
        this.paintProgress.setStrokeCap(Cap.ROUND);
        this.paintProgress.setColor(-1);
        this.rectF = new RectF(this.strokeProgressWidth, this.strokeProgressWidth, (float) this.viewSize, (float) this.viewSize);
        this.lineLength = (float) (((double) this.rectF.height()) * 0.2d);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) (((float) this.viewSize) + this.strokeProgressWidth), (int) (((float) this.viewSize) + this.strokeProgressWidth));
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawArc(this.rectF, this.startAngle, this.endAngle, false, this.paintBg);
        this.paintProgress.setStrokeWidth(this.strokeProgressWidth);
        canvas.drawArc(this.rectF, this.startAngle, this.progress, false, this.paintProgress);
        this.paintProgress.setStrokeWidth(this.strokeLineWidth);
        Canvas canvas2 = canvas;
        canvas2.drawLine(this.rectF.centerX() - this.lineLength, this.rectF.centerY() - this.lineLength, this.lineLength + this.rectF.centerX(), this.lineLength + this.rectF.centerY(), this.paintProgress);
        canvas2 = canvas;
        canvas2.drawLine(this.lineLength + this.rectF.centerX(), this.rectF.centerY() - this.lineLength, this.rectF.centerX() - this.lineLength, this.lineLength + this.rectF.centerY(), this.paintProgress);
    }

    public void updateProgress(float progress) {
        this.progress = (360.0f * progress) / 100.0f;
        postInvalidate();
    }
}
