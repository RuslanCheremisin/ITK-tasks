package rus.cheremisin;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        //Группируйте заказы по продуктам.
        Map<String, List<Order>> ordersGroupedByProducts = orders.stream().collect(groupingBy(Order::getProduct));

        //Для каждого продукта найдите общую стоимость всех заказов.
        Map<String, Double> productsTotalCosts = orders
                .stream()
                .collect(groupingBy
                        (Order::getProduct, Collectors
                                .summingDouble(Order::getCost)));

        //Отсортируйте продукты по убыванию общей стоимости.
        Map <String, Double> sortedProductsByTotalCosts = productsTotalCosts
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        //Выберите три самых дорогих продукта.
        List<String> topThreeProductsByCost = orders
                .stream()
                .sorted(Comparator.comparing(Order::getCost).reversed())
                .limit(3)
                .map(o -> o.getProduct()).toList();
        //Выведите результат: список трех самых дорогих продуктов и их общая стоимость.
        List<Order> topThreeOrdersByCost = orders
                .stream()
                .sorted(Comparator.comparing(Order::getCost).reversed())
                .limit(3).toList();
        double sum = topThreeOrdersByCost
                .stream()
                .map(Order::getCost)
                .reduce(0d, Double::sum);

        String topThreeProductsByCostStr = topThreeOrdersByCost
                .stream()
                .map(Order::getProduct)
                .reduce("", (acc, pr) -> pr + "\n" +  acc);
        System.out.println(topThreeProductsByCostStr + "\n" + "total cost is: " + sum);
    }
}

class Order {
    private String product;
    private double cost;

    public Order(String product, double cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public double getCost() {
        return cost;
    }
}