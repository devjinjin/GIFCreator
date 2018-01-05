package com.devjinjin.gifcreator;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * ISL_KOREA
 * Created by jylee on 2018-01-05.
 */

public class GifToast {
    private static Toast sToast;

    public static void showToastHasNotCamera(Context pContext) {
        if (pContext != null) {
            if (sToast != null) {
                sToast.cancel();
            }
            sToast = Toast.makeText(pContext, "카메라가 없어요", Toast.LENGTH_LONG);
            sToast.show();
        }
    }

    public static void showToastNeedUserPermission(Context pContext) {
        if (pContext != null) {
            if (sToast != null) {
                sToast.cancel();
            }
            sToast = Toast.makeText(pContext, "권한이 필요해요", Toast.LENGTH_LONG);
            sToast.show();
        }
    }

    public static void showToastCameraOpenError(Context pContext) {
        if (pContext != null) {
            if (sToast != null) {
                sToast.cancel();
            }
            sToast = Toast.makeText(pContext, "카메라 열기에러", Toast.LENGTH_LONG);
            sToast.show();
        }
    }
}
