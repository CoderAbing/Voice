package cn.xunlebao.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.OptionsPickerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.sb_main_music_value)
    SnailBar sbMainMusicValue;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.tv_main_sleep_time)
    TextView tvMainSleepTime;
    @BindView(R.id.tv_main_music_type)
    TextView tvMainMusicType;
    @BindView(R.id.iv_background_first)
    ImageView ivBackgroundFirst;
    @BindView(R.id.iv_background_second)
    ImageView ivBackgroundSecond;
    private AudioManager mAudioManger;
    private MyVolumeReceiver mReceiver;
    private OptionsPickerView mMusicPik;
    private OptionsPickerView mTimePicker;
    public static boolean isStart = false;
    private Thread mThread;

    private String[] mMusicName = {
        "雨声-1", "雨声-2", "雨声-3", "雨声-4", "雨声-5", "雨声-6", "雨声-7", "雨声-8", "雨声-9", "雨声-10", "雨声-11", "雨声-12", "雨声-13", "雨声-14", "雨声-15", "雨声-16"
        , "火-1", "火-2", "火-3", "火-4", "火-5", "火-6"
        , "森林-1", "森林-2", "森林-3", "森林-4", "森林-5", "森林-6", "森林-7", "森林-8", "森林-9", "森林-10", "森林-11", "森林-12", "森林-13"
        , "海-1", "海-2", "海-3", "海-4", "海-5"
        , "风-1", "风-2", "风-3", "风-4", "风-5", "风-6", "风-7"
        , "冥想-1", "冥想-2", "冥想-3", "冥想-4", "冥想-5"
    };
    private Integer[] mMusicRec = {
        R.raw.rain_1, R.raw.rain_2, R.raw.rain_3, R.raw.rain_4, R.raw.rain_5, R.raw.rain_6, R.raw.rain_7, R.raw.rain_8, R.raw.rain_9, R.raw.rain_10, R.raw.rain_11, R.raw.rain_12, R.raw.rain_13, R.raw.rain_14, R.raw.rain_15, R.raw.rain_16
        , R.raw.fire_1, R.raw.fire_2, R.raw.fire_3, R.raw.fire_4, R.raw.fire_5, R.raw.fire_6
        , R.raw.forest_1, R.raw.forest_2, R.raw.forest_3, R.raw.forest_4, R.raw.forest_5, R.raw.forest_6, R.raw.forest_7, R.raw.forest_8, R.raw.forest_9, R.raw.forest_10, R.raw.forest_11, R.raw.forest_12, R.raw.forest_13
        , R.raw.sea_1, R.raw.sea_2, R.raw.sea_3, R.raw.sea_4, R.raw.sea_5
        , R.raw.wind_1, R.raw.wind_2, R.raw.wind_3, R.raw.wind_4, R.raw.wind_5, R.raw.wind_6, R.raw.wind_7
        , R.raw.meditation_1, R.raw.meditation_2, R.raw.meditation_3, R.raw.meditation_4, R.raw.meditation_5
    };
    private Integer[] mRain = {R.drawable.rain_0, R.drawable.rain_1, R.drawable.rain_2, R.drawable.rain_3, R.drawable.rain_4, R.drawable.rain_5, R.drawable.rain_6, R.drawable.rain_7, R.drawable.rain_8, R.drawable.rain_9,};
    private Integer[] mFire = {R.drawable.fire_0, R.drawable.fire_1, R.drawable.fire_2, R.drawable.fire_3, R.drawable.fire_4, R.drawable.fire_5, R.drawable.fire_6, R.drawable.fire_7, R.drawable.fire_8, R.drawable.fire_9};
    private Integer[] mSea = {R.drawable.sea_0, R.drawable.sea_1, R.drawable.sea_2, R.drawable.sea_3, R.drawable.sea_4, R.drawable.sea_5, R.drawable.sea_6, R.drawable.sea_7, R.drawable.sea_8, R.drawable.sea_9};
    private Integer[] mForest = {R.drawable.forest_0, R.drawable.forest_1, R.drawable.forest_2, R.drawable.forest_3, R.drawable.forest_4, R.drawable.forest_5, R.drawable.forest_6, R.drawable.forest_7, R.drawable.forest_8, R.drawable.forest_9,};
    private Integer[] mWind = {R.drawable.wind_0, R.drawable.wind_1, R.drawable.wind_2, R.drawable.wind_3, R.drawable.wind_4, R.drawable.wind_5, R.drawable.wind_6, R.drawable.wind_7, R.drawable.wind_8, R.drawable.wind_9,};
    private Integer[] mMeditation = {R.drawable.meditation_1, R.drawable.meditation_2, R.drawable.meditation_3, R.drawable.meditation_4, R.drawable.meditation_4, R.drawable.meditation_5, R.drawable.meditation_6, R.drawable.meditation_7, R.drawable.meditation_8, R.drawable.meditation_9,};

    private ArrayList<String> mMusicList = new ArrayList();
    private String name = "雨声-1";
    private Random random;
    private int mRec;
    private AlphaAnimation V2I;
    private AlphaAnimation I2V;
    private boolean isTop = true;//标识ivBackgroundFirst是否在最上层
    private Bitmap bitFirst, bitSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initPickerView();
        setListener();
        updateImg();
        myRegisterReceiver();
    }

    private void initPickerView() {
        mMusicPik = new OptionsPickerView(this);
        mMusicPik.setCancelable(true);//是否点击外部消失
        mMusicPik.setTextSize(16f);//设置滚轮字体大小
        mMusicPik.setTitle("场景");//设置标题
        mMusicPik.setCancelText("取消");//设置取消文字
        mMusicPik.setCancelTextColor(Color.parseColor("#39B54A"));//设置取消文字颜色
        mMusicPik.setCancelTextSize(14f);//设置取消文字大小
        mMusicPik.setSubmitText("确认");//设置确认字体
        mMusicPik.setCancelTextSize(14f);//设置去人字体大小
        mMusicPik.setSubmitTextColor(Color.parseColor("#39B54A"));//设置确认字体颜色
        //optionsPicker.setHeadBackgroundColor(Color.RED);//设置头部背景色
        // 设置数据
        mMusicList = new ArrayList<>(Arrays.asList(mMusicName));

        mMusicPik.setPicker(mMusicList);
        // 设置选项单位
//        mOptionsPickerView.setLabels("性");
        mMusicPik.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                name = mMusicList.get(option1);
                tvMainMusicType.setText(name);
                Intent intent;
                if (isStart) {
                    intent = new Intent(PlayMusicService.MESSAGE_ACTION_MUSIC);
                    intent.putExtra("music", mMusicRec[option1]);
                    sendBroadcast(intent);
                } else {
                    intent = new Intent(MainActivity.this, PlayMusicService.class);
                    intent.putExtra("music", mMusicRec[option1]);
                    startService(intent);
                    isStart = true;
                }

            }
        });


        mTimePicker = new OptionsPickerView(this);
        mTimePicker.setCancelable(true);//是否点击外部消失
        mTimePicker.setTextSize(16f);//设置滚轮字体大小
        mTimePicker.setTitle("倒计时(分)");//设置标题
        mTimePicker.setCancelText("取消");//设置取消文字
        mTimePicker.setCancelTextColor(Color.parseColor("#39B54A"));//设置取消文字颜色
        mTimePicker.setCancelTextSize(14f);//设置取消文字大小
        mTimePicker.setSubmitText("确认");//设置确认字体
        mTimePicker.setCancelTextSize(14f);//设置去人字体大小
        mTimePicker.setSubmitTextColor(Color.parseColor("#39B54A"));//设置确认字体颜色
        //optionsPicker.setHeadBackgroundColor(Color.RED);//设置头部背景色
        // 设置数据
        final ArrayList<String> times = new ArrayList<>();
        times.add("关闭");
        times.add("15");
        times.add("30");
        times.add("60");
        times.add("90");
        times.add("120");
        mTimePicker.setPicker(times);
        // 设置选项单位
