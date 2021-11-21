package shupship.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class ReportDao {

    @Qualifier("postgresEntityManager")
    @Autowired
    private EntityManager entityManager;

//    @Autowired
//    private EntityManager entityManager;

}
