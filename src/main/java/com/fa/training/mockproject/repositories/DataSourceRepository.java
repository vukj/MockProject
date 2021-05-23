package com.fa.training.mockproject.repositories;

import com.fa.training.mockproject.entities.DataSources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataSourceRepository extends JpaRepository<DataSources, Integer> {
}
