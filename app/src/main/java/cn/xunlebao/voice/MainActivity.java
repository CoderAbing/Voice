package cn.xunlebao.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private AudioManager mAudioManger;
    private MyVolumeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setListener();
        myRegisterReceiver();
    }

    private void setListener() {
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
                Toast.makeText(this, "选择倒计时", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_main_music_type:
                Toast.makeText(this, "选择类型", Toast.LENGTH_SHORT).show();
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
