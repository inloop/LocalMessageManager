LocalMessageManager
================

Used for efficient and simple delivery of messages within you appliation. It's an alternative to [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html).

- Simple to use
- Can send any arbitrary object (doesn't need to be Parceable / Serializable / Bundle')
- Very efficient - objects are pooled and no new instances are created during delivery
- Uses standard Android Handler for message delivery
- Messages are always delivered to the main UI thread
- No Context needed
- Ligtweight - just one main class ([LocalMessageManager](https://github.com/inloop/LocalMessageManager/blob/master/library/src/main/java/eu/inloop/localmessagemanager/LocalMessageManager.java))


Comparision with LocalBroadcastManager
--------
- Less complex code (compare with [LocalBroadcastManager source](https://github.com/android/platform_frameworks_support/blob/master/v4/java/android/support/v4/content/LocalBroadcastManager.java))
- No need to use a Bundle to transfer the data and add unnecessary string keys
- Doesn't create any new instances, less impact on Garbage Collector 
- No need to define IntentFilters / Actions.
- Less error prone due to more straighforward API


How to implement
--------

You can **send a message** from anywhere. If you don't need any parameters, just use:
```java
LocalMessageManager.getInstance().send(R.id.msg_sample_event);
```
(The message ID is an integer. You can use simple constants or Android IDs or anything else).

If you want to send an arbitrary object:
```java
LocalMessageManager.getInstance().send(R.id.msg_sample_event, new MyCustomObject());
```

(you can also send a simple integer argument or a Bundle)

**Register receiver**. In case of an Activity this is usually onStart(), or onCreate(). 
You can also put this code into your base activity or fragment.
```java
LocalMessageManager.getInstance().addListener(this);
```
You can listen to all events or add only a listener to a specific message ID.

**Unregister receiver**. In case of an Activity this is usually in onStop() or onDestroy(). 
You can also put this code into your base activity or fragment.
```java
LocalMessageManager.getInstance().removeListener(this);
```

**Create a listener** or let your class implement `LocalMessageCallback`.
```java
@Override
public void handleMessage(@NonNull final LocalMessage msg) {
    switch (msg.getId()) {
        case R.id.msg_sample_event : {
            mTextView.setText("Received simple event");
        }
        break;
    }
}
```

Download
--------

Grab via Gradle:
```groovy
compile 'eu.inloop:localmessagemanager:0.1.0'
```
