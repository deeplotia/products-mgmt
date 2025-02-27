package com.example.covestro.products.exception

sealed class ProductException(message: String) : RuntimeException(message) {
    class ProductNotFoundException(message: String) : ProductException(message)
    class InvalidProductException(message: String) : ProductException(message)
    class DuplicateMaterialIdException(message: String) : ProductException(message)
    class InvalidCategoryException(message: String) : ProductException(message)
}
