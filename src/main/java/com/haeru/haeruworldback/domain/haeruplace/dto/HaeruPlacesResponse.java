package com.haeru.haeruworldback.domain.haeruplace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class HaeruPlacesResponse {

    List<HaeruPlaces> haeruPlaces;
    List<HaeruPlaces> recommandPlaces;
}
