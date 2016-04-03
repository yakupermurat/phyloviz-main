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

package net.phyloviz.nj.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;
import net.phyloviz.core.data.Profile;
import net.phyloviz.core.data.TypingData;
import net.phyloviz.algo.AbstractDistance;
import net.phyloviz.algo.DistanceProvider;
import net.phyloviz.upgmanjcore.distance.ClusteringDistance;
import org.openide.util.Lookup;

public final class NeighborJoiningVisualPanel1 extends JPanel {

	private DefaultComboBoxModel typeListModel;

	/** Creates new form NeighborJoingVisualPanel1 */
	public NeighborJoiningVisualPanel1(TypingData<? extends Profile> td) {

		typeListModel = new DefaultComboBoxModel();
		Collection<? extends DistanceProvider> result = Lookup.getDefault().lookupAll(DistanceProvider.class);  //gets all NJDistanceProviders
		Iterator<? extends DistanceProvider> ir = result.iterator();
		while (ir.hasNext()) {      //for each NJDistanceProvider get NJDistance
			AbstractDistance ad = ir.next().getDistance(td);
			if (ad != null && ad instanceof ClusteringDistance){
				typeListModel.addElement(ad);
                                //break;
                        }
		}

		initComponents();
		String dfn = org.openide.util.NbBundle.getMessage(typeListModel.getSelectedItem().getClass(), "AbstractDistance.description");
		URL url = typeListModel.getSelectedItem().getClass().getResource(dfn);
		if (url != null) {
					try {
			jEditorPane1.setPage(url);
			} catch (IOException e) {
				// Do nothing...
				System.err.println(e.getMessage());
			}

			Font font = UIManager.getFont("Label.font");
			String bodyRule = "body { font-family: " + font.getFamily() + "; "
				+ "font-size: " + font.getSize() + "pt; width: " + jPanel2.getParent().getSize().width + "px;}";
			((HTMLDocument) jEditorPane1.getDocument()).getStyleSheet().addRule(bodyRule);
		}
		
		jComboBox1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String dfn = org.openide.util.NbBundle.getMessage(typeListModel.getSelectedItem().getClass(), "AbstractDistance.description");
				URL url = typeListModel.getSelectedItem().getClass().getResource(dfn);
				if (url != null) {
					try {
						jEditorPane1.setPage(url);
					} catch (IOException e) {
						// Do nothing...
						System.err.println(e.getMessage());
					}

					Font font = UIManager.getFont("Label.font");
					String bodyRule = "body { font-family: " + font.getFamily() + "; "
						+ "font-size: " + font.getSize() + "pt; width: " + jPanel2.getParent().getSize().width + "px;}";
					((HTMLDocument) jEditorPane1.getDocument()).getStyleSheet().addRule(bodyRule);
				}
			}
		});

		
	}

	@Override
	public String getName() {
		return "Distance";
	}

	public AbstractDistance getDistance() {
		return (AbstractDistance) jComboBox1.getSelectedItem();
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
        jPanel4 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(1, 0, 0, 8));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(NeighborJoiningVisualPanel1.class, "NeighborJoiningVisualPanel1.jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 12, 2, 8));
        jPanel3.add(jLabel2);

        jPanel2.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0, 0, 8));

        jComboBox1.setModel(typeListModel);
        jPanel4.add(jComboBox1);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);
        jPanel2.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(NeighborJoiningVisualPanel1.class, "NeighborJoiningVisualPanel1.jLabel3.text")); // NOI18N
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        jPanel1.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1, java.awt.BorderLayout.SOUTH);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(jPanel5.getBackground());
        jEditorPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));
        jEditorPane1.setContentType(org.openide.util.NbBundle.getMessage(NeighborJoiningVisualPanel1.class, "NeighborJoiningVisualPanel1.jEditorPane1.contentType")); // NOI18N
        jEditorPane1.setMaximumSize(new java.awt.Dimension(200, 200));
        jScrollPane1.setViewportView(jEditorPane1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
