package com.pactworkshop.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final Map<String, Product> PRODUCTS = new HashMap<>();

    public List<Product> fetchAll() {
        initProducts();

        return new ArrayList<>(PRODUCTS.values());
    }

    public Optional<Product> getById(String id) {
        initProducts();

        return Optional.ofNullable(PRODUCTS.get(id));
    }

    private void initProducts() {
        PRODUCTS.put("1f518caa773f01a72fdfcd84913632d844ebc853",
            new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US", 83, "USD",
                "29a8b245-4f3e-41e7-ac82-97249ec39aab", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "1.2"));
        PRODUCTS.put("6cbbcc8fc23cc3378e4a70ace25ff0d5567f80d7",
            new Product("6cbbcc8fc23cc3378e4a70ace25ff0d5567f80d7", "patent", "JP", 83, "EUR",
                "26f17105-8ba0-4fb9-9d92-0346273c3ac8", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.2"));
        PRODUCTS.put("f4e249b80ddb28cf064d1fd27f5f68b3ec4a46ff",
            new Product("f4e249b80ddb28cf064d1fd27f5f68b3ec4a46ff", "trademark", "EU", 83, "ABC",
                "e300917c-51f8-4e57-95d1-b1d14eab3de2", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.6"));
    }
}
