import java.io.Serializable;
import java.util.*;
import big.data.*;

public class AuctionTable extends HashMap<String, Auction> implements
        Serializable {

    /**
     * Uses the BigData library to construct an AuctionTable from a
     * remote data source.
     * @param URL - the remote data source
     * @return The AuctionTable constructed from the remote data source.
     * @throws IllegalArgumentException if the URL does not represent a valid
     * datasource (can't connect or invalid syntax).
     */
    public static AuctionTable buildFromURL(String URL) throws
            IllegalArgumentException {
        if (!URL.startsWith("http") && !URL.endsWith(".xml")){
            throw new IllegalArgumentException("Bad url");
        }
        DataSource ds = DataSource.connect(URL).load();

        String[] sellerName = ds.fetchStringArray("listing/seller_info" +
                "/seller_name");
        String[] currentBid = ds.fetchStringArray("listing/auction_info" +
                "/current_bid");
        String[] timeLeft = ds.fetchStringArray("listing/auction_info" +
                "/time_left");
        String[] idNum = ds.fetchStringArray("listing/auction_info/" +
                "id_num");
        String[] bidderName = ds.fetchStringArray("listing/auction_info" +
                "/high_bidder/bidder_name");
        String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
        String[] memory = ds.fetchStringArray("listing/item_info/memory");
        String[] hardDrive = ds.fetchStringArray("listing/item_info/" +
                "hard_drive");

        AuctionTable auctionTable = new AuctionTable();

        for (int h = 0; h < sellerName.length; h++){
            String[] splitTimeLeft = timeLeft[h].split(",");
            int total = 0;

            for (int d = 0; d < splitTimeLeft.length; d++){
                if (splitTimeLeft[d].trim().contains("day")){
                    total += Integer.parseInt(splitTimeLeft[d].trim().
                            replaceAll("[^0-9]", "")) * 24;
                }
                if (splitTimeLeft[d].contains("hr") ||
                        splitTimeLeft[d].contains("hour")){
                    total += Integer.parseInt(splitTimeLeft[d].trim().
                            replaceAll("[^0-9]", ""));
                }
            }

            if (idNum[h].isEmpty()){
                idNum[h] = "N/A";
            }
            if (currentBid[h].isEmpty()) {
                currentBid[h] = "N/A";
            }
            if (bidderName[h].isEmpty()){
                bidderName[h] = "N/A";
            }
            if (cpu[h].isEmpty()){
                cpu[h] = "N/A";
            }
            if (memory[h].isEmpty()){
                memory[h] = "N/A";
            }
            if (hardDrive[h].isEmpty()){
                hardDrive[h] = "N/A";
            }

            Auction auction;

            if (!memory[h].equalsIgnoreCase("n/a") &&
                    !hardDrive[h].equalsIgnoreCase("n/a")) {
                auction = new Auction(idNum[h],
                        Double.parseDouble(currentBid[h].
                                replaceAll("[$,]+", "")),
                        sellerName[h], bidderName[h], total,
                        cpu[h] + " - " + memory[h].substring(0, 5) +
                                " - " + hardDrive[h].substring(0, 5));
            }
            else {
                auction = new Auction(idNum[h],
                        Double.parseDouble(currentBid[h].
                                replaceAll("[$,]+", "")),
                        sellerName[h], bidderName[h], total,
                        cpu[h] + " - " + memory[h] +
                                " - " + hardDrive[h]);
            }
            auctionTable.put(auction.getAuctionID(), auction);
        }
        return auctionTable;
    }

    /**
     * Manually posts an auction, and add it into the table.
     * @param auctionID - the unique key for this object
     * @param auction - The auction to insert into the table with the
     * corresponding auctionID
     * @throws IllegalArgumentException If the given auctionID is already
     * stored in the table.
     */
    public void putAuction(String auctionID, Auction auction)
            throws IllegalArgumentException {
        if (this.containsKey(auctionID)){
            throw new IllegalArgumentException("AuctionID already exists.");
        }
        else {
            this.put(auctionID, auction);
        }
    }

    /**
     * Get the information of an Auction that contains the given ID as key
     * @param auctionID - the unique key for this object
     * @return An Auction object with the given key, null otherwise.
     */
    public Auction getAuction(String auctionID) {
        return this.get(auctionID);
    }

    /**
     * Simulates the passing of time. Decrease the timeRemaining of all Auction
     * objects by the amount specified. The value cannot go below 0.
     * @param numHours - the number of hours to decrease the timeRemaining
     * value by.
     * @throws IllegalArgumentException If the given numHours is non positive
     */
    public void letTimePass(int numHours) throws IllegalArgumentException {
        if (numHours < 0){
            throw new IllegalArgumentException("Hours cannot be negative");
        }
        else {
            Set<String> keys = this.keySet();
            for (String key : keys) {
                if (this.get(key).getTimeRemaining() < numHours) {
                    this.get(key).timeRemaining = 0;
                }
                else {
                    this.get(key).decrementTimeRemaining(numHours);
                }
            }
        }
    }

    /**
     * Iterates over all Auction objects in the table and removes them if they
     * are expired (timeRemaining == 0).
     */
    public void removeExpiredAuctions() {
        this.values().removeIf(auction -> auction.getTimeRemaining() == 0);
    }

    /**
     * Prints the AuctionTable in tabular form.
     */
    public void printTable(){
        String p1 = "Auction ID |    Bid  |        Seller         " +
                "|          Buyer          |    Time   |  Item Info" + "\n";
        String p2 = "=======================================================" +
                "===========================================================" +
                "===============================\n";
        StringBuilder p3 = new StringBuilder();
        Set<String> keys = this.keySet();
        for (String key : keys) {
            p3.append(String.format("%-11s%-11s%-24s%-27s%-13s%-27s%n",
                    this.get(key).getAuctionID(),
                    " $ " + this.get(key).getCurrentBid(),
                    this.get(key).getSellerName(),
                    this.get(key).getBuyerName(),
                    this.get(key).getTimeRemaining() + " hours",
                    this.get(key).getItemInfo()));
        }
        System.out.println(p1 + p2 + p3);
    }
}
