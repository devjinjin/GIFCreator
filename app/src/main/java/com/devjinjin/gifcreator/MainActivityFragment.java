package com.devjinjin.gifcreator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {

    @BindView(R.id.iBCamera)
    ImageButton cameraImageButton;

    @BindView(R.id.imageButton2)
    ImageButton videoImageButton;

    @BindView(R.id.imageButton3)
    ImageButton drawingImageButton;

    @BindView(R.id.imageButton4)
    ImageButton editImageButton;

    public interface IMainMenuSelectedListener {
        void onMainMenuSelected(int type);
    }


    private static MainActivityFragment mInstance;
    private IMainMenuSelectedListener mListener;

    public static MainActivityFragment getInstance(IMainMenuSelectedListener pListener) {
        if (mInstance == null) {
            mInstance = new MainActivityFragment();
        }
        mInstance.setOnMainMenuSelectedListener(pListener);
        return mInstance;
    }

    private void setOnMainMenuSelectedListener(IMainMenuSelectedListener pListener) {
        mListener = pListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iBCamera, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.iBCamera:
                if (GifUtil.checkCameraHardware(getActivity())) {
                    mListener.onMainMenuSelected(0);
                } else {
                    GifToast.showToastHasNotCamera(getActivity());
                }
                break;
            case R.id.imageButton2:
                Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();

                break;
            case R.id.imageButton3:
                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();

                break;
            case R.id.imageButton4:
                Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();

                break;

        }
    }

}
