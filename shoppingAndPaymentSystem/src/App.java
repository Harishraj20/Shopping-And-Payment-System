
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class App {

    private static final ArrayList<Product> products = new ArrayList<>();
    static ArrayList<Product> cart = new ArrayList<>();

    private static void displayList(Scanner sc) {
        if (products.isEmpty()) {
            System.out.println("No products available for ordering.");
            return;
        }

        System.out.println("Available products for order:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }

        addproducts(sc);
    }

    private static void addproducts(Scanner sc) {

        while (true) {
            System.out.print("Enter the product Name to order (or 'done' to finish): ");

            String orderProductName = sc.nextLine().trim();
            if (!orderProductName.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid input! Please enter a valid product Name.");
                continue;
            }

            if (orderProductName.equalsIgnoreCase("done")) {
                System.out.println();
                System.out.println("Products added to the Cart Successfully!!");
                System.out.println();
                break;
            }
            boolean productFound = false;

            for (Product product : products) {
                if (product != null && product.getName().equalsIgnoreCase(orderProductName)) {
                    productFound = true;
                    System.out.println("Product is available: " + product);

                    System.out.print("Enter the quantity to order: ");
                    int quantityOrdered = sc.nextInt();
                    sc.nextLine();
                    if (quantityOrdered > 0 && quantityOrdered <= product.getTotalQuantity()) {
                        product.reduceQuanity(quantityOrdered);

                        boolean productInCart = false;
                        for (Product prod : cart) {
                            if (prod.getName().equals(product.getName())) {
                                prod.setTotalQuantity(prod.getTotalQuantity() + quantityOrdered);
                                productInCart = true;
                                break;
                            }
                        }

                        if (!productInCart) {
                            Product cartProduct = new Product(product.getName(), product.getCost(), quantityOrdered);
                            cart.add(cartProduct);
                        }
                        System.out.println("Added " + quantityOrdered + " " + product.getName()
                                + "(s) to the cart.");
                    } else {
                        System.out.println("Not Enough Stock.");
                    }
                    break;
                }
            }
            if (!productFound) {
                System.out.println("Product not available.");
            }
        }
    }

    private static void displayCancelList(Scanner sc) {
        if (cart.isEmpty()) {
            System.out.println();
            System.out.println("No products available in the cart cancellation.");
            System.out.println();
            return;
        }
        System.out.print("Are u sure to Modify Cart? (Yes/No): ");
        String productToCancel = sc.nextLine().trim();

        switch (productToCancel.toLowerCase()) {
            case "yes" -> {
                processCancellation(sc);
                break;
            }
            case "no" -> {
                System.out.println();
                System.out.println("Cancel request denied!");
                System.out.println();
                break;
            }
            default ->
                System.out.println("Enter a valid response, please! (Yes/No)");

        }

    }

    private static void processCancellation(Scanner sc) {
        int quantityToRemove;
        System.out.println("\n--- Order Summary ---");
        System.out.printf("%-5s %-20s %-10s %-10s %n", "S.No", "Product Name", "Quantity", "Unit Price");
        System.out.println("-------------------------------------------------------------");
        int sno = 1;
        for (Product prod : cart) {

            System.out.printf("%-5d %-20s %-10d %-10d %n", sno++, prod.getName(), prod.getTotalQuantity(),
                    prod.getCost());

            System.out.println();
            while (true) {
                System.out.print("Enter product name to remove from cart: ");
                String prodName = sc.nextLine().trim();

                if (!prodName.matches("[a-zA-Z ]+")) {
                    System.out.println("Enter valid product Name from the Cart");
                    continue;
                }
                boolean productFound = false;

                Iterator<Product> it = cart.iterator();

                while (it.hasNext()) {
                    Product prods = it.next();
                    if (prods.getName().equalsIgnoreCase(prodName)) {
                        productFound = true;

                        while (true) {

                            System.out.print("Enter number of quantities to remove: ");

                            try {
                                quantityToRemove = sc.nextInt();
                                sc.nextLine();
                            } catch (InputMismatchException e) {
                                System.out.println("Please enter a valid number for quantity!");
                                sc.nextLine();
                                continue;
                            }

                            if (quantityToRemove > 0 && quantityToRemove <= prods.getTotalQuantity()) {
                                prods.reduceQuanity(quantityToRemove);
                                updateQuantity(prod, quantityToRemove);

                                if (prods.getTotalQuantity() == 0) {
                                    it.remove();
                                    System.out.println();
                                    System.out.println("The Product removed successfully from the cart!");
                                    System.out.println();
                                    break;
                                } else {
                                    System.out.println(quantityToRemove + " " + prods.getName()
                                            + "(s) have been removed from the cart!");
                                }
                            } else {
                                System.out.println("Quantity to remove is greater than product's quantity in cart!");
                                continue;
                            }
                            break;
                        }
                    }
                    break;
                }
                if (!productFound) {
                    System.out.println("Product not found in the cart.");
                }
                break;

            }
        }

    }

    private static int calculateTotalPrice() {
        int totalPrice = 0;
        for (Product product : cart) {
            int totalProductPrice = product.getCost() * product.getTotalQuantity();
            totalPrice += totalProductPrice;
        }
        return totalPrice;
    }

    private static void cashPayment(Scanner sc, int totalPrice) {
        if (cart.isEmpty()) {

            System.out.println("Empty Cart. Payment cannot be processed.");
            System.out.println("-------------------------------------------------------------");
            System.out.println();
            return;
        }
        System.out.print("Choose payment Mode:");
        System.out.println();

        System.out.println("1. Direct Payment");
        System.out.println("2. EMI");
        System.out.println("3. Down Payment + EMI");
        System.out.println();

        while (true) {
            System.out.print("Enter payment Option (1 - 3): ");
            int paymentChoice;

            try {
                paymentChoice = sc.nextInt();
                sc.nextLine(); // This consumes new Line in the buffer when user enters valid input format. For
                // example user enters 1 and press enter 1<enter> will be added in buffer [1,/n]
                // nextLine in this will consume the /n in the buffer making it buffer empty
                // afterwards.....

            } catch (InputMismatchException e) {
                System.out.println("Enter valid input..");
                sc.nextLine(); // consumes the new Line in the buffers else cause the while loop to execute
                // with newLine created due to enter press....Using this clear the buffer Hence
                // buffer expects user to enter input and execution happends!
                continue;
            }
            switch (paymentChoice) {
                case 1 ->
                    proceedToPayment(sc, totalPrice);
                case 2 ->
                    availEMI(sc, totalPrice);
                case 3 ->
                    DownPaymentWithEMI(sc);
                default -> {
                    System.out.println("Invalid choice. Please enter valid choice between (1 - 3)");
                    continue; // if invalid input continue the loop without moving further
                }

            }
            break;

        }
    }

    private static void DownPaymentWithEMI(Scanner sc) {
        int totalPrice = calculateTotalPrice();

        System.out.println("The Total Bill Amount: " + totalPrice);

        int downPaymentAmount = downPayment(sc);
        totalPrice = totalPrice - downPaymentAmount;

        if (totalPrice == 0) {
            System.out.println("Payment done successfully!");
        }
        System.out.println("Remaining Balance: Rs. " + totalPrice);
        availEMI(sc, totalPrice);

    }

    private static int downPayment(Scanner sc) {
        int totalPrice = calculateTotalPrice();
        while (true) {
            System.out.print("Enter the Down Payment Amount: ");

            int downAmount = sc.nextInt();
            sc.nextLine();

            if (downAmount > 0 && downAmount <= totalPrice) {
                return downAmount;
            } else {
                System.out.println("Enter a valid payment amount between 1 and " + totalPrice);
            }
        }
    }
    // Here we used return once the condition satisfy if not satisfied the loop runs
    // continuously.
    // Handling varies based on the statement we use either if-else or switch case;

    private static void proceedToPayment(Scanner sc, int totalPrice) {
        System.out.println("Total Bill Amount: " + totalPrice);
        System.out.print("Please Enter the Cash for Payment:");
        int cash = sc.nextInt();
        sc.nextLine();
        if (cash >= totalPrice) {
            System.out.println();
            System.out.println("payment succesful!");
            System.out.println();
            System.out.println("Remaining Balance(In Rs): " + (cash - totalPrice));
            System.out.println();
            cart.clear();
        } else {
            System.out.println("Insufficient Amount Entered!!");
            System.out.println("Entered Cash: " + cash);
            System.out.println("Total Price: " + totalPrice);
            System.out.println();
        }

    }

    // Note For Undersstanding
    // when in use return in yes/no case, the method will be terminated on reaching
    // return. if any logics resides after switch case will not be executed!
    // allow the loop to case to continue.
    // once yes case executed, the switch statement ends, encounterss break
    // statement and terminate the loop.
    // We can add "return" here to leave method after yes case but.......
    // terminates the method after executing availEMI Method [Not ideal if there is
    // further statements to be executed in this same method as return terminate/end
    // the method]
    private static void availEMI(Scanner sc, int price) {

        System.out.println("Please Select the Tenure :");
        System.out.println("1. 6 Months");
        System.out.println("2. 12 Months");
        System.out.println("3. 24 Months");
        System.out.println("4. 36 Months");
        System.out.println();
        int months = 0;
        while (true) {

            System.out.print("Enter the choice of Tenure (1 - 4): ");
            int choiceMonth;
            try {
                choiceMonth = sc.nextInt();
                sc.nextLine();

            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Invalid input! Please enter between (1 - 4)");
                continue;
            }

            switch (choiceMonth) {
                case 1 ->
                    months = 6;
                case 2 ->
                    months = 12;
                case 3 ->
                    months = 24;
                case 4 ->
                    months = 36;
                default -> {
                    System.out.println("Select choice from the List!");
                    continue; // Skips the further part of the loop... since we have while true it again
                    // starts new iteration
                }
            }

            break;
        }

        double emiAmount = calculateEMI(months, price);
        System.out.println();
        System.out.println("You have chosen " + months + " months EMI.");
        System.out.println("Your monthly EMI is: Rs. " + (int) emiAmount);
        System.out.println("Total payment over " + months + " months: Rs. " + (int) (emiAmount * months));
        System.out.println("Thank You!");
        System.out.println();
        cart.clear();

    }

    private static double calculateEMI(int months, int billPrice) {
        double interestRate = 0.01;
        double monthlyRate = interestRate / 12;
        double emiAmount = (billPrice * monthlyRate * Math.pow(1 + monthlyRate, months))
                / (Math.pow(1 + monthlyRate, months) - 1);
        return emiAmount;
    }

    private static int availDiscountPrice(int totalPrice) {
        double discountVal = 0.02;
        double discountedPrice = totalPrice * discountVal;

        return (int) discountedPrice;
    }

    private static void cartSummary() {
        if (cart.isEmpty()) {
            System.out.println();
            System.out.println("Your cart is currently empty. Add some products to proceed.");
            System.out.println();
            return;
        }

        System.out.println("\n--- Order Summary ---");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Product Name", "Quantity", "Unit Price",
                "Total Price");
        System.out.println("-------------------------------------------------------------");

        int sno = 1;

        for (Product product : cart) {
            int totalProductPrice = product.getCost() * product.getTotalQuantity();
            System.out.printf("%-5d %-20s %-10d %-10d %-10d%n", sno++, product.getName(), product.getTotalQuantity(),
                    product.getCost(), totalProductPrice);
        }

        System.out.println("-------------------------------------------------------------");
        int billAmount = calculateTotalPrice();
        System.out.println("Subtotal (In Rs): " + calculateTotalPrice());

        if (billAmount > 50000) {
            int discountedPrice = availDiscountPrice(billAmount);
            System.out.println("Congratulations! You are qualified for a Discount.");
            System.out.println("Discounted Amount(In Rs): " + discountedPrice);
            billAmount = billAmount - discountedPrice;
        }

        System.out.println("Final Total (In Rs): " + billAmount);
        System.out.println();
    }

    private static void updateQuantity(Product product, int Quantity) {

        for (Product prods : products) {
            if (prods.getName().equalsIgnoreCase(product.getName())) {
                prods.setTotalQuantity(prods.getTotalQuantity() + Quantity);
                break;
            }
        }
    }

    public static void main(String[] args) {

        products.add(new Product("Laptop", 65000, 10));
        products.add(new Product("Phone", 25000, 25));
        products.add(new Product("Tablet", 53000, 15));
        products.add(new Product("Monitor", 105000, 30));
        products.add(new Product("Keyboard", 1500, 50));
        products.add(new Product("Mouse", 2100, 100));
        products.add(new Product("Printer", 15000, 12));
        products.add(new Product("Speakers", 8000, 20));
        products.add(new Product("Headphones", 1200, 40));
        products.add(new Product("Smartwatch", 2000, 15));
        cart.add(new Product("Speakers", 8000, 10));
        cart.add(new Product("Headphones", 1200, 5));
        cart.add(new Product("Smartwatch", 2000, 3));

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("1. Add Products To Cart");
            System.out.println("2. Cart Summary");
            System.out.println("3. Remove Products From Cart");
            System.out.println("4. Proceed to Payment");
            System.out.println("5. Exit");

            System.out.print("Enter any number between (1 - 5): ");
            try {
                int num = Integer.parseInt(sc.nextLine());

                switch (num) {
                    case 1 ->
                        displayList(sc);
                    case 2 ->
                        cartSummary();
                    case 3 ->
                        displayCancelList(sc);
                    case 4 ->
                        cashPayment(sc, calculateTotalPrice());
                    case 5 -> {
                        return;
                    }
                    default ->
                        System.out.println("Invalid Number!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }

        }
    }

}
