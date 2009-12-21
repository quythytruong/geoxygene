/*
 * This file is part of the GeOxygene project source files.
 * 
 * GeOxygene aims at providing an open framework which implements OGC/ISO specifications for
 * the development and deployment of geographic (GIS) applications. It is a open source
 * contribution of the COGIT laboratory at the Institut Géographique National (the French
 * National Mapping Agency).
 * 
 * See: http://oxygene-project.sourceforge.net
 * 
 * Copyright (C) 2005 Institut Géographique National
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this library (see file LICENSE if present); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 */

package fr.ign.cogit.geoxygene.tutorial.exemple.cartetopo;

import java.util.Iterator;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.contrib.cartetopo.Arc;
import fr.ign.cogit.geoxygene.contrib.cartetopo.CarteTopo;
import fr.ign.cogit.geoxygene.contrib.cartetopo.Noeud;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.feature.Population;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;

/** Classe fabrique de cartes topologiques :
 *  - soit par défaut
 *  - soit une carte topologique avec des noeuds valu�s
 * 
 * @author Eric Grosso - IGN / Laboratoire COGIT
 *
 */
public class CarteTopoFactory {
	
	static Logger logger = Logger.getLogger(CarteTopoFactory.class);
	
	
	/**création d'une CarteTopo à partir d'une FT_FeatureCollection
	 * 
	 * @param collection
	 * @return une carte topologique
	 */
	@SuppressWarnings("unchecked")
	public static CarteTopo creeCarteTopoDefaut(
			FT_FeatureCollection<? extends FT_Feature> collection) {
		
		//Initialisation d'une nouvelle CarteTopo
		CarteTopo carteTopo = new CarteTopo("Graphe");

		//R�cup�ration des arcs de la carteTopo
		Population<Arc> arcs = carteTopo.getPopArcs();

		Iterator<FT_Feature> it = (Iterator<FT_Feature>) collection.getElements().iterator();
		FT_Feature feature;
		Arc arc;

		//Import des arcs de la collection dans la carteTopo
		while (it.hasNext()) {
			feature = it.next();
			//création d'un nouvel élément
			arc = arcs.nouvelElement();
			//affectation de la géométrie de l'objet issu de la collection
			//� l'arc de la carteTopo
			arc.setGeometrie((GM_LineString) feature.getGeom());
			//instanciation de la relation entre l'arc créé et l'objet
			//issu de la collection
			arc.addCorrespondant(feature);
		}

		logger.info("Nombre d'arcs créés : " + carteTopo.getPopArcs().size());
		
		//création des noeuds manquants
		logger.info("création des noeuds manquants");
		carteTopo.creeNoeudsManquants(2);
		//création de la topologie Arcs Noeuds
		logger.info("création de la topologie Arcs Noeuds");
		carteTopo.creeTopologieArcsNoeuds(2);
		//La carteTopo est rendue planaire
		logger.info("La carte topologique est rendue planaire");
		carteTopo.rendPlanaire(2);
		logger.info("création des faces de la carte topologique");
		//création des faces de la carteTopo
		carteTopo.creeTopologieFaces();
		
		logger.info("Nombre de faces créées : " + carteTopo.getPopFaces().size());
		
		return carteTopo;
	}

	
	
	/**création d'une carte topologique �tendue (noeud valu�) à partir d'une FT_FeatureCollection
	 * 
	 * @param collection
	 * @return une carte topologique
	 */
	@SuppressWarnings("unchecked")
	public static CarteTopo creeCarteTopoEtendue(
			FT_FeatureCollection<? extends FT_Feature> collection) {
		
		//Initialisation d'une nouvelle CarteTopo
		CarteTopoEtendue carteTopo = new CarteTopoEtendue("Graphe");

		//R�cup�ration des arcs de la carteTopo
		Population<Arc> arcs = carteTopo.getPopArcs();

		Iterator<FT_Feature> it = (Iterator<FT_Feature>) collection.getElements().iterator();
		FT_Feature feature;
		Arc arc;

		//Import des arcs de la collection dans la carteTopo
		while (it.hasNext()) {
			feature = it.next();
			//création d'un nouvel élément
			arc = arcs.nouvelElement();
			//affectation de la géométrie de l'objet issu de la collection
			//� l'arc de la carteTopo
			arc.setGeometrie((GM_LineString) feature.getGeom());
			//instanciation de la relation entre l'arc créé et l'objet
			//issu de la collection
			arc.addCorrespondant(feature);
		}

		logger.info("Nombre d'arcs créés : " + carteTopo.getPopArcs().size());
		
		//création des noeuds manquants
		logger.info("création des noeuds manquants");
		carteTopo.creeNoeudsManquants(2);
		//création de la topologie Arcs Noeuds
		logger.info("création de la topologie Arcs Noeuds");
		carteTopo.creeTopologieArcsNoeuds(2);
		//Valuation des noeuds
		logger.info("Valuation des noeuds");

		//R�cup�ration des noeuds de la carteTopo
		Population<Noeud> noeuds = carteTopo.getPopNoeuds();
		
		Iterator<Noeud> itNoeuds = noeuds.iterator();
		while (itNoeuds.hasNext()) {
			NoeudValue noeudValue = (NoeudValue)itNoeuds.next();
			noeudValue.setDegre(noeudValue.arcs().size());
		}
		
		return carteTopo;
	}
	
}