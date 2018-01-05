#include <stdint.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include "GifEncoder.h"
#include <string.h>
#include <wchar.h>
#include <android/bitmap.h>
#include "GifDecoder.h"
#include "BitmapIterator.h"
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifEncoder_nativeInit(JNIEnv *env, jobject instance,
                                                                 jint width, jint height,
                                                                 jstring path_, jint encodingType,
                                                                 jint threadCount) {
    GifEncoder* gifEncoder = new GifEncoder(static_cast<EncodingType>(encodingType));
    gifEncoder->setThreadCount(threadCount);
    const char* pathChars = env->GetStringUTFChars(path_, 0);
    bool result = gifEncoder->init(width, height, pathChars);
    env->ReleaseStringUTFChars(path_, pathChars);
    if (result) {
        return (jlong) gifEncoder;
    } else {
        delete gifEncoder;
        return 0;
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifEncoder_nativeClose(JNIEnv *env, jobject instance,
                                                                  jlong handle) {

    GifEncoder* gifEncoder = (GifEncoder*)handle;
    gifEncoder->release();
    delete gifEncoder;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifEncoder_nativeSetDither(JNIEnv *env, jobject instance,
                                                                      jlong handle,
                                                                      jboolean useDither) {

    GifEncoder* gifEncoder = (GifEncoder*)handle;
    gifEncoder->setDither(useDither);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifEncoder_nativeSetThreadCount(JNIEnv *env,
                                                                           jobject instance,
                                                                           jlong handle,
                                                                           jint threadCount) {
    GifEncoder* gifEncoder = (GifEncoder*)handle;
    gifEncoder->setThreadCount(threadCount);

}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifEncoder_nativeEncodeFrame(JNIEnv *env,
                                                                        jobject instance,
                                                                        jlong handle,
                                                                        jobject bitmap,
                                                                        jint delayMs) {

    GifEncoder* gifEncoder = (GifEncoder*)handle;
    void* bitmapPixels;
    if (AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels) < 0) {
        return false;
    }
    uint16_t imgWidth = gifEncoder->getWidth();
    uint16_t imgHeight = gifEncoder->getHeight();
    uint32_t* src = (uint32_t*) bitmapPixels;
    uint32_t* tempPixels = new unsigned int[imgWidth * imgHeight];
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(tempPixels, bitmapPixels, pixelsCount);
    AndroidBitmap_unlockPixels(env, bitmap);
    gifEncoder->encodeFrame(tempPixels, delayMs);
    delete[] tempPixels;
    return true;

}

//DECODER
JNIEXPORT jlong JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeInit(JNIEnv *env, jobject instance) {

    return (jlong)new GifDecoder();

}

JNIEXPORT void JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeClose(JNIEnv *env, jobject instance,
                                                                  jlong handle) {

    delete (GifDecoder*)handle;

}

JNIEXPORT jboolean JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeLoad(JNIEnv *env, jobject instance,
                                                                 jlong handle, jstring fileName_) {
    const char* fileNameChars = env->GetStringUTFChars(fileName_, 0);
    bool result = ((GifDecoder*)handle)->load(fileNameChars);
    env->ReleaseStringUTFChars(fileName_, fileNameChars);
    return result;
}

JNIEXPORT jlong JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeLoadUsingIterator(JNIEnv *env,
                                                                              jobject instance,
                                                                              jlong handle,
                                                                              jstring fileName_) {
    const char* fileNameChars = env->GetStringUTFChars(fileName_, 0);
    BitmapIterator* result = ((GifDecoder*)handle)->loadUsingIterator(fileNameChars);
    env->ReleaseStringUTFChars(fileName_, fileNameChars);

    return (jlong)result;
}

JNIEXPORT jint JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeGetFrameCount(JNIEnv *env,
                                                                          jobject instance,
                                                                          jlong handle) {

    return ((GifDecoder*)handle)->getFrameCount();

}

JNIEXPORT jobject JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeGetFrame(JNIEnv *env, jobject instance,
                                                                     jlong handle, jint n) {

    GifDecoder* decoder = (GifDecoder*)handle;
    int imgWidth = decoder->getWidth();
    int imgHeight = decoder->getHeight();

    // Creating Bitmap Config Class
    jclass bmpCfgCls = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID bmpClsValueOfMid = env->GetStaticMethodID(bmpCfgCls, "valueOf", "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject jBmpCfg = env->CallStaticObjectMethod(bmpCfgCls, bmpClsValueOfMid, env->NewStringUTF("ARGB_8888"));

    // Creating a Bitmap Class
    jclass bmpCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapMid = env->GetStaticMethodID(bmpCls, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jobject jBmpObj = env->CallStaticObjectMethod(bmpCls, createBitmapMid, imgWidth, imgHeight, jBmpCfg);

    void* bitmapPixels;
    if (AndroidBitmap_lockPixels(env, jBmpObj, &bitmapPixels) < 0) {
        return 0;
    }
    uint32_t* src = (uint32_t*) bitmapPixels;
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(bitmapPixels, decoder->getFrame(n), pixelsCount);
    AndroidBitmap_unlockPixels(env, jBmpObj);

    return jBmpObj;

}

JNIEXPORT jint JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeGetDelay(JNIEnv *env, jobject instance,
                                                                     jlong handle, jint n) {

    return ((GifDecoder*)handle)->getDelay(n);

}

JNIEXPORT jint JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeGetWidth(JNIEnv *env, jobject instance,
                                                                     jlong handle) {

    return ((GifDecoder*)handle)->getWidth();

}

JNIEXPORT jint JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeGetHeight(JNIEnv *env, jobject instance,
                                                                      jlong handle) {

    return ((GifDecoder*)handle)->getHeight();

}

JNIEXPORT jboolean JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeBitmapIteratorHasNext(JNIEnv *env,
                                                                                  jobject instance,
                                                                                  jlong handle) {

    return ((BitmapIterator*)handle)->hasNext();

}

JNIEXPORT jobject JNICALL
Java_com_devjinjin_gifcreator_GifManager_GifDecoder_nativeBitmapIteratornext(JNIEnv *env,
                                                                               jobject instance,
                                                                               jlong decoderHandle,
                                                                               jlong iteratorHandle) {
    const uint32_t* frame = NULL;
    uint32_t delayMs = 0;
    bool result = ((BitmapIterator*)iteratorHandle)->next(&frame, &delayMs);
    if (!result) {
        return NULL;
    }

    GifDecoder* decoder = (GifDecoder*)decoderHandle;
    int imgWidth = decoder->getWidth();
    int imgHeight = decoder->getHeight();

    // Creating Bitmap Config Class
    jclass bmpCfgCls = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID bmpClsValueOfMid = env->GetStaticMethodID(bmpCfgCls, "valueOf", "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject jBmpCfg = env->CallStaticObjectMethod(bmpCfgCls, bmpClsValueOfMid, env->NewStringUTF("ARGB_8888"));

    // Creating a Bitmap Class
    jclass bmpCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapMid = env->GetStaticMethodID(bmpCls, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jobject jBmpObj = env->CallStaticObjectMethod(bmpCls, createBitmapMid, imgWidth, imgHeight, jBmpCfg);

    void* bitmapPixels;
    if (AndroidBitmap_lockPixels(env, jBmpObj, &bitmapPixels) < 0) {
        return NULL;
    }
    uint32_t* src = (uint32_t*) bitmapPixels;
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(bitmapPixels, frame, pixelsCount);
    AndroidBitmap_unlockPixels(env, jBmpObj);

    // Creating a GifImageClass
    jclass gifImageCls = env->FindClass("com/waynejo/androidndkgif/GifImage");
    jmethodID gifImageClsInit = env->GetMethodID(gifImageCls, "<init>", "(Landroid/graphics/Bitmap;I)V");
    return env->NewObject(gifImageCls, gifImageClsInit, jBmpObj, delayMs);

}

#ifdef __cplusplus
}
#endif
