package com.haeru.haeruworldback.domain;

import lombok.Getter;

@Getter
public enum Area {
    AEWOL("애월", "DT_0047"),
    JEJU("제주", "DT_0004"),
    SEONGSAN("성산", "DT_0022"),
    SEOGWIPO("서귀포", "DT_0010");

    private final String areaName;
    private final String areaCode;

    Area(String areaName, String areaCode) {
        this.areaName = areaName;
        this.areaCode = areaCode;
    }

}
