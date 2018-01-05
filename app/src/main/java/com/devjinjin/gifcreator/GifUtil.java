package com.devjinjin.gifcreator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * ISL_KOREA
 * Created by jylee on 2018-01-05.
 */

public class GifUtil {
    //    카메라 존재 여부 확인
    public static boolean checkCameraHardware(@NonNull Context pContext) {
        return pContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

}
