package sample.cafekiosk.unit;

import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class CafeKiosk {

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(final Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(final Beverage beverage, final int count) {
        if (count <= 0) throw new IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.");

        for (int i = 0; i < count; i++) beverages.add(beverage);
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        return beverages.stream().flatMapToInt(it -> IntStream.of(it.getPrice())).sum();
    }

    public Order createOrder() {
        return new Order(LocalDateTime.now(), beverages);
    }
}
