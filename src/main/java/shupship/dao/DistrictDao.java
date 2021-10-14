package shupship.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import shupship.common.Constants;
import shupship.domain.dto.AddressMapDto;
import shupship.domain.dto.DistrictDto;
import shupship.domain.dto.MapDto;
import shupship.domain.model.District;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class DistrictDao {
    private final Logger logger = LoggerFactory.getLogger(DistrictDao.class);

    @Autowired
    RestTemplate restTemplate;

    public List<DistrictDto> getListDistrict() {
        List<DistrictDto> list = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objects = restTemplate.getForObject(Constants.API_DISTRICT, String.class);
            MapDto res = objectMapper.readValue(objects, MapDto.class);
            List<AddressMapDto> list1 = objectMapper.convertValue(res.getData(), new TypeReference<ArrayList<AddressMapDto>>() {
            });
            if (CollectionUtils.isNotEmpty(list1)) {
                for (AddressMapDto model : list1) {
                    DistrictDto districtDto = new DistrictDto();
                    districtDto.setDistrictId(Long.parseLong(model.getId()));
                    districtDto.setDistrictCode(model.getCode());
                    districtDto.setFormattedAddress(model.getFormattedAddress());
                    districtDto.setDistrictName(model.getComponents().get(0).getName());
                    if (model.getComponents().size() >= 2){
                        districtDto.setProvinceCode(model.getComponents().get(1).getCode());
                    }
                    if (model.getGeometry() != null){
                        districtDto.setLat(model.getGeometry().getLocation().getLat());
                        districtDto.setLng(model.getGeometry().getLocation().getLng());
                    }
                    list.add(districtDto);
                }
            }
        } catch (Exception e) {
            logger.info("Query fail getListDistrict: " + e.getMessage());
        }
        return list;
    }

    public Map<Long, DistrictDto> getDistrictMap(Iterable<District> districts) {
        Map<Long, DistrictDto> list = new HashMap<>();
        try {
            for (District model : districts) {
                DistrictDto districtDto = new DistrictDto();
                districtDto.setProvinceCode(model.getProvinceCode());
                districtDto.setDistrictCode(model.getDistrictCode());
                districtDto.setDistrictId(model.getDistrictId());
                districtDto.setDistrictName(model.getDistrictName());
                districtDto.setFormattedAddress(model.getFormattedAddress());
                districtDto.setLat(model.getLat());
                districtDto.setLng(model.getLng());
                list.put(model.getDistrictId(), districtDto);
            }
        } catch (Exception e) {
            logger.info("Map District fail: " + e.getMessage());
        }
        return list;
    }

    public Map<String, District> getMapDistrict(Iterable<District> districts) {
        try {
            Map<String, District> liDistrictMap = new HashMap<>();
            for (District model : districts) {
                if (StringUtils.isNotEmpty(model.getDistrictCode())) {
                    liDistrictMap.put(model.getDistrictCode(), model);
                }
            }
            return liDistrictMap;
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}
