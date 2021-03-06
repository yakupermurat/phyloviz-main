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
package net.phyloviz.mlvadist;

import java.util.Comparator;
import net.phyloviz.algo.Edge;
import net.phyloviz.algo.AbstractDistance;
import net.phyloviz.core.data.Profile;
import net.phyloviz.core.data.TypingData;
import net.phyloviz.goeburst.tree.GOeBurstNode;
import net.phyloviz.mlva.MLVAType;

public class MLVADistance implements AbstractDistance<GOeBurstNode> {

	private int maxLV;
	private Comparator<Edge<GOeBurstNode>> ecmp;
	private Comparator<GOeBurstNode> pcmp;

	public MLVADistance(TypingData<? extends Profile> td, int maxLevel) {
		// -1 because of the id...
		maxLV = maxLevel;
		ecmp = new EdgeComparator();
		pcmp = new ProfileComparator();
	}

	public MLVADistance(TypingData<? extends Profile> td) {
		this(td, td.getHeaders().size() - 1);
	}

	
	@Override
	public int level(GOeBurstNode px, GOeBurstNode py) {
		int diffs = 0;

		for (int i = 0; i < px.profileLength(); i++) {
			if (px.getValue(i).compareTo(py.getValue(i)) != 0) {
				diffs++;
			}
		}

		return diffs;
	}

	@Override
	public int level(Edge<GOeBurstNode> e) {
		return level(e.getU(), e.getV());
	}

	@Override
	public int maxLevel() {
		return maxLV;
	}

	@Override
	public int compare(Edge<GOeBurstNode> f, Edge<GOeBurstNode> e) {
		return ecmp.compare(f, e);
	}

	@Override
	public String toString() {
		return "Euclidean + goeBURST distance";
	}

	@Override
	public int compare(GOeBurstNode px, GOeBurstNode py) {
		return pcmp.compare(px, py);
	}

	@Override
	public Comparator<GOeBurstNode> getProfileComparator() {
		return pcmp;
	}

	@Override
	public Comparator<Edge<GOeBurstNode>> getEdgeComparator() {
		return ecmp;
	}

	@Override
	public String info(GOeBurstNode px, GOeBurstNode py) {

		MLVAType a, b;

		a = (MLVAType) px.getProfile();	
		b = (MLVAType) py.getProfile();	
		double fVal = 0;
		for (int i = 0; i < a.profileLength(); i++)
			fVal += Math.pow(a.getDouble(i) - b.getDouble(i), 2);
		fVal = Math.sqrt(fVal);

		return "euclidean distance: " + fVal;
	}

	@Override
	public String info(Edge<GOeBurstNode> e) {
		return info(e.getU(), e.getV());
	}

	@Override
	public boolean configurable() {
		return false;
	}

	@Override
	public void configure() {
		// Do nothing
	}
	
	private class ProfileComparator implements Comparator<GOeBurstNode> {

		@Override
		public int compare(GOeBurstNode u, GOeBurstNode v) {
			return u.diffLV(v);
		}
	}
	
	private class EdgeComparator implements Comparator<Edge<GOeBurstNode>> {

		@Override
		public int compare(Edge<GOeBurstNode> f, Edge<GOeBurstNode> e) {
			int ret = 0;
			int k = 0;
			int lv = 0;

			ret = level(f) - level(e);
			if (ret != 0) {
				return ret;
			}

			
			MLVAType a, b;

			a = (MLVAType) f.getU().getProfile();	
			b = (MLVAType) f.getV().getProfile();	
			double fVal = 0;
			for (int i = 0; i < a.profileLength(); i++)
				fVal += Math.pow(a.getDouble(i) - b.getDouble(i), 2);
			fVal = Math.sqrt(fVal);
			
			a = (MLVAType) e.getU().getProfile();	
			b = (MLVAType) e.getV().getProfile();	
			double eVal = 0;
			for (int i = 0; i < a.profileLength(); i++)
				eVal += Math.pow(a.getDouble(i) - b.getDouble(i), 2);
			eVal = Math.sqrt(eVal);

			ret = Double.compare(fVal, eVal);
			if (ret != 0) {
				return Integer.signum(ret);
			}

			
			while (k < maxLV) {

				ret = Math.max( f.getU().getLV(k), f.getV().getLV(k))
					- Math.max(e.getU().getLV(k), e.getV().getLV(k));

				lv ++;
				
				if (ret != 0) {
					break;
				}

				ret = Math.min(f.getU().getLV(k), f.getV().getLV(k))
					- Math.min(e.getU().getLV(k), e.getV().getLV(k));

				lv ++;
				
				if (ret != 0) {
					break;
				}

				k++;
			}
		
			/* SAT is ignored! */

			/* ST frequency. */
			if (k >= maxLV) {

				lv = 2*maxLV + 2;
				
				ret = Math.max(f.getU().getFreq(), f.getV().getFreq())
					- Math.max(e.getU().getFreq(), e.getV().getFreq());

				lv ++;
				
				if (ret == 0) {
					ret = Math.min(f.getU().getFreq(), f.getV().getFreq())
						- Math.min(e.getU().getFreq(), e.getV().getFreq());
					lv ++;
				}
			}

			/* Decreasing... */
			ret *= -1;
			Comparator<String> cmp = new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					int size = o1.length() - o2.length();
					if (size != 0) {
						return size;
					}
					return o1.compareTo(o2);
				}
			};

			/* Last chance... */
			if (ret == 0) {

				String fMinId, fMaxId, eMinId, eMaxId;
				
				if (cmp.compare(f.getU().getID(), f.getV().getID()) < 0) {
					fMinId = f.getU().getID();
					fMaxId = f.getV().getID();
				} else {
					fMinId = f.getV().getID();
					fMaxId = f.getU().getID();
				}
					
				if (cmp.compare(e.getU().getID(), e.getV().getID()) < 0) {
					eMinId = e.getU().getID();
					eMaxId = e.getV().getID();
				} else {
					eMinId = e.getV().getID();
					eMaxId = e.getU().getID();
				}

				ret = fMinId.compareTo(eMinId);

				if (ret == 0)	
					ret = fMaxId.compareTo(eMaxId);
				
				lv += 2;
			}


			
			return Integer.signum(ret)*lv;
		}
	}
}
