package com.orderbook;

import com.orderbook.domain.Order;
import com.orderbook.domain.OrderBook;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        var orderBook = new OrderBook();
        var order = new Order(1, 234.23, 'B', 21);
        var order1 = new Order(2, 21.23, 'B', 21);
        var order2 = new Order(3, 34.45, 'B', 21);
        var order3 = new Order(4, 25.267, 'B', 21);
        var order4 = new Order(5, 25.267, 'B', 21);
        var order5 = new Order(51, 25.267, 'B', 21);


        var order6 = new Order(6, 234.23, 'O', 21);
        var order7= new Order(7, 21.23, 'O', 21);
        var order8 = new Order(8, 34.45, 'O', 21);
        var order9 = new Order(9, 25.267, 'O', 21);
        var order10 = new Order(10, 25.267, 'O', 21);
        var order11 = new Order(11, 25.267, 'O', 22);

        orderBook.addOrder(order);
        orderBook.addOrder(order1);
        orderBook.addOrder(order2);
        orderBook.addOrder(order3);
        orderBook.addOrder(order4);
        orderBook.addOrder(order5);
        orderBook.addOrder(order6);
        orderBook.addOrder(order7);
        orderBook.addOrder(order8);
        orderBook.addOrder(order9);
        orderBook.addOrder(order10);
        orderBook.addOrder(order11);

        orderBook.removeOrder(order.getId());
        orderBook.removeOrder(order10.getId());
        orderBook.removeOrder(order8.getId());

        orderBook.changeOrderSize(order9.getId(), 24);
        orderBook.changeOrderSize(order3.getId(), 23);

        double level1BidsPrice = orderBook.getPriceForSideLevel('B', 1);
        double level1OfferPrice = orderBook.getPriceForSideLevel('O', 1);

        System.out.println("Level 1 bids price " + level1BidsPrice);
        System.out.println("Leve 1 offer price " + level1OfferPrice);

        Optional<Long> totalSize = orderBook.getTotalSizeForSideLevel('B', 2);

        System.out.println("Total size at level 2: " + totalSize);

        var allOrders = orderBook.getAllOrdersFromSide('B');
        System.out.println("All bid orders: " + allOrders);

    }
}
