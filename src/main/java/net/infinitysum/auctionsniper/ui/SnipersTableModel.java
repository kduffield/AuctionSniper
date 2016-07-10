package net.infinitysum.auctionsniper.ui;

import net.infinitysum.auctionsniper.AuctionSniper;
import net.infinitysum.auctionsniper.SniperListener;
import net.infinitysum.auctionsniper.SniperSnapshot;

import javax.swing.table.AbstractTableModel;

/**
 * Created by Kevin on 09/07/2016.
 */
public class SnipersTableModel extends AbstractTableModel implements SniperListener {

    private final static String[] STATUS_TEXT = {
            "JOINING",
            "BIDDING",
            "WINNING",
            "LOST",
            "WON"
    };
    // private String statusText = STATUS_JOINING;

    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, AuctionSniper.SniperState.JOINING);
    private SniperSnapshot snapshot = STARTING_UP;
    // private String state;

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(AuctionSniper.SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }


    public enum Column {
        ITEM_IDENTIFIER("Item") {
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.itemId;
            }
        },
        LAST_PRICE("Last Price") {
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastPrice;
            }
        },
        LAST_BID("Last Bid") {
            public Object valueIn(SniperSnapshot snapshot) {
                return snapshot.lastBid;
            }
        },
        SNIPER_STATE("State") {
            public Object valueIn(SniperSnapshot snapshot) {
                return SnipersTableModel.textFor(snapshot.state);
            }
        };
        public final String name;

        private Column(String name) {
            this.name = name;
        }

        public static Column at(int offset) {
            return values()[offset];
        }

        abstract public Object valueIn(SniperSnapshot snapshot);
    }

}
