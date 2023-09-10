package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(final OrderCreateRequest request, final LocalDateTime registeredDateTime) {
        final List<String> productNumbers = request.getProductNumbers();
        final List<Product> products = findProductsBy(productNumbers);

        deductStockQuantities(products);

        Order order = Order.create(products, registeredDateTime);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(final List<Product> products) {
        final List<String> stockProductNumbers = extractStockProdutNumbers(products);

        final Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        final Map<String, Long> productCountingMap = createCountingMayBy(stockProductNumbers);

        for (final String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            final Stock stock = stockMap.get(stockProductNumber);
            final int quantity = productCountingMap.get(stockProductNumber).intValue();

            validateStockQuantity(stock, quantity);

            stock.deductQuantity(quantity);
        }
    }

    private void validateStockQuantity(final Stock stock, final int quantity) {
        if (stock.isQuantityLessThen(quantity)) {
            throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
        }
    }

    @NotNull
    private List<String> extractStockProdutNumbers(final List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<String, Stock> createStockMapBy(final List<String> stockProductNumbers) {
        final List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }

    @NotNull
    private Map<String, Long> createCountingMayBy(final List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

    @NotNull
    private List<Product> findProductsBy(final List<String> productNumbers) {
        final List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);
        final Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }
}
