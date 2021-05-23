package com.fa.training.mockproject.services;

import com.fa.training.mockproject.entities.DataSources;

public interface DataSourceService {

    void save(DataSources dataSources);

    DataSources findById(int id);
}
