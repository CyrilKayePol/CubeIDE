
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;

import javax.swing.*;

import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import lexer.TextLineNumber;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

public class Cube extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel, topPanel1, topPanel2;
	private JLabel topPanel1Labels[] = new JLabel[4];
	private JLabel topPanel2Labels[] = new JLabel[4];
	private ImageIcon topPanel2Icons[] = new ImageIcon[4];
	private String topPanel1Names[] = {"File", "Developers", "CubePiler", "Help"};
	
	private Icon icons[] = new ImageIcon[4];
	private String iconPaths[] = {"src/images/new_enter.png", "src/images/open_enter.png", "src/images/save_enter.png", "src/images/close_enter.png"};
	
	private String topPanel2IconNames[] = {"src/images/open.png", "src/images/new.png", "src/images/save.png", "src/images/close.png"};
	private JPanel filePanels[] = new JPanel[8];
	private Handler handler;

	private JTextPane textPane;
	private JScrollPane scrollPane;
	private TextLineNumber tln;

	private FileSystemView fileSystemView;
	private DefaultTreeModel treeModel;
	private JTree tree;
	private JWindow win;
	private JPanel centerPanel;
	
	public Cube() {
		this.setLayout(null);
		this.setBackground(Color.GRAY);			

		init();
		createSidePanel();
		//createTopPanel();
		createCenterPanel();
	}

	public JMenuBar createMenuBar(){
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem("New file", icons[0]);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));		
		menu.add(menuItem);

		menuItem = new JMenuItem("Open file", icons[1]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);		

		menuItem = new JMenuItem("Save", icons[2]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);		

		menuItem = new JMenuItem("Save as...", icons[2]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);										

		menu = new JMenu("Developers");		
		menuBar.add(menu);

		menu = new JMenu("Help");		
		menuBar.add(menu);

		return menuBar;
	}
	
	private void init() {
		sidePanel = new JPanel();
		topPanel1 = new JPanel();
		topPanel2 = new JPanel();
		centerPanel = new JPanel();
		handler = new Handler();
		
		for(int a = 0;a<4;a++) {
			topPanel1Labels[a] = new JLabel(topPanel1Names[a]);
			topPanel2Icons[a] = new ImageIcon(topPanel2IconNames[a]);
			topPanel2Labels[a] = new JLabel(topPanel2Icons[a]);
		}
		
		for(int i = 0; i < icons.length; i++){
			icons[i] = new ImageIcon(iconPaths[i]);
		}

	}
	
	private void createSidePanel() {
		sidePanel.setPreferredSize(new Dimension(200, 700));
		sidePanel.setBounds(2,0,220,700);			
		
		fileSystemView = FileSystemView.getFileSystemView();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
		
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
            }            
        };
                             
     // show the file system roots.
        File f = new File(".");
        String parentPath = f.getAbsolutePath();        
        int index = parentPath.lastIndexOf("\\");
        
        File parentFile = new File(parentPath.substring(0, index));
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(parentFile);
        root.add(parentNode);
        File[] roots = fileSystemView.getFiles(f, true);        
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            parentNode.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
            //
        }

        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(treeSelectionListener);
        tree.setCellRenderer(new FileTreeCellRenderer());
        tree.expandRow(0);
        JScrollPane treeScroll = new JScrollPane(tree);
        
        tree.setVisibleRowCount(15);
        
        Dimension preferredSize = treeScroll.getPreferredSize();
        Dimension widePreferred = new Dimension(
            210,
            650);
        treeScroll.setPreferredSize( widePreferred );
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeScroll,null);
        sidePanel.add(splitPane, BorderLayout.CENTER);
		this.add(sidePanel);
	}
	
	private void createCenterPanel() {
		
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView( tln );
		
		scrollPane.setBounds(0,0,1300,700);
		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(200, 700));
		centerPanel.add(scrollPane);
		centerPanel.setBounds(225,0,1200,700);
		this.add(centerPanel);

	}
	
	private class Handler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(topPanel1Labels[0] == e.getSource()) {
				topPanel1Labels[0].setForeground(Color.GREEN);
				createFileWindow();
			}else if(topPanel2Labels[0] == e.getSource()) {
				openFiles("user.home");
			}else if(filePanels[1] == e.getSource()) {
				win.dispose();
				topPanel1Labels[0].setForeground(Color.BLACK);
				openFiles("user.home");
			}else if(filePanels[2] == e.getSource()) {
				win.dispose();
				topPanel1Labels[0].setForeground(Color.BLACK);
				openFiles("user.dir");
			}else if(filePanels[7] == e.getSource()) {
				topPanel1Labels[0].setForeground(Color.BLACK);
				win.dispose();
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			for(int a = 0;a<8;a++) {
				if(a< 4) {
					if(topPanel1Labels[a] == e.getSource()) {
						topPanel1Labels[a].setText("<html><u>"+topPanel1Names[a]+"</u></html>");
						
					}else if(topPanel2Labels[a] == e.getSource()) {
						//topPanel2Labels[a].setIcon(new ImageIcon(topPanel2IconNamesEnter[a]));
						
					}
				}
				
				if(filePanels[a] == e.getSource()) {
					filePanels[a].setBackground(new Color(180, 236, 250));
					
				}
			}
			repaint();
			revalidate();
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
			for(int a = 0;a<8;a++) {
				if(a < 4) {
					if(topPanel1Labels[a] == e.getSource()) {
						topPanel1Labels[a].setText(topPanel1Names[a]);
						
					}else if(topPanel2Labels[a] == e.getSource()) {
						topPanel2Labels[a].setIcon(new ImageIcon(topPanel2IconNames[a]));
						
					}
				}
				
				if(filePanels[a] == e.getSource()) {
					filePanels[a].setBackground(Color.WHITE);
					
				}
			}
			repaint();
			revalidate();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	private void createFileWindow() {
		win = new JWindow();
		JPanel pan = new JPanel();
		JLabel labs[] = new JLabel[8];
		String names[] = {"New", "Open File", "Open Projects from File System", "Close", "Close All", "Save", "Save as", "Back"};
		pan.setLayout(new GridLayout(labs.length, 1));
			
		for(int a = 0;a<labs.length;a++) {
			
			labs[a] = new JLabel(names[a]);
			labs[a].setFont(new Font("Arial", Font.PLAIN, 12));
			labs[a].setBounds(50,5,200,20);
			filePanels[a] = new JPanel();
			filePanels[a].setLayout(null);
			filePanels[a].add(labs[a]);
			filePanels[a].setBackground(Color.WHITE);
			filePanels[a].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			filePanels[a].addMouseListener(handler);
			pan.add(filePanels[a]);
		}
				
		win.setSize(410,200);
		win.add(pan);
		win.setLocationRelativeTo(this);
		win.setLocation(20, 60);
		win.setVisible(true);
	}
	
	private void openFiles(String directory) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty(directory)));
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		}
	}
	
	 private void showChildren(final DefaultMutableTreeNode node) {
	        tree.setEnabled(false);

	        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
	            @Override
	            public Void doInBackground() {
	                File file = (File) node.getUserObject();
	                if (file.isDirectory()) {
	                    File[] files = fileSystemView.getFiles(file, true); //!!
	                    if (node.isLeaf()) {
	                        for (File child : files) {
	                            if (child.isDirectory() || child.isFile()) {
	                                publish(child);
	                            }
	                        }
	                    }                    
	                }
	                return null;
	            }

	            @Override
	            protected void process(List<File> chunks) {
	                for (File child : chunks) {
	                    node.add(new DefaultMutableTreeNode(child));
	                }
	            }

	            @Override
	            protected void done() {                
	                tree.setEnabled(true);
	            }
	        };
	        worker.execute();
	  }
}
