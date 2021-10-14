package shupship.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import shupship.common.Constants;
import shupship.domain.dto.AddressMapDto;
import shupship.domain.dto.MapDto;
import shupship.domain.dto.ProvinceDto;
import shupship.domain.model.Province;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProvinceDao {

    private final Logger logger = LoggerFactory.getLogger(ProvinceDao.class);

//    @Autowired
//    @Qualifier("oracleEntityManager")
//    private EntityManager entityOracleManager;

    @Autowired
    RestTemplate restTemplate;


    public Map<String, ProvinceDto> getProvinveMap(Iterable<Province> provinces) {
        Map<String, ProvinceDto> list = new HashMap<>();
        try {
            for (Province model : provinces) {
                ProvinceDto provinceDto = new ProvinceDto();
                provinceDto.setProvinceId(model.getProvinceId());
                provinceDto.setProvinceCode(model.getProvinceCode());
                provinceDto.setProvinceName(model.getProvinceName());
                provinceDto.setFormattedAddress(model.getFormattedAddress());
                provinceDto.setLat(model.getLat());
                provinceDto.setLng(model.getLng());
                list.put(model.getProvinceCode(), provinceDto);
            }
        } catch (Exception e) {
            logger.info("Map Province fail: " + e.getMessage());
        }
        return list;
    }

    public Map<String, Province> getMapProvince(Iterable<Province> provinces) {
        try {
            Map<String, Province> list = new HashMap<>();
            for (Province data : provinces) {
                if (StringUtils.isNotEmpty(data.getProvinceCode())) {
                    list.put(data.getProvinceCode(), data);
                }
            }
            return list;
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    public List<ProvinceDto> getListProvince() {

        List<ProvinceDto> list = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String objects = restTemplate.getForObject(Constants.API_PROVINCE, String.class);
            MapDto res = objectMapper.readValue(objects, MapDto.class);
            List<AddressMapDto> list1 = objectMapper.convertValue(res.getData(), new TypeReference<ArrayList<AddressMapDto>>() {
            });
//            AddressMapDto[] data = restTemplate.getForObject(Constants.API_PROVINCE, AddressMapDto[].class);

            if (CollectionUtils.isNotEmpty(list1)) {
                for (AddressMapDto model : list1) {
                    ProvinceDto provinceDto = new ProvinceDto();
                    provinceDto.setProvinceId(Long.parseLong(model.getId()));
                    provinceDto.setProvinceCode(model.getCode());
                    provinceDto.setProvinceName(model.getComponents().get(0).getName());
                    provinceDto.setFormattedAddress(model.getFormattedAddress());
                    provinceDto.setLat(model.getGeometry().getLocation().getLat());
                    provinceDto.setLng(model.getGeometry().getLocation().getLng());
                    list.add(provinceDto);
                }
            }
        } catch (Exception e) {
            logger.info("Query fail getListProvince: " + e.getMessage());
        }
        return list;
    }

//    public List<ProvinceDto> getListProvince1() {
//
//        List<ProvinceDto> list = new ArrayList<>();
//        try {
//            Query query = entityOracleManager.createNativeQuery("Select * from a");
//            List<Object[]> data = query.getResultList();
//
//            for (Object[] model : data) {
//                ProvinceDto provinceDto = new ProvinceDto();
//                provinceDto.setProvinceCode(String.valueOf(model[0]));
//                provinceDto.setProvinceName(String.valueOf(model[1]));
////                provinceDto.setCountryCode(String.valueOf(model[5]));
////                provinceDto.setUsing(new BigDecimal(String.valueOf(model[3])).longValue());
////                provinceDto.setZoneCode(new BigDecimal(String.valueOf(model[2])).longValue());
//                list.add(provinceDto);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("Query getListProvince fail");
//        }
//        return list;
//    }
}
