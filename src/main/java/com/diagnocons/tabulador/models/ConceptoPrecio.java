package com.diagnocons.tabulador.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conceptos_precios", uniqueConstraints = @UniqueConstraint(columnNames = "id_concepto"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
    public class ConceptoPrecio {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JoinColumn(name = "id_concepto")
        @ManyToOne(fetch = FetchType.LAZY)
        private Concepto concepto;

        private Double precio;

        @Column(name = "fecha_modificacion")
        private LocalDateTime fechaModificacion;

    @Override
    public String toString() {
        return "ConceptoPrecio{" +
                "id=" + id +
                ", concepto=" + concepto.getId() +
                ", precio=" + precio +
                ", fechaModificacion=" + fechaModificacion +
                '}';
    }
}

