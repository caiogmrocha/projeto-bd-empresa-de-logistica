package br.edu.ufape.projeto_bd.projeto_bd.domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products_translations")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE products_translations SET deleted_at = NOW() WHERE products_id = ? AND languages_id = ?")
@SQLRestriction ("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTranslation implements Serializable {

    @EmbeddedId
    private ProductTranslationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId") //A coluna de junção para este relacionamento (@JoinColumn(name = "products_id")) também serve como parte da chave primária. @MapsId mapeia para o atributo productId dentro da classe @EmbeddedId.
    @JoinColumn(name = "products_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("languageId")
    @JoinColumn(name = "languages_id")
    private Language language;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(max = 100, message = "O nome não pode ultrapassar 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
}
