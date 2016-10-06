package eu.inloop.localmessagemanager;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LocalMessage {

    @Nullable
    private Message mMessage;

    LocalMessage(@Nullable final Message message) {
        this.mMessage = message;
    }

    void setMessage(@Nullable final Message message) {
        mMessage = message;
    }

    /**
     * User-defined message code so that the recipient can identify
     * what this message is about.
     */
    public int getId() {
        checkIfMainThread();
        return mMessage.what;
    }

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #getData()} if you only need to store a
     * few integer values.
     */
    public int getArg1() {
        checkIfMainThread();
        return mMessage.arg1;
    }

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #getData()} if you only need to store a
     * few integer values.
     */
    public int getArg2() {
        checkIfMainThread();
        return mMessage.arg1;
    }

    /**
     * An arbitrary object to send to the recipient.
     */
    @Nullable
    public Object getObject() {
        checkIfMainThread();
        return mMessage.obj;
    }

    /**
     * Obtains a Bundle of arbitrary data associated with this
     * event.
     */
    @NonNull
    public Bundle getData() {
        checkIfMainThread();
        return mMessage.getData();
    }

    private void checkIfMainThread() {
        if (BuildConfig.DEBUG && Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("You can't use LocalMessage instance from a non-UI thread. " +
                    "Extract the data from LocalMessage and don't hold a reference to it outside of handleMessage()");
        }
    }

    @Override
    public String toString() {
        checkIfMainThread();
        final StringBuilder b = new StringBuilder();
        b.append("{ when=");
        b.append(" id=");
        b.append(getId());

        if (getArg1() != 0) {
            b.append(" arg1=");
            b.append(getArg1());
        }

        if (getArg2() != 0) {
            b.append(" arg2=");
            b.append(getArg2());
        }

        if (getObject() != null) {
            b.append(" obj=");
            b.append(getObject());
        }

        if (getData() != null) {
            b.append(" data=");
            b.append(getData());
        }

        b.append(" }");
        return b.toString();
    }
}
