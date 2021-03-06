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

package net.phyloviz.goeburst.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import net.phyloviz.core.data.Profile;

public abstract class Cluster<T extends Profile> implements Comparable<Cluster> {
	protected int id;
	protected TreeSet<T> nodes;
	protected ArrayList<Edge<T>> edges;
	protected int visibleEdges;
	protected int isolates;
	
	public Cluster () {
		id = 0;
		nodes = new TreeSet<T>();
		edges = new ArrayList<Edge<T>>();
		isolates = 0;
		visibleEdges = 0;
	}
	
	public Collection<T> getSTs() {
		return nodes;
	}
	
	public ArrayList<Edge<T>> getEdges() {
		return edges;
	}

	public int size() {
		return nodes.size();
	}
	
	public int getIsolates() {
		return isolates;
	}
	
	public int getVisibleEdges() {
		return visibleEdges;
	}
	
	public boolean add(T st) {
		// Note that a 'TreeSet' only stores distinct elements. 
		if (!nodes.add(st))
			return false;
		
		isolates += st.getFreq();
		
		return true;
	}
	
	public void add(Edge<T> e) {
		add(e.getU());
		add(e.getV());
		edges.add(e);
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}

	// TODO: rewrite this stuff...
	@Override
	public int compareTo(Cluster g) {
		return g.size() - size();
	}

	public boolean equals(Cluster g) {
		return compareTo(g) == 0;
	}
}
