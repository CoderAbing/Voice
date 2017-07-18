package cn.xunlebao.voice;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;

/**
 * Created by Vincent on 2017/7/17.
 */

public class SnailBar extends android.support.v7.widget.AppCompatSeekBar {
    public SnailBar(Context context) {
        super(context);
        init();
    }

    public SnailBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnailBar(Context context, AttributeSet attrs,
                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMax(100);//设置最大进度
        setThumbOffset(dip2px(getContext(), 20));
//        setBackgroundResource(R.drawable.bg);//设置背景
        int padding = dip2px(getContext(), (float) 25);
        setPadding(padding, 5, padding, 5);//设置内边距
        setProgressDrawable(getResources().
            getDrawable(R.drawable.snailbar_define_style));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimationDrawable drawable =
            (AnimationDrawable) this.getThumb();
        drawable.start();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources()
            .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
