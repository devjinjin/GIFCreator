package com.devjinjin.gifcreator.GifManager;

import android.graphics.Bitmap;

/**
 * ISL_KOREA
 * Created by jylee on 2018-01-04.
 */

public class GifImage {
    public final Bitmap bitmap;
    public final int delayMs;

    public GifImage(Bitmap bitmap, int delayMs) {
        this.bitmap = bitmap;
        this.delayMs = delayMs;
    }
}
