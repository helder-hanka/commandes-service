package com.ff.commandes_service.feignClient;

import com.ff.commandes_service.dto.ClientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service", url = "http://localhost:8080/api/client") // temporaire, sera remplacé par Eureka plus tard
public interface Client {
    // Vous pouvez définir des méthodes pour interagir avec le service client ici
    // Par exemple, pour obtenir un client par son ID :
    @GetMapping("/{id}")
    ClientResponseDto getClientById(@PathVariable("id") Long id);
}
