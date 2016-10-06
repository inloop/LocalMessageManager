package eu.inloop.localmessagemanager;

import android.support.annotation.NonNull;

public interface LocalMessageCallback {
    public void handleMessage(@NonNull LocalMessage localMessage);
}
