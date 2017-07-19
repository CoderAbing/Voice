package cn.xunlebao.voice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Vincent on 2017/7/18.
 */

public class PlayMusicService extends Service {
    public static final String MESSAGE_ACTION_MUSIC = "cn.xunlebao.voice.MUSIC_MP3";
    static MediaPlayer player;
    private static int mMusicId;
    private final IBinder binder = new AudioBinder();
    private MusicReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return binder;
    }
//
//    /**
//     * 当Audio播放完的时候触发该动作
//     */
//    @Override
//    public void onCompletion(MediaPlayer player) {
//        // TODO Auto-generated method stub
////        stopSelf();//结束了，则结束Service
//        if (!player.isPlaying()) {
//            player.start();
//        }
//    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate() {
        super.onCreate();
        mReceiver = new MusicReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(MESSAGE_ACTION_MUSIC);
        registerReceiver(mReceiver, mFilter);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMusicId = intent.getIntExtra("music", 0);
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, mMusicId);
        player.setLooping(true);
//        player.setOnCompletionListener(this);
        if (!player.isPlaying()) {
            player.start();
        }

        return START_STICKY;
    }

    public void onDestroy() {
        //super.onDestroy();
        if (player != null && player.isPlaying()) {
            player.stop();
        }
        player.release();
//        unregisterReceiver(mReceiver);
        MainActivity.isStart = false;
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    class AudioBinder extends Binder {
        //返回Service对象
        PlayMusicService getService() {
            return PlayMusicService.this;
        }

    }

    public static class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMusicId = intent.getIntExtra("music", -1);
            if (mMusicId != -1 && player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                }
                player = MediaPlayer.create(context, mMusicId);
                player.setLooping(true);
                player.start();
            }
        }
    }

}
