package shupship.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

public interface CrudService<E, P> {


    @Transactional
    Page<E> list(Pageable pageable);


}
