/*
 * This file is part of the GeOxygene project source files.
 * 
 * GeOxygene aims at providing an open framework which implements OGC/ISO specifications for
 * the development and deployment of geographic (GIS) applications. It is a open source
 * contribution of the COGIT laboratory at the Institut G�ographique National (the French
 * National Mapping Agency).
 * 
 * See: http://oxygene-project.sourceforge.net
 * 
 * Copyright (C) 2005 Institut G�ographique National
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

package fr.ign.cogit.geoxygene.contrib.delaunay;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import fr.ign.cogit.geoxygene.contrib.cartetopo.Chargeur;
import fr.ign.cogit.geoxygene.contrib.geometrie.Operateurs;
import fr.ign.cogit.geoxygene.feature.DataSet;
import fr.ign.cogit.geoxygene.feature.FT_Feature;
import fr.ign.cogit.geoxygene.feature.FT_FeatureCollection;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;

/**
 * @author  bonin
 * @version 1.0
 */

@SuppressWarnings("unchecked")
public class ChargeurTriangulation extends Chargeur {
	static Logger logger=Logger.getLogger(ChargeurTriangulation.class.getName());

	public ChargeurTriangulation() {}

	public static void importLigneEnPoints(String nomClasseGeo,
			Triangulation carte) throws Exception {
		FT_Feature objGeo;
		Class<?> clGeo = Class.forName(nomClasseGeo);
		NoeudDelaunay noeud;
		DirectPositionList listePoints;
		int i, j;

		System.out.println("D�but importLignesEnPoints");
		FT_FeatureCollection<?> listeFeatures = DataSet.db.loadAllFeatures(clGeo);
		for(i=0 ; i<listeFeatures.size() ; i++) {
			System.out.println("Nombre de lignes import�es :" + i);
			objGeo = listeFeatures.get(i);

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString ) {
				listePoints = ((GM_LineString)objGeo.getGeom()).getControlPoint();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						System.out.println("    Nombre de points cr��s :" + j);
					}
					noeud = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud.setCoord(listePoints.get(j));

				}
			}
		}
		System.out.println("Fin importLigneEnPoints");
	}

	public static void importLigneEnPoints(FT_FeatureCollection<?> listeFeatures,
			Triangulation carte) throws Exception {
		FT_Feature objGeo;
		NoeudDelaunay noeud;
		DirectPositionList listePoints;
		int i, j;

		System.out.println("D�but importLignesEnPoints");
		for(i=0 ; i<listeFeatures.size() ; i++) {
			System.out.println("Nombre de lignes import�es :" + i);
			objGeo = listeFeatures.get(i);

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString ) {
				listePoints = ((GM_LineString)objGeo.getGeom()).getControlPoint();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						System.out.println("    Nombre de points cr��s :" + j);
					}
					noeud = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud.setCoord(listePoints.get(j));

				}
			}
		}
		System.out.println("Fin importLigneEnPoints");
	}

	public static void importSegments(String nomClasseGeo,
			Triangulation carte) throws Exception {
		FT_Feature objGeo;
		Class<?> clGeo = Class.forName(nomClasseGeo);

		NoeudDelaunay noeud1;
		ArcDelaunay arc;
		DirectPositionList listePoints;
		DirectPosition dp;
		ArrayList<NoeudDelaunay> listeTemp, listeNoeuds, listeNoeudsEffaces = null;
		DirectPositionList tableau = null;
		Iterator<?> it, itEntrants, itSortants = null;
		int i, j;

		FT_FeatureCollection<?> listeFeatures = DataSet.db.loadAllFeatures(clGeo);
		Class<?>[] signaturea = {carte.getPopNoeuds().getClasse(),carte.getPopNoeuds().getClasse()};
		Object[] parama = new Object[2];

		for(i=0 ; i<listeFeatures.size() ; i++) {
			if (logger.isDebugEnabled()) logger.debug("Nombre de lignes import�es :" + i);
			objGeo = listeFeatures.get(i);

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString ) {
				listePoints = ((GM_LineString)objGeo.getGeom()).getControlPoint();
				listeTemp = new ArrayList<NoeudDelaunay>();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						if (logger.isDebugEnabled()) logger.debug("    Nombre de points cr��s :" + j);
					}
					noeud1 = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud1.setCoord(listePoints.get(j));
					listeTemp.add(noeud1);

				}
				for(j=0;j<listeTemp.size()-1;j++) {
					parama[0] = listeTemp.get(j);
					parama[1] = listeTemp.get(j+1);
					arc = (ArcDelaunay)carte.getPopArcs().nouvelElement(signaturea,parama);
				}
			}
		}

		// Filtrage des noeuds en double et correction de la topologie
		if (logger.isDebugEnabled()) logger.debug("Filtrage des noeuds en double");
		listeNoeuds = new ArrayList(carte.getListeNoeuds());
		it = carte.getListeNoeuds().iterator();
		tableau = new DirectPositionList();
		listeNoeudsEffaces = new ArrayList();
		while (it.hasNext()) {
			noeud1 = (NoeudDelaunay) it.next();
			dp = noeud1.getCoord();
			if (Operateurs.indice2D(tableau,dp) != -1) {
				if (logger.isDebugEnabled()) logger.debug("Elimination d'un doublon");
				itEntrants = noeud1.getEntrants().iterator();
				while (itEntrants.hasNext()) {
					arc = (ArcDelaunay) itEntrants.next();
					arc.setNoeudFin(listeNoeuds.get(Operateurs.indice2D(tableau,dp)));
				}
				itSortants = noeud1.getSortants().iterator();
				while (itSortants.hasNext()) {
					arc = (ArcDelaunay) itSortants.next();
					arc.setNoeudIni(listeNoeuds.get(Operateurs.indice2D(tableau,dp)));
				}
			}
			tableau.add(dp);
		}
		it = listeNoeudsEffaces.iterator();
		while (it.hasNext()) {
			noeud1 = (NoeudDelaunay) it.next();
			carte.getPopNoeuds().remove(noeud1); // pour la bidirection
		}
		if (logger.isDebugEnabled()) logger.debug("Fin importSegments");
	}

	public static void importSegments(FT_FeatureCollection listeFeatures,
			Triangulation carte) throws Exception {
		FT_Feature objGeo;

		NoeudDelaunay noeud1;
		ArcDelaunay arc;
		DirectPositionList listePoints;
		DirectPosition dp;
		ArrayList listeTemp, listeNoeuds, listeNoeudsEffaces = null;
		DirectPositionList tableau = null;
		Iterator it, itEntrants, itSortants = null;
		int i, j;

		Class[] signaturea = {carte.getPopNoeuds().getClasse(),carte.getPopNoeuds().getClasse()};
		Object[] parama = new Object[2];

		for(i=0 ; i<listeFeatures.size() ; i++) {
			if (logger.isDebugEnabled()) logger.debug("Nombre de lignes import�es :" + i);
			objGeo = listeFeatures.get(i);

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString ) {
				listePoints = ((GM_LineString)objGeo.getGeom()).coord();
				listeTemp = new ArrayList();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						if (logger.isDebugEnabled()) logger.debug("    Nombre de points cr��s :" + j);
					}
					noeud1 = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud1.setCoord(listePoints.get(j));
					listeTemp.add(noeud1);
				}
				for(j=0;j<listeTemp.size()-1;j++) {
					parama[0] = listeTemp.get(j);
					parama[1] = listeTemp.get(j+1);
					arc = (ArcDelaunay)carte.getPopArcs().nouvelElement(signaturea,parama);
				}
				parama[0] = listeTemp.get(listeTemp.size()-1);
				parama[1] = listeTemp.get(0);
				arc = (ArcDelaunay)carte.getPopArcs().nouvelElement(signaturea,parama);

			}

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon ) {
				listePoints = ((GM_Polygon)objGeo.getGeom()).coord();
				if (logger.isDebugEnabled()) logger.debug("Polygone "+i+" "+listePoints);
				listeTemp = new ArrayList();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						if (logger.isDebugEnabled()) logger.debug("    Nombre de points cr��s :" + j);
					}
					noeud1 = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud1.setCoord(listePoints.get(j));
					listeTemp.add(noeud1);
				}
				for(j=0;j<listeTemp.size()-1;j++) {
					parama[0] = listeTemp.get(j);
					parama[1] = listeTemp.get(j+1);
					arc = (ArcDelaunay)carte.getPopArcs().nouvelElement(signaturea,parama);
				}
			}
		}

		// Filtrage des noeuds en double et correction de la topologie
		if (logger.isDebugEnabled()) logger.debug("Filtrage des noeuds en double");
		listeNoeuds = new ArrayList(carte.getListeNoeuds());
		it = carte.getListeNoeuds().iterator();
		tableau = new DirectPositionList();
		listeNoeudsEffaces = new ArrayList();
		while (it.hasNext()) {
			noeud1 = (NoeudDelaunay) it.next();
			dp = noeud1.getCoord();
			if (Operateurs.indice2D(tableau,dp) != -1) {
				//if (logger.isDebugEnabled()) logger.debug("Elimination d'un doublon");
				itEntrants = noeud1.getEntrants().iterator();
				while (itEntrants.hasNext()) {
					arc = (ArcDelaunay) itEntrants.next();
					arc.setNoeudFin((NoeudDelaunay)listeNoeuds.get(Operateurs.indice2D(tableau,dp)));
				}
				itSortants = noeud1.getSortants().iterator();
				while (itSortants.hasNext()) {
					arc = (ArcDelaunay) itSortants.next();
					arc.setNoeudIni((NoeudDelaunay)listeNoeuds.get(Operateurs.indice2D(tableau,dp)));
				}
			}
			tableau.add(dp);
		}
		it = listeNoeudsEffaces.iterator();
		while (it.hasNext()) {
			noeud1 = (NoeudDelaunay) it.next();
			carte.getPopNoeuds().remove(noeud1); // pour la bidirection
		}
		if (logger.isDebugEnabled()) logger.debug("Fin importSegments");
		if (logger.isDebugEnabled()) logger.debug("liste de noeuds "+carte.getListeNoeuds().size());
		if (logger.isDebugEnabled()) logger.debug("liste des arcs "+carte.getListeArcs().size());
	}

	/**
	 * @param listeFeatures
	 * @param carte
	 * @throws Exception
	 */
	public static void importPolygoneEnPoints(FT_FeatureCollection listeFeatures, Triangulation carte) throws Exception {
		FT_Feature objGeo;
		NoeudDelaunay noeud;
		DirectPositionList listePoints;
		int i, j;

		if (logger.isDebugEnabled()) logger.debug("D�but importPolygoneEnPoints");
		for(i=0 ; i<listeFeatures.size() ; i++) {
			if (logger.isDebugEnabled()) logger.debug("Nombre de polygones import�es :" + i);
			objGeo = listeFeatures.get(i);

			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon ) {
				listePoints = ((GM_Polygon)objGeo.getGeom()).coord();
				for (j=0; j<listePoints.size(); j++) {
					if ( (j % 100) == 0 ) {
						if (logger.isDebugEnabled()) logger.debug("    Nombre de points cr��s :" + j);
					}
					noeud = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement();
					noeud.setCoord(listePoints.get(j));
				}
			}
		}
		if (logger.isDebugEnabled()) logger.debug("Fin importPolygoneEnPoints");
	}
	/**
	 * @param listeFeatures
	 * @param carte
	 * @throws Exception
	 */
	public static void importCentroidesPolygones(FT_FeatureCollection listeFeatures, Triangulation carte) throws Exception {
		FT_Feature objGeo;
		NoeudDelaunay noeud;
		int i;

		if (logger.isDebugEnabled()) logger.debug("D�but importCentroidesPolygones");
		for(i=0 ; i<listeFeatures.size() ; i++) {
			objGeo = listeFeatures.get(i);
			if ( objGeo.getGeom() instanceof fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon ) {
				GM_Point p = new GM_Point(objGeo.getGeom().centroid());
				noeud = (NoeudDelaunay)carte.getPopNoeuds().nouvelElement(p);
				noeud.addCorrespondant(objGeo);
				if (logger.isDebugEnabled()) logger.debug("Centroide import� = " + p);
			}
		}
		if (logger.isDebugEnabled()) logger.debug("Fin importCentroidesPolygones");
	}
}