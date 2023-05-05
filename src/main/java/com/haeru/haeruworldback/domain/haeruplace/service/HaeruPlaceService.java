package com.haeru.haeruworldback.domain.haeruplace.service;

import com.haeru.haeruworldback.domain.HaeruPlaces;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDto;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlacesResponseDto;
import com.haeru.haeruworldback.domain.Area;
import com.haeru.haeruworldback.domain.haeruplace.repository.HaeruPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HaeruPlaceService {

    private final HaeruPlaceRepository haeruPlaceRepository;

    public HaeruPlacesResponseDto getHaeruPlaces(String marineCollectionNames, String areaName) {
        HaeruPlaces selectAreaHaeruPlaces = new HaeruPlaces(haeruPlaceRepository.findByArea(Area.valueOf(areaName)));

        HaeruPlaces findHaeruPlaces = getContainMarinePlace(marineCollectionNames, selectAreaHaeruPlaces);

        if (findHaeruPlaces.isEmpty()) {
            List<HaeruPlaceDto> responseRecommendPlaces = selectAreaHaeruPlaces.transferDto();
            responseRecommendPlaces.sort(null);
            return new HaeruPlacesResponseDto(null, responseRecommendPlaces);
        }

        List<HaeruPlaceDto> responseHaeruPlaces = findHaeruPlaces.transferDto();
        responseHaeruPlaces.sort(null);
        return new HaeruPlacesResponseDto(responseHaeruPlaces, null);
    }

    private HaeruPlaces getContainMarinePlace(String marineCollectionNames, HaeruPlaces selectAreaHaeruPlaces) {
        List<String> selectMarineCollections = Arrays.stream(marineCollectionNames.split(","))
                .collect(Collectors.toList());

        return selectAreaHaeruPlaces.getContainMarineCollection(selectMarineCollections);
    }
}
