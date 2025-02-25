package com.example.covestro.products.service

import com.example.covestro.products.exception.ProductException
import com.example.covestro.products.model.Product
import com.example.covestro.products.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.*

class ProductServiceTest {
    private lateinit var productService: ProductService
    private val productRepository: ProductRepository = mockk()

    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        productService = ProductService(productRepository)
        product = Product(
            id = 1,
            materialId = "M123",
            name = "Product1",
            price = BigDecimal("100.00"),
            currency = "USD",
            category = "Category1",
            createdBy = "api-user",
            lastUpdateBy = "api-user"
        )
    }

    @Test
    fun `findById should throw ProductNotFoundException when id not found`() {
        every { productRepository.findById(1) } returns Optional.empty()

        val exception = assertThrows<ProductException.ProductNotFoundException> {
            productService.findById(1)
        }

        assertEquals("Product with id 1 not found", exception.message)
        verify { productRepository.findById(1) }
    }

    @Test
    fun `updateById should throw InvalidProductException when id does not match`() {
        val invalidProduct = product.copy(id = 2)

        val exception = assertThrows<ProductException.InvalidProductException> {
            productService.updateById(1, invalidProduct)
        }

        assertEquals("Product ID in the request body does not match the ID in the path", exception.message)
    }

    @Test
    fun `updateById should throw ProductNotFoundException when id not found`() {
        every { productRepository.existsById(1) } returns false

        val exception = assertThrows<ProductException.ProductNotFoundException> {
            productService.updateById(1, product)
        }

        assertEquals("Product with id 1 not found", exception.message)
        verify { productRepository.existsById(1) }
    }

    @Test
    fun `create should throw InvalidProductException when id is not zero`() {
        val exception = assertThrows<ProductException.InvalidProductException> {
            productService.create(product)
        }

        assertEquals("Product ID must be zero for creation", exception.message)
    }
}
