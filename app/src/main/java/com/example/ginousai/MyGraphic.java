package com.example.ginousai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class MyGraphic extends View {


private MainActivity mainActivity;

    public interface VisibilityChangeListener {
        void onVisibilityChange(boolean visible);
    }

    private VisibilityChangeListener visibilityChangeListener;

    //ゲームモード
    private int gamemode = 0;
    private boolean scorereset = false;
//    private boolean hidearrow = false;
    private boolean gamestart = false;
    private int scoreranking;

    //矢印描画用
    private int numarrow = 0;           //描画する矢印の数

    private int[] numbers;              //表示する矢印の種類
    private Bitmap l_arrow,u_arrow,r_arrow,d_arrow;     //矢印画像読込
    private Bitmap l_arrow_back,u_arrow_back,r_arrow_back,d_arrow_back;     //押された後の矢印画像読込

    //正誤判定用
    private int[] num = new int[4];     //setbtn_arrow初期化用
    private int[] setbtn = new int[4];  //mainよりどのbuttonが押されたか判別用
    private boolean[] judge;            //押されたbuttonと表示されているarrowの判定用

    //スコア用
    private double score = 0;
    private boolean perfect = true;
    private boolean correct = false;
    private int[] scoredate = new int[4];
    private boolean setscore = false;

    //カウントダウン用
    private static final long timecount = 60000;
//    private static final long timecount = 4000;//試験用
    private long timelimit = 0;
    private CountDownTimer countdown;
    private boolean settimer;
    private boolean starttime = false;
    private boolean gameover = false;


    //ゲームステージ用
    private int stagenum = 0;

    //BGM用
    public MediaPlayer mp1;

    //サウンド用
    private SoundPool sp;
    private int[] soundid = new int[3];


    public double getscoreranking(){
        return scoreranking;
    }
    public void setGamemode(int gamemode){
        this.gamemode = gamemode;
    }

    public void setScoreReset(boolean scorereset){
        this.scorereset = scorereset;
    }
    public int getGamemode(){
        return gamemode;
    }
    public boolean getScoreReset(){
        return scorereset;
    }
    public void setGameStart(boolean gamestart){
        this.gamestart = gamestart;
    }

    public void setOnVisibilityChangeListener(VisibilityChangeListener listener) {
        this.visibilityChangeListener = listener;
    }
    public void checkSomeConditionAndChangeVisibility(boolean hidearrow) {
        if (visibilityChangeListener != null) {
            visibilityChangeListener.onVisibilityChange(hidearrow);
        }
    }
    public void init_game(){
        Random random = new Random();
        int numprobability = 0;     //描画する矢印の確率
        numprobability = 10;
        if(random.nextInt(100) < numprobability){
            numarrow = random.nextInt(3) + 6;       // 6〜8の範囲でランダムな値
        }else {
            numarrow = random.nextInt(3) + 3;       // 3〜5の範囲でランダムな値
        }
//        numarrow = 3;                                // 【試験用】
        judge = new boolean[numarrow];              // 判定配列を初期化
        numbers = new int[numarrow];                // 表示矢印の番号格納数を初期化
        for (int i = 0; i < judge.length; i++) {    // numberにランダムな矢印の向きを格納
            numbers[i] = random.nextInt(4)+1;
        }
        for (int i = 0; i < judge.length; i++) {    // judgeに判定falseを格納
            judge[i] = false;
        }
        perfect = true;                             // score判定用変数を初期化
        setbtn_arrow(num);                          // buttonを初期化
    }
    public void init_bitmap(Context context){

        l_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.l_arrow01);             //画像を取得
        u_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.u_arrow01);             //画像を取得
        r_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.r_arrow01);             //画像を取得
        d_arrow = BitmapFactory.decodeResource(getResources(), R.drawable.d_arrow01);             //画像を取得
        l_arrow_back = BitmapFactory.decodeResource(getResources(), R.drawable.l_arrow_back);     //画像を取得
        u_arrow_back = BitmapFactory.decodeResource(getResources(), R.drawable.u_arrow_back);     //画像を取得
        r_arrow_back = BitmapFactory.decodeResource(getResources(), R.drawable.r_arrow_back);     //画像を取得
        d_arrow_back = BitmapFactory.decodeResource(getResources(), R.drawable.d_arrow_back);     //画像を取得

    }     //画像をbitmap化
    public void drawbitmap(Canvas canvas, Bitmap bitmap, int factor, Paint paint) {         //画像描画

        int cw = canvas.getWidth();
        int ch = canvas.getHeight();
        int left = 16 + ((factor-1) * 8);
        int top = 8;
        int right = 23 + ((factor-1) * 8);
        int bottom = 22;


        Rect srcrect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());     //画像の描画範囲を取得
        RectF rect = new RectF(cw * left / 80, ch * top / 80, cw * right / 80, ch * bottom / 80);//画像をどこに描画するかを取得

        canvas.save();
        canvas.drawBitmap(bitmap, srcrect, rect, paint);                                  //描画する
        canvas.restore();
    }   //画像描画
    public void draw_arrow(Canvas canvas, int num, int i, Paint paint) {
        switch (num) {

            case 1:
                drawbitmap(canvas, l_arrow, i, paint);
                break;
            case 2:
                drawbitmap(canvas, u_arrow, i, paint);
                break;
            case 3:
                drawbitmap(canvas, r_arrow, i, paint);
                break;
            case 4:
                drawbitmap(canvas, d_arrow, i, paint);
                break;

        }
    }              //矢印描画
    public void draw_backarrow(Canvas canvas, int num, int i, Paint paint) {
        switch (num) {

            case 1:
                drawbitmap(canvas, l_arrow_back, i, paint);
                break;
            case 2:
                drawbitmap(canvas, u_arrow_back, i, paint);
                break;
            case 3:
                drawbitmap(canvas, r_arrow_back, i, paint);
                break;
            case 4:
                drawbitmap(canvas, d_arrow_back, i, paint);
                break;

        }
    }          //暗転矢印描画
    public void setbtn_arrow(int[] value){          //mainbuttonのプッシュ受け口
    System.arraycopy(value,0,this.setbtn,0,setbtn.length);
}
    public void judge_arrow(Canvas canvas,  Paint paint){
        correct = false;
        boolean skipCheck = false;                                  //判定スキップ用変数
        for(int i = 0; i < judge.length; i++) {                   //描画矢印数
            if (!skipCheck) {                                       //判定スキップ(先頭の一段ずつ判定を行う)

                while (!judge[i]) {                             //正誤判定
                    for (int j = 0; j < setbtn.length; j++) {   //入力に対して設問と合っているかどうか判定
                        if (setbtn[j] == numbers[i]) {
                            judge[i] = true;                    //
                            skipCheck = true;
                            correct = true;
                            setbtn_arrow(num);                          // buttonを初期化
                            sp.play(soundid[0],1.0f,1.0f,0,0,1);
                            break;
                        }
                    }
                    if (!correct) {
                        break;                                      //無限ループを防ぐためにループを終了
                    }
                }
                if (judge[i]) {                                     //正誤に応じて描画
                    draw_backarrow(canvas, numbers[i], i, paint);
                } else {
                    draw_arrow(canvas, numbers[i], i, paint);
                    skipCheck = true;
                }
            } else {
                draw_arrow(canvas, numbers[i], i, paint);           //判定スキップ時の描画
            }
        }
    }
    public double game_score(){
        switch (gamemode){
            case 0:
                    score = 0;
                break;
            case 1:

                if (perfect){
                    sp.play(soundid[2],0.4f,0.4f,0,0,2);
                    score = score+30*1.2*numarrow;
                }else{
                    sp.play(soundid[1],1.0f,1.0f,0,0,0);
                    score = score+30*numarrow;
                }
                break;
            case 2:
                break;
        }

    return score;
}                   //スコア計算
    public void perfectgame(){
        if(input_btn(setbtn) == true && correct == false && perfect == true) {        //入力を一度でも間違えたらfalse
            perfect = false;
        }
    }
    public void setscoredate(){

        if(scoredate[3] < scoreranking){
            scoredate[3] = scoreranking;
        }

//        for (int i = 0; i < scoredate.length-1; i++) {
//            if (scoredate[i] < scoreranking){
//                scoredate[i] = scoredate[3];
//                break;
//            }
//        }

        for (int i = 0; i < scoredate.length - 1; i++) {
            for (int j = 0; j < scoredate.length - 1 - i; j++) {
                if (scoredate[j] < scoredate[j + 1]) {
                    // 値を入れ替える
                    int temp = scoredate[j];
                    scoredate[j] = scoredate[j + 1];
                    scoredate[j + 1] = temp;
                }
            }
        }
        setscore = true;
//
//        Float[] scoredateWrapper = new Float[scoredate.length];
//        for (int i = 0; i < scoredate.length; i++) {
//            scoredateWrapper[i] = scoredate[i];
//        }
//        Arrays.sort(scoredateWrapper, Collections.reverseOrder());
    }
    public boolean input_btn(int[] btn) {

        for (int value : btn) {
            if (value != 0) {
                return true;
            }
        }
        return false;
    }         //一つでも入力があればtrue

    public void starttimer() {
            countdown = new CountDownTimer(timelimit, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    settimer = true;
                    timelimit = millisUntilFinished;
                    invalidate();
                }

                @Override
                public void onFinish() {
                    gameover = true;
                    gamemode = 2;
                    invalidate();
                }
            }.start();
        }                    //ゲームタイム
    public boolean check_gameover(){
        if(timelimit <= 0){
            gameover = true;
        }
        return gameover;
    }
    public boolean gamewin(boolean[] judge) {

        for (int i = 0; i < judge.length; i++) {
            if (!judge[i]) {
                return false;
            }
        }
        return true;
    }     //ゲーム勝敗


    public MyGraphic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init_bitmap(context);
        init_game();

        AudioAttributes audioAttributes = new AudioAttributes.Builder().
                setUsage(AudioAttributes.USAGE_GAME).
                setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).
                build();


        mp1 = MediaPlayer.create(this.getContext(),R.raw.eiyuu_sousou);
        mp1.setVolume(0.1f, 0.1f);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            PlaybackParams params = new PlaybackParams();
            params.setSpeed(0.8f);  // 再生速度を設定
            mp1.setPlaybackParams(params);
        }

        sp = new SoundPool.Builder().
                setAudioAttributes(audioAttributes).
                setMaxStreams(1).build();
        soundid[0] = sp.load(getContext(), R.raw.se_sac02, 1);
        soundid[1] = sp.load(getContext(), R.raw.se_sac03, 1);
        soundid[2] = sp.load(getContext(), R.raw.se_sad03, 1);

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        //カラーコード
        int alpha = 255;
        int wallpaper[] = {50, 50, 50};
        int viewparam[] = {255, 255, 180};

        int cw = canvas.getWidth();
        int ch = canvas.getHeight();


        //背景描画
        Paint paint = new Paint();
        canvas.drawColor(Color.argb(alpha, wallpaper[0], wallpaper[1], wallpaper[2]));
        paint.setAntiAlias(true);

        switch (gamemode){
            case 0:     //タイトル画面
                mp1.start();
                paint.setTextSize((float)(cw*0.05));
                paint.setColor(Color.argb(alpha, viewparam[0], viewparam[1], viewparam[2]));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("画面をタップしてください",cw*20/40,ch*20/40,paint);
                paint.setTextSize((float)(cw*0.03));
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("スコアランキング",cw*2/40,ch*26/40,paint);
                canvas.drawText("1位: "+scoredate[0],cw*2/40,ch*29/40,paint);
                canvas.drawText("2位: "+scoredate[1],cw*2/40,ch*32/40,paint);
                canvas.drawText("3位: "+scoredate[2],cw*2/40,ch*35/40,paint);
                canvas.drawText("4位: "+scoredate[3],cw*2/40,ch*38/40,paint);
                checkSomeConditionAndChangeVisibility(true);
                timelimit = timecount;
                if(starttime == true){
                    countdown.cancel();
                    starttime = false;
                    settimer = false;
                    gamestart = false;

                }
                if(!setscore) {
                    setscoredate();
                }
                game_score();
                init_game();
                invalidate();
//                sp.play(soundid[1],1.0f,1.0f,0,10,4);
                break;


            case 1:     //ゲームプレイ

                checkSomeConditionAndChangeVisibility(false);
//                hidearrow = true;

                //----------------------時間を描画する-------------------//
                if (input_btn(setbtn)|gamestart && !settimer) {
                    starttimer();
                    starttime = true;
                }


                paint.setColor(Color.argb(alpha, viewparam[0], viewparam[1], viewparam[2]));
                paint.setTextSize((float)(cw*0.05));
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("time:  "+String.valueOf((int) timelimit/1000)+"秒",cw*23/40,ch*30/40,paint);
                judge_arrow(canvas,paint);

                perfectgame();
                paint.setColor(Color.argb(alpha, viewparam[0], viewparam[1], viewparam[2]));
                paint.setTextSize((float)(cw*0.05));
                canvas.drawText("score:  "+String.format("%,d",(int) score),cw*23/40,ch*20/40,paint);
                if(perfect){
                    canvas.drawText("PERFECT!!",cw*23/40,ch*25/40,paint);
                }else{
                    canvas.drawText("MISS",cw*23/40,ch*25/40,paint);
                }

                if(!starttime){
                    paint.setColor(Color.argb(alpha, wallpaper[0], wallpaper[1], wallpaper[2]));
                    canvas.drawRect(cw*20/40,ch*15/40,cw,ch,paint);
                    paint.setColor(Color.argb(alpha, viewparam[0], viewparam[1], viewparam[2]));
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize((float) (cw * 0.03));
                    canvas.drawText("矢印と同じボタンを押すと正解です",cw*18/40,ch*20/40,paint);
                    canvas.drawText("間違えずにボタンを押すと高得点です",cw*18/40,ch*23/40,paint);
                    canvas.drawText("制限時間は60秒です",cw*18/40,ch*26/40,paint);
                    canvas.drawText("矢印を押すとスタートです",cw*18/40,ch*32/40,paint);
                    sp.play(soundid[0],1.0f,1.0f,0,0,1);

                }
//                sp.play(soundid[0],1.0f,1.0f,0,10,8);

                //-----------------------設問リセット&スコア加点--------------------------//
                if (gamewin(judge)) {
                    game_score();
                    init_game();
                    invalidate();
                }
                break;

            //----------------------ステージ数を描画する-------------------//


    //        canvas.drawText("残りのステージ数:  "+String.valueOf(stagenum),cw*22/40,ch*25/40,paint);

            //----------------------ゲームオーバーを描写する-------------------//
            case 2:         //GAMEOVER
                if(gameover == true) {
                    canvas.drawColor(Color.argb(alpha, wallpaper[0], wallpaper[1], wallpaper[2]));
                    paint.setColor(Color.argb(alpha, viewparam[0], viewparam[1], viewparam[2]));
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize((float) (cw * 0.1));
                    canvas.drawText("GAMEOVER", cw * 20 / 40, ch * 20 / 40, paint);
                    paint.setTextSize((float) (cw * 0.08));
                    canvas.drawText("score:  " + String.format("%,d", (int) score), cw * 20 / 40, ch * 27 / 40, paint);
                    scoreranking = (int) score;
                    setscore = false;
                    checkSomeConditionAndChangeVisibility(true);
                }
                break;

        }


    }
        //----------------------タイトルを描画する---------------------//
        //----------------------ゲームクリアを描写する-------------------//

        //----------------------ハイスコア表示---------------------//
}

