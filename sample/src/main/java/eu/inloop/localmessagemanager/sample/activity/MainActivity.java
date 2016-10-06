package eu.inloop.localmessagemanager.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import eu.inloop.localmessagemanager.LocalMessageManager;
import eu.inloop.localmessagemanager.sample.MyCustomObject;
import eu.inloop.localmessagemanager.sample.R;


public class MainActivity extends AppCompatActivity implements Handler.Callback {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(android.R.id.text1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalMessageManager.getInstance().addListener(this);

        // You can also listen to a specific message
        // LocalMessageManager.getInstance().addListener(R.id.msg_sample_event, mSimpleMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalMessageManager.getInstance().removeListener(this);

        // You can also listen to a specific message
        // LocalMessageManager.getInstance().removeListener(mSimpleMessageListener);
    }

    @Override
    public boolean handleMessage(final Message msg) {
        switch (msg.what) {
            case R.id.msg_sample_event : {
                mTextView.setText("Received simple event (" + System.currentTimeMillis() / 100 + ")");
            }
            break;
            case R.id.msg_custom_payload_event : {
                mTextView.setText("Received custom object (" + ((MyCustomObject)msg.obj).getSomeValue() + ")");
            }
        }
        return false;
    }

    /*
    // Listener for specific message ID

    private static Handler.Callback mSimpleMessageListener = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    };

    */

}
