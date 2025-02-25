package com.example.covestro.products.repository

import com.example.covestro.products.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * Repository for managing products.
 */
@Repository
interface ProductRepository : CrudRepository<Product, Long> {

    override fun findAll(): List<Product>

    override fun findById(id: Long): Optional<Product>

    fun save(product: Product): Product

}
