package com.demo.simulateTouch;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button click;
    private Button touch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        click = (Button) findViewById(R.id.button);
        touch = (Button) findViewById(R.id.button2);
        testClick();
        testTouch();
    }

    private void testClick() {
        simulateClick(click, 0, 0);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "模拟了点击", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void testTouch() {
        touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Toast.makeText(MainActivity.this, "模拟了触摸", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            simulateTouch(event.getX(), event.getY());
                        }
                    }, 500);
                }
                return false;
            }
        });
    }

    /**
     * 模拟点击
     *
     * @param view
     * @param x
     * @param y
     */
    private void simulateClick(View view, float x, float y) {
        final MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
        final MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    /**
     * 模拟发射触摸事件
     * 需开线程
     */
    private void simulateTouch(final float x, final float y) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation mInst = new Instrumentation();
                mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
                mInst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
            }
        }).start();
    }
}
