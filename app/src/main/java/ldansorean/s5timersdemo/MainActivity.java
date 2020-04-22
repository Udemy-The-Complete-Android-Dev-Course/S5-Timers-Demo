package ldansorean.s5timersdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    public static final int PACE = 1000;
    private TextView handlerMsg, timerMsg, countDownTimerMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerMsg = findViewById(R.id.handler);
        timerMsg = findViewById(R.id.timer);
        countDownTimerMsg = findViewById(R.id.countDownTimer);

        startHandler();
        startTimer();
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        final AtomicInteger counter = new AtomicInteger(0);

        new CountDownTimer(10000, PACE) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimerMsg.setText(String.format("%d seconds have passed.", counter.getAndIncrement()));
            }

            @Override
            public void onFinish() {
                countDownTimerMsg.setText("Countdown finished!");
            }
        }.start();
    }

    private void startTimer() {
        final AtomicInteger counter = new AtomicInteger(0);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //timer tasks don't run in the main thread but in a separate one. That means we can't directly update a view in the TimerTask and have to use a workaround.
                //=> Only the original thread that created a view hierarchy can touch its views.
                setTextValue(timerMsg, String.format("%d seconds have passed.", counter.getAndIncrement()));
            }
        }, 0, PACE);
    }

    private void setTextValue(final TextView textView, final String newMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(newMsg);
            }
        });
    }

    private void startHandler() {
        final AtomicInteger counter = new AtomicInteger(0);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                handlerMsg.setText(String.format("%d seconds have passed.", counter.getAndIncrement()));
                //the runnable keeps telling the handler to trigger it again
                handler.postDelayed(this, PACE);
            }
        });
    }
}
