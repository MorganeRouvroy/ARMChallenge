package src.main.appInterface;

import src.main.sqlUtils.Connector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class Interface extends JFrame {

	private static final long serialVersionUID = 1L;

    private JPanel panel;

    public void init() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Connector.closeCon();
			}
		});
		setBackground(Color.WHITE);
        setTitle("BBA Innovation Challenge App");
		setForeground(Color.WHITE);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 550);

        JPanel outerPane = new JPanel();
        outerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(outerPane);
        outerPane.setLayout(new BorderLayout(3, 3));
		
		panel = new JPanel();
        outerPane.add(panel);
        panel.setMaximumSize(new Dimension(60000, 60000));
		panel.setLayout(new BorderLayout(10, 10));

        panel.add(createTopPanel(), BorderLayout.NORTH);
        panel.add(createMap(), BorderLayout.CENTER);
    }

    JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setMaximumSize(new Dimension(60000, 60000));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        Font font = new Font("Tahoma", Font.PLAIN, 23);
        EmptyBorder border = new EmptyBorder(5, 5, 5, 5);

        JLabel searchLbl = new JLabel("Search for");
        searchLbl.setFont(font);
        topPanel.add(searchLbl);
        searchLbl.setBorder(border);

        JComboBox<String> targetCmbBox = new JComboBox<>();
        targetCmbBox.setFont(font);
        targetCmbBox.setBorder(border);
        topPanel.add(targetCmbBox);
        targetCmbBox.setModel(new DefaultComboBoxModel<>(new String[]{"Hospitals", "Schools"}));

        JLabel rangeLbl = new JLabel("in a range of ");
        rangeLbl.setFont(font);
        topPanel.add(rangeLbl);
        rangeLbl.setBorder(border);

        JTextField txtX = new JTextField();
        txtX.setFont(font);
        topPanel.add(txtX);
        txtX.setBorder(border);
        txtX.setText("X");
        txtX.setColumns(10);

        JLabel kmLbl = new JLabel("km");
        kmLbl.setFont(font);
        topPanel.add(kmLbl);
        kmLbl.setBorder(border);

        JButton findBtn = new JButton("Find");
        findBtn.setFont(font);
        findBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                System.out.print("Query database and add the resulting points to map");
            }
        });
        findBtn.setActionCommand("Find");
        findBtn.setBorder(border);
        topPanel.add(findBtn);

        return topPanel;
    }

    JXMapKit createMap() {
        JXMapKit mapKit = new JXMapKit();
        mapKit.getZoomSlider().setBackground(UIManager.getColor("Button.light"));
        mapKit.getZoomSlider().setForeground(Color.LIGHT_GRAY);
        GridBagLayout gridBagLayout = (GridBagLayout) mapKit.getMainMap().getLayout();
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        gridBagLayout.columnWidths = new int[]{618, 10};
        gridBagLayout.rowWeights = new double[]{1.0};
        gridBagLayout.rowHeights = new int[]{478};
        mapKit.getMainMap().setPanEnabled(true);
        mapKit.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapKit.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(20);

        // Set the focus
        GeoPosition bogota = new GeoPosition(4.710989, -74.072092);
        mapKit.setZoom(7);
        mapKit.setAddressLocation(bogota);

        return mapKit;
    }

}
