package br.edu.ufape.projeto_bd.projeto_bd.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "legal_entities")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "suppliers_id")
public class LegalEntity extends Supplier {

    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    private String cnpj;
}