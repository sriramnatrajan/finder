package com.focusmedica.aqrshell;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MediaPlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    ImageView pauseAndResume,refresh,next,prev,screenshot,pencil;
    SeekBar videoProgress;
    private Handler mHandler = new Handler();;
    private Utilities util;
    TextView current;
    boolean playFlag,isTrue=false;
    VideoView videoView;
    int vDuration,currentpos;
    LinearLayout linear;
    RelativeLayout relate;
    View mView;
    Uri uri;
    private Paint mPaint;
    private Canvas mCanvas;
        int video_url=R.raw.introduction;
    DataModel content,content1;
    ArrayList<DataModel> chapter=new ArrayList<>();
    private MyDataBase handler;
    String videoName;
    String path = Environment.getExternalStorageDirectory()
            + File.separator + "Pictures/screenshot.png";
    private static final int REQUEST_CODE = 0x11;
    final Context context = this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String vdoname,language,titleName;
    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
         // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {

            //We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        videoView=(VideoView)findViewById(R.id.videoView);
        videoProgress=(SeekBar)findViewById(R.id.seekbar);
        videoProgress.setOnSeekBarChangeListener(this);
        pauseAndResume=(ImageView) findViewById(R.id.btnPauseResume);
        relate=(RelativeLayout) findViewById(R.id.relate);
        linear=(LinearLayout)findViewById(R.id.linear);
        screenshot=(ImageView)findViewById(R.id.btnScreenshot);
        refresh=(ImageView)findViewById(R.id.btnRefresh);
        pencil=(ImageView)findViewById(R.id.btnPencil);
        current=(TextView)findViewById(R.id.current);
        linear.setVisibility(View.INVISIBLE);
        util = new Utilities();
        Bundle extras = getIntent().getExtras();
        final String video_name = extras.getString("title");
        /*vdoname=getIntent().getStringExtra("vdoname");
        language=getIntent().getStringExtra("language");
        titleName=getIntent().getStringExtra("titleName");*/

            uri=Uri.parse(video_name);
            //  uri=Uri.parse(getFilesDir()+"/"+video_name);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, Uri.parse(video_name));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            vDuration = Integer.parseInt(time );

            playVideo(uri);

        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear.getVisibility() == View.INVISIBLE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linear.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                }else{
                    linear.setVisibility(View.INVISIBLE);
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTrue == true) {
                    relate.removeView(mView);
                    mView = new DrawingView(MediaPlayerActivity.this);
                    relate.addView(mView);
                }
            }
        });

        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTrue==false) {
                    mView = new DrawingView(MediaPlayerActivity.this);
                    relate.addView(mView, new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT));
                    init();
                    pencil.setImageResource(R.drawable.pencil1);
                    isTrue=true;
                }else if(isTrue==true){
                    relate.removeView(mView);
                    pencil.setImageResource(R.drawable.pencil);
                    isTrue=false;
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog mDialog=new Dialog(context);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.dialog_layout);


                TextView text=(TextView)mDialog.findViewById(R.id.tv_dialogText);
                TextView desc=(TextView)mDialog.findViewById(R.id.TV_delete);
                final Button yes=(Button)mDialog.findViewById(R.id.valid_btn);

                Button no=(Button)mDialog.findViewById(R.id.login_btn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        File file = new File(video_name);
                        if (file.exists()) {
                            file.delete();
                            //btnDownload.setImageResource(R.drawable.download);
                            Toast.makeText(getApplicationContext(), "Content Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mDialog.dismiss();
                    }
                });
                mDialog.show();
               /* final AlertDialog.Builder builder=new AlertDialog.Builder(MediaPlayerActivity.this).setTitle("Delete")
                        .setMessage("Are you sure want to delete ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                File file = new File(getFilesDir() + "/" + titleName);
                                if (file.exists() || position == 0) {
                                    file.delete();
                                    //btnDownload.setImageResource(R.drawable.download);
                                    Toast.makeText(getApplicationContext(), "Content Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).setNegativeButton("No",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                builder.show();*/
            }
        });

        pauseAndResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (pauseAndResume.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.pause, getTheme()).getConstantState()) {
                        videoView.pause();
                        pauseAndResume.setImageResource(R.drawable.play1);
                    } else {
                        videoView.start();
                        pauseAndResume.setImageResource(R.drawable.pause);
                    }
                }else{
                    if (pauseAndResume.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.pause).getConstantState()) {
                        videoView.pause();
                        pauseAndResume.setImageResource(R.drawable.play1);
                    } else {
                        videoView.start();
                        pauseAndResume.setImageResource(R.drawable.pause);
                    }
                }
            }
        });

        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions(MediaPlayerActivity.this);
                Bitmap bitmap = takeScreenshot(MediaPlayerActivity.this,uri);
                saveBitmap(bitmap);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration =vDuration;
        int currentPosition = util.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        videoView.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    //canvas
    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
    }


    private Bitmap takeScreenshot(Context context, Uri uri) {
        MediaMetadataRetriever mediametadataretriever = new MediaMetadataRetriever();
        try {
            mediametadataretriever.setDataSource(context, uri);
            Bitmap bitmap = mediametadataretriever.getFrameAtTime(videoView.getCurrentPosition()*1000);
            Bitmap mainBitmap= ThumbnailUtils.extractThumbnail(bitmap, videoView.getWidth(), videoView.getHeight(), 2);
            Bitmap bmOverlay = Bitmap.createBitmap(videoView.getWidth(), videoView.getHeight(), bitmap.getConfig());
            relate.setDrawingCacheEnabled(true);
            Bitmap bitmap1=relate.getDrawingCache();
            mCanvas=new Canvas(bmOverlay);
            mCanvas.drawBitmap(mainBitmap,new Matrix(), null);
            mCanvas.drawBitmap(bitmap1, 0,0, null);
            if(null != mainBitmap)
            {
                return bmOverlay;
            }
            return bmOverlay;
        } catch (Throwable t) {
            return null;
        } finally {
            try
            {
                mediametadataretriever.release();
            }
            catch(RuntimeException e) { }
        }
    }

    private void saveBitmap(Bitmap bitmap) {
        FileOutputStream fos;
        try {
            fos =new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sentmail(path);
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void sentmail(String path) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"mail.gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                "Acne");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Screenshot of image");
        emailIntent.setType("image/jpg");
        Uri myUri = Uri.parse("file://" + path);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = vDuration;
            long currentDuration = videoView.getCurrentPosition();

            // Displaying Total Duration time
            //total.setText(""+util.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            current.setText(""+util.milliSecondsToTimer(currentDuration));
            // Updating progress bar
            int progress = (int)(util.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            videoProgress.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);

        }
    };

    private void playVideo(Uri uri) {

        if (videoView.isPlaying() )
        {
            videoView.stopPlayback();
        }
        videoView.setVideoURI(uri);
        videoView.start();
        updateProgressBar();
        videoView.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                playFlag=false;
                finish();
            }
        });
    }

    //Canvas
    private class DrawingView extends View {

        private Path path;
        private Bitmap mBitmap;

        public DrawingView(Context context) {
            super(context);
            path = new Path();
            mBitmap = Bitmap.createBitmap(relate.getWidth(), relate.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(mBitmap);
        }

        private ArrayList<PathWithPaint> _graphics1 = new ArrayList<PathWithPaint>();

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            PathWithPaint pp = new PathWithPaint();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                path.moveTo(event.getX(), event.getY());
                path.lineTo(event.getX(), event.getY());
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                path.lineTo(event.getX(), event.getY());
                pp.setPath(path);
                pp.setmPaint(mPaint);
                _graphics1.add(pp);
            }
            invalidate();
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (_graphics1.size() > 0) {
                canvas.drawPath(
                        _graphics1.get(_graphics1.size() - 1).getPath(),
                        _graphics1.get(_graphics1.size() - 1).getmPaint());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            currentpos = savedInstanceState.getInt("videoProgress");
            videoView.seekTo(currentpos);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save current video progress
        savedInstanceState.putInt("videoProgress", videoView.getCurrentPosition());
        super.onSaveInstanceState(savedInstanceState);
    }
}
