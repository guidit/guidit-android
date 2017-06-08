package com.cse421.guidit.callbacks;

/**
 * Created by ho on 2017-06-08.
 */

public interface ImageUploadListener {
    void onSuccess (String url);
    void onFailed ();
}
