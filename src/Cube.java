
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

public class Cube extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel, topPanel1, topPanel2;
	private JLabel topPanel1Labels[] = new JLabel[4];
	private JLabel topPanel2Labels[] = new JLabel[4];
	private ImageIcon topPanel2Icons[] = new ImageIcon[4];
	private String topPanel1Names[] = {"File", "Developers", "CubePiler", "Help"};
	private String topPanel2IconNamesEnter[] = {"src/images/open_enter.png", "src/images/new_enter.png", "src/images/save_enter.png", "src/images/close_enter.png"};
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
		this.setPreferredSize(new Dimension(1400,700));
		this.setBackground(new Color(250,250,250));
		
		init();
		createSidePanel();
		createTopPanel();
		createCenterPanel();
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

	}
	
	private void createSidePanel() {
		sidePanel.setPreferredSize(new Dimension(200, 700));
		sidePanel.setBounds(0,50,200,700);			
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
		
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                showChildren(node);
            }            
        };
                        
        File f = new File(".");
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView());
        chooser.setCurrentDirectory(f);

        fileSystemView = chooser.getFileSystemView();

        File roots = fileSystemView.getDefaultDirectory();
 
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(roots);
            root.add( node );
            File[] files = fileSystemView.getFiles(roots, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
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
            200,
            (int)preferredSize.getHeight());
        treeScroll.setPreferredSize( widePreferred );
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,treeScroll,null);
        sidePanel.add(splitPane, BorderLayout.CENTER);
		this.add(sidePanel);
	}
	
	private void createTopPanel() {
		topPanel1.setPreferredSize(new Dimension(1400, 20));
		topPanel1.setBounds(0,0,1400,20);
		topPanel1.setLayout(null);
		this.add(topPanel1);
		
		int x = 5;
		for(int a = 0; a<4; a++) {
			if(a == 0) {
				topPanel1Labels[a].setBounds(x, 0, 30, 20);
				x+=30;
			}else {
				topPanel1Labels[a].setBounds(x, 0, 70, 20);
				x+=70;
			}
			topPanel1Labels[a].setFont(new Font("Arial", Font.PLAIN, 12));
			topPanel1Labels[a].addMouseListener(handler);
			topPanel1.add(topPanel1Labels[a]);
			
		}
		
		topPanel2.setPreferredSize(new Dimension(1400, 30));
		topPanel2.setBackground(new Color(180, 236, 250, 70));
		topPanel2.setBounds(0,20,1400,30);
		topPanel2.setLayout(null);
		this.add(topPanel2);
		
		x = 5;
		int y = 0;
		for(int b = 0; b<4; b++) {
			if(b != 0)
				y = 4;
			topPanel2Labels[b].setBounds(x, y, topPanel2Icons[b].getIconWidth(), topPanel2Icons[b].getIconHeight());
			topPanel2Labels[b].addMouseListener(handler);
			topPanel2.add(topPanel2Labels[b]);
			x += topPanel2Icons[b].getIconWidth() + 20;
			
		}
	}
	
	private void createCenterPanel() {

		
		textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		tln = new TextLineNumber(textPane);
		scrollPane.setRowHeaderView( tln );
		
		scrollPane.setBounds(0,0,1200,700);
		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(1200, 700));
		centerPanel.add(scrollPane);
		centerPanel.setBounds(200,50,1200,700);
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
						topPanel2Labels[a].setIcon(new ImageIcon(topPanel2IconNamesEnter[a]));
						
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
	                            if (child.isDirectory() || child.getName().endsWith(".cube") || child.getName().endsWith(".txt")) {
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
