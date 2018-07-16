

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sqlUtils.Connecter;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.Dimension;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import java.awt.GridBagLayout;
import javax.swing.border.BevelBorder;
import javax.swing.UIManager;

public class Interface extends JFrame {

	static GraphicsDevice device = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtX;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					device.setFullScreenWindow(frame);
					new Connecter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Connecter.closeCon();
			}
		});
		setBackground(Color.WHITE);
		setTitle("BBA Innovation Challenge App ");
		setForeground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 923, 575);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(3, 3));
		
		panel = new JPanel();
		panel.setMaximumSize(new Dimension(60000, 60000));
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(10, 10));
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(60000, 60000));
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		
		JLabel Search = new JLabel("Search for");
		Search.setFont(new Font("Tahoma", Font.PLAIN, 23));
		panel_1.add(Search);
		Search.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 23));
		comboBox.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel_1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Hospitals", "Schools"}));
		
		JLabel lblNewLabel = new JLabel("in a range of ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 23));
		panel_1.add(lblNewLabel);
		lblNewLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		txtX = new JTextField();
		txtX.setFont(new Font("Tahoma", Font.PLAIN, 23));
		panel_1.add(txtX);
		txtX.setBorder(new EmptyBorder(5, 5, 5, 5));
		txtX.setText("X");
		txtX.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("kms");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 23));
		panel_1.add(lblNewLabel_1);
		lblNewLabel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JButton btnNewButton = new JButton("GO");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 23));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.print("Query database and add the resulting points to map");
			}
		});
		btnNewButton.setActionCommand("GO");
		btnNewButton.setBorder(new EmptyBorder(5, 10, 5, 10));
		panel_1.add(btnNewButton);
		
		JXMapKit mapKit = new JXMapKit();
		mapKit.getZoomSlider().setBackground(UIManager.getColor("Button.light"));
		mapKit.getZoomSlider().setForeground(Color.LIGHT_GRAY);
		GridBagLayout gridBagLayout = (GridBagLayout) mapKit.getMainMap().getLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.columnWidths = new int[]{618, 10};
		gridBagLayout.rowWeights = new double[]{1.0};
		gridBagLayout.rowHeights = new int[]{478};
		mapKit.getMainMap().setPanEnabled(true);
		panel.add(mapKit, BorderLayout.CENTER);
		mapKit.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapKit.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition bogota = new GeoPosition(4.710989, -74.072092);
        mapKit.setZoom(7);
        mapKit.setAddressLocation(bogota);
	}

}
