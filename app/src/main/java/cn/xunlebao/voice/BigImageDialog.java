package cn.xunlebao.voice;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;

/**
 * Created by Vincent on 2017/7/22.
 */

public class BigImageDialog extends Dialog {
    private ImageView imageView;

    public BigImageDialog(Context context) {
        super(context, R.style.floag_dialog);
        setContentView(R.layout.dialog_big_dialog);

        imageView = (ImageView) findViewById(R.id.iv_dialog_big_image);
        Glide.with(getContext()).load(R.drawable.main_image).into(imageView);
        setCanceledOnTouchOutside(true);
    }
}
