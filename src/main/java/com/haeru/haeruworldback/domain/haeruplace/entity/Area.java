package com.haeru.haeruworldback.domain.haeruplace.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Area {
    AEWOL("애월"),
    JEJU("제주"),
    SEONGSAN("성산"),
    SEOGWIPO("서귀포"),
    ;

    private final String areaName;

}
