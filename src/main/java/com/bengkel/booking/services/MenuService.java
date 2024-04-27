package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.BookingOrder;

import com.bengkel.booking.repositories.BookingOrderRepository;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
    private static List<BookingOrder> listAllBookOrders = BookingOrderRepository.getAllBookingOrder();
	private static Scanner input = new Scanner(System.in);
	public static void run() {
        boolean isLooping = true;
        Customer loggedInCustomer = null;
        do {
            System.out.println("Aplikasi Booking Bengkel");
            System.out.println("1. Login");
            System.out.println("0. Exit");
            System.out.print("Pilihan: ");

            String choice = input.nextLine();

            if ("1".equals(choice)) {
                loggedInCustomer = login();
                if (loggedInCustomer != null) {
                    mainMenu(loggedInCustomer);
                }
            } else if ("0".equals(choice)) {
                System.out.println("Terima kasih telah menggunakan aplikasi kami. Sampai jumpa!");
                System.exit(0);
            } else {
                System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (isLooping);

	}

	public static Customer login() {
        return BengkelService.loginMenu(listAllCustomers);
	}

	public static void mainMenu(Customer loggedInCustomer) {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;

		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);

			switch (menuChoice) {
			case 1:
				//panggil fitur Informasi Customer
                BengkelService.showCustomerInfo(loggedInCustomer, listAllCustomers);
				break;
			case 2:
				//panggil fitur Booking Bengkel
                BengkelService.bookingBengkel(loggedInCustomer, listAllCustomers, listAllItemService);
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
                BengkelService.topUpSaldoCoin(loggedInCustomer, listAllCustomers);
				break;
			case 4:
				//panggil fitur Informasi Booking Order
                BengkelService.showBookOrders(loggedInCustomer, listAllBookOrders);
				break;
			default:
				System.out.println("Logout");
				isLooping = false;
				break;
			}
		} while (isLooping);


	}

	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
