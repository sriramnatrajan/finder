package com.focusmedica.aqrshell;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentActivity extends Activity {
    TextView mtitle,mDescription;
    ImageView previewThumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mtitle=(TextView)findViewById(R.id.content_title);
        //previewThumbnail=(ImageView)findViewById(R.id.ivChapter);
        mDescription=(TextView)findViewById(R.id.content_description);
        //Bitmap bitmap=(Bitmap)getIntent().getParcelableExtra("bmp");

        Bundle bundle=getIntent().getExtras();
     /*   if(getIntent().hasExtra("byteArray")) {
            ImageView previewThumbnail = new ImageView(this);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            previewThumbnail.setImageBitmap(b);
        }
        previewThumbnail=(ImageView)findViewById(R.id.ivChapter);*/
        String title=bundle.getString("Content title");
        String descrip=bundle.getString("Content description");
        mtitle.setText(title);
        mDescription.setText(descrip);

    }
}
