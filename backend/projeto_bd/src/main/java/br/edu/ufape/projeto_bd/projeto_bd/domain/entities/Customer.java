package br.edu.ufape.projeto_bd.projeto_bd.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Talvez fosse interessante usar Long ao invés de Long nos ids
//O nome addresses não 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE customers SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "id")	
	  private Long id;
	  
	  @Column(name = "name", length = 100, nullable = false)
	  private String name;
	  
	  @ManyToOne(cascade = CascadeType.ALL)
	  @JoinColumn(name="addresses_id", nullable=false)
	  private Address addresses;
	  
	  @Column(name="credit_limit", nullable=false)
	  private BigDecimal creditLimit;
	  
	  @CreatedDate
	  @Column(name = "created_at", nullable = false, updatable = false)
	  private LocalDateTime createdAt;

	  @LastModifiedDate
	  @Column(name = "updated_at", nullable = false)
	  private LocalDateTime updatedAt;

	  @Column(name = "deleted_at")
	  private LocalDateTime deletedAt;
}
