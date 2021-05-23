package com.fa.training.mockproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class DataSources extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dataSourceId;

    @Column
    @Length(max = 30)
    private String dataSourceName;///

    @OneToMany(mappedBy = "dataSources")
    @JsonIgnore
    private List<CompetencyRankingProfileDetails> competencyRankingProfileDetailsList;

    @ManyToMany
    @JsonIgnore
    private List<CompetencyComponentDetails> competencyComponentDetailsList;

    public DataSources() {
    }

    public DataSources(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
