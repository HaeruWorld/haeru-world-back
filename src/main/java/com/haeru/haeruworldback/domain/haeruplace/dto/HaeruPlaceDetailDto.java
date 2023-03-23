package com.haeru.haeruworldback.domain.haeruplace.dto;

import com.haeru.haeruworldback.domain.Location;
import com.haeru.haeruworldback.domain.MarineCollections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class HaeruPlaceDetailDto {

    private String name;
    private String address;
    private String areaName;
    private Location location;
    private List<MarineCollections> marineCollections;
    private String startTime;
    private String endTime;

}