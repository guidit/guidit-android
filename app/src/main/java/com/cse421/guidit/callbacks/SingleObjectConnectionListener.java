package com.cse421.guidit.callbacks;

/**
 * Created by ho on 2017-06-05.
 */

public interface SingleObjectConnectionListener {
    void connectionSuccess(Object object);
    void connectionFailed();
}
