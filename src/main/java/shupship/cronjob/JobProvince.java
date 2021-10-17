package shupship.cronjob;


import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shupship.dao.ProvinceDao;
import shupship.domain.dto.ProvinceDto;
import shupship.domain.model.Province;
import shupship.repo.IProvinceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JobProvince {

    private final Logger logger = LoggerFactory.getLogger(JobProvince.class);

    @Autowired
    ProvinceDao provinceDao;

    @Autowired
    IProvinceRepository provinceRepository;

    //    @Scheduled(cron = "0 0 * ? * *")
//    @Scheduled(cron = "30 19 18 17 8 ?")
    public void syncProvince() {
        try {
            logger.info("Start sync Province");
            List<ProvinceDto> listProvince = provinceDao.getListProvince();
            Iterable<Province> list = provinceRepository.findAll();

            Map<String, ProvinceDto> liLongProvinceDtoMap = provinceDao.getProvinveMap(list);
            Map<String, Province> liLongProvinceMap = provinceDao.getMapProvince(list);

            List<Province> listToUpdate = new ArrayList<>();
            List<Province> listToSave = new ArrayList<>();

            for (ProvinceDto model : listProvince) {
                ProvinceDto provinceDto = liLongProvinceDtoMap.get(model.getProvinceCode());
                boolean check = model.equals(provinceDto);
                if (!check) {
                    Province province = liLongProvinceMap.get(model.getProvinceCode());
                    if (province != null) {
                        province.setProvinceId(model.getProvinceId());
                        province.setProvinceCode(model.getProvinceCode());
                        province.setProvinceName(model.getProvinceName());
                        province.setFormattedAddress(model.getFormattedAddress());
                        province.setLat(model.getLat());
                        province.setLng(model.getLng());
                        listToUpdate.add(province);
                        logger.info("Update" + model.getProvinceName());
                    } else {
                        Province province1 = new Province();
                        province1.setProvinceId(model.getProvinceId());
                        province1.setProvinceCode(model.getProvinceCode());
                        province1.setProvinceName(model.getProvinceName());
                        province1.setFormattedAddress(model.getFormattedAddress());
                        province1.setLat(model.getLat());
                        province1.setLng(model.getLng());
                        province1.setCreatedBy(-1L);
                        logger.info("Add" + model.getProvinceCode());
                        listToSave.add(province1);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(listToUpdate)) {
                provinceRepository.saveAll(listToUpdate);
                logger.info("Update done");
            }
            if (CollectionUtils.isNotEmpty(listToSave)) {
                provinceRepository.saveAll(listToSave);
                logger.info("Add done");
            }
            logger.info("Done sync Province");
        } catch (Exception e) {
            logger.info("Sync Povince fail: " + e.getMessage());
        }
    }
}
