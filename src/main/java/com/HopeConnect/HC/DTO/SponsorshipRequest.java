package com.HopeConnect.HC.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SponsorshipRequest {
    private String userEmail;
    private Long orphanId;
    private Date startDate;
    private Date endDate;
}