package com.liferay.neo4j.util;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author mate.thurzo
 */
public class TopologicalNode implements Node {

	public static TopologicalNode create(Node node) {
		return new TopologicalNode(node);
	}

	private TopologicalNode(Node node) {
		_node = node;
	}

	public List<TopologicalNode> getChildren() {
		Iterable<Relationship> relationships = _node.getRelationships(
			Direction.OUTGOING);

		List<TopologicalNode> children = new ArrayList<>();

		for (Relationship relationship : relationships) {
			Node otherNode = relationship.getOtherNode(_node);

			children.add(TopologicalNode.create(otherNode));
		}

		return children;
	}

	@Override
	public long getId() {
		return _node.getId();
	}

	@Override
	public void delete() {
		_node.delete();
	}

	@Override
	public Iterable<Relationship> getRelationships() {
		return _node.getRelationships();
	}

	@Override
	public boolean hasRelationship() {
		return _node.hasRelationship();
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType... relationshipTypes) {
		return _node.getRelationships(relationshipTypes);
	}

	@Override
	public Iterable<Relationship> getRelationships(Direction direction, RelationshipType... relationshipTypes) {
		return _node.getRelationships(direction, relationshipTypes);
	}

	@Override
	public boolean hasRelationship(RelationshipType... relationshipTypes) {
		return _node.hasRelationship(relationshipTypes);
	}

	@Override
	public boolean hasRelationship(Direction direction, RelationshipType... relationshipTypes) {
		return _node.hasRelationship(direction, relationshipTypes);
	}

	@Override
	public Iterable<Relationship> getRelationships(Direction direction) {
		return _node.getRelationships(direction);
	}

	@Override
	public boolean hasRelationship(Direction direction) {
		return _node.hasRelationship(direction);
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType relationshipType, Direction direction) {
		return _node.getRelationships(relationshipType, direction);
	}

	@Override
	public boolean hasRelationship(RelationshipType relationshipType, Direction direction) {
		return _node.hasRelationship(relationshipType, direction);
	}

	@Override
	public Relationship getSingleRelationship(RelationshipType relationshipType, Direction direction) {
		return _node.getSingleRelationship(relationshipType, direction);
	}

	@Override
	public Relationship createRelationshipTo(Node node, RelationshipType relationshipType) {
		return _node.createRelationshipTo(node, relationshipType);
	}

	@Override
	public Iterable<RelationshipType> getRelationshipTypes() {
		return _node.getRelationshipTypes();
	}

	@Override
	public int getDegree() {
		return _node.getDegree();
	}

	@Override
	public int getDegree(RelationshipType relationshipType) {
		return _node.getDegree(relationshipType);
	}

	@Override
	public int getDegree(Direction direction) {
		return _node.getDegree(direction);
	}

	@Override
	public int getDegree(RelationshipType relationshipType, Direction direction) {
		return _node.getDegree(relationshipType, direction);
	}

	@Override
	public void addLabel(Label label) {
		_node.addLabel(label);
	}

	@Override
	public void removeLabel(Label label) {
		_node.removeLabel(label);
	}

	@Override
	public boolean hasLabel(Label label) {
		return _node.hasLabel(label);
	}

	public int getTopologicalLabel() {
		return _topologicalLabel;
	}

	public void setTopologicalLabel(int topologicalLabel) {
		_topologicalLabel = topologicalLabel;
	}

	@Override
	public Iterable<Label> getLabels() {
		return _node.getLabels();
	}

	@Override
	public GraphDatabaseService getGraphDatabase() {
		return _node.getGraphDatabase();
	}

	@Override
	public boolean hasProperty(String s) {
		return _node.hasProperty(s);
	}

	@Override
	public Object getProperty(String s) {
		return _node.getProperty(s);
	}

	@Override
	public Object getProperty(String s, Object o) {
		return _node.getProperty(s, o);
	}

	@Override
	public void setProperty(String s, Object o) {
		_node.setProperty(s, o);
	}

	@Override
	public Object removeProperty(String s) {
		return _node.removeProperty(s);
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		return _node.getPropertyKeys();
	}

	@Override
	public Map<String, Object> getProperties(String... strings) {
		return _node.getProperties(strings);
	}

	@Override
	public Map<String, Object> getAllProperties() {
		return _node.getAllProperties();
	}

	private Node _node;
	private int _topologicalLabel;

}
