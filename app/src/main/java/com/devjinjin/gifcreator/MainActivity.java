package com.devjinjin.gifcreator;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devjinjin.gifcreator.Camera.CameraFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_CAMERA = 100;
    private BaseFragment mCurrentFragment; //직접 대입 하지 말기


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mCurrentFragment == null) {
            mCurrentFragment = MainActivityFragment.getInstance(menuSelectedListener);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, mCurrentFragment, "mainFragment");
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final MainActivityFragment.IMainMenuSelectedListener menuSelectedListener = new MainActivityFragment.IMainMenuSelectedListener() {
        @Override
        public void onMainMenuSelected(int type) {
            switch (type){
                case 0:
                    confirmCameraPermissionAfterCameraOpen();

                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;

            }
        }
    };

    private void confirmCameraPermissionAfterCameraOpen(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    MY_PERMISSION_REQUEST_CAMERA);
        }else{
            openCamera();
        }
    }

    private void openCamera(){
        if(!(mCurrentFragment instanceof CameraFragment)){
            mCurrentFragment = CameraFragment.getInstance();

            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.container, mCurrentFragment, "cameraFragment");
            ft.addToBackStack("cameraFragment");
            ft.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 3) {
            return;
        }
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA:
                if (grantResults.length == 3) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED
                            || grantResults[1] == PackageManager.PERMISSION_DENIED
                            || grantResults[2] == PackageManager.PERMISSION_DENIED) {
                        GifToast.showToastNeedUserPermission(getApplicationContext());
                    }else{
                        openCamera();
                    }
                }
                break;
            default:
                break;

        }
    }

