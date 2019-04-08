package cube.gui;
// Java Program to create a text editor using java 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel; 

@SuppressWarnings("serial")
public
class CubeEditor extends JPanel implements ActionListener {  
	JTextArea t;  
	JFrame f;
	private JPanel sidePanel;
	private JPanel centerPanel; 
	private FileSystemView fileSystemView;
	private DefaultTreeModel treeModel;
	private JTree tree;
	
	private JTextPane textPane;
	private JScrollPane scrollPane, belowScrollPane;
	private TextLineNumber tln;
	private Icon icons[] = new ImageIcon[3];
	private String iconPaths[] = {"res/new_enter.png", "res/open_enter.png", "res/save_enter.png", "res/close_enter.png"};
	private String selectedPath;
	
	private JButton run;
	private JLabel console;
	public static JTextPane consolePane;
	private JMenuBar mb;

	public CubeEditor() {  
		setLayout(null);
		
		try { 
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");  
			MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
		} 
		catch (Exception e) { 
		} 

		init();
		createSidePanel();						
		createCenterPanel();		
		//t.setBounds(221, 0, 700, 700);

		mb = new JMenuBar();  
		JMenu m1 = new JMenu("File"); 

		// Create menu items 
		JMenuItem mi1 = new JMenuItem("New", icons[0]); 		
		JMenuItem mi2 = new JMenuItem("Open", icons[1]); 
		JMenuItem mi3 = new JMenuItem("Open Folder", icons[1]); 
		JMenuItem mi4 = new JMenuItem("Save", icons[2]);
		JMenuItem mi8 = new JMenuItem("Save as...", icons[2]); 

		mi1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		mi2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		mi4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		
		// Add action listener 
		mi1.addActionListener(this); 		
		mi2.addActionListener(this); 
		mi3.addActionListener(this); 
		mi4.addActionListener(this);
		mi8.addActionListener(this); 

		m1.add(mi1); 
		m1.add(mi2); 
		m1.add(mi3);
		m1.add(mi4);
		m1.add(mi8);
		//m1.add(mi9); 

		// Create amenu for menu 
		JMenu m2 = new JMenu("Edit"); 

		// Create menu items 
		JMenuItem mi5 = new JMenuItem("cut");
		JMenuItem mi6 = new JMenuItem("copy");		
		JMenuItem mi7 = new JMenuItem("paste"); 

		// Add action listener 
		mi4.addActionListener(this); 
		mi5.addActionListener(this); 		
		mi6.addActionListener(this); 

		m2.add(mi5); 
		m2.add(mi6); 
		m2.add(mi7);  

		mb.add(m1); 
		mb.add(m2); 

		//setJMenuBar(mb); 
		setSize(500, 500); 
		//f.setVisible(true); 
	} 
	
	public JMenuBar getMB() {
		return mb;
	}
	private void init() {
		for(int i = 0; i < icons.length; i++){
			icons[i] = new ImageIcon(iconPaths[i]);
		}
	}

	private void createCenterPanel() {
		t = new JTextArea();		
		centerPanel = new JPanel();
		scrollPane = new JScrollPane(t);
		LineNumberingTextArea lineNumberingTextArea = new LineNumberingTextArea(t);
		scrollPane.setRowHeaderView(lineNumberingTextArea);
		t.getDocument().addDocumentListener(new DocumentListener()
		{
		    @Override
		    public void insertUpdate(DocumentEvent documentEvent)
		    {
		        lineNumberingTextArea.updateLineNumbers();
		    }

		    @Override
		    public void removeUpdate(DocumentEvent documentEvent)
		    {
		        lineNumberingTextArea.updateLineNumbers();
		    }

		    @Override
		    public void changedUpdate(DocumentEvent documentEvent)
		    {
		        lineNumberingTextArea.updateLineNumbers();
		    }
		});
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		scrollPane.setBounds(0,0,1050,435);
		
		run = new JButton("RUN");
		run.setBounds(50, 440, 100, 30);
		run.addActionListener(this);
		
		console = new JLabel("CONSOLE", JLabel.CENTER);
		console.setBackground(new Color(211, 211, 211));
		console.setOpaque(true);
		console.setBounds(495, 440, 100, 40);
		
		consolePane = new JTextPane();
		consolePane.setFont(new Font(Font.MONOSPACED, 1, 12));
		consolePane.setBackground(new Color(211, 211, 211));
		belowScrollPane = new JScrollPane(consolePane);
		belowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		belowScrollPane.setBounds(50, 475, 1050, 185);
		
		centerPanel.setLayout(null);
		centerPanel.setPreferredSize(new Dimension(200, 700));
		centerPanel.add(scrollPane);
		centerPanel.setBounds(225,0,1200,700);
		
		add(centerPanel);
	}

	private void createSidePanel() {
		sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(200, 700));
		sidePanel.setBounds(2, 0, 220, 700);
		
