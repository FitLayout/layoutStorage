/**
 * ClassificationPlugin.java
 *
 * Created on 23. 1. 2015, 21:44:40 by burgetr
 */
package org.fit.layout.storage.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.fit.layout.gui.Browser;
import org.fit.layout.gui.BrowserPlugin;
import org.fit.layout.model.AreaTree;
import org.fit.layout.model.LogicalAreaTree;
import org.fit.layout.model.Page;
import org.fit.layout.storage.BigdataInterface;
import org.fit.layout.storage.model.BigdataAreaTree;
import org.fit.layout.storage.model.BigdataPage;
import org.openrdf.model.Model;
import org.openrdf.model.impl.URIImpl;



/**
 * 
 * @author imilicka
 */
public class StoragePlugin implements BrowserPlugin
{
    private Browser browser;
    BigdataInterface bdi = null;	
    
    private JPanel pnl_main;
    private JPanel tbr_connection;
    private JTextField tfl_urlRDFDB;
    private JLabel lbl_rdfDb;
    private JButton btn_loadDBData;
    private JPanel tbr_storageSelection;
    private JLabel lbl_urls;
    private JComboBox<String> cbx_pages;
    private JButton btn_loadBoxModel;
    private JPanel tbr_control;
    private JButton btn_saveBoxTreeModel;
    private JButton btn_removePage;
    private JButton btn_clearDB;
    private JButton btn_saveAreaTreeModel;
    private JComboBox<String> cbx_areaTrees;
    private JButton btn_loadAreaTreeModel;
    
    
	//=============================
    
    /**
     * @wbp.parser.entryPoint
     */
    public boolean init(Browser browser)
    {
        this.browser = browser;
        this.browser.addToolPanel("Model Storage", getPnl_main()  );
        return true;
    }
    
    
    private JPanel getPnl_main() {
    	
    	 if (pnl_main == null) {
    		 
             pnl_main = new JPanel();
             GridBagLayout gbl_main = new GridBagLayout();
             gbl_main.columnWeights = new double[] { 0.0 };
             gbl_main.rowWeights = new double[] { 0.0, 0.0, 0.0 };
             pnl_main.setLayout(gbl_main);
             GridBagConstraints gbc_connection = new GridBagConstraints();
             gbc_connection.weightx = 1.0;
             gbc_connection.anchor = GridBagConstraints.EAST;
             gbc_connection.fill = GridBagConstraints.BOTH;
             gbc_connection.insets = new Insets(0, 0, 5, 0);
             gbc_connection.gridx = 0;
             gbc_connection.gridy = 0;
             pnl_main.add(getPnl_connection(), gbc_connection);
             GridBagConstraints gbc_storageSelection = new GridBagConstraints();
             gbc_storageSelection.weightx = 1.0;
             gbc_storageSelection.fill = GridBagConstraints.BOTH;
             gbc_storageSelection.insets = new Insets(0, 0, 5, 0);
             gbc_storageSelection.gridx = 0;
             gbc_storageSelection.gridy = 1;
             pnl_main.add(getPnl_storageSelection(), gbc_storageSelection);
             GridBagConstraints gbc_control = new GridBagConstraints();
             gbc_control.weightx = 1.0;
             gbc_control.anchor = GridBagConstraints.EAST;
             gbc_control.fill = GridBagConstraints.BOTH;
             gbc_control.insets = new Insets(0, 0, 5, 0);
             gbc_control.gridx = 0;
             gbc_control.gridy = 2;
             pnl_main.add(getPnl_control(), gbc_control);
         }
         return pnl_main;
    }
    
    
    //Connection panel=================================================================
    
    private JPanel getPnl_connection() 
    {
		if (tbr_connection == null) {
			tbr_connection = new JPanel();
			tbr_connection.add(getLbl_RdfDb());
			tbr_connection.add(getTfl_urlRDFDB());
			tbr_connection.add(getBtn_loadDBData());
		}
		return tbr_connection;
	}
	
