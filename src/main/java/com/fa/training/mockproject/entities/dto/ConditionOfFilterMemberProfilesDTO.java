package com.fa.training.mockproject.entities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionOfFilterMemberProfilesDTO {
    private Integer jobRoleId;
    private Long periodId;
    private String memberName;
    private String submittedDate;
    private String status;
    private String domainName;

    public ConditionOfFilterMemberProfilesDTO() {
    }

    public ConditionOfFilterMemberProfilesDTO(Integer jobRoleId, Long periodId, String memberName, String submittedDate, String status, String domainName) {
        this.jobRoleId = jobRoleId;
        this.periodId = periodId;
        this.memberName = memberName;
        this.submittedDate = submittedDate;
        this.status = status;
        this.domainName = domainName;
    }
}
