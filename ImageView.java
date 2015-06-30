import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;
public class ImageView extends JFrame implements KeyListener 
{
	private JButton previousB, browseB, nextB;
	private JPanel panel, panel1;
	private	JScrollPane scrollPane;
	private javax.swing.Timer scrollTimer;

	private PreviousButtonHandler pbHandler;
	private BrowseButtonHandler bbHandler;
	private NextButtonHandler nbHandler;

	public String dirpath, prevDirPath;
	public String[] fileArray;
	public File Fold;
	public File[] listOfFiles;
	public int imagenum, pHeight, pWidth, scrollValue, tsv, max, vis, zoom, key;
	public Container pane;
	
	public ImageView() throws IOException 
	{
		try
		{
			zoom = 100;
			this.addKeyListener(this); 
			this.setFocusable(true);
			
			previousB = new JButton("<|");
			pbHandler = new PreviousButtonHandler();
			previousB.addActionListener(pbHandler);
			browseB = new JButton("..");
			bbHandler = new BrowseButtonHandler();
			browseB.addActionListener(bbHandler);
			nextB = new JButton("|>");
			nbHandler = new NextButtonHandler();
			nextB.addActionListener(nbHandler);
			
			dirpath = "";
			
			Icon image = new ImageIcon("default.png");
			JLabel view = new JLabel(image);
			
			setTitle("Image Viewer");
			
			pane = getContentPane();
			pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
			
			panel = new JPanel();
			panel.setLayout(new GridLayout(1, 3));
			panel.setMaximumSize(new Dimension( Short.MAX_VALUE, 100));
			panel1 = new JPanel();
			panel1.setLayout(new GridLayout(1, 1));
			
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(panel1, BorderLayout.CENTER);
			/*scrollListener = createScrollListener();
			scrollTimer = new Timer(scrollSpeedThrottle, scrollListener);
			scrollTimer.setInitialDelay(300);*/
			
			panel.add(previousB);
			panel.add(browseB);
			panel.add(nextB);
			panel1.add(view, BorderLayout.CENTER);
			pane.add(panel);
			pane.add(scrollPane);
			
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			ActionListener scrollAction = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					if(tsv == scrollValue)
					{
						scrollTimer.stop();
					}
					else
					{
						scrollPane.getVerticalScrollBar().setValue(tsv);
						scrollPane.repaint();
						if(key == KeyEvent.VK_UP)
							tsv-=10;
						else if(key == KeyEvent.VK_DOWN)
							tsv+=10;
					}
				}
			};