		//addFolderTree();
	}

	public void addFolderTree(String path) {
		sidePanel.removeAll();
		this.f.remove(sidePanel);
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
                             
        // Show the file system roots.
        /*File f = new File(".");
        String parentPath = f.getAbsolutePath();        
        int index = parentPath.lastIndexOf("\\");
        
        File parentFile = new File(parentPath.substring(0, index));*/
        File parentFile = new File(path);
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(parentFile);
        root.add(parentNode);
        File[] roots = fileSystemView.getFiles(parentFile, true);        
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            parentNode.add( node );
            File[] files = fileSystemView.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
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
        this.f.add(sidePanel);   
        this.f.revalidate();
        this.f.repaint();
        
	}
	
	public void setFrame(JFrame f) {
		this.f = f;
	}
	public void actionPerformed(ActionEvent e) { 
		String s = e.getActionCommand(); 

		if (s.equals("cut")) { 
			t.cut(); 
		} 
		else if (s.equals("copy")) { 
			t.copy(); 
		} 
		else if (s.equals("paste")) { 
			t.paste(); 
		} 
		else if (s.equals("Save as...")) { 
			// Create an object of JFileChooser class 
			 saveAs();
		} 
		else if (s.equals("Save")) { 			 
			if(selectedPath != null) {
			 try { 
				 	File selectedFile = new File(selectedPath);
					// Create a file writer 
					FileWriter wr = new FileWriter(selectedFile, false); 

					// Create buffered writer to write 
					BufferedWriter w = new BufferedWriter(wr); 

					// Write 
					w.write(t.getText()); 

					w.flush(); 
					w.close(); 
				} 
				catch (Exception evt) { 
					JOptionPane.showMessageDialog(f, evt.getMessage()); 
				} 
			}else {
				saveAs();
			}
		} 
		else if (s.equals("Open")) { 
			// Create an object of JFileChooser class 			
			JFileChooser j = new JFileChooser("c:"); 

			// Invoke the showsOpenDialog function to show the save dialog 
			int r = j.showOpenDialog(null); 

			// If the user selects a file 
			if (r == JFileChooser.APPROVE_OPTION) { 
				// Set the label to the path of the selected directory
				selectedPath = j.getSelectedFile().getAbsolutePath();
				File fi = new File(selectedPath); 

				try { 
					// String 
					String s1 = "", sl = ""; 
					FileReader fr = new FileReader(fi);  
					BufferedReader br = new BufferedReader(fr); 

					sl = br.readLine(); 

					// Take the input from the file 
					while ((s1 = br.readLine()) != null) { 
						sl = sl + "\n" + s1; 
					} 

					// Set the text 
					t.setText(sl); 
				} 
				catch (Exception evt) { 
					JOptionPane.showMessageDialog(f, evt.getMessage()); 
				} 
			} 			 
		} 
		else if (s.equals("New")) { 
			t.setText(""); 
		}else if (s.equals("Open Folder")) {
			JFileChooser j = new JFileChooser("c:");
			j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int r = j.showOpenDialog(null);
			this.f.remove(sidePanel);
			
			if(j.getSelectedFile() != null) {
				String path = j.getSelectedFile().getAbsolutePath();
				addFolderTree(path);					
			}
			
			sidePanel.repaint();
			sidePanel.revalidate();
			this.f.repaint();
			this.f.revalidate();
		}
	}
	
	 private void saveAs() {
		 JFileChooser j = new JFileChooser("f:"); 

			// Invoke the showsSaveDialog function to show the save dialog 
			int r = j.showSaveDialog(null); 

			if (r == JFileChooser.APPROVE_OPTION) { 

				// Set the label to the path of the selected directory 
				File fi = new File(j.getSelectedFile().getAbsolutePath()); 

				try { 
					// Create a file writer 
					FileWriter wr = new FileWriter(fi, false); 

					// Create buffered writer to write 
					BufferedWriter w = new BufferedWriter(wr); 

					// Write 
					w.write(t.getText()); 

					w.flush(); 
					w.close(); 
				} 
				catch (Exception evt) { 
					JOptionPane.showMessageDialog(f, evt.getMessage()); 
				} 
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
	                            if (child.isDirectory() || 
	                            		(child.isFile())) {	                            	
	                            	publish(child);	                            	
	                            }
	                        }
	                    }                    
	                }else if(file.isFile() && (file.getName().endsWith(".java") || file.getName().endsWith(".cube"))) {
	                	// TODO render file in  JTextPane
	                	Document doc = t.getDocument();
	                	t.setText("");
	                	String line = null;
	                	
	                	try {
	                		
	                		selectedPath = file.getPath();
	                		FileReader fr = new FileReader(file.getPath());
	                		BufferedReader br = new BufferedReader(fr);
             			
	                		while((line = br.readLine()) != null) {
	                			//System.out.println(line);
	                			
	                			try {	                				
	                				//System.out.println("length: " + doc.getLength());
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

} 
