
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static final ArrayList<Product> products = new ArrayList<>();
    static ArrayList<Product> cart = new ArrayList<>();
    static int totalPrice = 0;

    private static void displayList(Scanner sc) {
        if (products.isEmpty()) {
            System.out.println("No products available for ordering.");
            return;
        }

        System.out.println("Available products for order:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }

        while (true) {
            System.out.print("Enter the product Name to order (or 'done' to finish): ");
            String orderProductName = sc.nextLine().trim();

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
                        int productTotalPrice = product.getCost() * quantityOrdered;
                        totalPrice += productTotalPrice;

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

                        System.out.println("Cart after adding products: " + cart);
                        System.out.println("Added " + quantityOrdered + " " + product.getName() + "(s) to the cart. Total Price for this product: " + productTotalPrice);

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
        System.out.print("Your are ready to cancel the order?(Yes/No):");

        String productToCancel = sc.nextLine().trim();
        if (productToCancel.equalsIgnoreCase("yes")) {
            System.out.println("\n--- Order Summary ---");
            System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Product Name", "Quantity", "Unit Price", "Total Quantity");
            System.out.println("-------------------------------------------------------------");
            int sno = 1;
            for (Product prod : cart) {

                System.out.printf("%-5d %-20s %-10d %-10d %-10d%n", sno++, prod.getName(), prod.getTotalQuantity(), prod.getCost(), prod.getTotalQuantity());

            }
            System.out.println();
            System.out.print("Enter product name to remove from cart: ");
            String prodName = sc.nextLine().trim();

            boolean productFound = false;
            for (Product prod : cart) {
                if (prod.getName().equalsIgnoreCase(prodName)) {
                    updateQuantity(prod);
                    cart.remove(prod);
                    productFound = true;
                    System.out.println("Product " + prodName + " has been removed from the cart.");
                    System.out.println();
                    break;
                }
            }

            if (!productFound) {
                System.out.println("Product not found in the cart.");
            }

        } else if (productToCancel.equalsIgnoreCase("no")) {
            System.out.println();
            System.out.println("Cancel request denied!");
            System.out.println();
        }

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

        while (true) {
            System.out.println("1. Direct Payment");
            System.out.println("2. EMI");
            System.out.println("3. Down Payment + EMI");
            System.out.println();
            System.out.println("Enter payment Option (1 - 3)");
            int paymentChoice = sc.nextInt();
            sc.nextLine();
            switch (paymentChoice) {
                case 1 ->
                    proceedToPayment(sc, totalPrice);
                case 2 ->
                    availEMI(sc);
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

        System.out.println("The Total Bill Amount: " + totalPrice);

        int downPaymentAmount = downPayment(sc);
        totalPrice = totalPrice - downPaymentAmount;

        if (totalPrice == 0) {
            System.out.println("Payment done successfully!");
        }
        System.out.println("Remaining Balance: Rs. " + totalPrice);
        availEMI(sc);

    }

    private static int downPayment(Scanner sc) {
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
    //Here we used return once the condition satisfy if not satisfied the loop runs continuously.
    //Handling varies based on the statement we use either if-else or switch case;

    private static void proceedToPayment(Scanner sc, int totalPrice) {
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

    //Note For Undersstanding
    // when in use return in yes/no case, the method will be terminated on reaching return. if any logics resides after switch case will not be executed!
    //allow the loop to case to continue.
    //once yes case executed, the switch statement ends, encounterss break statement and terminate the loop.
    //We can add "return" here to leave method after yes case but.......
    // terminates the method after executing availEMI Method [Not ideal if there is further statements to be executed in this same method as return terminate/end the method]
    private static void availEMI(Scanner sc) {

        System.out.println("Please Select the Tenure :");
        System.out.println("1. 6 Months");
        System.out.println("2. 12 Months");
        System.out.println("3. 24 Months");
        System.out.println("4. 36 Months");
        System.out.println();
        int months = 0;
        while (true) {

            System.out.print("Enter the choice of Tenure (1 - 4): ");
            int choiceMonth = sc.nextInt();
            sc.nextLine();

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
                    continue; //Skips the further part of the loop... since we have while true it again starts new iteration
                }
            }

            break;
        }

        double emiAmount = calculateEMI(months);
        System.out.println();
        System.out.println("You have chosen " + months + " months EMI.");
        System.out.println("Your monthly EMI is: Rs. " + (int) emiAmount);
        System.out.println("Total payment over " + months + " months: Rs. " + (int) (emiAmount * months));
        System.out.println("Thank You!");
        System.out.println();

    }

    private static double calculateEMI(int months) {
        double interestRate = 0.01;
        double monthlyRate = interestRate / 12;
        double emiAmount = (totalPrice * monthlyRate * Math.pow(1 + monthlyRate, months))
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
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Product Name", "Quantity", "Unit Price", "Total Price");
        System.out.println("-------------------------------------------------------------");

        int sno = 1;
        for (Product product : cart) {
            int totalProductPrice = product.getCost() * product.getTotalQuantity();
            System.out.printf("%-5d %-20s %-10d %-10d %-10d%n", sno++, product.getName(), product.getTotalQuantity(), product.getCost(), totalProductPrice);
        }

        System.out.println("-------------------------------------------------------------");
        System.out.println("Subtotal (In Rs): " + totalPrice);

        if (totalPrice > 50000) {
            int discountedPrice = availDiscountPrice(totalPrice);
            System.out.println("Congratulations! You are qualified for a Discount.");
            System.out.println("Discounted Total(In Rs): " + discountedPrice);
            totalPrice = totalPrice - discountedPrice;
        }

        System.out.println("Final Total (In Rs): " + totalPrice);
        System.out.println();
    }

    private static void updateQuantity(Product product) {

        for (Product prods : products) {
            if (prods.getName().equalsIgnoreCase(product.getName())) {
                prods.setTotalQuantity(prods.getTotalQuantity() + product.getTotalQuantity());
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
                    case 3 -> {
                        displayCancelList(sc);
                    }
                    case 4 ->
                        cashPayment(sc, totalPrice);
                    case 5 -> {
                        {
                            return;
                        }
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
