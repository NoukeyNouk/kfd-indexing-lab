package lab2

fun selectProducts(
    products: List<Product>,
    recommendedIds: List<Long>,
    excludedIds: List<Long>
): List<Product> {
    val excludedSet = excludedIds.toSet()
    val filtered = mutableMapOf<Long, Product>()
    for (product in products) {
        if (product.available && product.id !in excludedSet) {
            filtered[product.id] = product
        }
    }

    val answer = mutableListOf<Product>()

    for (id in recommendedIds) {
        val product = filtered.remove(id) ?: continue
        answer.add(product)
    }

    return answer
}
