package crawler;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI implements ActionListener{
	//GUI componets
	JFrame jframe;
	JPanel textPanel;
	JLabel background;
	JTextArea status;
	JTextField url;
	JTextField port;
	JTextField host;
	JTextArea progress;
	JComboBox<String> proxy;
	JFileChooser choose;
	JButton filechose;
	JTextField filepath;
	JButton start;
	JScrollPane output;
	JPanel textArea;
	
	static Configuration conf = new Configuration();
	private LinkedList<String> urlLst = new LinkedList<String>();
	static BlockingQueue<Page> queue = new LinkedBlockingQueue<>(500);
	
	public static void main(String[] args)
	{
		GUI userInterface = new GUI();

	}
	public void start(JTextArea showArea,JTextArea progress)
	{
		//set proxy configure
		System.setProperty("http.proxySet", "true"); 
		System.setProperty("http.proxyHost", conf.getHost()); 
		System.setProperty("http.proxyPort", conf.getPort());
		DealwithURL urlResolver = new DealwithURL(queue);	
		int count = 1;
		int total = conf.getUrlStr().split(";").length;
		
		//start
		for (String singleUrl : conf.getUrlStr().split(";")) {
			urlLst.add(singleUrl);
		}
		
		ExecutorService Threadpool = Executors.newFixedThreadPool(5);
		for(String url : urlLst) {
			progress.setText("     " + count++ + "/" + total);
			Book book = urlResolver.getSource(url);
			showArea.append("Title: " + book.getTitle() + "\n" + "total pages: " + book.getPagesCnt() + "\n");
			if (new DownloadComickbook(conf).isDownloaded(book.getTitle())) {
				showArea.append("comic book "+book.getTitle()+" already exists");
				continue;
			}
			conf.setPages(book.getPagesCnt()+conf.getPages());
		}
		for (int i = 0; i < 20; i++) {
			Threadpool.execute(new DownloadComickbook(conf, showArea ,progress,queue));
		}
		
		System.setProperty("http.proxySet", "false");
	}
	public GUI()
	{
		jframe  = new JFrame("comic downloader");
		jframe.setSize(250, 500);
		Container con = jframe.getContentPane();
		
		textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());
		textPanel.setPreferredSize(new Dimension(150,170));
		url = new JTextField();
		port = new JTextField();
		host = new JTextField();
		proxy = new JComboBox<String>();
		choose = new JFileChooser();
		filechose = new JButton("...");
		filechose.addActionListener(this);
		filepath = new JTextField();
		progress =  new JTextArea();
		Dimension dimension = new Dimension(170,25);
		url.setPreferredSize(dimension);
		port.setPreferredSize(dimension);
		host.setPreferredSize(dimension);
		progress.setPreferredSize(dimension);
		progress.setLineWrap(true);
		progress.setEditable(false);
		progress.setBorder(BorderFactory.createLoweredBevelBorder());
		filepath.setPreferredSize(new Dimension(145,25));
		//private path
		filepath.setText("F:/����/Ѹ������");
		filechose.setPreferredSize(new Dimension(20,20));
		proxy.setPreferredSize(dimension);
		proxy.addItem("customize");
		proxy.addItem("shadowsocks");
		proxy.addItem("freegate");
		proxy.addActionListener(this);
		
		choose.setApproveButtonText("ȷ��");  
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  //����ֻѡ��Ŀ¼
		textPanel.add(new JLabel("proxy"));
		textPanel.add(proxy);
		textPanel.add(new JLabel("host  "));
		textPanel.add(host);
		textPanel.add(new JLabel("port  "));
		textPanel.add(port);
		textPanel.add(new JLabel("   url    "));
		textPanel.add(url);
		textPanel.add(new JLabel(" loca"));
		textPanel.add(filepath);
		textPanel.add(filechose);
		textPanel.add(new JLabel("progress"));
		textPanel.add(progress);
		
		start = new JButton("download");
		start.addActionListener(this);
		textArea = new JPanel();
		status = new JTextArea();				
		status.setLineWrap(true);
		status.setEditable(false);
		status.setPreferredSize(new Dimension(200, 800));
		status.setBorder(BorderFactory.createLoweredBevelBorder());
		output = new JScrollPane(status);
		output.setPreferredSize(new Dimension(200, 200));
		textArea.add(output);
				
		con.setLayout(new BorderLayout());
		con.add(textPanel,BorderLayout.NORTH);
		con.add(textArea,BorderLayout.CENTER);
		con.add(start,BorderLayout.SOUTH);
		jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(proxy))
        {
        	if(proxy.getSelectedIndex()==1)
        	{
        	   port.setText("1080");
        	   host.setText("127.0.0.1");
        	}
        	if(proxy.getSelectedIndex()==2)
        	{
        	   port.setText("8580");
        	   host.setText("127.0.0.1");
        	}
        }
        if(e.getSource().equals(filechose))
        {
            int state = choose.showOpenDialog(textPanel);
            if(state == JFileChooser.CANCEL_OPTION||state == JFileChooser.ERROR_OPTION)
            {
            	return;
            }
            else
            {            	
            	filepath.setText(choose.getSelectedFile().getAbsolutePath());
            }
            
        }
        if(e.getSource().equals(start))
        {
        	if(port.getText()!=null&&host.getText()!=null&&url.getText()!=null&&filepath.getText()!=null)
        	{
        		status.setText("");
        		conf.setHost(host.getText());
        		conf.setLocation(filepath.getText());
        		conf.setPort(port.getText());
        		conf.setUrlStr(url.getText());
        		progress.setText("");
        		start(status,progress);
        	}
        	else {

				return;
			}
        }
		
	}

}
