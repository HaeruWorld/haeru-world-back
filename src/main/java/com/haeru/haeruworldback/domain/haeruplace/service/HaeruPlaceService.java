package com.haeru.haeruworldback.domain.haeruplace.service;

import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaces;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlacesResponse;
import com.haeru.haeruworldback.domain.haeruplace.entity.Area;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import com.haeru.haeruworldback.domain.haeruplace.repository.HaeruPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HaeruPlaceService {

    private final HaeruPlaceRepository haeruPlaceRepository;

    public HaeruPlacesResponse getHaeruPlacesList(String marineCollections, String area) {
        StringTokenizer st = new StringTokenizer(marineCollections, ",");
        List<String> marineCollectionsList = new ArrayList<>();
        while(st.hasMoreTokens()) {
            marineCollectionsList.add(st.nextToken());
        }

        List<HaeruPlace> resultList = new ArrayList<>();
        List<HaeruPlace> findHaeruPlacesByArea = haeruPlaceRepository.findByArea(Area.valueOf(area));
        for(HaeruPlace haeruPlace : findHaeruPlacesByArea) {
            String marineCollectionsString = haeruPlace.getMarineCollections();
            st = new StringTokenizer(marineCollectionsString,",");

            Set<String> nameSet = new HashSet<>();
            while(st.hasMoreTokens()) {
                nameSet.add(st.nextToken());
            }

            boolean flag = true;
            for(String marineCollectionName : marineCollectionsList) {
                if(!nameSet.contains(marineCollectionName)) {
                    flag = false;
                    break;
                }
            }

            if(flag) resultList.add(haeruPlace);
        }

        List<HaeruPlaces> haeruPlaces = new ArrayList<>();
        List<HaeruPlaces> recommandPlaces = new ArrayList<>();
        HaeruPlacesResponse haeruPlacesResponse = new HaeruPlacesResponse();

        if(resultList.size() > 0) {
            for(HaeruPlace haeruPlace : resultList) {
                haeruPlaces.add(haeruPlace.toHaeruPlaces());
            }
            sortListByMarker(haeruPlaces);

            recommandPlaces = null;

        } else {
            for(HaeruPlace haeruPlace : findHaeruPlacesByArea) {
                recommandPlaces.add(haeruPlace.toHaeruPlaces());
            }
            sortListByMarker(recommandPlaces);

            haeruPlaces = null;
        }

        haeruPlacesResponse.setHaeruPlaces(haeruPlaces);
        haeruPlacesResponse.setRecommandPlaces(recommandPlaces);

        return haeruPlacesResponse;
    }

    public List<HaeruPlaces> sortListByMarker(List<HaeruPlaces> list) {
        list.sort(new Comparator<HaeruPlaces>() {
            @Override
            public int compare(HaeruPlaces o1, HaeruPlaces o2) {
                return o1.getMarkerPosition().getX().compareTo(o2.getMarkerPosition().getX());
            }
        });

        return list;
    }
}
