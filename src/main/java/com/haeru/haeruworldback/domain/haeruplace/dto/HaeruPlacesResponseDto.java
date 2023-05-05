package com.haeru.haeruworldback.domain.haeruplace.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class HaeruPlacesResponseDto {

    private final List<HaeruPlaceDto> haeruPlaces;
    private final List<HaeruPlaceDto> recommendPlaces;

}
