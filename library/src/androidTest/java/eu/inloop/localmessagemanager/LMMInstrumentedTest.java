package eu.inloop.localmessagemanager;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LMMInstrumentedTest {

    private static final String TAG = "LMMInstrumentedTest";

    private boolean mReceivedAllMessages;
    private long mStartSendTime;

    @Test
    public void singleMessageTest() {
        final int messageIdValid = 1;
        final int messageIdWrong = 2;
        
        mReceivedAllMessages = false;
        LocalMessageManager.getInstance().addListener(new LocalMessageCallback() {
            @Override
            public void handleMessage(@NonNull LocalMessage localMessage) {
                mReceivedAllMessages = true;
                logTime(mStartSendTime);

                //Check If incoming message is correct ID
                assertEquals("Got message after removing listener!", messageIdValid, localMessage.getId());

                //Remove listener
                LocalMessageManager.getInstance().removeListener(this);

                //Send message after removed listener
                LocalMessageManager.getInstance().send(messageIdWrong);
            }
        });

        mStartSendTime = System.nanoTime();
        LocalMessageManager.getInstance().send(messageIdValid);

        SystemClock.sleep(50);

        assertEquals("Did not receive message!", !mReceivedAllMessages, false);
    }

    @Test
    public void multipleMessageTest() throws Exception {
        final int messageId1 = 1;
        final int message1Arg = -1;
        final int messageId2 = 2;
        final int message2Arg = -2;
        final int messageId3 = 3;
        final int message3Arg = -3;

        mReceivedAllMessages = false;
        LocalMessageManager.getInstance().addListener(new LocalMessageCallback() {
            boolean recId1, recId2, recId3;

            @Override
            public void handleMessage(@NonNull LocalMessage localMessage) {
                int localMessageId = localMessage.getId();
                int localMessageArg1 = localMessage.getArg1();

                switch (localMessageId) {
                    case messageId1:
                        recId1 = localMessageArg1 == message1Arg;
                        break;
                    case messageId2:
                        recId2 = localMessageArg1 == message2Arg;
                        break;
                    case messageId3:
                        recId3 = localMessageArg1 == message3Arg;
                        break;
                }

                if (recId1 && recId2 && recId3) {
                    mReceivedAllMessages = true;
                    logTime(mStartSendTime);
                    LocalMessageManager.getInstance().removeListener(this);
                }
            }
        });

        mStartSendTime = System.nanoTime();

        LocalMessageManager.getInstance().send(messageId1, message1Arg);
        LocalMessageManager.getInstance().send(messageId2, message2Arg);
        LocalMessageManager.getInstance().send(messageId3, message3Arg);

        SystemClock.sleep(50);

        assertEquals("Did not receive all messages!", !mReceivedAllMessages, false);
    }

    @Test
    public void concurrentModificationTest() {
        mReceivedAllMessages = false;

        LocalMessageManager.getInstance().addListener(new LocalMessageCallback() {
            @Override
            public void handleMessage(@NonNull LocalMessage localMessage) {
                mReceivedAllMessages = true;

                //Remove listener
                LocalMessageManager.getInstance().removeListener(this);

            }
        });

        LocalMessageCallback localMessageCallback = new LocalMessageCallback() {
            @Override
            public void handleMessage(@NonNull LocalMessage localMessage) {
                // this one should not be delivered
            }
        };

        LocalMessageManager.getInstance().addListener(localMessageCallback);

        LocalMessageManager.getInstance().send(1);

        SystemClock.sleep(50);
    }

    private void logTime(long nanoseconds) {
        long nano = System.nanoTime() - nanoseconds;
        long ms = TimeUnit.NANOSECONDS.toMillis(nano);
        Log.d(TAG, "Received message(s) after " + nano + " nanoseconds (" + ms + " ms)");
    }
}
