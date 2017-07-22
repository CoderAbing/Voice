package cn.xunlebao.voice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


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
    private PendingIntent pintent;

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

        pintent = PendingIntent.getService(this, 0, intent, 0);

        Notification notify = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.drawable.notification_logo)
                //设置通知标题
                .setContentTitle("Voice")
                //设置通知内容
                .setContentText("这是来自大自然的声音")
                .setContentIntent(pintent)
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                .setWhen(System.currentTimeMillis())
                .build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;


        //让该service前台运行，避免手机休眠时系统自动杀掉该服务
        //如果 id 为 0 ，那么状态栏的 notification 将不会显示。
        startForeground(startId, notify);

        mMusicId = intent.getIntExtra("music", 0);
        //我们从raw文件夹中获取一个应用自带的mp3文件
        player = MediaPlayer.create(this, mMusicId);
        player.setLooping(true);
//        player.setOnCompletionListener(this);
        if (!player.isPlaying()) {
            player.start();
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (player != null) {
                    player.setLooping(true);
                    player.start();
                }

            }
        });

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
                player.setLooping(true);
                player = MediaPlayer.create(context, mMusicId);
                player.start();
            }
        }
    }

}
