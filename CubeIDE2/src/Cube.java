
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Cube extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel sidePanel, topPanel1, topPanel2, centerPanel;
	private JLabel topPanel1Labels[] = new JLabel[4];
	
	private DefaultTreeModel treeModel;
	private FileSystemView fileSystemView;
	private JTree tree;
	
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
		sidePanel = new JPanel(new BorderLayout(3,3));
		topPanel1 = new JPanel();
		topPanel2 = new JPanel();
		centerPanel = new JPanel();
		String topPanel1Names[] = {"File", "Search", "Edit", "View"};
		
		for(int a = 0;a<4;a++) {
			topPanel1Labels[a] = new JLabel(topPanel1Names[a]);
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
		//System.out.println(chooser.getCurrentDirectory().toString());
        fileSystemView = chooser.getFileSystemView();
        
        // show the file system roots.
        File roots = fileSystemView.getDefaultDirectory();
        //for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(roots);
            root.add( node );
            File[] files = fileSystemView.getFiles(roots, true);
            for (File file : files) {
                if (file.isDirectory() || file.getName().endsWith(".cube")) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        //}        

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
		topPanel1.setBackground(Color.PINK);
		topPanel1.setBounds(0,0,1400,20);
		topPanel1.setLayout(null);
		this.add(topPanel1);
		
		int start = 5;
		for(int a = 0; a<4; a++) {
			if(a == 1) {
				topPanel1Labels[a].setBounds(start, 0, 50, 20);
				start+=50;
			}else {
				topPanel1Labels[a].setBounds(start, 0, 30, 20);
				start+=30;
			}
			topPanel1Labels[a].setFont(new Font("Arial", Font.PLAIN, 12));
			topPanel1.add(topPanel1Labels[a]);
		}
		
		topPanel2.setPreferredSize(new Dimension(1400, 30));
		topPanel2.setBackground(Color.ORANGE);
		topPanel2.setBounds(0,20,1400,30);
		this.add(topPanel2);
	}
	
	private void createCenterPanel() {
		centerPanel.setPreferredSize(new Dimension(1200,700));
		centerPanel.setBackground(Color.GREEN);
		centerPanel.setBounds(200,50,1200,700);
		this.add(centerPanel);
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
	                            if (child.isDirectory()) {
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
