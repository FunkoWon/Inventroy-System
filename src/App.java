import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

public class App implements Runnable {
    private volatile boolean stop = false;
    private static Scanner sc = new Scanner(System.in);
    private static List<Inventory> inventoryList = new ArrayList<>();
    private static List<UserData> accounts = new ArrayList<>(List.of(
            new UserData("alice", "pass1", "Manager"),
            new UserData("bob", "pass2", "Staff"),
            new UserData("charlie", "pass3", "Staff"),
            new UserData("diana", "pass4", "Supervisor"),
            new UserData("eve", "pass5", "Admin")
    ));

    @Override
    public void run() {
        while (!stop) {
            System.out.print("\b*");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMasking() {
        this.stop = true;
    }

    static class UserData {
        String username;
        String password;
        String position;

        public UserData(String username, String password, String position) {
            this.username = username;
            this.password = password;
            this.position = position;
        }
    }

    static class Inventory {
        String itemname;
        int itemquantity;
        double itemprice;

        public Inventory(String itemname, int itemquantity, double itemprice) {
            this.itemname = itemname;
            this.itemquantity = itemquantity;
            this.itemprice = itemprice;
        }
    }

    public static void main(String[] args) throws Exception {
        if (!loadDataFromExcel()) {
            System.out.println("[System] No existing data found. Initializing with defaults...");
            inventoryList.add(new Inventory("item1", 10, 10.0));
            inventoryList.add(new Inventory("item2", 20, 20.0));
            inventoryList.add(new Inventory("item3", 30, 30.0));
            inventoryList.add(new Inventory("item4", 40, 40.0));
            inventoryList.add(new Inventory("item5", 50, 50.0));
            saveDataToExcel();
        } else {
            System.out.println("[System] Data loaded successfully from MyData.xlsx");
        }

        while (true) {
            System.out.println("Welcome to the Inventory System!");
            Thread.sleep(500);
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Username: ");
                String inputUsername = sc.nextLine();

                System.out.print("Password: ");
                App masker = new App();
                Thread thread = new Thread(masker);
                thread.start();
                String inputPassword = sc.nextLine();
                masker.stopMasking();
                thread.join();

                boolean authenticated = false;
                for (UserData account : accounts) {
                    if (inputUsername.toLowerCase().equals(account.username)
                            && inputPassword.equals(account.password)) {
                        authenticated = true;
                        break;
                    }
                }

                if (authenticated) {
                    System.out.println("\nLogin successful!");
                    MainMenu();
                } else {
                    System.out.println("\nInvalid username or password.");
                    Thread.sleep(1000);
                }
            } else if (choice.equals("2")) {
                registerUser();
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
    }

    public static void registerUser() {
        System.out.println("\n--- Register New User ---");
        System.out.print("Enter username: ");
        String username = sc.nextLine().trim().toLowerCase();

        if (username.isEmpty()) {
            System.out.println("Error: Username cannot be empty.");
            return;
        }

        // Check if user already exists
        for (UserData acc : accounts) {
            if (acc.username.equals(username)) {
                System.out.println("Error: Username already exists.");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("Error: Password cannot be empty.");
            return;
        }

        System.out.print("Enter position: ");
        String position = sc.nextLine().trim();
        if (position.isEmpty())
            position = "Staff";

        accounts.add(new UserData(username, password, position));
        System.out.println("User registered successfully!");
        saveDataToExcel();
    }

    public static void MainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Inventory System Menu ---");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Update item");
            System.out.println("4. View item");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear buffer
            } catch (Exception e) {
                sc.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> updateItem();
                case 4 -> viewItems();
                case 5 -> exit = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void addItem() {
        System.out.println("\nAdd Item");
        System.out.print("Enter item name: ");
        String itemName = sc.nextLine();
        System.out.print("Enter item quantity: ");
        int itemQuantity = sc.nextInt();
        System.out.print("Enter item price: ");
        double itemPrice = sc.nextDouble();
        sc.nextLine();

        inventoryList.add(new Inventory(itemName, itemQuantity, itemPrice));
        System.out.println("Item added successfully!");
        saveDataToExcel();
    }

    public static void viewItems() {
        System.out.println("\n--- Current Inventory ---");
        if (inventoryList.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            for (int i = 0; i < inventoryList.size(); i++) {
                Inventory item = inventoryList.get(i);
                System.out.printf("%d. %s | Qty: %d | Price: $%.2f%n",
                        (i + 1), item.itemname, item.itemquantity, item.itemprice);
            }
        }
        System.out.println("-------------------------");
    }

    public static void removeItem() {
        System.out.println("\n--- Remove Item ---");
        if (inventoryList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (int i = 0; i < inventoryList.size(); i++) {
            System.out.println((i + 1) + ". " + inventoryList.get(i).itemname);
        }
        System.out.print("Enter item number to remove: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        if (index >= 0 && index < inventoryList.size()) {
            inventoryList.remove(index);
            System.out.println("Item removed successfully!");
            saveDataToExcel();
        } else {
            System.out.println("Invalid item number.");
        }
    }

    public static void updateItem() {
        System.out.println("\n--- Update Item ---");
        if (inventoryList.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (int i = 0; i < inventoryList.size(); i++) {
            System.out.println((i + 1) + ". " + inventoryList.get(i).itemname);
        }
        System.out.print("Enter item number to update: ");
        int index = sc.nextInt() - 1;
        sc.nextLine();

        if (index >= 0 && index < inventoryList.size()) {
            Inventory item = inventoryList.get(index);
            System.out.print("Enter new quantity: ");
            item.itemquantity = sc.nextInt();
            System.out.print("Enter new price: ");
            item.itemprice = sc.nextDouble();
            sc.nextLine();
            System.out.println("Item updated successfully!");
            saveDataToExcel();
        } else {
            System.out.println("Invalid item number.");
        }
    }

    public static void saveDataToExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet inventorySheet = workbook.createSheet("Inventory");
            Row invHeader = inventorySheet.createRow(0);
            invHeader.createCell(0).setCellValue("Item Name");
            invHeader.createCell(1).setCellValue("Item Quantity");
            invHeader.createCell(2).setCellValue("Item Price");

            for (int i = 0; i < inventoryList.size(); i++) {
                Inventory item = inventoryList.get(i);
                Row row = inventorySheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.itemname);
                row.createCell(1).setCellValue(item.itemquantity);
                row.createCell(2).setCellValue(item.itemprice);
            }

            Sheet userSheet = workbook.createSheet("UserData");
            Row userHeader = userSheet.createRow(0);
            userHeader.createCell(0).setCellValue("Username");
            userHeader.createCell(1).setCellValue("Password");
            userHeader.createCell(2).setCellValue("Position");

            for (int i = 0; i < accounts.size(); i++) {
                Row row = userSheet.createRow(i + 1);
                UserData acc = accounts.get(i);
                row.createCell(0).setCellValue(acc.username);
                row.createCell(1).setCellValue(acc.password);
                row.createCell(2).setCellValue(acc.position);
            }

            try (FileOutputStream fileOut = new FileOutputStream("MyData.xlsx")) {
                workbook.write(fileOut);
            }
            System.out.println("[System] Data synchronized with MyData.xlsx");
        } catch (IOException e) {
            System.err.println("Error saving data to Excel: " + e.getMessage());
        }
    }

    public static boolean loadDataFromExcel() {
        File file = new File("MyData.xlsx");
        if (!file.exists())
            return false;

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet inventorySheet = workbook.getSheet("Inventory");
            if (inventorySheet != null) {
                inventoryList.clear();
                for (int i = 1; i <= inventorySheet.getLastRowNum(); i++) {
                    Row row = inventorySheet.getRow(i);
                    if (row != null) {
                        try {
                            String name = row.getCell(0).getStringCellValue();
                            int qty = (int) row.getCell(1).getNumericCellValue();
                            double price = row.getCell(2).getNumericCellValue();
                            inventoryList.add(new Inventory(name, qty, price));
                        } catch (Exception e) {
                        }
                    }
                }
            }

            Sheet userSheet = workbook.getSheet("UserData");
            if (userSheet != null) {
                List<UserData> loadedUsers = new ArrayList<>();
                for (int i = 1; i <= userSheet.getLastRowNum(); i++) {
                    Row row = userSheet.getRow(i);
                    if (row != null) {
                        try {
                            String user = row.getCell(0).getStringCellValue();
                            String pass = row.getCell(1).getStringCellValue();
                            String pos = (row.getCell(2) != null) ? row.getCell(2).getStringCellValue() : "Staff";
                            loadedUsers.add(new UserData(user, pass, pos));
                        } catch (Exception e) {
                        }
                    }
                }
                if (!loadedUsers.isEmpty()) {
                    accounts = loadedUsers;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}