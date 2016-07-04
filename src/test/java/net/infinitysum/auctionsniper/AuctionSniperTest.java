package net.infinitysum.auctionsniper;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;

/**
 * Created by Kevin on 29/06/2016.
 */
public class AuctionSniperTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    private final Mockery context = new Mockery();
    private final SniperListener sniperListener = context.mock(SniperListener.class);

    private final Auction auction = context.mock(Auction.class);

    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);




    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception{
        auctionServer.startSellingItem();
        application.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();
        application.showsSniperHasLostAuction();

    }

    @Test
    public void reportsLostWhenAuctionCloses() {
        context.checking(new Expectations() {{
            one(sniperListener).sniperLost();
        }});

        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        context.checking(new Expectations() {{
            one(auction).bid(price + increment);
            atLeast(1).of(sniperListener).sniperBidding();
        }});
    }



    @After
    public void stopAuction(){
        auctionServer.stop();
    }

    @After
    public void stopApplication(){
        application.stop();
    }
}
