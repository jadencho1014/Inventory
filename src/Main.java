//imports
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Main {

    //method to scan in int
    static int intScan () {
        Scanner input = new Scanner(System.in);

        while (true) {
            try {
                return Integer.parseInt(input.nextLine());
            }
            catch (Exception e) {
                System.out.println("\nPlease enter a valid integer.");
            }
        }
    }

    //method to scan in long
    static long longScan () {
        Scanner input = new Scanner(System.in);

        while (true) {
            try {
                return Long.parseLong(input.nextLine());
            }
            catch (Exception e) {
                System.out.println("\nPlease enter a valid integer.");
            }
        }
    }

    //method to scan in float
    static float floatScan () {
        Scanner input = new Scanner(System.in);

        while (true) {
            try {
                return Float.parseFloat(input.nextLine());
            }
            catch (Exception e) {
                System.out.println("\nPlease enter a valid real number.");
            }
        }
    }

    //tax method
    static float tax (float total) {
        return (Math.round(total * 113) / 100f);
    }

    //main method
    public static void main(String[] args) throws IOException, UnknownHostException {
        Scanner input = new Scanner(System.in);

        //output IP address
        System.out.println("\nIP address: " + InetAddress.getLocalHost().getHostAddress());

        //create file
        File myFile = new File ("src/inventory.txt");
        myFile.createNewFile();

        //variable declaration
        int numOfItems, count;
        char menu;
        float total;
        String date, temp;

        //declare lists
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<Float> price = new ArrayList<Float>();
        ArrayList<Integer> quantity = new ArrayList<Integer>();
        ArrayList<Long> itemNumber = new ArrayList<Long>();

        //loop until user wants to exit
        while (true) {
            count = 0;

            //empty lists
            name.clear();
            price.clear();
            quantity.clear();
            itemNumber.clear();

            //read from txt file
            Scanner myReader = new Scanner (myFile);

            //read date
            if (myReader.hasNextLine()) {
                date = myReader.nextLine();
            }
            else {
                date = "Inventory is empty.";
            }

            //read item attributes
            while (myReader.hasNextLine()) {
                name.add(myReader.nextLine());
                price.add(Float.parseFloat(myReader.nextLine()));
                quantity.add(Integer.parseInt(myReader.nextLine()));
                itemNumber.add(Long.parseLong(myReader.nextLine()));

                count++;
            }

            myReader.close();

            //display date and menu
            System.out.println("\n" + date + "\n\nChoose an option:\n\ta. add items to the inventory\n\tb. display the inventory\n\tc. sort the inventory alphabetically\n\td. sort the inventory by price\n\te. exit the program");
            menu = input.nextLine().charAt(0);

            //add items
            if (menu == 'a' || menu == 'A') {
                System.out.println("\nHow many items would you like to add? (You have to enter at least 5 if it’s the first time you are loading up the inventory)");

                //do not let user less than 5 items
                while (true) {
                    try {
                        numOfItems = intScan();

                        if (myFile.length() == 0 && numOfItems < 5) {
                            throw new Exception();
                        }

                        break;
                    }
                    catch (Exception e) {
                        System.out.println("\nPlease enter at least 5 items.");
                    }
                }

                //item attributes input
                for (int i = 0; i < numOfItems; i++) {
                    System.out.println("\nEnter the name of item " + (count + 1) + ".");
                    name.add(input.nextLine());

                    System.out.println("\nEnter the price of item " + (count + 1) + ".");
                    price.add(floatScan());

                    System.out.println("\nEnter the quantity of item " + (count + 1) + ".");
                    quantity.add(intScan());

                    System.out.println("\nEnter the number for item " + (count + 1) + ".");

                    //only let user enter 10 digit numbers
                    while (true) {
                        try {
                            itemNumber.add(longScan());

                            if (String.valueOf(itemNumber.get(count)).length() != 10) {
                                itemNumber.remove(count);

                                throw new Exception();
                            }

                            break;
                        }
                        catch (Exception e) {
                            System.out.println("\nPlease enter a 10 digit number.");
                        }
                    }

                    //prevent user from entering the same item more than once
                    for (int j = 0; j < count; j++) {
                        if (itemNumber.get(count).longValue() == itemNumber.get(j).longValue()) {
                            System.out.println("\nAn item of that number already exists. Would you like to update it? (y/n)");
                            menu = input.nextLine().charAt(0);

                            //update item
                            if (menu == 'y' || menu == 'Y') {
                                name.set(j, name.get(count));
                                price.set(j, price.get(count));
                                quantity.set(j, quantity.get(count));
                            }

                            name.remove(count);
                            price.remove(count);
                            quantity.remove(count);
                            itemNumber.remove(count);

                            count--;

                            break;
                        }
                    }

                    count++;
                }

                //update date
                date = "Last updated: " + java.time.LocalDate.now().toString();

                //write to text file
                FileWriter myWriter = new FileWriter("inventory.txt");

                myWriter.write(date + "\n");

                for (int i = 0; i < count; i++) {
                    myWriter.write(name.get(i) + "\n" + Float.toString(price.get(i)) + "\n" + Integer.toString(quantity.get(i)) + "\n" + Long.toString(itemNumber.get(i)) + "\n");
                }

                myWriter.close();
            }

            //display inventory
            else if (menu == 'b' || menu == 'B') {
                total = 0f;

                //table
                System.out.println("\n+---------+--------+---------+------------+----------+");
                System.out.printf("%-10s", "|Item");
                System.out.printf("%-9s", "|Price");
                System.out.printf("%-10s", "|Quantity");
                System.out.printf("%-13s", "|Item Number");
                System.out.println("|Subtotal  |\n+---------+--------+---------+------------+----------+");

                for (int i = 0; i < count; i++) {
                    System.out.printf("%-10s", "|" + name.get(i));
                    System.out.printf("%-9s", "|" + price.get(i));
                    System.out.printf("%-10s", "|" + quantity.get(i));
                    System.out.printf("%-13s", "|" + itemNumber.get(i));
                    System.out.printf("%-11s", "|" + Math.round((price.get(i) * quantity.get(i)) * 100.0) / 100.0);
                    System.out.println("|");

                    total = price.get(i) * quantity.get(i) + total;
                }

                System.out.println("+---------+--------+---------+------------+----------+\n\nTotal: $" + tax(total));
            }

            //sort alphabetically
            else if (menu == 'c' || menu == 'C') {
                for (int i = 0; i < count - 1; i++) {
                    for (int j = 0; j < count - 1 - i; j++) {
                        if (name.get(j).compareToIgnoreCase(name.get(j + 1)) > 0) {
                            temp = name.get(j);
                            name.set(j, name.get(j + 1));
                            name.set((j + 1), temp);

                            temp = price.get(j).toString();
                            price.set(j, price.get(j + 1));
                            price.set((j + 1), Float.parseFloat(temp));

                            temp = quantity.get(j).toString();
                            quantity.set(j, quantity.get(j + 1));
                            quantity.set((j + 1), Integer.parseInt(temp));

                            temp = itemNumber.get(j).toString();
                            itemNumber.set(j, itemNumber.get(j + 1));
                            itemNumber.set((j + 1), Long.parseLong(temp));
                        }
                    }
                }

                //write to text file
                FileWriter myWriter = new FileWriter("inventory.txt");

                myWriter.write(date + "\n");

                for (int i = 0; i < count; i++) {
                    myWriter.write(name.get(i) + "\n" + Float.toString(price.get(i)) + "\n" + Integer.toString(quantity.get(i)) + "\n" + Long.toString(itemNumber.get(i)) + "\n");
                }

                myWriter.close();
            }

            //sort by price
            else if (menu == 'd' || menu == 'D') {
                for (int i = 0; i < count - 1; i++) {
                    for (int j = 0; j < count - 1 - i; j++) {
                        if (price.get(j) < price.get(j + 1)) {
                            temp = name.get(j);
                            name.set(j, name.get(j + 1));
                            name.set((j + 1), temp);

                            temp = price.get(j).toString();
                            price.set(j, price.get(j + 1));
                            price.set((j + 1), Float.parseFloat(temp));

                            temp = quantity.get(j).toString();
                            quantity.set(j, quantity.get(j + 1));
                            quantity.set((j + 1), Integer.parseInt(temp));

                            temp = itemNumber.get(j).toString();
                            itemNumber.set(j, itemNumber.get(j + 1));
                            itemNumber.set((j + 1), Long.parseLong(temp));
                        }
                    }
                }

                //write to text file
                FileWriter myWriter = new FileWriter("inventory.txt");

                myWriter.write(date + "\n");

                for (int i = 0; i < count; i++) {
                    myWriter.write(name.get(i) + "\n" + Float.toString(price.get(i)) + "\n" + Integer.toString(quantity.get(i)) + "\n" + Long.toString(itemNumber.get(i)) + "\n");
                }

                myWriter.close();
            }
            else {
                try {
                    if (menu != 'e' && menu != 'E') {
                        throw new Exception();
                    }

                    //exit the program
                    break;
                }

                //let the user enter again
                catch (Exception e) {
                    System.out.println("\nPlease choose one of the options.");
                }
            }
        }

        System.out.println("Thank you for using the program.");
    }
}