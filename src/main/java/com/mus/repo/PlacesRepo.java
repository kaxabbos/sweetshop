package com.mus.repo;

import com.mus.model.Places;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacesRepo extends JpaRepository<Places, Long> {
    List<Places> findAllByCategory(String category);
}
