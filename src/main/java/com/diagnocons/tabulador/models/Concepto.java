package com.diagnocons.tabulador.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conceptos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Concepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_to")
    private Long id;

    @Override
    public String toString() {
        return "Concepto{" +
                "id=" + id +
                '}';
    }
}
