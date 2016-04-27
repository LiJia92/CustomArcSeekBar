package com.android.lovesixgod.customarcseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.seek_bar_progress);
        CustomArcSeekBar seekBar = (CustomArcSeekBar) findViewById(R.id.seek_bar);
        seekBar.setListener(new CustomArcSeekBar.OnProgressChangedListener() {
            @Override
            public void OnProgressChanged(int level) {
                textView.setText(String.valueOf(level));
            }
        });
    }
}
