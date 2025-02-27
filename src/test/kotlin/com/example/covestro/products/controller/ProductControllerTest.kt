package com.example.covestro.products.controller

import com.example.covestro.products.model.Product
import com.example.covestro.products.service.ProductService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var productService: ProductService

    private lateinit var headers: HttpHeaders

    @BeforeEach
    fun setUp() {
        headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
    }

    @Test
    fun `findAll should return list of products`() {
        every { productService.findAll() } returns listOf(PRODUCT)

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(PRODUCT.id))
            .andExpect(jsonPath("$[0].materialId").value(PRODUCT.materialId))
            .andExpect(jsonPath("$[0].name").value(PRODUCT.name))
    }

    @Test
    fun `findById should return product when found`() {
        every { productService.findById(1) } returns PRODUCT

        mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(PRODUCT.id))
            .andExpect(jsonPath("$.materialId").value(PRODUCT.materialId))
            .andExpect(jsonPath("$.name").value(PRODUCT.name))
    }

    @Test
    fun `create should save and return product`() {
        every { productService.create(any()) } returns PRODUCT

        mockMvc.perform(
            post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "materialId": "M123",
                        "name": "Product1",
                        "price": 100.00,
                        "currency": "USD",
                        "category": "Category1",
                        "createdBy": "api-user",
                        "lastUpdateBy": "api-user"
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(PRODUCT.id))
            .andExpect(jsonPath("$.materialId").value(PRODUCT.materialId))
            .andExpect(jsonPath("$.name").value(PRODUCT.name))
    }

    @Test
    fun `updateById should update and return product`() {
        every { productService.updateById(1, any()) } returns PRODUCT

        mockMvc.perform(
            put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "id": 1,
                        "materialId": "XYZ",
                        "name": "ABC",
                        "price": 100.00,
                        "currency": "EUR",
                        "category": "Coatings",
                        "createdBy": "api-user",
                        "lastUpdateBy": "api-user"
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(PRODUCT.id))
            .andExpect(jsonPath("$.materialId").value(PRODUCT.materialId))
            .andExpect(jsonPath("$.name").value(PRODUCT.name))
    }

    companion object {
        val PRODUCT = Product(
            materialId = "702707UX",
            name = "WH1000",
            price = BigDecimal("3.44"),
            currency = "USD",
            category = "Plastics"
        )
    }
}
