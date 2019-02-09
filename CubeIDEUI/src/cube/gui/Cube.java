package cube.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import cube.exceptions.SourceException;
import cube.syntax.Lexer;
import cube.syntax.SyntaxChecker;
import cube.syntax.Token;

public class Cube extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel;
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
	private JScrollPane scrollPane, belowScrollPane;
	private TextLineNumber tln;
	private JButton run;
	
	private FileSystemView fileSystemView;
	private DefaultTreeModel treeModel;
	private JTree tree;
	private JWindow win;
	private JPanel centerPanel;
	
	private JMenuBar menuBar;
	private JMenu menuFile, menuDevelopers, menuHelp;
	private JMenuItem menuItem;
	private JLabel console;
	
	public static JTextPane consolePane;
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
		
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menuFile = new JMenu("File");
		
		menuFile.setMnemonic(KeyEvent.VK_A);
		menuFile.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menuFile);

		//a group of JMenuItems
		menuItem = new JMenuItem("New file", icons[0]);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));		
		menuFile.add(menuItem);

		menuItem = new JMenuItem("Open file", icons[1]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menuFile.add(menuItem);		

		menuItem = new JMenuItem("Save", icons[2]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menuFile.add(menuItem);		

		menuItem = new JMenuItem("Save as...", icons[2]);
		//menuItem.setMnemonic(KeyEvent.VK_B);
		menuFile.add(menuItem);	
		
		for(int i = 0; i< 4; i++) {
			menuFile.getItem(i).addActionListener(this);
		}
		
		menuFile.addActionListener(this);
		menuDevelopers = new JMenu("Developers");		
		menuBar.add(menuDevelopers);

		menuHelp = new JMenu("Help");		
		menuBar.add(menuHelp);

		return menuBar;
	}
	
	private void init() {
		sidePanel = new JPanel();
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
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0,0,1100,435);
		
		run = new JButton("RUN");
		run.setBounds(50, 440, 100, 30);
		run.addActionListener(this);
		
		console = new JLabel("CONSOLE", JLabel.CENTER);
		console.setBackground(new Color(211, 211, 211));
		console.setOpaque(true);
		console.setBounds(495, 440, 100, 40);
		
		consolePane = new JTextPane();
		consolePane.setBackground(new Color(211, 211, 211));
		belowScrollPane = new JScrollPane(consolePane);
		belowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		belowScrollPane.setBounds(50, 475, 1050, 185);
		
		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(200, 700));
		centerPanel.add(scrollPane);
		centerPanel.add(belowScrollPane);
		centerPanel.add(run);
		centerPanel.add(console);
		
		centerPanel.setBounds(225,0,1200,700);
		
		this.add(centerPanel);

	}
	
	private class Handler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			Object obj = e.getSource();
			if(topPanel1Labels[0] == obj) {
				topPanel1Labels[0].setForeground(Color.GREEN);
				createFileWindow();
			}else if(topPanel2Labels[0] == obj) {
				openFiles("user.home");
			}else if(filePanels[1] == obj) {
				win.dispose();
				topPanel1Labels[0].setForeground(Color.BLACK);
				openFiles("user.home");
			}else if(filePanels[2] == obj) {
				win.dispose();
				topPanel1Labels[0].setForeground(Color.BLACK);
				openFiles("user.dir");
			}else if(filePanels[7] == obj) {
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
	                }else if(file.isFile() && (file.getName().endsWith(".java") || file.getName().endsWith(".cube"))) {
	                	// TODO render file in  JTextPane
	                	StyledDocument doc = textPane.getStyledDocument();
	                	textPane.setText("");
	                	String line = null;
	                	
	                	try {
	                		
	                		FileReader fr = new FileReader(file.getPath());
                			BufferedReader br = new BufferedReader(fr);
                			
	                		while((line = br.readLine()) != null) {
	                			//System.out.println(line);
	                			
	                			try {	                				
									doc.insertString(doc.getLength(), line + "\n", null);
								} catch (BadLocationException e) {
									e.printStackTrace();
								}
	                		}
	                		
	                		br.close();
	                	}catch(IOException e) {
	                		e.printStackTrace();
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object obj = arg0.getSource();
		
		if(obj == menuFile.getItem(3)|| obj == menuFile.getItem(2)) {
			saveFile(textPane.getText());
		}
		else if(obj == menuFile.getItem(1)) {
			openFile();
		}
		else if(obj == run) {
			 @SuppressWarnings("unused")
			String msg = "";
			 Lexer lexer = new Lexer();
			 consolePane.setText("");
			 @SuppressWarnings("unused")
			String output = "Tokens\n";
		        try {
		            int itemNumber = 0;
		            for (Token token : lexer.getTokens( textPane.getText())) {
		                output+=(String.format("%d) %s\t%s\tl:%d c:%d\n", itemNumber, token.getValue(), token.getType(), token.getStartingRow(), token.getStartingColumn()));
		                itemNumber++;
		            }
		           output += ("Source String "+textPane.getText());
		           
		           // get rid of comments
		           StringBuilder sb = new StringBuilder();
		           Scanner s = new Scanner(textPane.getText());
		           while (s.hasNextLine()) {
		        	   String line = s.nextLine();
		        	   
		        	   if (line.length() == 0) {
		        		   sb.append('\n');
		        		   continue;
		        	   }
		        	   
		        	   if (line.trim().charAt(0) == '#')
		        		   continue;
		        	   else
		        		   sb.append(line + '\n');
		           }
		           s.close();
		           String cleanedString = new String(sb);
		           System.out.println(textPane.getText());
		           System.out.println(cleanedString);
		           
		            SyntaxChecker sc = new SyntaxChecker(lexer.getTokens(cleanedString), cleanedString);
		          
		            try {
		            	 sc.start();
		            }
		            catch(IndexOutOfBoundsException e) {
		            	//System.out.println("Index Out of bounds");
		            }
		           
		        } catch (SourceException se) {
		            System.out.println(se.getMessage());
		        }
		        
		        
		}
	}
	public void saveFile(String toSave) {
	    JFileChooser chooser = new JFileChooser();
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	        try {
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".cube");
	            fw.write(toSave);
	            fw.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	public void openFile() {
		 JFileChooser chooser = new JFileChooser();
		 int result = chooser.showOpenDialog(this);
         if (result==JFileChooser.APPROVE_OPTION) {
             File file = chooser.getSelectedFile();
             try {
                textPane.setPage(file.toURI().toURL());
             } catch(Exception e) {
                 e.printStackTrace();
             }
         }
     }
}
