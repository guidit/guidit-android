package com.cse421.guidit.callbacks;

import com.cse421.guidit.vo.SightVo;

import java.util.ArrayList;

/**
 * Created by ho on 2017-05-21.
 */

public interface FavoriteConnectionEventListener {
    void onSuccess (ArrayList<SightVo> items);
    void onFailed ();
}
