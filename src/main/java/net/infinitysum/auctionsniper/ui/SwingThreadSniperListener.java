package net.infinitysum.auctionsniper.ui;

import net.infinitysum.auctionsniper.SniperListener;
import net.infinitysum.auctionsniper.SniperSnapshot;

import javax.swing.*;

/**
 * Created by Kevin on 10/07/2016.
 */


public class SwingThreadSniperListener implements SniperListener {
    private final SniperListener delegate;

    public SwingThreadSniperListener(SniperListener delegate) {
        this.delegate = delegate;
    }

    public void sniperStateChanged(final SniperSnapshot snapshot) {
        {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    delegate.sniperStateChanged(snapshot);
                }
            });
        }
    }


}