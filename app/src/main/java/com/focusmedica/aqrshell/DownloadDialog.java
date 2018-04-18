package com.focusmedica.aqrshell;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class DownloadDialog extends Dialog implements OnClickListener {
    public static final int DOWNLOAD_CANCEL_BY_USER = 1;
    private int buttonAction;
    private ProgressBar progressBar;
    private TextView tvMessage;
    CancelListener cancelListener;

    public DownloadDialog(Context context, CancelListener cancelListener) {
        super(context);
        this.tvMessage = null;
        this.progressBar = null;
        this.buttonAction = 0;
        this.cancelListener=cancelListener;
        initUI();
    }

    public DownloadDialog(Context context) {
        super(context);
        this.tvMessage = null;
        this.progressBar = null;
        this.buttonAction = 0;
        initUI();
    }

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
        this.tvMessage = null;
        this.progressBar = null;
        this.buttonAction = 0;
        initUI();
    }

    protected DownloadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.tvMessage = null;
        this.progressBar = null;
        this.buttonAction = 0;
        initUI();
    }

    private void initUI() {
        requestWindowFeature(DOWNLOAD_CANCEL_BY_USER);
        setContentView(R.layout.progress);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.tvMessage = (TextView) findViewById(R.id.tv_download_dialog);
        this.progressBar = (ProgressBar) findViewById(R.id.progress_bar_download_dialog);
        this.progressBar.setOnClickListener(this);
    }

    public void updateDialogProgress(String text, float progress) {
        this.tvMessage.setText(text);
        this.progressBar.updateProgress(progress);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.progress_bar_download_dialog) {
            this.buttonAction = DOWNLOAD_CANCEL_BY_USER;
            dismiss();
            cancelListener.cancel();
        }
    }

    public int getButtonAction() {
        return this.buttonAction;
    }

    public void resetButtonAction() {
        this.buttonAction = 0;
    }
}
