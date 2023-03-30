package com.haeru.haeruworldback.domain.haeruplace.dto;

import com.haeru.haeruworldback.domain.MarkerPosition;
import lombok.*;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class HaeruPlaceDto implements Comparable<HaeruPlaceDto> {

    private final Long id;
    private final String name;
    private final String address;
    private final MarkerPosition markerPosition;
    private final List<String> marineCollections;

    @Override
    public int compareTo(HaeruPlaceDto o) {
        return this.markerPosition.compareTo(o.markerPosition);
    }
}
