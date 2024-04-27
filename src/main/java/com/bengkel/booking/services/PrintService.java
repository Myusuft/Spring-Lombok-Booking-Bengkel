package com.bengkel.booking.services;

import java.util.List;
import java.util.stream.Collectors;

import com.bengkel.booking.models.*;

public class PrintService {

	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";

		System.out.printf("%-25s %n", title);
		System.out.println(line);

		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}

    public static void showCustomerDetail(Customer customer) {
        String formatTable = "| %-15s | %-15s | %-30s | %-15s | %-15s |%n";
        String line = "+------------------------------------------------------------------------------------------------------------------------------------+%n";
        System.out.format(line);
        System.out.format(formatTable, "CUSTOMER ID", "NAME", "CUSTOMER STATUS", "ADDRESS", "SALDO COIN");
        System.out.format(line);

        String statusCustomer = "";
        String saldoKoin = "";
        if(customer instanceof MemberCustomer){
            statusCustomer = "Member";
            saldoKoin = Double.toString(((MemberCustomer) customer).getSaldoCoin());
        }else if(customer instanceof Customer){
            statusCustomer = "Non Member";
            saldoKoin = "-";
        }

        System.out.format(formatTable, customer.getCustomerId(), customer.getName(), statusCustomer, customer.getAddress(), saldoKoin);
        System.out.printf(line);
    }

    //Silahkan Tambahkan function print sesuai dengan kebutuhan.
    public static void showCustomerVehicles(Customer customer) {
        String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
        String line = "+-----------------------------------------------------------------------------------------------+%n";
        System.out.format(line);
        System.out.format(formatTable, "NO", "VEHICLE ID", "COLOR", "BRAND", "TRANSMISSION", "YEAR", "VEHICLE TYPE");
        System.out.format(line);
        int number = 1;
        String vehicleType = "";
        for(Vehicle vehicle : customer.getVehicles()) {
            if (vehicle instanceof Car) {
                vehicleType = "Car";
            } else {
                vehicleType = "Motor";
            }
            System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(), vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
            number++;
        }
        System.out.printf(line);
    }
}
