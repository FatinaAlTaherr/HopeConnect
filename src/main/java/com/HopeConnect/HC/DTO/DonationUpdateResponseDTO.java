package com.HopeConnect.HC.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonationUpdateResponseDTO {
    private String message;
    private String title;
    private String description;
    private String imageUrl;
}