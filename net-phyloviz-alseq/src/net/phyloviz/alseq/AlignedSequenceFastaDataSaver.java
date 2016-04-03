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

package net.phyloviz.alseq;

import javax.swing.table.TableModel;
import net.phyloviz.core.data.DataModel;
import net.phyloviz.core.data.DataSaver;

/**
 *
 * @author martanascimento
 */
public class AlignedSequenceFastaDataSaver extends DataSaver {

    public AlignedSequenceFastaDataSaver(DataModel dm) {
        super(dm);
    }

    @Override
    public String toSave() {
        StringBuilder sb = new StringBuilder();
        TableModel tm = dm.tableModel();

        for (int k = 0; k < tm.getRowCount(); k++) {
            sb.append(">").append(tm.getValueAt(k, 0)).append("\n");
            for (int i = 1; i < tm.getColumnCount(); i++) {
                sb.append(tm.getValueAt(k, i));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
