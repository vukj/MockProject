package com.fa.training.mockproject.services.impl;

import com.fa.training.mockproject.entities.DataSources;
import com.fa.training.mockproject.repositories.DataSourceRepository;
import com.fa.training.mockproject.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DataSourceServiceImp implements DataSourceService {
    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Override
    public void save(DataSources dataSources) {
        dataSourceRepository.save(dataSources);
    }

    @Override
    public DataSources findById(int id) {
        return dataSourceRepository.getOne(id);
    }
}
