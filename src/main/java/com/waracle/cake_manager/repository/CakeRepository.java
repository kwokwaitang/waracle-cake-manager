package com.waracle.cake_manager.repository;

import com.waracle.cake_manager.model.Cake;
import org.springframework.data.repository.CrudRepository;

public interface CakeRepository extends CrudRepository<Cake, Long> {
}
