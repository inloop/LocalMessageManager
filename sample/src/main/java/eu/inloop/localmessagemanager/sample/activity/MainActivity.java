package eu.inloop.localmessagemanager.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import eu.inloop.localmessagemanager.LocalMessage;
import eu.inloop.localmessagemanager.LocalMessageCallback;
import eu.inloop.localmessagemanager.LocalMessageManager;
import eu.inloop.localmessagemanager.sample.MyCustomObject;
import eu.inloop.localmessagemanager.sample.R;


public class MainActivity extends AppCompatActivity implements LocalMessageCallback {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(android.R.id.text1);


        LocalMessageManager.getInstance().addListener(this);
        // You can also listen to a specific message
        // LocalMessageManager.getInstance().addListener(R.id.msg_sample_event, mSimpleMessageListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalMessageManager.getInstance().addListener(this);

        // You can also listen to a specific message
        // LocalMessageManager.getInstance().addListener(R.id.msg_sample_event, mSimpleMessageListener);
    }

    @Override
    public void handleMessage(@NonNull final LocalMessage msg) {
        switch (msg.getId()) {
            case R.id.msg_sample_event : {
                mTextView.setText("Received simple event (" + System.currentTimeMillis() / 100 + ")");
            }
            break;
            case R.id.msg_custom_payload_event : {
                mTextView.setText("Received custom object (" + ((MyCustomObject)msg.getObject()).getSomeValue() + ")");
            }
        }
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
