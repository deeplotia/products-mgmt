package com.example.covestro.products.service

import com.example.covestro.products.exception.ProductException
import com.example.covestro.products.model.Product
import com.example.covestro.products.repository.ProductRepository
import org.hibernate.exception.ConstraintViolationException
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.postgresql.util.PSQLException
import org.springframework.dao.DataIntegrityViolationException

/**
    * Service class for managing products.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    private val log: Logger = LoggerFactory.getLogger(ProductService::class.java)

    fun findAll(): List<Product> {
        log.info("Fetching all products")
        return productRepository.findAll()
    }

    fun findById(id: Long): Product {
        log.info("Fetching product with id $id")
        return productRepository.findById(id).orElseThrow {
            log.error("Product with id $id not found")
            ProductException.ProductNotFoundException("Product with id $id not found")
        }
    }

    fun updateById(id: Long, product: Product): Product {
        log.info("Updating product with id $id")
        if (product.id != id) {
            log.error("Product ID in the request body does not match the ID in the path")
            throw ProductException.InvalidProductException("Product ID in the request body does not match the ID in the path")
        }
        if (!productRepository.existsById(id)) {
            log.error("Product with id $id not found")
            throw ProductException.ProductNotFoundException("Product with id $id not found")
        }
        return try {
            productRepository.save(product)
        } catch (ex: DataIntegrityViolationException) {
            handleException(ex, product)
        }
    }

    fun create(product: Product): Product {
        log.info("Creating new product")
        if (product.id != 0L) {
            log.error("Product ID must be zero for creation")
            throw ProductException.InvalidProductException("Product ID must be zero for creation")
        }
        return try {
            productRepository.save(product)
        } catch (ex: DataIntegrityViolationException) {
            handleException(ex, product)
        }
    }

    private fun handleException(ex: Exception, product: Product): Product {
        if (ex.cause is ConstraintViolationException) {
            val sqlException = ex.cause as ConstraintViolationException
            when {
                sqlException.message?.contains("products_category_check") == true -> {
                    log.error("Invalid category: ${product.category}")
                    throw ProductException.InvalidCategoryException("Invalid category: ${product.category}")
                }
                sqlException.message?.contains("products_material_id_key") == true -> {
                    log.error("Material ID ${product.materialId} already exists")
                    throw ProductException.DuplicateMaterialIdException("Material ID ${product.materialId} already exists")
                }
            }
        }
        throw ex
    }
}
