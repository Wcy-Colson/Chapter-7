package com.bytedance.videoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static VideoView videoView;
    private Button button;
    private SeekBar seekBar;
    private TextView textView;
    private static Bundle bundle;
    private static int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(bundle!=null){
            current = bundle.getInt("value");
        }
        setContentView(R.layout.activity_main);
//        ImageView imageView = findViewById(R.id.imageView);
//        String url = "https://s3.pstatp.com/toutiao/static/img/logo.271e845.png";
//        Glide.with(this).load(url).into(imageView);

        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        videoView.seekTo(current);

        button = findViewById(R.id.start_and_pause);
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);

        handler.postDelayed(runnable,1000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button.getText().equals("start")){
                    button.setText("pause");
                    videoView.start();
                }
                else {
                    button.setText("start");
                    videoView.pause();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int duration = videoView.getDuration();
                int current = seekBar.getProgress()*duration/100;
                videoView.seekTo(current);
            }
        });
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(runnable,1000);
        }
    };

    private void refresh(){
        float current = videoView.getCurrentPosition();
        float duration = videoView.getDuration();
        int value = (int)(100*current/duration);
        seekBar.setProgress(value);
        current /= 1000;
        duration /= 1000;
        System.out.println(current);
        int hour = (int)(current / 3600);
        int minute = (int)((current%3600)/60);
        int second = (int)(current%60);
        int total_h = (int)(duration / 3600);
        int total_m = (int)((duration%3600)/60);
        int total_s = (int)(duration%60);
        textView.setText(Integer.toString(hour)+":"+Integer.toString(minute)+":"+Integer.toString(second)
        +"/"+Integer.toString(total_h)+":"+Integer.toString(total_m)+":"+Integer.toString(total_s)
        );
    }

    private String getVideoPath(int resID){
        return "android.resource://" + this.getPackageName() + "/" + resID;
    }

    @Override
    protected void onPause() {
        super.onPause();

        current = videoView.getCurrentPosition();
        if(bundle != null ){
            bundle.clear();
            bundle = null;
        }
            bundle = new Bundle();
        bundle.putInt("value",current);
    }
}
