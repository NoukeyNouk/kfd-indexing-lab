package lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductSelectionTest {
    @Test
    fun selectsAvailableProductsInRecommendationOrder() {
        val keyboard = Product(1, "Keyboard", true)
        val mouse = Product(2, "Mouse", false)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)
        val products = listOf(keyboard, mouse, monitor, webcam)

        val result = selectProducts(
            products,
            listOf(3L, 2L, 3L, 99L, 1L, 4L),
            listOf(1L)
        )

        assertEquals(listOf(monitor, webcam), result)
    }

    @Test
    fun returnsEmptyListWhenThereAreNoRecommendations() {
        val products = listOf(Product(1, "Keyboard", true))

        val result = selectProducts(products, emptyList(), emptyList())

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun preservesRecommendationOrderRegardlessOfCatalogOrder() {
        val keyboard = Product(1, "Keyboard", true)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)

        val result = selectProducts(
            listOf(webcam, keyboard, monitor),
            listOf(3L, 1L, 4L),
            emptyList()
        )

        assertEquals(listOf(monitor, keyboard, webcam), result)
    }

    @Test
    fun handlesRepeatedRecommendationsAndExclusions() {
        val keyboard = Product(1, "Keyboard", true)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)

        val result = selectProducts(
            listOf(keyboard, monitor, webcam),
            listOf(3L, 1L, 3L, 4L, 1L, 4L),
            listOf(1L, 1L, 99L, 99L)
        )

        assertEquals(listOf(monitor, webcam), result)
    }

    @Test
    fun skipsUnavailableExcludedAndUnknownProducts() {
        val unavailable = Product(1, "Mouse", false)
        val excluded = Product(2, "Keyboard", true)
        val selected = Product(3, "Monitor", true)

        val result = selectProducts(
            listOf(unavailable, excluded, selected),
            listOf(99L, 1L, 2L, 3L),
            listOf(2L, 100L)
        )

        assertEquals(listOf(selected), result)
    }

    @Test
    fun doesNotIncludeProductsMissingFromRecommendations() {
        val keyboard = Product(1, "Keyboard", true)
        val monitor = Product(3, "Monitor", true)

        val result = selectProducts(listOf(keyboard, monitor), listOf(3L), emptyList())

        assertEquals(listOf(monitor), result)
    }

    @Test
    fun returnsEmptyListWhenCatalogIsEmpty() {
        val result = selectProducts(emptyList(), listOf(1L, 1L, 99L), listOf(99L))

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun returnsEmptyListWhenNoProductsQualify() {
        val products = listOf(Product(1, "Mouse", false), Product(2, "Keyboard", true))

        val result = selectProducts(products, listOf(1L, 2L, 99L, 2L), listOf(2L))

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun doesNotModifyInputsAndKeepsRepeatedCallsIndependent() {
        val keyboard = Product(1, "Keyboard", true)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)
        val products = mutableListOf(webcam, keyboard, monitor)
        val recommendations = mutableListOf(3L, 1L, 3L, 4L, 99L)
        val exclusions = mutableListOf(1L, 1L)

        val first = selectProducts(products, recommendations, exclusions)
        val other = selectProducts(products, listOf(1L, 4L), listOf(3L))
        val repeated = selectProducts(products, recommendations, exclusions)

        assertEquals(listOf(monitor, webcam), first)
        assertEquals(listOf(keyboard, webcam), other)
        assertEquals(listOf(monitor, webcam), repeated)
        assertEquals(listOf(webcam, keyboard, monitor), products)
        assertEquals(listOf(3L, 1L, 3L, 4L, 99L), recommendations)
        assertEquals(listOf(1L, 1L), exclusions)
    }
}
