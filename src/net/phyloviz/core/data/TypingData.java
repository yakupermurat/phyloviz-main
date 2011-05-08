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

/* Fri Nov 12 15:38:55 UTC 2010
 * Created by aplf@
 */

package net.phyloviz.core.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.table.AbstractTableModel;
import net.phyloviz.core.explorer.TypingDataNode;
import net.phyloviz.core.util.NodeFactory;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class TypingData<T extends AbstractProfile> implements DataModel, Lookup.Provider, NodeFactory {

	private InstanceContent ic;
	private AbstractLookup lookup;

	private String description;
	private String[] headers;
	private HashSet<String>[] domains;
	private HashMap<String, Integer> h2idx;

	private ArrayList<T> collection;
	private TreeSet<T> unique;

	private TableModel model;
	private DataSaver saver;

	private boolean weightOk;
	private int weight;

	@SuppressWarnings("unchecked")
	public TypingData(int nColumns) {
		headers = new String[nColumns];
		domains = new HashSet[nColumns];
		for (int i = 0; i < nColumns; i++)
			domains[i] = new HashSet<String>();
		h2idx = new HashMap<String, Integer>();

		collection = new ArrayList<T>();
		unique = new TreeSet<T>();

		weightOk = false;
	}

	public TypingData(String[] ha) {
		this(ha.length);
		System.arraycopy(ha, 0, headers, 0, ha.length);
	}

	@Override
	public int size() {
		return collection.size();
	}

	public void setHeader(int idx, String header) {
		if (idx >= 0 && idx < headers.length)
			headers[idx] = header;
	}

	public Collection<String> getHeaders() {
		return Arrays.asList(headers);
	}

	public boolean addData(T profile) {

		// Do we have values for all fields?
		if (profile.profileLength() != headers.length - 1)
			return false;

		if (! unique.add(profile))
			return false;

		domains[0].add(profile.getID());
		for (int i = 0; i < profile.profileLength(); i++)
			domains[i+1].add(profile.getValue(i));

		collection.add(profile);

		weightOk = false;

		return true;
	}

	public T getEqual(T profile) {
		T p = unique.ceiling(profile);

		if (p.equals(profile))
			return p;

		return null;
	}

	public void sort(Comparator<T> c) {
		Collections.sort(collection, c);
	}

	@Override
	public DataIterator iterator() {
		return new DataIterator();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		description = desc;
	}

	@Override
	public HashSet<String> getDomain(int idx) {
		if (idx < 0 || idx > domains.length)
			return null;

		return domains[idx];
	}

	@Override
	public AbstractNode getNode() {
		return new TypingDataNode(this);
	}

	@Override
	public Lookup getLookup() {
		return lookup;
	}

	public void add(Object o) {
		ic.add(o);
	}

	public void remove(Object o) {
		ic.remove(o);
	}

	@Override
	public int getKey() {
		return 0;
	}

	@Override
	public String getDomainLabel(int idx) {
		if (idx < 0 || idx >= headers.length)
			return null;

		return headers[idx];
	}

	@Override
	public Collection<? extends DataItem> getItems() {
		return new ArrayList<T>(collection);
	}

	@Override
	public synchronized int weight() {

		if (! weightOk ) {
			weight = 0;
			Iterator<T> i = collection.iterator();
			while (i.hasNext())
				weight += i.next().weight();
		}

		return weight;
	}

	@Override
	public boolean addColumn(String domainLabel, ColumnFiller cf) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public class DataIterator implements Iterator<T> {

		private Iterator<T> i;
		private T last;
		private int idx;

		public DataIterator() {
			i = collection.iterator();
			last = null;
			idx = 0;
		}

		@Override
		public boolean hasNext() {
			return i.hasNext();
		}

		@Override
		public T next() {
			last = i.next();
			idx ++;
			return last;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supported!");
		}

		public String getID() {
			if (last == null)
				return null;
			
			return last.getID();
		}

		public String getValue(int idx) {
			if (last == null)
				return null;

			return last.getValue(idx);
		}

		public String getValue(String column) {
			return getValue(h2idx.get(column) - 1);
		}

		public int index() {
			return idx;
		}
	}

	@Override
	public String toString() {
		return description;
	}

	@Override
	public TableModel tableModel() {
		if (model == null)
			model = new TableModel();

		return model;
	}

	@Override
	public DataSaver getSaver() {
		if (saver == null)
			saver = new DataSaver(this);

		return saver;
	}

	public class TableModel extends AbstractTableModel {

		@Override
		public int getRowCount() {
			return size();
		}

		@Override
		public int getColumnCount() {
			return headers.length;
		}

		@Override
		public String getColumnName(int idx) {
			return getDomainLabel(idx);
		}

		@Override
		public int findColumn(String name) {
			if (h2idx.containsKey(name))
				return h2idx.get(name);

			return -1;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex >= collection.size())
				return null;

			return collection.get(rowIndex).get(columnIndex);
		}
	}
}
