package eu.inloop.localmessagemanager;

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

	/**
	 * @see android.os.Handler#sendMessage(android.os.Message)
	 */
	public final boolean sendMessage(@NonNull final Message msg) {
		return mHandler.sendMessage(msg);
	}

    /**
     * @see android.os.Handler#sendMessage(android.os.Message)
     */
    public final boolean sendMessage(final int what, @NonNull final Object payload) {
        return mHandler.sendMessage(mHandler.obtainMessage(what, payload));
    }

    /**
	 * @see android.os.Handler#sendEmptyMessage(int)
	 */
	public final boolean sendEmptyMessage(final int what) {
		return mHandler.sendEmptyMessage(what);
	}

	/**
	 * @see android.os.Handler#sendEmptyMessageDelayed(int, long)
	 */
	public final boolean sendEmptyMessageDelayed(final int what, final long delayMillis) {
		return mHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	/**
	 * @see android.os.Handler#sendEmptyMessageAtTime(int, long)
	 */
	public final boolean sendEmptyMessageAtTime(final int what, final long uptimeMillis) {
		return mHandler.sendEmptyMessageAtTime(what, uptimeMillis);
	}

	/**
	 * @see android.os.Handler#sendMessageDelayed(android.os.Message, long)
	 */
	public final boolean sendMessageDelayed(@NonNull final Message msg, final long delayMillis) {
		return mHandler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * @see android.os.Handler#sendMessageAtTime(android.os.Message, long)
	 */
	public boolean sendMessageAtTime(@NonNull final Message msg, long uptimeMillis) {
		return mHandler.sendMessageAtTime(msg, uptimeMillis);
	}

	/**
	 * @see android.os.Handler#sendMessageAtFrontOfQueue(android.os.Message)
	 */
	public final boolean sendMessageAtFrontOfQueue(@NonNull final Message msg) {
		return mHandler.sendMessageAtFrontOfQueue(msg);
	}

	/**
	 * @see android.os.Handler#removeMessages(int)
	 */
	public final void removeMessages(final int what) {
		mHandler.removeMessages(what);
	}

	/**
	 * @see android.os.Handler#removeMessages(int, java.lang.Object)
	 */
	public final void removeMessages(final int what,final Object object) {
		mHandler.removeMessages(what, object);
	}

	/**
	 * Returns a new {@link android.os.Message Message} from the global message pool. More efficient than
	 * creating and allocating new instances. The retrieved message has its handler set to this instance (Message.target == this).
	 *  If you don't want that facility, just call Message.obtain() instead.
	 */
    @SuppressWarnings("WeakerAccess")
    @NonNull
	public Message obtainMessage() {
		return mHandler.obtainMessage();
	}

	/**
	 * Same as {@link #obtainMessage()}, except that it also sets the what member of the returned Message.
	 *
	 * @param what Value to assign to the returned Message.what field.
	 * @return A Message from the global message pool.
	 */
    @NonNull
	public Message obtainMessage(final int what) {
		return mHandler.obtainMessage(what);
	}

	/**
	 *
	 * Same as {@link #obtainMessage()}, except that it also sets the what and obj members
	 * of the returned Message.
	 *
	 * @param what Value to assign to the returned Message.what field.
	 * @param obj Value to assign to the returned Message.obj field.
	 * @return A Message from the global message pool.
	 */
    @NonNull
	public Message obtainMessage(final int what, final Object obj) {
		return mHandler.obtainMessage(what, obj);
	}

	/**
	 *
	 * Same as {@link #obtainMessage()}, except that it also sets the what, arg1 and arg2 members of the returned
	 * Message.
	 * @param what Value to assign to the returned Message.what field.
	 * @param arg1 Value to assign to the returned Message.arg1 field.
	 * @param arg2 Value to assign to the returned Message.arg2 field.
	 * @return A Message from the global message pool.
	 */
    @NonNull
	public Message obtainMessage(final int what, final int arg1, final int arg2) {
		return mHandler.obtainMessage(what, arg1, arg2);
	}

	/**
	 *
	 * Same as {@link #obtainMessage()}, except that it also sets the what, obj, arg1,and arg2 values on the
	 * returned Message.
	 * @param what Value to assign to the returned Message.what field.
	 * @param arg1 Value to assign to the returned Message.arg1 field.
	 * @param arg2 Value to assign to the returned Message.arg2 field.
	 * @param obj Value to assign to the returned Message.obj field.
	 * @return A Message from the global message pool.
	 */
    @NonNull
	public Message obtainMessage(final int what, final int arg1, final int arg2, final Object obj) {
		return mHandler.obtainMessage(what, arg1, arg2, obj);
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