//        mOptionsPickerView.setLabels("性");
        mTimePicker.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int option1, int option2, int option3) {
                String time = times.get(option1);
                tvMainSleepTime.setText(time);
                if ("关闭".equals(time)) {
                    Toast.makeText(MainActivity.this, time, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, time, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void updateRec() {
        if (random == null)
            random = new Random();

        switch (name.substring(0, 1)) {
            case "雨":
                mRec = mRain[random.nextInt(mRain.length)];
                break;
            case "火":
                mRec = mFire[random.nextInt(mFire.length)];
                break;
            case "森":
                mRec = mForest[random.nextInt(mForest.length)];
                break;
            case "海":
                mRec = mSea[random.nextInt(mSea.length)];
                break;
            case "风":
                mRec = mWind[random.nextInt(mWind.length)];
                break;
            case "冥":
                mRec = mMeditation[random.nextInt(mMeditation.length)];
                break;
            default:
                mRec = R.drawable.forest_5;
        }
    }

    private void updateImg() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        updateRec();
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTop) {
                                    ivBackgroundFirst.startAnimation(V2I);
                                    ivBackgroundSecond.startAnimation(I2V);
                                } else {
                                    ivBackgroundFirst.startAnimation(I2V);
                                    ivBackgroundSecond.startAnimation(V2I);
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mThread.start();
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    private void setListener() {
        I2V = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.invisible_2_visible);
        V2I = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.visible_2_invisible);
        V2I.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateRec();
                if (isTop) {
                    ivBackgroundFirst.setImageBitmap(readBitMap(MainActivity.this, mRec));
                    isTop = false;

                } else {
                    ivBackgroundSecond.setImageBitmap(readBitMap(MainActivity.this, mRec));
                    isTop = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAudioManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManger.setMode(AudioManager.STREAM_MUSIC);
        sbMainMusicValue.setMax(mAudioManger.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int currVolume = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
        sbMainMusicValue.setProgress(currVolume);
        sbMainMusicValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManger.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 注册当音量发生变化时接收的广播
     */

    private void myRegisterReceiver() {
        mReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mThread.interrupt();
        stopService(new Intent(this, PlayMusicService.class));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            int currVolume = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC) - 2;// 降低当前的媒体音量
            mAudioManger.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume, 0);
            sbMainMusicValue.setProgress(currVolume);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            int currVolume = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC) + 2;// 提升当前的媒体音量
            mAudioManger.setStreamVolume(AudioManager.STREAM_MUSIC, currVolume, 0);
            sbMainMusicValue.setProgress(currVolume);
            return true;
        } else return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_main_sleep_time, R.id.tv_main_music_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_main_sleep_time:
                mTimePicker.show();
                break;
            case R.id.tv_main_music_type:
                mMusicPik.show();
                break;
        }
    }

    /**
     * 处理音量变化时的界面显示
     *
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                sbMainMusicValue.setProgress(currVolume);
            }
        }
    }

}
