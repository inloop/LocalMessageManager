package eu.inloop.localmessagemanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
@AnyThread
public class LocalMessageManager implements Callback {

    @Nullable
    private static volatile LocalMessageManager sInstance = null;

    @NonNull
    private final Handler mHandler;

    @NonNull
    private final SparseArray<List<LocalMessageCallback>> mListenersSpecific;

    @NonNull
    private final List<LocalMessageCallback> mListenersUniversal;

    @NonNull
    private LocalMessage mMessage;

    @NonNull
    public static LocalMessageManager getInstance() {
        if (sInstance == null) {
            synchronized (LocalMessageManager.class) {
                if (sInstance == null) {
                    sInstance = new LocalMessageManager();
                }
            }
        }
        //noinspection ConstantConditions
        return sInstance;
    }

    private LocalMessageManager() {
        mHandler = new Handler(Looper.getMainLooper(), this);
        mMessage = new LocalMessage(null);
        mListenersUniversal = new ArrayList<>();
        mListenersSpecific = new SparseArray<>();
    }

    /**
     * Sends an empty Message containing only the message ID.
     * @param id - message ID
     */
    public final void sendEmptyMessage(final int id) {
        mHandler.sendEmptyMessage(id);
    }

    /**
     * Sends a Message containing the ID and an arbitrary Object.
     * @param id - message ID
     * @param payload - arbitrary object
     */
    public final void sendMessage(final int id, @NonNull final Object payload) {
        mHandler.sendMessage(mHandler.obtainMessage(id, payload));
    }

    /**
     * Sends a Message containing the ID and one integer argument.
     * More effective than sending an arbitrary object.
     * @param id - message ID
     * @param arg1 - integer argument
     */
    public final void sendMessage(final int id, final int arg1) {
        mHandler.sendMessage(mHandler.obtainMessage(id, arg1));
    }

    /**
     * Sends a Message containing the ID and two integer arguments.
     * More effective than sending an arbitrary object.
     * @param id - message ID
     * @param arg1 - integer argument
     * @param arg2 - integer argument
     */
    public final void sendMessage(final int id, final int arg1, final int arg2) {
        mHandler.sendMessage(mHandler.obtainMessage(id, arg1, arg2));
    }

    /**
     * Sends a Message containing the ID and a Bundle object.
     * More effective than sending an arbitrary object.
     * @param id - message ID
     * @param bundle - bundle
     */
    public final void sendMessage(final int id, @NonNull final Bundle bundle) {
        mHandler.sendMessage(mHandler.obtainMessage(id, bundle));
    }

    /**
     * Add listener for specific type of message by its ID.
     * Don't forget to call {@link #removeListener(LocalMessageCallback)} or
     * {@link #removeListeners(int)}
     *
     * @param id     ID of message that will be only notified to listener
     * @param listener listener
     */
    public synchronized void addListener(int id, @NonNull final LocalMessageCallback listener) {
        List<LocalMessageCallback> whatListofListeners = mListenersSpecific.get(id);
        if (whatListofListeners == null) {
            whatListofListeners = new ArrayList<>();
            mListenersSpecific.put(id, whatListofListeners);
        }
        if (!whatListofListeners.contains(listener)) {
            whatListofListeners.add(listener);
        }
    }

    /**
     * Add listener for all messages.
     *
     * @param listener listener
     */
    public synchronized void addListener(@NonNull final LocalMessageCallback listener) {
        if (!mListenersUniversal.contains(listener)) {
            mListenersUniversal.add(listener);
        }
    }

    /**
     * Remove listener for all messages.
     *
     * @param listener The listener to remove.
     */
    public synchronized void removeListener(@NonNull final LocalMessageCallback listener) {
        if (mListenersUniversal.contains(listener)) {
            mListenersUniversal.remove(listener);
        }
    }

    /**
     * Remove all listeners for desired message ID.
     *
     * @param id The id of the message to stop listening to.
     */
    public synchronized void removeListeners(final int id) {
        mListenersSpecific.delete(id);
    }

    /*
     *
     * @see android.os.Handler.Callback#handleMessage(android.os.Message)
     */
    @Override
    public boolean handleMessage(@NonNull final Message msg) {
        mMessage.setMessage(msg);
        // proces listeners for specified type of message what
        synchronized (mListenersSpecific) {
            final List<LocalMessageCallback> whatListofListeners = mListenersSpecific.get(msg.what);
            if (whatListofListeners != null) {
                if (whatListofListeners.size() == 0) {
                    mListenersSpecific.remove(msg.what);
                } else {
                    for (final LocalMessageCallback callback : whatListofListeners) {
                        callback.handleMessage(mMessage);
                    }
                }
            }
        }

        // process universal listeners
        synchronized (mListenersUniversal) {
            for (final LocalMessageCallback callback : mListenersUniversal) {
                callback.handleMessage(mMessage);
            }
        }
        return true;
    }

}