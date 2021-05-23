package com.fa.training.mockproject.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplate {
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String content;
}