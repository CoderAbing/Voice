package cn.xunlebao.voice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Vincent on 2017/7/18.
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    private int mMusicId;
    private final IBinder binder = new AudioBinder();

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        mMusicId = arg0.getIntExtra("music", 0);

        return binder;
    }

    /**
     * 当Audio播放完的时候触发该动作
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
//        stopSelf();//结束了，则结束Service
        if (!player.isPlaying()) {
            player.start();
        }
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate() {
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, R.raw.sea_1);
        player.setOnCompletionListener(this);
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!player.isPlaying()) {
            player.start();
        }
        return START_STICKY;
    }

    public void onDestroy() {
        //super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
    }

    //为了和Activity交互，我们需要定义一个Binder对象
    class AudioBinder extends Binder {

        //返回Service对象
        PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }


}
