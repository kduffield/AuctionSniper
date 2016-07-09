package net.infinitysum.auctionsniper;

/**
 * Created by Kevin on 04/07/2016.
 */
public interface AuctionEventListener {
    enum PriceSource {
        FromSniper, FromOtherBidder;
    }

    ;

    public void auctionClosed();

    void currentPrice(int price, int increment, PriceSource priceSource);
}
