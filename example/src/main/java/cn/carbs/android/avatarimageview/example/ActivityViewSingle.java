package cn.carbs.android.avatarimageview.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * @author Carbs.Wang
 */
public class ActivityViewSingle extends AppCompatActivity {

    private static String texts =   "朝辞白帝彩云间" +
                                    "千里江陵一日还" +
                                    "两岸猿声啼不住" +
                                    "轻舟已过万重山";
    private AvatarImageView aivLeft;
    private AvatarImageView aivRight;
    private Button button0;
    private Button button1;
    private Button button2;

    private int mIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single);

        aivLeft = (AvatarImageView) this.findViewById(R.id.aiv_left);
        aivLeft.setTextAndColor("朝", 0x66660000);
        aivRight = (AvatarImageView) this.findViewById(R.id.aiv_right);
        aivRight.setTextAndColor("安卓", 0x66660000);

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
                int mod = mIndex % 3;
                if (mod == 0) {
                    aivLeft.setImageResource(R.drawable.id_014);
                    aivRight.setImageResource(R.drawable.id_014);
                } else if(mod == 1){
                    if(mIndex + 1 > texts.length()){
                        mIndex = 0;
                    }
                    String show = texts.substring(mIndex, mIndex + 1);
                    aivLeft.setTextAndColor(show, AvatarImageView.COLORS[4]);
                    aivRight.setTextAndColor(show, AvatarImageView.COLORS[4]);
                } else if(mod == 2){
                    if(mIndex + 2 > texts.length()){
                        mIndex = 0;
                    }
                    String show = texts.substring(mIndex, mIndex + 2);
                    aivLeft.setTextAndColor(show, AvatarImageView.COLORS[3]);
                    aivRight.setTextAndColor(show, AvatarImageView.COLORS[3]);
                }
                mIndex++;
            }
        });
    }
}