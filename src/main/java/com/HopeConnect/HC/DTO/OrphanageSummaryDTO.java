package com.HopeConnect.HC.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrphanageSummaryDTO {
    private Long id;
    private String name;
    private String email;
    private String location;
}
