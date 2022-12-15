package com.haeru.haeruworldback.domain.haeruplace.entity;

import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaces;
import com.haeru.haeruworldback.domain.haeruplace.dto.MarkerPosition;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HaeruPlace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long haeruPlaceId;

    @Column(nullable = false)
    @ApiModelProperty(value="장소명", example = "도두어촌", required = true)
    private String name;

    @Column(nullable = false)
    @ApiModelProperty(value="주소", example = "제주시 사수포구 용담 2동 경계", required = true)
    private String address;

    @Column(nullable = false)
    @ApiModelProperty(value="출현 어종", example = "보말,게,소라,미역", required = true)
    private String marineCollections;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @ApiModelProperty(value="지역", example = "JEJU", required = true)
    private Area area;

    @Column(nullable = false)
    @ApiModelProperty(value="위도", example = "33.511333", required = true)
    private Double latitude;

    @Column(nullable = false)
    @ApiModelProperty(value="경도", example = "126.483503", required = true)
    private Double longitude;

    @Column(nullable = false)
    private Double markerPositionX;

    @Column(nullable = false)
    private Double markerPositionY;

    public HaeruPlaces toHaeruPlaces() {
        MarkerPosition markerPosition = new MarkerPosition();
        markerPosition.setX(this.markerPositionX);
        markerPosition.setY(this.markerPositionY);
        List<String> marineCollectionNameList = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(this.marineCollections, ",");
        while(st.hasMoreTokens()) {
            marineCollectionNameList.add(st.nextToken());
        }

        return HaeruPlaces.builder()
                .id(this.haeruPlaceId)
                .name(this.name)
                .address(this.address)
                .markerPosition(markerPosition)
                .marineCollections(marineCollectionNameList)
                .build();
    }
}
