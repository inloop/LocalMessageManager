LocalMessageManager
================

Used for efficient and simple delivery of messages within you appliation. It's an alternative to [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html)

- Simple to use
- Can send any arbitrary object (doesn't need to be Parceable / Serializable / Bundle')
- Very efficient - objects are pooled and no new instances are created during delivery
- Uses standard Android Handler for message delivery
- Messages are always delivered to the main UI thread
- No Context needed
- Ligtweight - just one main class ([LocalMessageManager](https://github.com/inloop/LocalMessageManager/blob/master/library/src/main/java/eu/inloop/localmessagemanager/LocalMessageManager.java))

How to implement
--------


Download
--------

Grab via Gradle:
```groovy
compile 'eu.inloop:localmessagemanager:0.1.0'
```
