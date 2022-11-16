package com.orderbook.helper;

import java.util.LinkedList;
import java.util.List;

// data structure used to keep prices in order
public class PriceBook {

    // Linked list - keep elements in order
    // Insert / Remove - O(log n) - use binary search in order to compute the right index
    // the list is ordered (ascending / descending - depending on the side property)
    private final LinkedList<Double> book = new LinkedList<>();
    private final Character side;

    public PriceBook(Character side) {
        this.side = side;
    }

    public void addPrice(double price) {
        int insertIndex = searchIndex(price);
        if (insertIndex > 0 && insertIndex < book.size() && book.get(insertIndex) == price) {
            return;
        }
        book.add(insertIndex, price);
    }

    public void removePrice(double price) {
        int removeIndex = searchIndex(price);
        if (book.get(removeIndex) == price) {
            book.remove(removeIndex);
        }
    }

    public int searchIndex(double price) {
        int start = 0;
        int end = book.size() - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (book.get(mid) == price) {
                return mid;
            } else {
                if (searchLeft(mid, price)) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
            }
        }

        return end + 1;
    }

    private boolean searchLeft(int mid, double price) {
        if (side == 'O' && book.get(mid) > price) {
            return true;
        } else {
            return side == 'B' && book.get(mid) < price;
        }
    }

    public List<Double> getPricesList() {
        return book;
    }

    public double getPriceAtLevel(int level) {
        if (book.size() >= level) {
            return book.get(level - 1);
        } else {
            return -1;
        }
    }

}
