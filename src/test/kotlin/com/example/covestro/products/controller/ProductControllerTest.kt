package com.example.covestro.products.controller

import com.example.covestro.products.model.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        product = Product(
            id = 1,
            materialId = "M123",
            name = "Product1",
            price = BigDecimal("100.00"),
            currency = "USD",
            category = "Category1"
        )
    }

    @Test
    fun findAll() {
        val response: ResponseEntity<Array<Product>> =
            restTemplate.getForEntity("http://localhost:$port/api/products", Array<Product>::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body!!.isNotEmpty())
    }

    @Test
    fun findById() {
        val response: ResponseEntity<Product> =
            restTemplate.getForEntity("http://localhost:$port/api/products/1", Product::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(product.id, response.body!!.id)
    }

    @Test
    fun create() {
        val response: ResponseEntity<Product> =
            restTemplate.postForEntity("http://localhost:$port/api/products", product, Product::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(product.name, response.body!!.name)
    }

    @Test
    fun updateById() {
        restTemplate.put("http://localhost:$port/api/products/1", product)
        val response: ResponseEntity<Product> =
            restTemplate.getForEntity("http://localhost:$port/api/products/1", Product::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(product.name, response.body!!.name)
    }
}