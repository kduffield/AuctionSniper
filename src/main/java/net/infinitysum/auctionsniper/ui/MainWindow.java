package net.infinitysum.auctionsniper.ui;

import net.infinitysum.auctionsniper.SniperSnapshot;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Kevin on 03/07/2016.
 */
public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    private static final String SNIPERS_TABLE_NAME = "Auction Sniper Table";
    // private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        super(APPLICATION_TITLE);
        this.snipers = snipers;

        setName(MAIN_WINDOW_NAME);
        //add(sniperStatus);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

//    private static JLabel createLabel(String initialText){
//        JLabel result = new JLabel(initialText);
//        result.setName(SNIPER_STATUS_NAME);
//        result.setBorder(new LineBorder(Color.BLACK));
//        return result;
//    }


    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        snipers.sniperStateChanged(sniperSnapshot);


    }

}
