package com.example.covestro.products.model

import java.math.BigDecimal
import java.time.Instant
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.GenerationType
import jakarta.persistence.Column

@Entity
@Table(name = "products")
data class Product(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = 0,

	@Column(name = "material_id")
	val materialId: String? = "",

	@Column(name = "name")
	val name: String? = "",

	@Column(name = "price")
	val price: BigDecimal? = BigDecimal.ZERO,

	@Column(name = "currency")
	val currency: String? = "",

	@Column(name = "category")
	val category: String? = "",

	@Column(name = "created_at")
	val createdAt: Instant? = Instant.now(),

	@Column(name = "created_by")
	val createdBy: String? = "api-user",

	@Column(name = "last_update")
	val lastUpdate: Instant? = Instant.now(),

	@Column(name = "last_update_by")
	val lastUpdateBy: String? = "api-user",

)
