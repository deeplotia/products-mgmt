package com.example.covestro.products.controller

import com.example.covestro.products.exception.ProductException
import com.example.covestro.products.model.Product
import com.example.covestro.products.service.ProductService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

/**
 * Controller class for managing products.
 */
@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {
    private val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping
    fun findAll(): List<Product> {
        logger.info("Received request to fetch all products")
        return productService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): Product {
        logger.info("Received request to fetch product with id $id")
        return productService.findById(id)
    }

    @PutMapping("/{id}")
    fun updateById(@PathVariable id: Long, @RequestBody product: Product): Product {
        logger.info("Received request to update product with id $id")
        return productService.updateById(id, product)
    }

    @PostMapping
    fun create(@RequestBody product: Product): Product {
        logger.info("Received request to create a new product")
        return productService.create(product)
    }
}

@ControllerAdvice
class GlobalExceptionHandler {

   @ExceptionHandler(ProductException::class)
   @ResponseBody
   fun handleProductException(ex: ProductException): ResponseEntity<String> {
       return ResponseEntity(ex.message ?: "Invalid product", HttpStatus.BAD_REQUEST)
   }

   @ExceptionHandler(Exception::class)
   @ResponseBody
   fun handleGenericException(ex: Exception): ResponseEntity<String> {
       return ResponseEntity(ex.message ?: "An error occurred", HttpStatus.INTERNAL_SERVER_ERROR)
   }
}
