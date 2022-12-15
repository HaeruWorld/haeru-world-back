package com.haeru.haeruworldback.domain.haeruplace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Location {

    private Double latitude;
    private Double longitude;
}
