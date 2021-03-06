/*-
 * Copyright (c) 2016, PHYLOViZ Team <phyloviz@gmail.com>
 * All rights reserved.
 * 
 * This file is part of PHYLOViZ <http://www.phyloviz.net/>.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.phyloviz.pubmlst.wizard;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import net.phyloviz.pubmlst.soap.PubmlstSOAP;

public final class PubMLSTVisualPanel2 extends JPanel {

	private int iMaxST;
	private Task task;
	private String sData;
	private boolean bEmpty;
	private String sPubMLSTDB;
	private PubmlstSOAP soapClient;

	/** Creates new form PubMLSTVisualPanel2 */
	public PubMLSTVisualPanel2() {
		initComponents();

		jToggleButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JToggleButton tb = (JToggleButton) ae.getSource();
				if (tb.isSelected()) {
					jProgressBar1.setString(null);
					task = new Task();
					task.addPropertyChangeListener(new PropertyChangeListener() {

						@Override
						public void propertyChange(PropertyChangeEvent pce) {
							if ("progress" == pce.getPropertyName()) {
								int progress = (Integer) pce.getNewValue();
								jProgressBar1.setValue(progress);
							}
						}
					});
					task.execute();
				} else {
					task.cancel(true);
				}
			}
		});

		try {
			URL url = PubMLSTVisualPanel2.class.getResource("PubMLSTVisualPanel2.html");
			jEditorPane1.setEditorKit(new HTMLEditorKit());
			jEditorPane1.setPage(url);
			Font font = UIManager.getFont("Label.font");
			String bodyRule = "body { font-family: " + font.getFamily() + "; "
					+ "font-size: " + font.getSize() + "pt; width: " + jEditorPane1.getSize().width + "px;}";
			((HTMLDocument) jEditorPane1.getDocument()).getStyleSheet().addRule(bodyRule);
		} catch (IOException e) {
			Logger.getLogger(PubMLSTVisualPanel2.class.getName()).log(Level.WARNING,
				e.getLocalizedMessage());
		}

	}
	
	private PubmlstSOAP getSOAPClient() {
		if (soapClient == null) {
			soapClient = new PubmlstSOAP();
		}
		return soapClient;
	}

	public void setDatabase(String sDBNameShort, String sDBName) {
		// Form initialization
		sPubMLSTDB = sDBNameShort;
		iMaxST = getSOAPClient().getProfileCount(sPubMLSTDB);
		jlPubMLST.setText(sDBName + " (n=" + iMaxST + ")");
		ArrayList<String> alLocus = getSOAPClient().getLocusList(sPubMLSTDB);
		sData = "st";
		String sProfile = "";
		for (String s : alLocus) {
			sData += "\t" + s;
			sProfile += "   " + s;
		}
		jlProfile.setText(sProfile.trim());
		//sData += "\tclonal_complex";
		bEmpty = true;

		// ProgressBar initialization
		jProgressBar1.setMinimum(0);
		jProgressBar1.setMaximum(100);
		jProgressBar1.setStringPainted(true);
		jProgressBar1.setString(null);
		jProgressBar1.setValue(0);
		jToggleButton1.setSelected(false);
		jToggleButton1.setEnabled(true);
	}

	@Override
	public String getName() {
		return "Typing Data";
	}

	public StringReader getTypingData() {
		return new StringReader(sData);
	}

	public boolean isEmpty() {
		return bEmpty;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jlPubMLST = new javax.swing.JLabel();
        jlProfile = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(3, 0, 0, 8));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PubMLSTVisualPanel2.class, "PubMLSTVisualPanel2.jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel3.add(jLabel2);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PubMLSTVisualPanel2.class, "PubMLSTVisualPanel2.jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel3.add(jLabel1);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, org.openide.util.NbBundle.getMessage(PubMLSTVisualPanel2.class, "PubMLSTVisualPanel2.jToggleButton1.text")); // NOI18N
        jToggleButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel3.add(jToggleButton1);

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(3, 0, 0, 8));

        org.openide.awt.Mnemonics.setLocalizedText(jlPubMLST, org.openide.util.NbBundle.getMessage(PubMLSTVisualPanel2.class, "PubMLSTVisualPanel2.jlPubMLST.text")); // NOI18N
        jPanel4.add(jlPubMLST);

        org.openide.awt.Mnemonics.setLocalizedText(jlProfile, org.openide.util.NbBundle.getMessage(PubMLSTVisualPanel2.class, "PubMLSTVisualPanel2.jlProfile.text")); // NOI18N
        jPanel4.add(jlProfile);

        jProgressBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel4.add(jProgressBar1);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jEditorPane1.setBackground(jPanel2.getBackground());
        jEditorPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        jEditorPane1.setEditable(false);
        jEditorPane1.setMaximumSize(new java.awt.Dimension(200, 200));
        add(jEditorPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel jlProfile;
    private javax.swing.JLabel jlPubMLST;
    // End of variables declaration//GEN-END:variables

	class Task extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
		    if (!getSOAPClient().hasConnection()) {
			   bEmpty = true;
			   jProgressBar1.setString(org.openide.util.NbBundle.getMessage(
			 PubMLSTVisualPanel2.class, "Connection.offline"));
			 jToggleButton1.setSelected(false);
			 jToggleButton1.setEnabled(true);
			   return null;
		    }
			for (int st = 1, i = 1; st <= iMaxST; i++) {
				if (isCancelled()) {
					bEmpty = true;
					jProgressBar1.setString("Canceled!");
					return null;
				}
				String profile = getSOAPClient().getProfile(sPubMLSTDB, i);
				if (profile != null) {
					sData += "\n" + profile;
					int percent = st * 100 / iMaxST;
					setProgress(percent);
					jProgressBar1.setString(st + " of " + iMaxST + " STs (" + percent + "%)");
					st++;
				}
				// Failsafe
				if (iMaxST < 1000 && i > 3 * iMaxST
						|| iMaxST > 1000 && i > 2 * iMaxST
						|| iMaxST > 3000 && i > 1.5 * iMaxST) {
					jProgressBar1.setString("Force cancel: too many missing data!");
					break;
				}
			}
			bEmpty = false;
			jToggleButton1.setEnabled(false);
			jProgressBar1.setString("Done!");
			return null;
		}

		@Override
		public void done() {
			setCursor(null); //turn off the wait cursor
		}
	}
}
