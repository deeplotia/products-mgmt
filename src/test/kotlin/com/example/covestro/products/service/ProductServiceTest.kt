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
import java.util.Optional

class ProductServiceTest {
    private lateinit var productService: ProductService
    private val productRepository: ProductRepository = mockk()

    @BeforeEach
    fun setUp() {
        productService = ProductService(productRepository)
    }

    @Test
    fun `findById should throw ProductNotFoundException when id not found`() {
        every { productRepository.findById(1) } returns Optional.empty()

        val exception = assertThrows<ProductException.ProductNotFoundException> {
            productService.findById(1)
        }

        assertEquals("Product with id 1 not found", exception.message)
        verify(exactly = 1) { productRepository.findById(1) }
    }

    @Test
    fun `updateById should throw InvalidProductException when id does not match`() {
        val invalidProduct = PRODUCT.copy(id = 2)

        val exception = assertThrows<ProductException.InvalidProductException> {
            productService.updateById(1, invalidProduct)
        }

        assertEquals("Product ID in the request body does not match the ID in the path", exception.message)
        verify(exactly = 0) { productRepository.findById(any()) }
        verify(exactly = 0) { productRepository.save(any()) }
        verify(exactly = 0) { productRepository.findAll() }
    }

    @Test
    fun `updateById should throw ProductNotFoundException when id not found`() {
        every { productRepository.existsById(1) } returns false

        val exception = assertThrows<ProductException.ProductNotFoundException> {
            productService.updateById(1, PRODUCT)
        }

        assertEquals("Product with id 1 not found", exception.message)
        verify(exactly = 1) { productRepository.existsById(1) }
        verify(exactly = 0) { productRepository.save(any()) }
        verify(exactly = 0) { productRepository.findAll() }
    }

    @Test
    fun `create should throw InvalidProductException when id is not zero`() {
        val exception = assertThrows<ProductException.InvalidProductException> {
            productService.create(PRODUCT)
        }

        assertEquals("Product ID must be zero for creation", exception.message)
        verify(exactly = 0) { productRepository.save(any()) }
        verify(exactly = 0) { productRepository.findAll() }
    }

    @Test
    fun `create should save the product when provided with valid input`() {
        every { productRepository.save(any()) } returns PRODUCT
        every { productRepository.findAll() } returns emptyList()

        productService.create(PRODUCT.copy(0L))

        verify(exactly = 1) { productRepository.save(any()) }
        verify(exactly = 1) { productRepository.findAll() }
    }

    companion object {
        val PRODUCT = Product(
            id = 1,
            materialId = "702707UX",
            name = "WH1000",
            price = BigDecimal("3.44"),
            currency = "USD",
            category = "Plastics",
            createdBy = "api-user",
            lastUpdateBy = "api-user"
        )
    }
}
