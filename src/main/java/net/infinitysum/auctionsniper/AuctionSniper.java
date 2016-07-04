package net.infinitysum.auctionsniper;

/**
 * Created by Kevin on 04/07/2016.
 */
public class AuctionSniper implements AuctionEventListener {

    private final SniperListener sniperListener;

    private final Auction auction;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    public void currentPrice(int price, int increment) {
        auction.bid(price + increment);
        sniperListener.sniperBidding();

    }
}
