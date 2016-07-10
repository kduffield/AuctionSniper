package net.infinitysum.auctionsniper;

import com.objogate.exception.Defect;

/**
 * Created by Kevin on 04/07/2016.
 */
public class AuctionSniper implements AuctionEventListener {

    private SniperSnapshot snapshot;
    private final SniperListener sniperListener;

    private final Auction auction;
    private final String itemId;


    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
        this.itemId = itemId;
        this.auction = auction;
        this.sniperListener = sniperListener;
        this.snapshot = SniperSnapshot.joining(itemId);
        //notifyChange();
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
            case FromOtherBidder:
                int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }
        notifyChange();

    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }

    public enum SniperState {
        JOINING {
            @Override
            public SniperState whenAuctionClosed() {
                return LOST;
            }
        },
        BIDDING {
            @Override
            public SniperState whenAuctionClosed() {
                return LOST;
            }
        },
        WINNING {
            @Override
            public SniperState whenAuctionClosed() {
                return WON;
            }
        },
        LOST,
        WON;

        public SniperState whenAuctionClosed() {
            throw new Defect("Auction is already closed");
        }
    }
}
