package com.fa.training.mockproject.entities;


import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class PeriodPattern extends CommonGenericClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long periodId;

    @NotNull
    private String name;

    @NotNull
    private String year;

    @OneToMany(mappedBy = "periodPattern")
    @JsonIgnore
    private List<CompetencyRankingPatterns> competencyRankingPatternsList;

}
