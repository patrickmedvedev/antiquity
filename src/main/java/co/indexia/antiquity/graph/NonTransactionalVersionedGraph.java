/**
 * Copyright (c) 2012-2013 "Indexia Technologies, ltd."
 *
 * This file is part of Antiquity.
 *
 * Antiquity is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package co.indexia.antiquity.graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * A non transactional {@link VersionedGraph} implementation.
 * 
 * @param <T>
 *            The type of the graph
 * @param <V>
 *            The type of the graph version
 */
public abstract class NonTransactionalVersionedGraph<T extends Graph, V extends Comparable<V>> extends VersionedGraph<T, V> {
	public NonTransactionalVersionedGraph(T baseGraph) {
		super(baseGraph);
	}

	// Versioned Graph Events
	// --------------------------------------------------------------
	@Override
	public void vertexAdded(Vertex vertex) {
		System.out.println(String.format("==Vertex [%s] added==", vertex));
		versionAddedVertices(getNextGraphVersion(), Arrays.asList(vertex));
	}

	@Override
	public void vertexPropertyChanged(Vertex vertex, String key, Object oldValue, Object setValue) {
		System.out.println(String.format("==Vertex [%s] property[%s] was modified [%s -> %s]==",
				vertex,
				key,
				oldValue,
				setValue));

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(key, oldValue);

		versionModifiedVertex(getLatestGraphVersion(), getNextGraphVersion(), vertex, props);
	}

	@Override
	public void vertexPropertyRemoved(Vertex vertex, String key, Object removedValue) {
		System.out.println(String.format("==Vertex property [%s] was removed [%s->%s]==", vertex, removedValue));

		Map<String, Object> props = new HashMap<String, Object>();
		props.put(key, removedValue);

		versionModifiedVertex(getLatestGraphVersion(), getNextGraphVersion(), vertex, props);
	}

	@Override
	public void vertexRemoved(Vertex vertex) {
		System.out.println(String.format("==Vertex [%s] removed==", vertex));
		versionRemovedVertices(getNextGraphVersion(), Arrays.asList(vertex));
	}

	@Override
	public void edgeAdded(Edge edge) {
		System.out.println(String.format("==Edge [%s] added==", edge));
		versionAddedEdges(getNextGraphVersion(), Arrays.asList(edge));
	}

	@Override
	public void edgePropertyChanged(Edge edge, String key, Object oldValue, Object setValue) {
		// Currently modified edges values are not versioned
	}

	@Override
	public void edgePropertyRemoved(Edge edge, String key, Object removedValue) {
		// Currently modified edges values are not versioned
	}

	@Override
	public void edgeRemoved(Edge edge) {
		System.out.println(String.format("==Edge [%s] removed==", edge));
		versionRemovedEdges(getNextGraphVersion(), Arrays.asList(edge));
	}
}
