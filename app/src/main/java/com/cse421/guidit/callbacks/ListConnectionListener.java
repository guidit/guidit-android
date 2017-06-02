package com.cse421.guidit.callbacks;

import com.cse421.guidit.vo.FeedVo;

import java.util.ArrayList;

/**
 * Created by ho on 2017-06-02.
 */

public interface ListConnectionListener <T> {
    void setList(ArrayList<T> list);
}
