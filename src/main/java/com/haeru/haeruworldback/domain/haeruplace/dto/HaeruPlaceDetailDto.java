package com.haeru.haeruworldback.domain.haeruplace.dto;

import com.haeru.haeruworldback.domain.Location;
import lombok.*;

import java.util.List;

@Getter
@Builder
public class HaeruPlaceDetailDto {

    private String name;
    private String address;
    private String area;
    private Location location;
    private List<MarineCollectionDto> marineCollections;
    private String startTime;
    private String endTime;

}
