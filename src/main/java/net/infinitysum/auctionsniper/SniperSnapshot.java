package net.infinitysum.auctionsniper;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Created by Kevin on 09/07/2016.
 */
public class SniperSnapshot {

    public final String itemId;
    public final int lastPrice;
    public final int lastBid;
    public final AuctionSniper.SniperState state;


    public SniperSnapshot(String itemId, int lastPrice, int lastBid, AuctionSniper.SniperState newSniperState) {
        this.itemId = itemId;
        this.lastPrice = lastPrice;
        this.lastBid = lastBid;
        this.state = newSniperState;
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public SniperSnapshot bidding(int newLastPrice, int newLastBid) {
        return new SniperSnapshot(itemId, newLastPrice, newLastBid, AuctionSniper.SniperState.BIDDING);
    }

    public SniperSnapshot winning(int newLastPrice) {
        return new SniperSnapshot(itemId, newLastPrice, lastBid, AuctionSniper.SniperState.WINNING);
    }


    public static SniperSnapshot joining(String itemId) {
        return new SniperSnapshot(itemId, 0, 0, AuctionSniper.SniperState.JOINING);
    }

    public SniperSnapshot closed() {
        return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
    }
}
