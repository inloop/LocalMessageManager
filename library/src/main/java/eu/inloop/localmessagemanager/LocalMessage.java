package eu.inloop.localmessagemanager;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LocalMessage {

    @NonNull
    private final Message mMessage;

    LocalMessage(@NonNull final Message message) {
        this.mMessage = message;
    }

    /**
     * User-defined message code so that the recipient can identify
     * what this message is about.
     */
    public int getId() {
        return mMessage.what;
    }

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #getData()} if you only need to store a
     * few integer values.
     */
    public int getArg1() {
        return mMessage.arg1;
    }

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #getData()} if you only need to store a
     * few integer values.
     */
    public int getArg2() {
        return mMessage.arg1;
    }

    /**
     * An arbitrary object to send to the recipient.
     */
    @Nullable
    public Object getObject() {
        return mMessage.obj;
    }

    /**
     * Obtains a Bundle of arbitrary data associated with this
     * event.
     */
    @NonNull
    public Bundle getData() {
        return mMessage.getData();
    }

    @Override
    public String toString() {
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
