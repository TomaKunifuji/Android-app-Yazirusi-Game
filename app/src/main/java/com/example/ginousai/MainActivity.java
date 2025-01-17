package com.example.ginousai;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private MyGraphic myGraphic;
    private String number = "";
    private int[] setbtn = new int[4];
    private float[] scoredate = new float[4];
    public void setNumber(String number){
        this.number = number;
    }
//    public void viewButton(String str){//-------------------データ保存通知-------------------//
//
//        Toast.makeText(this,str,Toast.LENGTH_LONG).show();
//    }
//    public void data_clear() {//-------------------データクリア-------------------//
//
//        new AlertDialog.Builder(this)
//                .setTitle("確認")
//                .setMessage("現在保存してあるデータを全て削除しますがよろしいですか？")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        number = "0.0";
//                        for (int i=0; i<scoredate.length; i++) {
//                            score_data_input(number);
//                            viewButton("保存しているデータを全て削除しました。");
//                        }
//                    }
//                })
//                .setNegativeButton("キャンセル",null)
//                .show();
//    }
//    public void score_data_input(float number) {   //--------------------アクションバー用にデータを入れる---------------------//
//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        SharedPreferences preferences = getSharedPreferences("pref",MODE_PRIVATE);
//
//        scoredate[3] = preferences.getFloat("score_date2", 0);
//        scoredate[2] = preferences.getFloat("score_date1", 0);
//        scoredate[1] = preferences.getFloat("score_date", 0);
//
//
//
////        if (!inputnumber.isEmpty()) {
////            try {
////                scoredate[0] = Float.parseFloat(inputnumber);  // number を整数に変換
////            } catch (NumberFormatException e) {
////                scoredate[0] = 5000;  // 変換できない場合はデフォルト値を設定
////                Log.e("calc_data_input", "数値変換エラー: " + inputnumber);  // エラーをログに表示
////            }
////        } else {
////            scoredate[0] = 0;  // 空の場合は 0 を代入
////            Log.e("calc_data_input", "number が空です");
////        }
//        scoredate[0] = (float) myGraphic.getscoreranking();
//
//        if(scoredate[0] < (float) myGraphic.getscoreranking()){
//            scoredate[0] = (float) myGraphic.getscoreranking();
//        }
//
//        editor.putFloat("score_date3", scoredate[3]);
//        editor.putFloat("score_date2", scoredate[2]);
//        editor.putFloat("score_date1", scoredate[1]);
//        editor.putFloat("score_date", scoredate[0]);
//
//        editor.apply();
//    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // ナビゲーションバーを非表示
                        | View.SYSTEM_UI_FLAG_FULLSCREEN       // ステータスバーを非表示

        );
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        hideSystemUI();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        hideSystemUI();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        myGraphic = (MyGraphic)findViewById(R.id.mygraphic1);

        myGraphic.setGamemode(0);
        myGraphic.setScoreReset(false);
//        ImageView imageView = (ImageView) findViewById(R.id.imageView10);
//        LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout1);
        ImageButton reset_btn = (ImageButton) findViewById(R.id.imageButton5);

//        reset_btn.setVisibility(View.GONE);
//        layout.setVisibility(View.GONE);
//        imageView.setVisibility(View.GONE);


        ImageButton l_btn = (ImageButton) findViewById(R.id.imageButton);
        l_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "左が押されました", Toast.LENGTH_SHORT).show();
                setbtn[0] = 1;
                myGraphic.setbtn_arrow(setbtn);
                myGraphic.invalidate();
                setbtn[0] = 0;
            }
        });

        ImageButton u_btn = (ImageButton) findViewById(R.id.imageButton2);
        u_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "上が押されました", Toast.LENGTH_SHORT).show();
                setbtn[1] = 2;
                myGraphic.setbtn_arrow(setbtn);
                myGraphic.invalidate();
                setbtn[1] = 0;
            }
        });

        ImageButton r_btn = (ImageButton) findViewById(R.id.imageButton3);
        r_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "右が押されました", Toast.LENGTH_SHORT).show();
                setbtn[2] = 3;
                myGraphic.setbtn_arrow(setbtn);
                myGraphic.invalidate();
                setbtn[2] = 0;
            }
        });

        ImageButton d_btn = (ImageButton) findViewById(R.id.imageButton4);
        d_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "下が押されました", Toast.LENGTH_SHORT).show();
                setbtn[3] = 4;
                myGraphic.setbtn_arrow(setbtn);
                myGraphic.invalidate();
                setbtn[3] = 0;
            }
        });

        ImageButton reset = (ImageButton) findViewById(R.id.imageButton5);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "下が押されました", Toast.LENGTH_SHORT).show();

                switch (myGraphic.getGamemode()){
                    case 0:
                        myGraphic.setScoreReset(true);
                        myGraphic.invalidate();
                        break;

                    case 1:
                        myGraphic.setGamemode(0);
                        myGraphic.invalidate();
                        break;

                    case 2:
                        myGraphic.setGamemode(0);
//                        score_data_input();
                        myGraphic.invalidate();
                        break;
                }

            }
        });

        myGraphic.setOnVisibilityChangeListener(new MyGraphic.VisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean visible) {
                if (visible) {
                    l_btn.setVisibility(View.INVISIBLE);
                    u_btn.setVisibility(View.INVISIBLE);
                    r_btn.setVisibility(View.INVISIBLE);
                    d_btn.setVisibility(View.INVISIBLE);
                    reset_btn.setVisibility(View.VISIBLE);
//                    layout.setVisibility(View.VISIBLE);
//                    imageView.setVisibility(View.VISIBLE);
                } else {
                    l_btn.setVisibility(View.VISIBLE);
                    u_btn.setVisibility(View.VISIBLE);
                    r_btn.setVisibility(View.VISIBLE);
                    d_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                switch (myGraphic.getGamemode()){
                    case 0:
                        myGraphic.setGamemode(1);
                        myGraphic.invalidate();
                        break;
                    case 1:
                        myGraphic.setGameStart(true);
                        break;
                }
//                if(myGraphic.getGamemode() == 0) {
//                    myGraphic.setGamemode(1);
//                    myGraphic.invalidate();
//                }
//                if(myGraphic.getGamemode() == 1){
//                    myGraphic.setGameStart(true);
//                }
//                break;
        }
        return super.onTouchEvent(event);
    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            // フルスクリーンモードを再設定
////            hideSystemUI();
//        }
//    }

}