    private JTextField getTfl_urlRDFDB() 
    {
		if (tfl_urlRDFDB == null) {
			tfl_urlRDFDB = new JTextField();
			//tfl_urlRDFDB.setMinimumSize(new Dimension(12, 20));
			tfl_urlRDFDB.setHorizontalAlignment(SwingConstants.LEFT);
			tfl_urlRDFDB.setText("http://localhost:8080/bigdata/sparql");
			//tfl_urlRDFDB.setColumns(30);
		}
		return tfl_urlRDFDB;
	}
	
	private JLabel getLbl_RdfDb() 
	{
		if (lbl_rdfDb == null) {
			lbl_rdfDb = new JLabel("RDF DB");
		}
		return lbl_rdfDb;
	}
	
	private JButton getBtn_loadDBData() 
	{
		if (btn_loadDBData == null) {
			btn_loadDBData = new JButton("Establish Connection");
			btn_loadDBData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					loadAllPages();
				}
			});
		}
		return btn_loadDBData;
	}
	
	
	//Selection panel =============================
	
	private JPanel getPnl_storageSelection() 
	{
		if (tbr_storageSelection == null) {
			tbr_storageSelection = new JPanel();
			tbr_storageSelection.add(getLbl_pages());
			tbr_storageSelection.add(getCbx_pages());
			tbr_storageSelection.add(getBtn_loadBoxModel());
			tbr_storageSelection.add(getCbx_areaTrees());
			tbr_storageSelection.add(getBtn_loadAreaTreeModel());
			
		}
		return tbr_storageSelection;
	}
	
	private JLabel getLbl_pages() 
	{
		if (lbl_urls == null) {
			lbl_urls = new JLabel("URLs");
		}
		return lbl_urls;
	}
	
	private JComboBox<String> getCbx_pages() 
	{
		if (cbx_pages == null) {
			cbx_pages = new JComboBox<String>();
			cbx_pages.setMaximumRowCount(8);
			cbx_pages.setPreferredSize(new Dimension(300,25));
			cbx_pages.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					cbx_areaTrees.removeAllItems();
			    	
			    	if( cbx_pages.getItemCount()>0 ) 
			    	{
			    		cbx_areaTrees.setEnabled(true);
			    		
			    		try {
							List<String> areaTrees = bdi.getAreaTreeIdsForPageId(cbx_pages.getSelectedItem().toString() );
							for(String area: areaTrees) {
								cbx_areaTrees.addItem(area);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						} 
			    	}
			    	else {
			    		cbx_areaTrees.setEnabled(false);
			    	}
				}
			});
		}
		return cbx_pages;
	}
	
	private JButton getBtn_loadBoxModel() 
	{
		if (btn_loadBoxModel == null) {
			btn_loadBoxModel = new JButton("Load Box Model");
			btn_loadBoxModel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
			        try {
			        	String pageId = cbx_pages.getSelectedItem().toString();
			        	Model modelStatements = bdi.getBoxModelForPageId(pageId);
						Page page = new BigdataPage(modelStatements, pageId.substring(0, pageId.lastIndexOf("#")) );
						browser.setPage(page);
						
					} catch (Exception e1) {
						
						JOptionPane.showMessageDialog((Component)browser,
							    "Cannot load defined launch!",
							    "Loading Error",
							    JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
			btn_loadBoxModel.setEnabled(false);
		}
		return btn_loadBoxModel;
	}
	
	/**
	 * it loads distinct URLs into ulrsComboBox
	 */
	private void loadAllPages() 
	{
		String DBConnectionUrl = tfl_urlRDFDB.getText();
		
		cbx_pages.removeAllItems();
		
		try {
			bdi = new BigdataInterface(DBConnectionUrl, false);
			
			List<String> listURL = bdi.getAllPageIds();
			for(String url : listURL) {
				cbx_pages.addItem(url);
			}
			
			getBtn_loadBoxModel().setEnabled(true);
			getBtn_loadAreaTreeModel().setEnabled(true);
			
			getBtn_saveBoxTreeModel().setEnabled(true);
			getBtn_removePage().setEnabled(true);
			getBtn_clearDB().setEnabled(true);
			getBtn_saveAreaTreeModel().setEnabled(true);
		}
		catch (Exception e) {

			JOptionPane.showMessageDialog((Component) browser,
				    "There is a problem with DB connection: "+e.getMessage(),
				    "Connection Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	//Control panel =============================
	
	private JPanel getPnl_control() 
	{
		if (tbr_control == null) {
			tbr_control = new JPanel();
			tbr_control.add(getBtn_saveBoxTreeModel());
			tbr_control.add(getBtn_saveAreaTreeModel());
			tbr_control.add(getBtn_removePage());
			tbr_control.add(getBtn_clearDB());
		}
		return tbr_control;
	}
	
	private JButton getBtn_saveBoxTreeModel() 
	{
		if (btn_saveBoxTreeModel == null) {
			btn_saveBoxTreeModel = new JButton("Save Box Tree to DB");
			btn_saveBoxTreeModel.setEnabled(false);
			btn_saveBoxTreeModel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					Page page = browser.getPage();
					
					if(page!=null) {
						bdi.insertPageBoxModel(page);
						
						loadAllPages();
					}
					else {
						/*
						JOptionPane.showMessageDialog(mainWindow,
							    "There is not loaded web page! You have to load some before saving it into RDF DB!",
							    "Info",
							    JOptionPane.ERROR_MESSAGE);
							    */
					}
				}
			});
		}
		return btn_saveBoxTreeModel;
	}
	
	private JButton getBtn_removePage() 
	{
		if (btn_removePage == null) {
			btn_removePage = new JButton("Remove Model");
			btn_removePage.setEnabled(false);
			btn_removePage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) 
				{
					try {
						String pageId = cbx_pages.getSelectedItem().toString();
						bdi.removePage(pageId);
						
						loadAllPages();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return btn_removePage;
	}
	
	private JButton getBtn_clearDB() 
	{
		if (btn_clearDB == null) {
			btn_clearDB = new JButton("Clear DB");
			btn_clearDB.setEnabled(false);
			btn_clearDB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					bdi.clearRDFDatabase();
					loadAllPages();
				}
			});
		}
		return btn_clearDB;
	}

	
	/**
	 * stores actual 
	 * @return
	 */
	private JButton getBtn_saveAreaTreeModel() {
		if (btn_saveAreaTreeModel == null) {
			btn_saveAreaTreeModel = new JButton("Save Area Tree to DB");
			btn_saveAreaTreeModel.setEnabled(false);
			btn_saveAreaTreeModel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Page page = browser.getPage();
					AreaTree atree = browser.getAreaTree();
					LogicalAreaTree ltree = browser.getLogicalTree();
					
					if(atree!=null && page!=null) {
						bdi.insertAreaTree(atree, ltree, new URIImpl(cbx_pages.getSelectedItem().toString()));
					}
					
					loadAllPages();
				}
			});
			
			
		}
		return btn_saveAreaTreeModel;
	}
	
	private JComboBox<String> getCbx_areaTrees() {
		if (cbx_areaTrees == null) {
			cbx_areaTrees = new JComboBox<String>();
			cbx_areaTrees.setPreferredSize(new Dimension(300,25));
			
		}
		return cbx_areaTrees;
	}
	
	private JButton getBtn_loadAreaTreeModel() {
		if (btn_loadAreaTreeModel == null) {
			btn_loadAreaTreeModel = new JButton("Load AreaTree");
			btn_loadAreaTreeModel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if(cbx_areaTrees.getItemCount()>0) {
						
						try {
							Model m = bdi.getAreaModelForAreaTreeId( cbx_areaTrees.getSelectedItem().toString() );
							BigdataAreaTree bdAreaTree = new BigdataAreaTree(m, browser.getPage().getSourceURL().toString());
							browser.setAreaTree(bdAreaTree);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			});
		}
		return btn_loadAreaTreeModel;
	}
}
