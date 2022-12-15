package com.haeru.haeruworldback.domain.haeruplace.dto;

import com.haeru.haeruworldback.domain.haeruplace.entity.Area;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class HaeruPlaceDetail {

    private String name;
    private String address;
    private String area;
    private Location location;
    private List<MarineCollections> marineCollections;
    private String startTime;
    private String endTime;

}