//    int index = 0;
//    private boolean useDither = true;
//@OnClick(R.id.button2)
//public void onClick(){
//    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            index = 0;
//            while (index < 30) {
//                Bitmap bitmap = getViewBitmap(drawingView);
//                if (bitmap != null) {
//                    saveImageFile(index, bitmap);
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                index++;
//
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.this, "저장끝", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    });
//    thread.start();
//}
//
//    private void saveImageFile(int index, Bitmap bitmap) {
//
//        FileOutputStream fos;
//        try {
//            fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + index + ".jpg");
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Bitmap getViewBitmap(View v) {
//
//
//        boolean willNotCache = v.willNotCacheDrawing();
//        v.setWillNotCacheDrawing(false);
//
//        // Reset the drawing cache background color to fully transparent
//        // for the duration of this operation
//        int color = v.getDrawingCacheBackgroundColor();
//        v.setDrawingCacheBackgroundColor(0);
//
//        if (color != 0) {
//            v.destroyDrawingCache();
//        }
//        v.buildDrawingCache();
//        Bitmap cacheBitmap = v.getDrawingCache();
//        if (cacheBitmap == null) {
//            Log.e("test", "failed getViewBitmap(" + v + ")", new RuntimeException());
//            return null;
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
//
//        // Restore the view
//        v.destroyDrawingCache();
//        v.setWillNotCacheDrawing(willNotCache);
//        v.setDrawingCacheBackgroundColor(color);
//
//        return bitmap;
//    }
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
////    public native String stringFromJNI();
//
//    private String setupSampleFile() {
//        AssetManager assetManager = getAssets();
//        String srcFile = "sample1.gif";
//        String destFile = getFilesDir().getAbsolutePath() + File.separator + srcFile;
//        copyFile(assetManager, srcFile, destFile);
//        return destFile;
//    }
//
//    private void copyFile(AssetManager assetManager, String srcFile, String destFile) {
//        try {
//            InputStream is = assetManager.open(srcFile);
//            FileOutputStream os = new FileOutputStream(destFile);
//
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = is.read(buffer)) != -1) {
//                os.write(buffer, 0, read);
//            }
//            is.close();
//            os.flush();
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onDecodeGIF(View v) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String destFile = setupSampleFile();
//
//                final GifDecoder gifDecoder = new GifDecoder();
//                final boolean isSucceeded = gifDecoder.load(destFile);
//                runOnUiThread(new Runnable() {
//                    int idx = 0;
//
//                    @Override
//                    public void run() {
//                        if (isSucceeded) {
//                            Bitmap bitmap = gifDecoder.frame(idx);
//                            imageView.setImageBitmap(bitmap);
//                            if (idx + 1 < gifDecoder.frameNum()) {
//                                imageView.postDelayed(this, gifDecoder.delay(idx));
//                            }
//                            ++idx;
//                        } else {
//                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }).start();
//    }
//
//    public void onDecodeGIFUsingIterator(View v) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String destFile = setupSampleFile();
//
//                final GifDecoder gifDecoder = new GifDecoder();
//                final GifImageIterator iterator = gifDecoder.loadUsingIterator(destFile);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (iterator.hasNext()) {
//                            GifImage next = iterator.next();
//                            if (null != next) {
//                                imageView.setImageBitmap(next.bitmap);
//                                imageView.postDelayed(this, next.delayMs);
//                            } else {
//                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            iterator.close();
//                        }
//                    }
//                });
//            }
//        }).start();
//    }
//
//    public void onEncodeGIF(View v) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    encodeGIF();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void encodeGIF() throws IOException {
//        String dstFile ="make" + System.currentTimeMillis() + ".gif";
//        final String filePath = Environment.getExternalStorageDirectory() + File.separator + dstFile;
//        int width = 400;
//        int height = 400;
//        int delayMs = 100;
//
//        String root = Environment.getExternalStorageDirectory().toString();
//        String path = root + "/" + 0 + ".jpg";
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, opts);
//
////        float newWidth;
////        float newHeight;
////
////        //넓이가 크다면
////        if(opts.outWidth > opts.outHeight){
////            if(opts.outWidth>width){
////                //가져온 이미지의 넓이가 지정 사이즈보다 크다면
////                float value = width / opts.outWidth;
////                newWidth = width;
////                newHeight = (int) (opts.outHeight * value);
////            }else{
////                float value = opts.outWidth / width;
////                newWidth = width;
////                newHeight = (int) (opts.outHeight * value);
////            }
////        } else {
////            if(opts.outHeight>height){
////                //가져온 이미지의 넓이가 지정 사이즈보다 크다면
////                float value = height / opts.outHeight;
////                newHeight = height;
////                newWidth = (int) (opts.outWidth * value);
////            }else{
////                float value = opts.outHeight / height;
////                newHeight = height;
////                newWidth = (int) (opts.outWidth * value);
////            }
////        }
//
//        int scaleFactor = Math.min(opts.outWidth / width, opts.outHeight / height);
//        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        opts.inJustDecodeBounds = false;
//        opts.inSampleSize = scaleFactor;
//        opts.inPurgeable = true;
//
//        Bitmap resize = BitmapFactory.decodeFile(path, opts);
//
//        GifEncoder gifEncoder = new GifEncoder();
//        gifEncoder.init(resize.getWidth(), resize.getHeight(), filePath, GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
//        gifEncoder.setDither(useDither);
//        resize.recycle();
////        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
////        Canvas canvas = new Canvas(bitmap);
////        Paint p = new Paint();
////        int[] colors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFFFFFFFF};
////        for (int color : colors) {
////            p.setColor(color);
////            canvas.drawRect(0, 0, width, height, p);
////            gifEncoder.encodeFrame(bitmap, delayMs);
////        }
//
//        Canvas canvas = new Canvas();
//        Paint p = new Paint();
//        for (int i = 0; i < 30; i++) {
//
//            String imagePath = root + "/" + i + ".jpg";
//            Bitmap  orgImage = BitmapFactory.decodeFile(imagePath, opts);
//
//            if (orgImage != null) {
//                canvas.drawBitmap(orgImage, orgImage.getWidth(), orgImage.getHeight(), p);
//                gifEncoder.encodeFrame(orgImage, delayMs);
//                orgImage.recycle();
//            }
//        }
//        gifEncoder.close();
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "done : " + filePath, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void onDisableDithering(View v) {
//        useDither = false;
//    }
}
