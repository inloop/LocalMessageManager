package eu.inloop.localmessagemanager;

import android.support.annotation.NonNull;

@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface LocalMessageCallback {
    public void handleMessage(@NonNull LocalMessage localMessage);
}
