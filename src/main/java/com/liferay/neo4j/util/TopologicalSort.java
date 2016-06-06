package com.liferay.neo4j.util;

import com.liferay.neo4j.result.StringResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Procedure;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * @author daniel.kocsis
 * @author mate.thurzo
 */
public class TopologicalSort {

	@Context
	public Log log;

	@Context
	public GraphDatabaseService db;

	@Procedure
	public Stream<StringResult> sort() {
		Node startNode = db.findNode(Label.label("Group"), "entityId", 1);

		try {
			TopologicalNode[] sortedNodes = heapSort(
				topologicalSort(TopologicalNode.create(startNode)));

			StringBuffer sb = new StringBuffer();

			sb.append("Topological order: ");

			for (TopologicalNode node : sortedNodes) {
				sb.append(node.getProperty("name"));
				sb.append("    ");
			}

			return Stream.of(new StringResult(sb.toString()));
		}
		catch (Exception e) {
			return Stream.of(new StringResult(e.getMessage()));
		}
	}

	protected void buildHeap(TopologicalNode[] nodes) {
		int n = nodes.length - 1;

		for (int i = n/2; i >= 0; i--) {
			maxHeap(n, nodes, i);
		}
	}

	protected void exchange(
		TopologicalNode[] manifestTreeNodes, int i, int j) {

		TopologicalNode node = manifestTreeNodes[i];
		manifestTreeNodes[i] = manifestTreeNodes[j];
		manifestTreeNodes[j] = node;
	}

	protected TopologicalNode[] heapSort(
		Collection<TopologicalNode> manifestTreeNodes) {

		TopologicalNode[] nodesArray = manifestTreeNodes.toArray(
			new TopologicalNode[manifestTreeNodes.size()]);

		buildHeap(nodesArray);

		int maxIndex = nodesArray.length - 1;

		for (int i = maxIndex; i > 0; i--) {
			exchange(nodesArray, 0, i);
			maxIndex--;
			maxHeap(maxIndex, nodesArray, 0);
		}

		return nodesArray;
	}

	protected void maxHeap(int n, TopologicalNode[] manifestTreeNodes, int i) {
		int left = 2 * i;

		int right = left + 1;
		int largest = -1;

		if ((left <= n) &&
			(manifestTreeNodes[left].getTopologicalLabel() >
				manifestTreeNodes[i].getTopologicalLabel())) {

			largest = left;
		}
		else {
			largest = i;
		}

		if ((right <= n) &&
			(manifestTreeNodes[right].getTopologicalLabel() >
				manifestTreeNodes[largest].getTopologicalLabel())) {

			largest = right;
		}

		if (largest != i) {
			exchange(manifestTreeNodes, i, largest);
			maxHeap(n, manifestTreeNodes, largest);
		}
	}

	protected List<TopologicalNode> topologicalSort(TopologicalNode rootNode)
		throws Exception {

		Stack<TopologicalNode> sortedNodes = new Stack<>();
		Set<TopologicalNode> markedNodes = new HashSet<>();
		Set<TopologicalNode> tempMarkedNodes = new HashSet<>();

		visit(tempMarkedNodes, markedNodes, sortedNodes, rootNode, 0);

		return sortedNodes;
	}

	protected void visit(
			Set<TopologicalNode> tempMarkedNodes,
			Set<TopologicalNode> markedNodes,
			Stack<TopologicalNode> sortedNodes,
			TopologicalNode manifestTreeNode,
			int depth)
		throws Exception {

		if (tempMarkedNodes.contains(manifestTreeNode)) {
			throw new Exception("Not a DAG");
		}

		tempMarkedNodes.add(manifestTreeNode);

		if (markedNodes.contains(manifestTreeNode)) {
			return;
		}

		manifestTreeNode.setTopologicalLabel(depth++);

		for (TopologicalNode dependentNode : manifestTreeNode.getChildren()) {
			log.info(String.valueOf(dependentNode.getProperty("name")));

			visit(
				tempMarkedNodes, markedNodes, sortedNodes, dependentNode,
				depth);
		}

		markedNodes.add(manifestTreeNode);
		tempMarkedNodes.remove(manifestTreeNode);
		sortedNodes.push(manifestTreeNode);
	}

}
