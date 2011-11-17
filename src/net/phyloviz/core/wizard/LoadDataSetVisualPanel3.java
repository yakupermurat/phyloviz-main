/*-
 * Copyright (c) 2011, PHYLOViZ Team <phyloviz@gmail.com>
 * All rights reserved.
 * 
 * This file is part of PHYLOViZ <http://www.phyloviz.net>.
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
 * 
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole combination.
 * 
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent modules,
 * and to copy and distribute the resulting executable under terms of your
 * choice, provided that you also meet, for each linked independent module,
 * the terms and conditions of the license of that module.  An independent
 * module is a module which is not derived from or based on this library.
 * If you modify this library, you may extend this exception to your version
 * of the library, but you are not obligated to do so.  If you do not wish
 * to do so, delete this exception statement from your version.
 */

package net.phyloviz.core.wizard;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.openide.util.NbPreferences;

public final class LoadDataSetVisualPanel3 extends JPanel {

	private DefaultComboBoxModel keyListModel;

	/** Creates new form LoadDataSetVisualPanel2 */
	public LoadDataSetVisualPanel3() {
		keyListModel = new DefaultComboBoxModel();
		initComponents();

		try {
			URL url = LoadDataSetVisualPanel1.class.getResource("LoadDataSetVisualPanel3.html");
			jEditorPane1.setEditorKit(new HTMLEditorKit());
			jEditorPane1.setPage(url);
			Font font = UIManager.getFont("Label.font");
			String bodyRule = "body { font-family: " + font.getFamily() + "; "
				+ "font-size: " + font.getSize() + "pt; width: " + jEditorPane1.getSize().width + "px;}";
			((HTMLDocument) jEditorPane1.getDocument()).getStyleSheet().addRule(bodyRule);
		} catch (IOException e) {
			// Do nothing...
			System.err.println(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return "Isolate Data";
	}

	public String getFilename() {
		return jTextField1.getText();
	}

	public String getKey() {
		return (String) jComboBox1.getSelectedItem();
	}

	public int getKeyIndex() {
		return jComboBox1.getSelectedIndex();
	}

	private void updateKeyList(String[] result) {

		keyListModel.removeAllElements();
		if (result == null)
			return;

		for (int i = 0; i < result.length; i++)
			keyListModel.addElement(result[i]);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(2, 0, 0, 8));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(LoadDataSetVisualPanel3.class, "LoadDataSetVisualPanel3.jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel4.add(jLabel1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(LoadDataSetVisualPanel3.class, "LoadDataSetVisualPanel3.jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel4.add(jLabel2);

        jPanel3.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel5.setLayout(new java.awt.GridLayout(2, 0, 0, 8));

        jTextField1.setText(org.openide.util.NbBundle.getMessage(LoadDataSetVisualPanel3.class, "LoadDataSetVisualPanel3.jTextField1.text")); // NOI18N
        jPanel5.add(jTextField1);

        jComboBox1.setModel(keyListModel);
        jPanel5.add(jComboBox1);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        jPanel6.setLayout(new java.awt.GridLayout(2, 0, 0, 8));

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(LoadDataSetVisualPanel3.class, "LoadDataSetVisualPanel3.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1);

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(LoadDataSetVisualPanel3.class, "LoadDataSetVisualPanel3.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2);

        jPanel3.add(jPanel6, java.awt.BorderLayout.EAST);

        add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBorder(null);

        jEditorPane1.setBackground(jPanel3.getBackground());
        jEditorPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        jEditorPane1.setEditable(false);
        jEditorPane1.setMaximumSize(new java.awt.Dimension(200, 200));
        jScrollPane1.setViewportView(jEditorPane1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		if (fc == null)
			fc = new JFileChooser();
		String dir = NbPreferences.forModule(LoadDataSetWizardAction.class).get("LAST_DIR", "");
		if (dir != null)
			fc.setCurrentDirectory(new File(dir));
		int r = fc.showDialog(this, "Open");
		if (r == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
			jTextField1.setText(fc.getSelectedFile().getAbsolutePath());
			NbPreferences.forModule(LoadDataSetWizardAction.class).put("LAST_DIR", fc.getCurrentDirectory().getPath());

			String[] result = null;
			try {
				// Get headers...
				 result = new BufferedReader(new FileReader(fc.getSelectedFile())).readLine().split("\t", -1);
			} catch (FileNotFoundException ex) {
				// If there is any problem with the file, we will check it later.
			} catch (IOException ex) {
				// If there is any problem with the file, we will check it later.
			}
			updateKeyList(result);
		}
}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

		String[] result = null;
			try {
				// Get headers...
				 result = new BufferedReader(new FileReader(new File(jTextField1.getText()))).readLine().split("\t", -1);

			} catch (FileNotFoundException ex) {
				// If there is any problem with the file, we will check it later.
			} catch (IOException ex) {
				// If there is any problem with the file, we will check it later.
			}
			updateKeyList(result);
	}//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

	private JFileChooser fc;
}
