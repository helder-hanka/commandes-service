package com.ff.commandes_service.feignClient;

import com.ff.commandes_service.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service", url = "http://localhost:8080/api/products") // temporaire, sera remplacé par Eureka plus tard
public interface ProduitClient {
    // Définir les méthodes pour interagir avec le service produit
    // Par exemple, pour obtenir un produit par son ID :
    @GetMapping("/{id}")
    ProductResponseDto getProductById(@PathVariable("id") Long id);

    @GetMapping("{id}/stock")
    int getProductStockById(@PathVariable("id") Long id);

    @GetMapping("/list/{id}")
    ProductResponseDto getProductListUser(@PathVariable("id") Long id);
}
