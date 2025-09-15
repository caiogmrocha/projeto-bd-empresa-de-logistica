package br.edu.ufape.projeto_bd.projeto_bd.domain.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa a tabela orders_products (tabela associativa entre orders e products).
 */
@Entity
@Table(name = "orders_products")
@Getter
@Setter
@NoArgsConstructor
@IdClass(OrderProduct.OrderProductId.class)
public class OrderProduct {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", nullable = false)
    private Product product;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    /**
     * Classe auxiliar para representar a PK composta (orders_id, products_id).
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderProductId implements Serializable {
        private Long order;
        private Long product;

        public OrderProductId(Long order, Long product) {
            this.order = order;
            this.product = product;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderProductId)) return false;
            OrderProductId that = (OrderProductId) o;
            return Objects.equals(order, that.order) &&
                   Objects.equals(product, that.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(order, product);
        }
    }
}
