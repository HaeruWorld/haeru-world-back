package com.haeru.haeruworldback.domain.haeruplace.service;

import com.haeru.haeruworldback.domain.haeruplace.dto.HaeruPlaceDetail;
import com.haeru.haeruworldback.domain.haeruplace.dto.Location;
import com.haeru.haeruworldback.domain.haeruplace.dto.MarineCollections;
import com.haeru.haeruworldback.domain.haeruplace.entity.HaeruPlace;
import com.haeru.haeruworldback.domain.haeruplace.entity.MarineCollection;
import com.haeru.haeruworldback.domain.haeruplace.exception.HaeruPlaceNotFoundException;
import com.haeru.haeruworldback.domain.haeruplace.repository.HaeruPlaceRepository;
import com.haeru.haeruworldback.domain.haeruplace.repository.MarineCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HaeruPlaceDetailService {

    private final HaeruPlaceRepository haeruPlaceRepository;
    private final MarineCollectionRepository marineCollectionRepository;

    // 해루질 장소 상세 조회
    public HaeruPlaceDetail getHaeruPlacesDetail(Long haeruPlaceId) {
        HaeruPlaceDetail haeruPlaceDetail = new HaeruPlaceDetail();

        HaeruPlace findHaeruPlace = haeruPlaceRepository.findById(haeruPlaceId).orElseThrow(HaeruPlaceNotFoundException::new);

        haeruPlaceDetail.setName(findHaeruPlace.getName());
        haeruPlaceDetail.setAddress(findHaeruPlace.getAddress());

        String marineCollectionsName = findHaeruPlace.getMarineCollections();
        StringTokenizer st = new StringTokenizer(marineCollectionsName,",");

        List<String> marineCollectionNameList = new ArrayList<>();
        while(st.hasMoreTokens()) {
            marineCollectionNameList.add(st.nextToken());
        }

        List<MarineCollections> list = new ArrayList<>();
        for(String name : marineCollectionNameList) {
            MarineCollection findMarineCollection = marineCollectionRepository.findByName(name);
            list.add(findMarineCollection.toDto());
        }
        haeruPlaceDetail.setMarineCollections(list);

        Location location = new Location(findHaeruPlace.getLatitude(), findHaeruPlace.getLongitude());
        haeruPlaceDetail.setLocation(location);

        List<String> resultTime = getSeaTime(findHaeruPlace.getArea().toString());

        if(resultTime.size() == 0) {
            haeruPlaceDetail.setStartTime(null);
            haeruPlaceDetail.setEndTime(null);
        } else {
            haeruPlaceDetail.setStartTime(resultTime.get(0));
            haeruPlaceDetail.setEndTime(resultTime.get(1));
        }

        return haeruPlaceDetail;
    }


    public List<String> getSeaTime(String area) {

        // 저조시간 관측 지역코드
        String areaCode = "DT_0010";
        switch (area) {
            case "JEJU":
                areaCode = "DT_0004";
                break;
            case "AEWOL":
                areaCode = "DT_0047";
                break;
            case "SEONGSAN":
                areaCode = "DT_0022";
                break;
            case "default":
                areaCode = "DT_0010";
                break;
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        String date = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String reqURL = "https://www.khoa.go.kr/api/oceangrid/tideObsPreTab/search.do?ServiceKey=CA7svK0E39kQdfoWyx70rQ==&ObsCode="
                + areaCode + "&Date=" + date + "&ResultType=json";
        String result = "";
        List<String> resultTimeList = new ArrayList<>();
        try {

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setRequestProperty("charset", "UTF-8");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                result += line;
            }

            // 하루 저조시간 리스트
            List<String> timeList = getTphTime(result);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lawStartTime = LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),9,0,0);
            LocalDateTime lawEndTime = LocalDateTime.of(now.getYear(),now.getMonth(),now.getDayOfMonth(),17,0,0);

            LocalDateTime resultTime = null;
            for(int i = 0; i < timeList.size(); i++) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(timeList.get(i), formatter);
                if(dateTime.isBefore(lawEndTime) && dateTime.isAfter(lawStartTime)) {
                    resultTime = dateTime;
                    break;
                }
            }

            if(resultTime == null) {
                return resultTimeList;
            }

            // 법정 시간 비교
            LocalDateTime startTime = resultTime.minusHours(1);
            if (startTime.isBefore(lawStartTime)) startTime = lawStartTime;
            LocalDateTime endTime = resultTime.plusHours(1);
            if(endTime.isAfter(lawEndTime)) endTime = lawEndTime;


            // 해루질 가능 시작, 종료시간
            String startResultTime = startTime.format(DateTimeFormatter.ofPattern("HH:mm"));;
            String endResultTime = endTime.format(DateTimeFormatter.ofPattern("HH:mm"));;

            resultTimeList.add(startResultTime);
            resultTimeList.add(endResultTime);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultTimeList;
    }

    // 하루 저조 시간 구하기
    public List<String> getTphTime(String result) {
        List<String> list = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(result);

            JSONObject resultData = (JSONObject) jsonObj.get("result");
            JSONArray data = (JSONArray) resultData.get("data");

            for(int i = 0; i < data.size(); i++) {
                JSONObject object = (JSONObject) data.get(i);
                String hl_code = (String) object.get("hl_code");
                if(hl_code.equals("저조")) {
                    String time = (String) object.get("tph_time");
                    list.add(time);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }


}
