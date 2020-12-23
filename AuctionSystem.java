import java.io.*;
import java.util.Scanner;


public class AuctionSystem implements Serializable {
    /**
     * auctionTable is the table we'll use to showcase the auction and it's
     * data
     *
     * username is the name that the user will give him/herself
     */
    AuctionTable auctionTable = new AuctionTable();
    String username;

    /**
     * The method should first prompt the user for a username.
     * This should be stored in username The rest of the program will be
     * executed on behalf of this user. Implement the following menu options:
     *
     * (D) - Import Data from URL
     * (A) - Create a New Auction
     * (B) - Bid on an Item
     * (I) - Get Info on Auction
     * (P) - Print All Auctions
     * (R) - Remove Expired Auctions
     * (T) - Let Time Pass
     * (Q) - Quit
     */
    public static void main(String[] args) throws IOException {
        AuctionSystem auctionSystem = new AuctionSystem();
        Scanner h = new Scanner(System.in);
        System.out.println("Starting...");
        try {
            // Checking to see if an auctionTable and it's data exist already
            FileInputStream file = new FileInputStream("auction.obj");
            ObjectInputStream inStream = new ObjectInputStream(file);

            auctionSystem.auctionTable = (AuctionTable) inStream.readObject();
            System.out.println("Loading previous Auction Table...");
        }
        catch (Exception e){
            System.out.println("No previous auction table detected.");
            System.out.println("Creating new table...");
        }

        System.out.print("\nPlease select a username: ");
        auctionSystem.username = h.nextLine();
        String letter = "";

        //loop the menu until the user types q
        while (!letter.equalsIgnoreCase("q")) {
            System.out.println("\n(D) - Import Data from URL\n" +
                    "(A) - Create a New Auction\n" +
                    "(B) - Bid on an Item\n" +
                    "(I) - Get Info on Auction\n" +
                    "(P) - Print All Auctions\n" +
                    "(R) - Remove Expired Auctions\n" +
                    "(T) - Let Time Pass\n" +
                    "(Q) - Quit\n");
            System.out.print("Please select an option: ");
            letter = h.nextLine().toUpperCase();
            try {
                if (letter.equalsIgnoreCase("d")) {
                    System.out.print("Please enter a URL: ");
                    String url = h.nextLine().trim();
                    System.out.println("Loading...");
                    auctionSystem.auctionTable =
                            AuctionTable.buildFromURL(url);
                    System.out.print("Auction data loaded successfully!\n");
                }
                else if (letter.equalsIgnoreCase("a")) {
                    System.out.println("\nCreating new Auction as " +
                            auctionSystem.username + ".");
                    System.out.print("Please enter an Auction ID: ");
                    String id = h.nextLine();
                    System.out.print("Please enter an Auction time (hours): ");
                    int hours = h.nextInt();
                    System.out.print("Please enter some Item Info: ");
                    String info = h.next();
                    h.nextLine();
                    System.out.println();
                    Auction auction = new Auction(id, auctionSystem.username,
                            hours, info);
                    auctionSystem.auctionTable.putAuction
                            (auction.getAuctionID(), auction);
                    System.out.print("Auction " + auction.getAuctionID() +
                            " inserted into table.\n");
                }
                else if (letter.equalsIgnoreCase("b")) {
                    double bid = 0;
                    System.out.print("Please enter an Auction ID: ");
                    String auctionID = h.nextLine();
                    if (auctionSystem.auctionTable.getAuction(auctionID).
                            getTimeRemaining() != 0){
                        System.out.print("Auction " + auctionID +
                                " is OPEN\n");
                        System.out.print("\t Current Bid: $ " + auctionSystem.
                                auctionTable.getAuction(auctionID).
                                getCurrentBid() + "\n\n");
                        System.out.print("What would you like to bid?: ");
                        bid = h.nextDouble();
                        auctionSystem.auctionTable.getAuction(auctionID).
                                newBid(auctionSystem.username, bid);
                        System.out.print("Bid accepted.\n");
                    }
                    else {
                        auctionSystem.auctionTable.getAuction(auctionID).
                                newBid(auctionSystem.username, bid);
                    }
                    h.nextLine();
                }
                else if (letter.equalsIgnoreCase("i")) {
                    System.out.print("Please enter an Auction ID: ");
                    String auctionID = h.nextLine();

                    System.out.print("Auction " + auctionID + ":\n");
                    System.out.print("\t Seller: " + auctionSystem.
                            auctionTable.getAuction(auctionID).
                            getSellerName() + "\n");
                    System.out.print("\t Buyer: " + auctionSystem.
                            auctionTable.getAuction(auctionID).getBuyerName() +
                            "\n");
                    System.out.print("\t Time: " + auctionSystem.
                            auctionTable.getAuction(auctionID).
                            getTimeRemaining() + " hours\n");
                    System.out.print("\t Info: " + auctionSystem.
                            auctionTable.getAuction(auctionID).getItemInfo() +
                            "\n");
                }
                else if (letter.equalsIgnoreCase("p")) {
                    auctionSystem.auctionTable.printTable();
                }
                else if (letter.equalsIgnoreCase("r")) {
                    if (auctionSystem.auctionTable != null) {
                        System.out.print("Removing expired auctions....\n");
                        auctionSystem.auctionTable.removeExpiredAuctions();
                        System.out.print("All expired auctions removed.\n");
                    }
                }
                else if (letter.equalsIgnoreCase("t")) {
                    System.out.print("How many hours should pass: ");
                    int hours = h.nextInt();
                    System.out.println("Time passing...");
                    auctionSystem.auctionTable.letTimePass(hours);
                    System.out.print("Auction times updated.\n");
                    h.nextLine();
                }
            }
            catch (Exception e){
                System.out.print(e.getMessage());
            }
        }
        System.out.println("\nWriting Auction Table to file...");
        // Writes the auction table and information into a file
        FileOutputStream file = new FileOutputStream("auction.obj");
        ObjectOutputStream outStream = new ObjectOutputStream(file);
        outStream.writeObject(auctionSystem.auctionTable);
        System.out.println("Done!\n");
        System.out.print("Goodbye.");
    }
}
