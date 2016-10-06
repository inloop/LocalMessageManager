package eu.inloop.localmessagemanager;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LocalMessage {

    @NonNull
    private final Message mMessage;

    LocalMessage(@NonNull final Message message) {
        this.mMessage = message;
    }

    public int getId() {
        return mMessage.what;
    }

    public int getArg1() {
        return mMessage.arg1;
    }

    public int getArg2() {
        return mMessage.arg1;
    }

    @Nullable
    public Object getObject() {
        return mMessage.obj;
    }

    @Nullable
    public Bundle getData() {
        return mMessage.getData();
    }
}
