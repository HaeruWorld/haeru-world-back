package com.haeru.haeruworldback.domain.haeruplace.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class HaeruPlaces {

    private Long id;
    private String name;
    private String address;
    private MarkerPosition markerPosition;
    private List<String> marineCollections;

}
