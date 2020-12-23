import java.io.Serializable;

public class Auction implements Serializable {
    /**
     * timeRemaining is the time in hours
     * currentBid is the current bid that's on the auction
     * auctionID is the number or id associated with the auction
     * sellerName is the name of the auction seller
     * buyerName is the name of the buyer
     * itemInfo is the information about the item
     */
    int timeRemaining;
    double currentBid;
    String auctionID;
    String sellerName;
    String buyerName;
    String itemInfo;

    /**
     * Auction Constructor with parameters
     * @param auctionID - argument used for auctionID
     * @param currentBid - argument used for currentBid
     * @param sellerName - argument used for sellerName
     * @param buyerName - argument used for buyerName
     * @param timeRemaining - argument used for timeRemaining
     * @param itemInfo - argument used for itemInfo
     */
    public Auction(String auctionID, double currentBid, String sellerName,
                   String buyerName, int timeRemaining, String itemInfo){
        this.auctionID = auctionID;
        this.currentBid = currentBid;
        this.timeRemaining = timeRemaining;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.itemInfo = itemInfo;
    }

    /**
     * Second Auction Constructor with parameters
     * @param auctionID - argument used for auctionID
     * @param sellerName - argument used for sellerName
     * @param timeRemaining - argument used for timeRemaining
     * @param itemInfo - argument used for itemInfo
     */
    public Auction(String auctionID, String sellerName, int timeRemaining,
                   String itemInfo){
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.timeRemaining = timeRemaining;
        this.itemInfo = itemInfo;
    }

    /**
     * @return the timeRemaining
     */
    public int getTimeRemaining(){
        return timeRemaining;
    }

    /**
     * @return the currentBid
     */
    public double getCurrentBid() {
        return currentBid;
    }

    /**
     * @return the auctionID
     */
    public String getAuctionID(){
        return auctionID;
    }

    /**
     * @return the sellerName
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * @return the buyerName
     */
    public String getBuyerName(){
        return buyerName;
    }

    /**
     * @return the itemInfo
     */
    public String getItemInfo(){
        return itemInfo;
    }

    /**
     * Decreases the time remaining for this auction by the specified amount.
     * If time is greater than the current remaining time for the auction, then
     * the time remaining is set to 0 (i.e. no negative times).
     *
     * timeRemaining has been decremented by the indicated amount and is
     * greater than or equal to 0.
     *
     * @param time - the amount of time we're decreasing by
     */
    public void decrementTimeRemaining(int time){
        if (getTimeRemaining() >= 0) {
            if (time > timeRemaining) {
                timeRemaining = 0;
            }
            else{
                timeRemaining -= time;
            }
        }
    }

    /**
     * Makes a new bid on this auction. If bidAmt is larger than currentBid,
     * then the value of currentBid is replaced by bidAmt and buyerName is is
     * replaced by bidderName.
     *
     * The auction is not closed (i.e. timeRemaining > 0).
     *
     * @param bidderName - buyerName will be replaced by this name
     * @param bidAmt - currentBid will be replaced by this amount
     * @throws ClosedAuctionException if the auction is closed (timeRemaining
     * = 0)
     */
    public void newBid(String bidderName, double bidAmt) throws
            ClosedAuctionException{
        if (timeRemaining == 0){
            throw new ClosedAuctionException("Auction " + auctionID + " is " +
                    "CLOSED\n \t Current Bid: $ " + currentBid + "\n\n " +
                    "You can no longer bid on this item.\n");
        }
        else {
            if (bidAmt > currentBid) {
                currentBid = bidAmt;
                buyerName = bidderName;
            }
        }
    }

    /**
     * @return a string of data members in tabular form
     */
    public String toString(){
        String p1 = "Auction ID |    Bid  |        Seller         " +
                "|          Buyer          |    Time   |  Item Info" + "\n";
        String p2 = "=======================================================" +
                "===========================================================" +
                "=================\n";

        String p3 = getAuctionID() + "   $ " + getCurrentBid() + "     " +
                        getSellerName() + "       " + getBuyerName() + "      "
                        + getTimeRemaining() + " hours" + "   " +
                        getItemInfo();
        return p1 + p2 + p3;
    }
}