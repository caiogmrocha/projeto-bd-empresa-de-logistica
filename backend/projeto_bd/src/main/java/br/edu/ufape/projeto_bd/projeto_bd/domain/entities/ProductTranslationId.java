package br.edu.ufape.projeto_bd.projeto_bd.domain.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTranslationId implements Serializable {
    
    @Column(name = "products_id")
    private Long productId;

    @Column(name = "languages_id")
    private Long languageId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductTranslationId that = (ProductTranslationId) o;
        return Objects.equals(productId, that.productId) &&
               Objects.equals(languageId, that.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, languageId);
    }
}
