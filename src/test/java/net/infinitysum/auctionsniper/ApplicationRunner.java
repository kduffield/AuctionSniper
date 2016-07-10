package net.infinitysum.auctionsniper;

import net.infinitysum.auctionsniper.ui.Main;
import net.infinitysum.auctionsniper.ui.MainWindow;

import static net.infinitysum.auctionsniper.AuctionSniper.SniperState.*;
import static net.infinitysum.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import static net.infinitysum.auctionsniper.ui.SnipersTableModel.textFor;

/**
 * Created by Kevin on 29/06/2016.
 */
public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@localhost/Auction";
    private AuctionSniperDriver driver;

    private String itemId;


    public void startBiddingIn(final FakeAuctionServer auction) {
        System.out.println("Starting bidding for " + auction.getItemId());
        itemId = auction.getItemId();

        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
        driver.showsSniperStatus(itemId, 0, 0, textFor(JOINING));
//        driver.showsSniperStatus(Main.STATUS_JOINING);

    }

    public void showsSniperHasLostAuction() {
        //TODO properly deal with the lastprice and lastbid

        driver.showsSniperStatus(itemId, 0, 0, textFor(LOST));
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, textFor(BIDDING));
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, textFor(WINNING));
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, textFor(WON));
    }
}
