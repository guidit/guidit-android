package com.cse421.guidit.vo;

import java.util.ArrayList;

/**
 * Created by jeongyi on 2017. 5. 17..
 * test 용도 입니다 나중에 삭제할거임!!!!
 */

public class SightListVo {

    public static SightListVo sightListVo;

    public static SightListVo getInstance () {
        if (sightListVo == null) {
            sightListVo = new SightListVo();
        }
        return sightListVo;
    }

    public static void destroy () {
        sightListVo = null;
    }

    private ArrayList<SightVo> sightList;

    public ArrayList<SightVo> getSightList() {
        return sightList;
    }

    public void setSightList(ArrayList<SightVo> sightList) {
        this.sightList = sightList;
    }
}
