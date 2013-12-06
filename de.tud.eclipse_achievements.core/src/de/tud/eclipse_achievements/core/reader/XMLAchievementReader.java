/*******************************************************************************
 * Copyright (c) 2012.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Max Leuthaeuser, Christian Vogel, Uwe Schmidt
 ******************************************************************************/
/**
 * 
 */
package de.tud.eclipse_achievements.core.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tud.eclipse_achievements.core.AchievementCorePlugin;
import de.tud.eclipse_achievements.index.entities.Achievement;

/**
 * @author Max
 */
public class XMLAchievementReader implements IAchievementReader {
	private static final String PATH = "resources/achievements.xml";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Achievement> read() {
		InputStream inputStream = null;
		
		try {
			inputStream = FileLocator.openStream(AchievementCorePlugin.getDefault()
					.getBundle(), new Path(PATH), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loadAchievements(inputStream);
	}

	/**
	 * Load achievements from XML input stream.
	 */
	private Collection<Achievement> loadAchievements(InputStream inputStream) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		
		List<Achievement> m_achievements = new LinkedList<Achievement>();
		
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(inputStream);

			NodeList catNodes = doc.getElementsByTagName("category");
			for (int i = 0; i < catNodes.getLength(); ++i) {
				Node n = catNodes.item(i);
				
				//String catName = n.getAttributes().getNamedItem("name")
						//.getNodeValue();

				m_achievements.addAll(parseAchievements(n));
			}
			
			return m_achievements;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Collections.emptyList();
	}

	private List<Achievement> parseAchievements(Node n)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		List<Achievement> achievements = new ArrayList<Achievement>();
		
		for (Node childNode = n.getFirstChild(); childNode != null;) {
			Node nextChild = childNode.getNextSibling();
			if ("achievement".equals(childNode.getNodeName())) {
				String className = childNode.getAttributes()
						.getNamedItem("className").getNodeValue();
				System.out.println("Loading: " + className);
				
				//Class<?> theClass = Class.forName(className);
				
				Achievement achievement = new Achievement();
				
				parseAchievementNode(childNode, achievement);
				
				achievements.add(achievement);
			}
			childNode = nextChild;
		}
		
		return achievements;
	}
	
	private void parseAchievementNode(Node node, Achievement achievement) {
		Node nextChild = null;
		for (Node childNode = node.getFirstChild(); childNode != null;) {
			nextChild = childNode.getNextSibling();
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				parseAchievementPropertyNode(childNode,achievement);
			}
			childNode = nextChild;
		}
	}
	
	private void parseAchievementPropertyNode(Node node, Achievement achievement) {
		String nodeName = node.getNodeName();
		System.out.println("node: " + nodeName);
		String value = node.getTextContent();
		System.out.println("value: " + value);
		
		if ("name".equals(nodeName)) {
			achievement.setName(value);
		} else if ("description".equals(nodeName)) {
			achievement.setDescription(value);
		} else if ("icon".equals(nodeName)) {
			achievement.setIcon(value);
		} else if ("requiredCompletions".equals(nodeName)) {
			achievement.setMaxCompletion(Integer.parseInt(value));
		}
	}

}