			scrollTimer = new javax.swing.Timer(10, scrollAction);
			scrollTimer.setInitialDelay(0);
		}
		catch(Exception h)
		{
			System.out.println ( "Please verify that you selected a valid image file");	
		}
	}
	public ImageView(String filepath) throws IOException 
	{
		try
		{
			zoom = 100;
			this.addKeyListener(this); 
			this.setFocusable(true);
			
			previousB = new JButton("<|");
			pbHandler = new PreviousButtonHandler();
			previousB.addActionListener(pbHandler);
			browseB = new JButton("..");
			bbHandler = new BrowseButtonHandler();
			browseB.addActionListener(bbHandler);
			nextB = new JButton("|>");
			nbHandler = new NextButtonHandler();
			nextB.addActionListener(nbHandler);
			
			dirpath = "";
			
			Icon image = new ImageIcon("default.png");
			JLabel view = new JLabel(image);
			
			setTitle("Image Viewer");
			
			pane = getContentPane();
			pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
			
			panel = new JPanel();
			panel.setLayout(new GridLayout(1, 3));
			panel.setMaximumSize(new Dimension( Short.MAX_VALUE, 100));
			panel1 = new JPanel();
			panel1.setLayout(new GridLayout(1, 1));
			
			scrollPane = new JScrollPane();
			scrollPane.getViewport().add(panel1, BorderLayout.CENTER);
			
			panel.add(previousB);
			panel.add(browseB);
			panel.add(nextB);
			panel1.add(view, BorderLayout.CENTER);
			pane.add(panel);
			pane.add(scrollPane);
			
			setUndecorated(true);
			setExtendedState(MAXIMIZED_BOTH);
			setVisible(true);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			File opfile = new File(filepath);
			if (opfile.isDirectory())
				Fold = opfile;
			else
				Fold = new File(opfile.getParent());
			listOfFiles = Fold.listFiles();
			Arrays.sort(listOfFiles, new WindowsExplorerFileComparator());
			if (opfile.isDirectory())
				imagenum = 0;
			else
				imagenum = Arrays.binarySearch(listOfFiles, opfile, new WindowsExplorerFileComparator());
			dirpath = Fold.getPath() +"/";
			updateGUI();
		}
		catch(Exception h)
		{
			System.out.println ( "Please verify that you selected a valid image file");	
			h.printStackTrace();
		}
	}
	public class PreviousButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				if (imagenum>0)
				{
					imagenum = imagenum - 1;
					updateGUI();
				}
				else
				{
					imagenum = listOfFiles.length-1;
					updateGUI();
				}
			}
			catch(Exception h)
			{
				System.out.println ( "Please verify that you selected a valid image file");	
			}
		}
	}
	public class BrowseButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				JFileChooser chooser = new JFileChooser();
				if (prevDirPath != null)
					chooser.setCurrentDirectory(new File(prevDirPath));
				else
					chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(null);
				File selectedPfile = chooser.getSelectedFile();
				dirpath = selectedPfile.getPath() + "/";
				prevDirPath = dirpath;
				
				Fold = new File(dirpath);
				listOfFiles = Fold.listFiles();
				Arrays.sort(listOfFiles, new WindowsExplorerFileComparator());
				imagenum = 0;
				
				updateGUI();
			}
			catch(Exception h)
			{
				h.printStackTrace();
				System.out.println ( "Please verify that you selected a valid image file");	
			}
		}
	}
	public class NextButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{	
				if (imagenum<listOfFiles.length-1)
				{
					imagenum = imagenum + 1;
				}
				else
				{
					imagenum = 0;
					updateGUI();
				}
			}
			catch(Exception h)
			{
				System.out.println ( "Please verify that you selected a valid image file");	
			}
		}
	}
	public void keyPressed(KeyEvent e) 
	{
		
        switch (e.getKeyCode()) 
		{
			case KeyEvent.VK_LEFT : 
				try
				{
					if (imagenum>0)
					{
						imagenum = imagenum - 1;
						updateGUI();
					}
					else
					{
						imagenum = listOfFiles.length-1;
						updateGUI();
					}
				}
				catch(Exception h)
				{
					System.out.println ( "Please verify that you selected a valid image file");	
				}   
				break;
            case KeyEvent.VK_RIGHT: 
				try
				{	
					if (imagenum<listOfFiles.length-1)
					{
						imagenum = imagenum + 1;
						updateGUI();
					}
					else
					{
						imagenum = 0;
						updateGUI();
					}
				}
				catch(Exception h)
				{
					System.out.println ( "Please verify that you selected a valid image file");	
				}
				break;
            case KeyEvent.VK_UP   :
				key = KeyEvent.VK_UP;
				tsv  = scrollPane.getVerticalScrollBar().getValue();
				scrollValue = tsv - 150;
				scrollTimer.start();
				break;
            case KeyEvent.VK_DOWN :	
				key = KeyEvent.VK_DOWN;
				tsv  = scrollPane.getVerticalScrollBar().getValue();
				scrollValue = tsv + 150;
				scrollTimer.start();
				break;
			case KeyEvent.VK_SPACE :
				scrollValue = scrollPane.getVerticalScrollBar().getValue();
				max = scrollPane.getVerticalScrollBar().getMaximum();
				vis = scrollPane.getVerticalScrollBar().getVisibleAmount();
				if (scrollValue == 0)
				{
					scrollValue = (max-vis)/2;
					scrollPane.getVerticalScrollBar().setValue(scrollValue);
					scrollPane.repaint();
				}
				else if (scrollValue == max-vis)
				{
					try
					{	
						if (imagenum<listOfFiles.length-1)
						{
							imagenum = imagenum + 1;
							
							updateGUI();
						}
						else
						{
							imagenum = 0;
							
							updateGUI();
						}
					}
					catch(Exception h)
					{
						System.out.println ( "Please verify that you selected a valid image file");	
					}
				}
				else
				{
					scrollValue = max-vis;
					scrollPane.getVerticalScrollBar().setValue(scrollValue);
					scrollPane.repaint();
				}
				break; 
			case KeyEvent.VK_MINUS:
				if (zoom > 10)
					zoom = zoom - 10;
				updateGUI();
				break;
			case KeyEvent.VK_EQUALS:
				zoom = zoom + 10;
				updateGUI();
				break;
        }
    }
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent kevt) {}
	public void updateGUI()
	{
		ImageIcon image = new ImageIcon(dirpath + listOfFiles[imagenum].getName());
		Image img = image.getImage();
		Image newimg = img.getScaledInstance((image.getIconWidth() * zoom)/100, (image.getIconHeight() * zoom)/100,  java.awt.Image.SCALE_SMOOTH);
		image = new ImageIcon(newimg);
		JLabel view = new JLabel(image);
		
		setTitle("Image Viewer");
		pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		panel1.removeAll(); 
		
		panel.add(previousB);
		panel.add(browseB);
		panel.add(nextB);
		panel1.add(view, BorderLayout.CENTER);
		panel1.updateUI(); 
		scrollPane.getVerticalScrollBar().setValue(0);
		scrollPane.repaint();
	}
	public static void main(String[] args) throws IOException
	{
		try
		{
			if (args.length == 1)
			{
				ImageView GUI = new ImageView(args[0]);
			}
			else
			{
				ImageView GUI = new ImageView();
			}
		}
		catch(Exception h)
		{
			System.out.println ( "Please verify that you selected a valid image file");	
		}
	}
}
