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
	private final SparseArray<List<LocalMessageCallback>> mListenersSpecific = new SparseArray<>();

	@NonNull
	private final List<LocalMessageCallback> mListenersUniversal = new ArrayList<>();

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
	}

    public final boolean sendEmptyMessage(final int what) {
        return mHandler.sendEmptyMessage(what);
    }

    public final boolean sendMessage(final int what, @NonNull final Object payload) {
        return mHandler.sendMessage(mHandler.obtainMessage(what, payload));
    }

    public final boolean sendMessage(final int what, final int arg1) {
        return mHandler.sendMessage(mHandler.obtainMessage(what, arg1));
    }

    public final boolean sendMessage(final int what, final int arg1, final int arg2) {
        return mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
    }

    public final boolean sendMessage(final int what, @NonNull final Bundle bundle) {
        return mHandler.sendMessage(mHandler.obtainMessage(what, bundle));
    }

    @NonNull
	public Handler getHandler() {
		return mHandler;
	}

	/**
	 * Add listener for specific type of message by its {@link Message#what}.
	 *
 	 * <p>Warning: Listener is referenced via weak reference, do not use annonymous class!<p/>
	 *
	 * <p>Warning:
	 *
	 * @param what ID of message that will be only notified to listener
	 * @param listener listener
	 */
	public synchronized void addListener(int what, @NonNull final LocalMessageCallback listener) {
		List<LocalMessageCallback> whatListofListeners = mListenersSpecific.get(what);
		if (whatListofListeners == null) {
			whatListofListeners = new ArrayList<>();
			mListenersSpecific.put(what, whatListofListeners);
		}
		if (!whatListofListeners.contains(listener)) {
			whatListofListeners.add(listener);
		}
	}

	/**
	 * Add listener for all messages.
	 *
	 * <p>Warning: Listener is referenced via weak reference, do not use annonymous class!<p/>
	 *
	 * @param listener listener
	 */
	public synchronized void addListener(@NonNull final LocalMessageCallback listener) {
		if (!mListenersUniversal.contains(listener)) {
			mListenersUniversal.add(listener);
		}
	}

	/**
	 * Remove listener for specific type of message by its {@link Message#what}.
	 *
	 * @param what The id of the message to stop listening to.
	 * @param listener The listener to remove.
	 */
	public synchronized void removeListener(int what, @NonNull final LocalMessageCallback listener) {
		final List<LocalMessageCallback> whatListofListeners = mListenersSpecific.get(what);
		if (whatListofListeners == null) {
			return;
		}
		if (whatListofListeners.contains(listener)) {
			whatListofListeners.remove(listener);
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
	 * @param what The id of the message to stop listening to.
	 */
	public synchronized void removeListeners(final int what) {
		mListenersSpecific.delete(what);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see android.os.Handler.Callback#handleMessage(android.os.Message)
	 */
	@Override
	public boolean handleMessage(@NonNull final Message msg) {
		// proces listeners for specified type of message what
		synchronized (mListenersSpecific) {
			final List<LocalMessageCallback> whatListofListeners = mListenersSpecific.get(msg.what);
			if (whatListofListeners != null) {
				if (whatListofListeners.size() == 0) {
					mListenersSpecific.remove(msg.what);
				} else {
                    for (final LocalMessageCallback callback : whatListofListeners) {
                        callback.handleMessage(new LocalMessage(msg));
                    }
                }
			}
		}

		// process universal listeners
        synchronized (mListenersUniversal) {
            for (final LocalMessageCallback callback : mListenersUniversal) {
                callback.handleMessage(new LocalMessage(msg));
            }
        }
		return true;
	}

}