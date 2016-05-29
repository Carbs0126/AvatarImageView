package cn.carbs.android.avatarimageview.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * @author Carbs.Wang
 */
public class ActivityViewSingle extends AppCompatActivity {

    private AvatarImageView aiv;
    private Button button0;
    private Button button1;
    private Button button2;

    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single);

        aiv = (AvatarImageView) this.findViewById(R.id.aiv);
        aiv.setTextAndColor("安", 0x66660000);

        button0 = (Button) this.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityViewSingle.this, ActivityViewListNet.class);
                startActivity(it);
            }
        });

        button1 = (Button) this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivityViewSingle.this, ActivityViewListLocal.class);
                startActivity(it);
            }
        });

        button2 = (Button) this.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (flag) {
                    InputStream is = getResources().openRawResource(R.drawable.id_014);
                    Bitmap mBitmap = BitmapFactory.decodeStream(is);
                    aiv.setBitmap(mBitmap);
                } else {
                    aiv.setTextAndColor("卓", AvatarImageView.COLORS[2]);
                }
                flag = !flag;
            }
        });

    }


}

