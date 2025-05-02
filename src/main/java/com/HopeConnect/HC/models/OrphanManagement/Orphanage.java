package com.HopeConnect.HC.models.OrphanManagement;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orphanage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String contactPerson;
    private String phoneNumber;

    @OneToMany(mappedBy = "orphanage", cascade = CascadeType.ALL)
    private List<Orphan> orphans;
}
