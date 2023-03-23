package com.haeru.haeruworldback.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MarkerPosition implements Comparable<MarkerPosition>{

    private final Double x;
    private final Double y;

    @Override
    public int compareTo(MarkerPosition o) {
        return this.x.compareTo(o.x);
    }
}
