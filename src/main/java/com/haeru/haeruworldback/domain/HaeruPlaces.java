package com.haeru.haeruworldback.domain;

import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDto;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
public class HaeruPlaces {

    private final List<HaeruPlace> haeruPlaces;

    public HaeruPlaces(List<HaeruPlace> haeruPlaces) {
        this.haeruPlaces = haeruPlaces;
    }

    public HaeruPlaces getContainMarineCollection(List<String> selectMarineCollections) {
        return new HaeruPlaces(haeruPlaces.stream()
                .filter(place -> place.isContainMarineCollection(selectMarineCollections))
                .collect(Collectors.toList()));
    }

    public List<HaeruPlaceDto> transferDto() {
        return haeruPlaces.stream()
                .map(HaeruPlace::toHaeruPlaceDto)
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return haeruPlaces.isEmpty();
    }
}
