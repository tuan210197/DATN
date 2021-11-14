package shupship.repo;

import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import shupship.domain.model.Result;

public interface IResultRepository extends JpaRepository<Result, Long> {

}
