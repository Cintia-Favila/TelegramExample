package org.switf.telegramexample.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.switf.telegramexample.models.PlaceModel;


@Repository
public interface PlaceJpaRepository extends JpaRepository<PlaceModel, Integer> {
}
