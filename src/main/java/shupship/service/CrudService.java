package shupship.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

public interface CrudService<E, P> {


    @Transactional
    E save(E entity);

    @Transactional
    Page<E> list(Pageable pageable);

    @Transactional
    Page<E> search(Specification<E> specification, Pageable pageable);

    @Transactional
    E get(P id);

    @Transactional
    void delete(E entity);

}
