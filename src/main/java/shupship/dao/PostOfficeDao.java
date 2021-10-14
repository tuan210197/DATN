//package shupship.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Repository;
//import shupship.common.Constants;
//import shupship.domain.dto.PostOfficeDto;
//import shupship.domain.model.PostOffice;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class PostOfficeDao {
//
//    private final Logger logger = LoggerFactory.getLogger(PostOfficeDao.class);
//
//    @Autowired
//    @Qualifier("oracleEntityManager")
//    private EntityManager entityOracleManager;
//
//    public List<PostOfficeDto> getListPostOffice() {
//        List<PostOfficeDto> list = new ArrayList<>();
//        try {
//            Query query = entityOracleManager.createNativeQuery(Constants.QUERY_POSTOFFICE);
//            List<Object[]> data = query.getResultList();
//            for (Object[] model : data) {
//                PostOfficeDto p = new PostOfficeDto();
//                p.setPostCode(String.valueOf(model[0]));
//                p.setProvinceCode(String.valueOf(model[1]));
//                p.setDistrictCode(String.valueOf(model[2]));
//                p.setWardCode(String.valueOf(model[3]));
//                p.setPostName(String.valueOf(model[4]));
//                p.setLat(Double.parseDouble(String.valueOf(model[5])));
//                p.setLng(Double.parseDouble(String.valueOf(model[6])));
//                p.setPostPhone(String.valueOf(model[7]));
//                list.add(p);
//            }
//        } catch (Exception e) {
//            logger.info("Query getListPostOffice fail: " + e.getMessage());
//        }
//        return list;
//    }
//
//    public Map<String, PostOfficeDto> getMapPostOfficeDto(Iterable<PostOffice> postOffices) {
//        Map<String, PostOfficeDto> list = new HashMap<>();
//        try {
//            for (PostOffice model : postOffices) {
//                PostOfficeDto data = new PostOfficeDto();
//                data.setPostCode(model.getPostCode());
//                data.setPostName(model.getPostName());
//                data.setProvinceCode(model.getProvinceCode());
//                data.setLat(model.getLat());
//                data.setLng(model.getLng());
//                data.setDistrictCode(model.getDistrictCode());
//                data.setWardCode(model.getWardCode());
//                data.setPostPhone(model.getPostPhone());
//                list.put(model.getPostCode(), data);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("Query fail");
//        }
//
//        return list;
//    }
//
//    public Map<String, PostOffice> getMapPostoffice(Iterable<PostOffice> postOffices) {
//        Map<String, PostOffice> list = new HashMap<>();
//        for (PostOffice data : postOffices) {
//            list.put(data.getPostCode(), data);
//        }
//        return list;
//    }
//
//}
