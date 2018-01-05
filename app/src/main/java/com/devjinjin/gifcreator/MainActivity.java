package com.devjinjin.gifcreator;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.devjinjin.gifcreator.Camera.CameraFragment;

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
}
