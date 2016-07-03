package net.infinitysum.auctionsniper;

import org.junit.After;
import org.junit.Test;

/**
 * Created by Kevin on 29/06/2016.
 */
public class AuctionSniperTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception{
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showsSniperHasLostAuction();

    }

    @After
    public void stopAuction(){
        auction.stop();
    }

    @After
    public void stopApplication(){
        application.stop();
    }
}
