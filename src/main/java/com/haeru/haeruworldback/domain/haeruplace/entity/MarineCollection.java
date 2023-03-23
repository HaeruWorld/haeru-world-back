package com.haeru.haeruworldback.domain.haeruplace.entity;

import com.haeru.haeruworldback.domain.haeruplace.dto.MarineCollectionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarineCollection {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marineCollectionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String imageUrl;

    public MarineCollectionDto toDto() {
        return MarineCollectionDto.builder()
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }

}
