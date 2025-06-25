package com.ff.commandes_service.service;

import com.ff.commandes_service.dto.ClientResponseDto;
import com.ff.commandes_service.dto.CommandeDetailsResponse;
import com.ff.commandes_service.dto.OrderRequest;
import com.ff.commandes_service.dto.ProductResponseDto;
import com.ff.commandes_service.entity.Orders;
import com.ff.commandes_service.entity.OrderStatus;
import com.ff.commandes_service.feignClient.Client;
import com.ff.commandes_service.feignClient.ProduitClient;
import com.ff.commandes_service.rabbitmq.OrderEventPublisher;
import com.ff.commandes_service.rabbitmq.events.OrderEvent;
import com.ff.commandes_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OrderRepository orderRepository;
    private final Client client;
    private final ProduitClient produitClient;
    private final OrderEventPublisher publisher;

    public Orders createOrder(OrderRequest orders) {
        // Vérifier si les données de la commande sont valides
        if (orders.getProductId() == null || orders.getUserId() == null || orders.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid order data: Product ID, User ID and Quantity must be provided and valid.");
        }
        // Vérifier si le stock du produit est suffisant
        isStockSufficient(orders.getProductId());
        // Créer une nouvelle commande avec les données fournies
        var orderToSave = Orders.builder()
                .productId(orders.getProductId())
                .userId(orders.getUserId())
                .quantity(orders.getQuantity())
                .totalPrice(orders.getTotalPrice())
                .orderStatus(OrderStatus.PENDING)
                .orderDate(orders.getOrderDate())
                .orderDate(orders.getOrderDate() != null ? orders.getOrderDate() : LocalDateTime.now())
                .build();
        Orders saveOrder = orderRepository.save(orderToSave);
        publisher.publish(new OrderEvent(saveOrder));
        return saveOrder;
    }
    // ne pas oublier la vérifier test Unitaire pour la modification de la méthode
    public CommandeDetailsResponse getOrderById(Long id) {
        Orders orders = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        ProductResponseDto product = produitClient.getProductById(orders.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Product not found for order with id: " + id);
        }
        ClientResponseDto clientDetails = client.getClientById(orders.getUserId());
        return new CommandeDetailsResponse(orders, product, clientDetails);

    }
    // À Vérifier pour le retirement de la méthode
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    // Methode Modifier voir le test unitaire pour la modification de la méthode
    //public List<Orders> getOrdersByStatus(OrderStatus status) {
    public List<CommandeDetailsResponse> getOrdersByStatus(OrderStatus status) {
        // 1. Récupérer toutes les commandes avec le statut donné
        List<Orders> orders = orderRepository.findByOrderStatus(status);

        // 2. Vérifier si des commandes ont été trouvées
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders found with status: " + status);
        }

        // 3. Mapper chaque commande à une CommandeDetailsResponse
        // Chaque commande aura ses propres détails de produit et de client
        return orders.stream()
                .map(order -> {
                    // Récupérer le produit spécifique à cette commande
                    ProductResponseDto product = produitClient.getProductById(order.getProductId());
                    if (product == null) {
                        // Optionnel : gérer le cas où un produit est manquant pour une commande spécifique.
                        // Plutôt que de lancer une exception pour toutes les commandes, on pourrait
                        // logger un avertissement ou inclure une valeur par défaut.
                        // Pour l'exemple, nous allons quand même lancer une exception pour la clarté.
                        throw new IllegalArgumentException("Product not found for order ID: " + order.getId() + " with status: " + status);
                    }

                    // Récupérer le client spécifique à cette commande
                    ClientResponseDto clientDetails = client.getClientById(order.getUserId());
                    if (clientDetails == null) {
                        // Similaire au produit, gérer l'absence du client.
                        throw new IllegalArgumentException("Client not found for order ID: " + order.getId() + " with status: " + status);
                    }

                    // Créer la réponse détaillée pour cette commande
                    return new CommandeDetailsResponse(order, product, clientDetails);
                })
                .toList();
    }
    // Méthode pour mettre à jour le statut d'une commande
    // Methode modifier voir le test unitaire pour la modification de la méthode
    public Optional<CommandeDetailsResponse> updateOrdersByStatus(Long id, OrderStatus orderStatus){
        // Vérifier si l'ID de la commande est valide
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid order ID: " + id);
        }
        // Vérifier si le statut de la commande est valide
        if (orderStatus == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }

        return Optional.of(orderRepository.findById(id).map(c -> {
            if (c.getOrderStatus() == OrderStatus.CANCELLED || c.getOrderStatus() == OrderStatus.RETURNED) {
                throw new IllegalArgumentException("Order cannot be updated to " + orderStatus + " after it has been " + c.getOrderStatus());
            }
            c.setOrderStatus(orderStatus);
            if (orderStatus == OrderStatus.CANCELLED) {
                c.setCancelledDate(LocalDateTime.now());
            } else if (orderStatus == OrderStatus.RETURNED) {
                c.setReturnedDate(LocalDateTime.now());
            }

            ProductResponseDto product = produitClient.getProductById(c.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found for order with id: " + id);
            }
            // Vérifier le stock du produit avant de mettre à jour la commande
            isStockSufficient(c.getProductId());

            // Mettre à jour la commande avec le nouveau statut
            Orders updateOrder = orderRepository.save(c);

            // Récupérer les détails du client
            ClientResponseDto clientDetails = client.getClientById(c.getUserId());
            if (clientDetails == null) {
                throw new IllegalArgumentException("Client not found for order with id: " + id);
            }
            // Retourner la réponse détaillée de la commande
            return new CommandeDetailsResponse(updateOrder, product, clientDetails);
        }).orElseThrow(()-> new IllegalArgumentException("Order not found with id: " + id)));
    }

    private void isStockSufficient(Long productId) {
        // Récupérer le stock du produit
        int stock =produitClient.getProductStockById(productId);
        // Vérifier si le stock est négatif
        if (stock < 0) {
            throw new IllegalArgumentException("Stock for product with id: " + productId + " is negative: " + stock);
        }
    }
}
