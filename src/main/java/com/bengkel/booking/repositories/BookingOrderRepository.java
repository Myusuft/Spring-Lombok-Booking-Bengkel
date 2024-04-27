package com.bengkel.booking.repositories;

import java.util.ArrayList;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
public class BookingOrderRepository {
    private static List<BookingOrder> listAllBookOrders = new ArrayList<>();

    public static List<BookingOrder> getAllBookingOrder(){
        return listAllBookOrders;
    }
}
