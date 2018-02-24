package com.austin.android.alexa;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class MainFragment extends Fragment implements VoiceView.OnRecordListener{

    private static final String TAG = MainFragment.class.getName();

    private VoiceView mVoiceView;
    private RecordManager mRecorder;
    private Handler mHandler;
    private boolean mIsRecording = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVoiceView = (VoiceView) view.findViewById(R.id.voice_view);
        mVoiceView.setOnRecordListener(this);
        mRecorder = new RecordManager(new File(Environment.getExternalStorageDirectory(), "audio.amr"));
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onRecordStart() {
        Log.d(TAG, "onRecordStart");
        mRecorder.startRecord();
        mIsRecording = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mIsRecording) {
                    float radius = (float) Math.log10(Math.max(1, mRecorder.getMaxAmplitude() - 500)) * ScreenUtils.dp2px(getActivity(), 20);
                    mVoiceView.animateRadius(radius);
                    mHandler.postDelayed(this, 50);
                }
            }
        });
    }

    @Override
    public void onRecordFinish() {
        Log.d(TAG, "onRecordFinish");
        mIsRecording = false;
        mRecorder.stopRecord();
    }

    @Override
    public void onDestroy() {
        mIsRecording = false;
        mRecorder.stopRecord();
        super.onDestroy();
    }
}
