package com.orderbook.domain;

import com.orderbook.exception.OrderNotFoundException;
import com.orderbook.helper.PriceBook;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


//  Part B
//  Suggestions for making the classes better suited for real-life, latency-sensitive trading operations
//
//  1. Order - size property - AtomicInt
//      This will ensure that there are no race conditions when updating the size of an Order
//
//  2. Thread safe structuresPriceBook structure should be synchronized - it should not allow multiple threads to access / perform modifications at once on one structure
//      Each of the data structures below should be synchronized
//          HashMap<Double, LinkedHashSet<Order>> bids
//          HashMap<Double, LinkedHashSet<Order>> offers
//          PriceBook bidsPriceBook
//          PriceBook bidsPriceBook
//      An operation regarding bid data structure should not block another operation done for offers (these operations can run in parallel)
//
//  3. Add concurrency tests - make sure that all the concurrency issues are solved (multi threads environment)

public class OrderBook {

    // store bids / offers -> insert, update, remove O(1)
    HashMap<Double, LinkedHashSet<Order>> bids = new HashMap<>();
    HashMap<Double, LinkedHashSet<Order>> offers = new HashMap<>();

    // id -> Order - keep order id as an index
    // add / remove - O(1)
    HashMap<Long, Order> orderIdToOrder = new HashMap<>();

    // ordered data structure - bid prices - descending order
    // insert, search - O(log n), remove - O(1)
    PriceBook bidsPriceBook = new PriceBook('B');
    // ordered data structure - offer prices - ascending order
    // insert, search - O(log n), remove - O(1)
    PriceBook offersPriceBook = new PriceBook('O');


    // time complexity: O(log n), n the number of prices for a side
    public void addOrder(Order order) {
        getRightOrderCollection(order.getSide()).computeIfAbsent(order.getPrice(), it -> new LinkedHashSet<>()).add(order);

        PriceBook priceBook = getRightPriceBook(order.getSide());
        priceBook.addPrice(order.getPrice());
        orderIdToOrder.put(order.getId(), order);
    }

    // time complexity: O(log n), n number of prices for a side
    public void removeOrder(long orderId) {
        var order = getOrderById(orderId);
        orderIdToOrder.remove(orderId);

        var ordersCollection = getRightOrderCollection(order.getSide());
        var bookPriceCollection = getRightPriceBook(order.getSide());
        LinkedHashSet<Order> orders = ordersCollection.get(order.getPrice());
        orders.remove(order);
        if (orders.isEmpty()) {
            ordersCollection.remove(order.getPrice());
            bookPriceCollection.removePrice(order.getPrice());
        }
    }

    // time complexity: O(1)
    public void changeOrderSize(long orderId, long newSize) {
        var order = getOrderById(orderId);
        order.setSize(newSize);
    }

    // time complexity: O(1)
    public double getPriceForSideLevel(Character side, int level) {
        return getRightPriceBook(side).getPriceAtLevel(level);
    }

    // time complexity: O(m), m the number of orders at a level -> close to be O(1)
    public Optional<Long> getTotalSizeForSideLevel(Character side, int level) {
        var price = getRightPriceBook(side).getPriceAtLevel(level);
        var orders = getRightOrderCollection(side).get(price);

        return orders.stream().map(Order::getSize).reduce(Long::sum);
    }

    // time complexity: O(n), n the number of orders to be returned
    public List<Order> getAllOrdersFromSide(Character side) {
        var prices = getRightPriceBook(side).getPricesList();
        var ordersMap = getRightOrderCollection(side);

        return prices
                .stream()
                .map(ordersMap::get)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Order getOrderById(Long orderId) {
        if (!orderIdToOrder.containsKey(orderId)) {
            throw new OrderNotFoundException("Order not found for given id!");
        }

        return orderIdToOrder.get(orderId);
    }

    private HashMap<Double, LinkedHashSet<Order>> getRightOrderCollection(Character orderSide) {
        if (orderSide == 'B') {
            return bids;
        } else {
            return offers;
        }
    }

    private PriceBook getRightPriceBook(Character orderSide) {
        if (orderSide == 'B') {
            return bidsPriceBook;
        } else {
            return offersPriceBook;
        }
    }

}
