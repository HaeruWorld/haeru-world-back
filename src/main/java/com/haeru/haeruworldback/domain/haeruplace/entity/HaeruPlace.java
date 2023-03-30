package com.haeru.haeruworldback.domain.haeruplace.entity;

import com.haeru.haeruworldback.domain.Area;
import com.haeru.haeruworldback.domain.Location;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDetailDto;
import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDto;
import com.haeru.haeruworldback.domain.MarkerPosition;
import com.haeru.haeruworldback.domain.haeruplace.dto.MarineCollectionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HaeruPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long haeruPlaceId;

    @Column(nullable = false)
    @ApiModelProperty(value = "장소명", example = "도두어촌", required = true)
    private String name;

    @Column(nullable = false)
    @ApiModelProperty(value = "주소", example = "제주시 사수포구 용담 2동 경계", required = true)
    private String address;

    @Column(nullable = false)
    @ApiModelProperty(value = "출현 어종", example = "보말,게,소라,미역", required = true)
    private String marineCollections;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @ApiModelProperty(value = "지역", example = "JEJU", required = true)
    private Area area;

    @Column(nullable = false)
    @ApiModelProperty(value = "위도", example = "33.511333", required = true)
    private Double latitude;

    @Column(nullable = false)
    @ApiModelProperty(value = "경도", example = "126.483503", required = true)
    private Double longitude;

    @Column(nullable = false)
    private Double markerPositionX;

    @Column(nullable = false)
    private Double markerPositionY;

    public HaeruPlaceDto toHaeruPlaceDto() {
        return HaeruPlaceDto.builder()
                .id(haeruPlaceId)
                .name(name)
                .address(address)
                .markerPosition(new MarkerPosition(markerPositionX, markerPositionY))
                .marineCollections(Arrays.stream(marineCollections.split(","))
                        .collect(Collectors.toList()))
                .build();
    }

    public HaeruPlaceDetailDto haeruPlaceDetailDto(List<MarineCollectionDto> marineCollectionDtos, List<String> canHaeruTime) {
        return HaeruPlaceDetailDto.builder()
                .name(name)
                .address(address)
                .area(area.getAreaName())
                .location(new Location(latitude, longitude))
                .marineCollections(marineCollectionDtos)
                .startTime(canHaeruTime.get(0))
                .endTime(canHaeruTime.get(1))
                .build();
    }

    public boolean isContainMarineCollection(List<String> selectMarineCollections) {
        for (String name : selectMarineCollections) {
            if (marineCollections.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
