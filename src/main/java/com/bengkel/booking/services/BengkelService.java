package com.bengkel.booking.services;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Motorcyle;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.BookingOrderRepository;

public class BengkelService {

	//Silahkan tambahkan fitur-fitur utama aplikasi disini

	//Login
    public static Customer loginMenu(List<Customer> listCustomer){
        int attempt = 0;
        while(attempt < 3){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input your Customer ID : ");
            String id = scanner.nextLine();
            System.out.println("Input your Password");
            String password = scanner.nextLine();

            boolean idFound = false;
            for(Customer customer : listCustomer){
                if(customer.getCustomerId().equals(id)){
                    idFound = true;
                    if(customer.getPassword().equals(password)){
                        System.out.println("Your are successfully Login! Welcome " + customer.getName() + " to Bengkel Apps");
                        return customer;
                    } else {
                        System.out.println("Password yang anda masukan salah, Silahkan coba lagi!");
                        break;
                    }
                }
            }

            if(!idFound){
                System.out.println("Customer Id tidak ditemukan atau salah, Silahkan Coba lagi!");
            }

            attempt += 1;

            if(attempt == 3){
                System.out.println("Anda telah mencoba login sebanyak tiga kali. Aplikasi akan berhenti (Exit)!");
                System.exit(0);
            }
        }

        return null;
    }

	//Info Customer
    public static void showCustomerInfo(Customer customers, List<Customer> listCustomer){
        String id = customers.getCustomerId();
        for(Customer customer : listCustomer){
            if(customer.getCustomerId().equals(id)){
                PrintService.showCustomerDetail(customer);
                System.out.println("List Kendaraan: ");
                PrintService.showCustomerVehicles(customer);
            }
        }
    }

	//Booking atau Reservation
    public static void bookingBengkel(Customer customers,List<Customer> listCustomer,List<ItemService> listItemServices){
        Scanner scanner = new Scanner(System.in);
        String id = customers.getCustomerId();
        for(Customer customer : listCustomer){
            if(customer.getCustomerId().equals(id)){
                PrintService.showCustomerVehicles(customer);
                Vehicle kendaraan = null;
                while(true){
                    System.out.println("Input Vehicle Id : ");
                    String kendaraanId = scanner.nextLine();
                    for(Vehicle vehicle : customer.getVehicles()){
                        if(vehicle.getVehiclesId().equals(kendaraanId)){
                            kendaraan = vehicle;
                            break;
                        }
                    }
                    if(kendaraan != null){
                        break;
                    }else{
                        System.out.println("Kendaraan tidak ditemukan. Silahkan coba lagi!");
                    }
                }

                List<ItemService> availableServices = new ArrayList<>();
                for(ItemService services : listItemServices){
                    if((kendaraan instanceof Car && services.getVehicleType().equals("Car")) ||
                        (kendaraan instanceof Motorcyle && services.getVehicleType().equals("Motorcyle"))){
                        availableServices.add(services);
                        System.out.println("Service yang tersedia adalah : " + services.getServiceName() + " dengan harga : Rp." + services.getPrice());
                    }
                }

                int minServices = 1;
                int maxServices = (customer instanceof MemberCustomer) ? 2 : 1;
                List<ItemService> chosenServices = new ArrayList<>();
                for(int i=0; i<maxServices; i++){
                    while(true){
                        System.out.println("Choose service :(ex: Ganti Oli) ");
                        if(i >= minServices){
                            System.out.println("Tidak ingin menambah service? Ketik Enter.");
                        }
                        String serviceName = scanner.nextLine();
                        if(i >= minServices && serviceName.isEmpty()){
                            break;
                        }
                        ItemService chosenService = null;
                        for(ItemService service : availableServices){
                            if(service.getServiceName().equals(serviceName)){
                                chosenService = service;
                                break;
                            }
                        }
                        if(chosenService != null){
                            chosenServices.add(chosenService);
                            break;
                        }else{
                            System.out.println("Service tidak ditemukan, silakan coba lagi.");
                        }
                    }
                }

                double totalCost = 0;
                for(ItemService service : chosenServices){
                    totalCost += service.getPrice();
                }

                String paymentMethod = "";
                if(customer instanceof MemberCustomer){
                    while(true){
                        System.out.println("Choose Payment Method (1. Saldo Coin, 2. Cash): ");
                        int paymentMethodChoice = scanner.nextInt();
                        if(paymentMethodChoice == 1 || paymentMethodChoice == 2){
                            paymentMethod = (paymentMethodChoice == 1) ? "Saldo Coin" : "Cash";
                            break;
                        }else{
                            System.out.println("Pilihan tidak valid, silakan coba lagi.");
                        }
                    }
                    if(paymentMethod.equals("Saldo Coin")){
                        MemberCustomer memberCustomer = (MemberCustomer) customer;
                        double discountedCost = totalCost * 0.9;
                        if(memberCustomer.getSaldoCoin() < discountedCost){
                            System.out.println("Saldo Coin tidak mencukupi");
                            return;
                        }
                        memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() - discountedCost);
                    }
                } else {
                    paymentMethod = "Cash";
                }

                BookingOrder bookingOrder = new BookingOrder();
                int orderCount = BookingOrderRepository.getAllBookingOrder().size();
                bookingOrder.setBookingId(String.format("Svcm-%03d", orderCount + 1));
                bookingOrder.setCustomer(customer);
                bookingOrder.setServices(chosenServices);
                bookingOrder.setPaymentMethod(paymentMethod);
                bookingOrder.setTotalServicePrice(totalCost);

                bookingOrder.calculatePayment();

                BookingOrderRepository.getAllBookingOrder().add(bookingOrder);

                System.out.println("Total biaya service: " + bookingOrder.getTotalPayment());
            }
        }
    }

	//Top Up Saldo Coin Untuk Member Customer
    public static void topUpSaldoCoin(Customer customers, List<Customer> listCustomers){
        Scanner scanner = new Scanner(System.in);
        String id = customers.getCustomerId();
        for(Customer customer : listCustomers){
            if(customer.getCustomerId().equals(id)){
                if(customer instanceof MemberCustomer){
                    System.out.println("Please input the amount of coins you'd like to add to your balance: ");
                    double amount = scanner.nextDouble();
                    MemberCustomer memberCustomer = (MemberCustomer) customer;
                    memberCustomer.setSaldoCoin(memberCustomer.getSaldoCoin() + amount);
                    System.out.println("Top up saldo coin berhasil. Saldo coin Anda sekarang: " + memberCustomer.getSaldoCoin());
                }else{
                    System.out.println("Maaf, fitur ini hanya dapat digunakan oleh Member saja!");
                }
                break;
            }
        }
    }

    //Booking Order
    public static void showBookOrders(Customer customers, List<BookingOrder> listBookOrder){
        String id = customers.getCustomerId();
        String formatTable = "| %-15s | %-15s | %-30s | %-15s | %-15s |%n";
        String line = "+====================================================================================================================+%n";
        System.out.format(line);
        System.out.format(formatTable, "BOOKING ID", "CUSTOMER NAME", "LIST SERVICE", "PAYMENT METHOD", "TOTAL PAYMENT");
        System.out.format(line);
        for(BookingOrder order : listBookOrder){
            if(order.getCustomer().getCustomerId().equals(id)){
                String services = order.getServices().stream()
                    .map(ItemService::getServiceName)
                    .collect(Collectors.joining(", "));
                System.out.format(formatTable, order.getBookingId(), order.getCustomer().getName(), services, order.getPaymentMethod(), order.getTotalPayment());
            }
        }
        System.out.printf(line);
    }

    //Logout

}
