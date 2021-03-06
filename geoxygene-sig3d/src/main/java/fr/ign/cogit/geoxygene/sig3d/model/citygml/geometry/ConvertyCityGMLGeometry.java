package fr.ign.cogit.geoxygene.sig3d.model.citygml.geometry;

import java.util.ArrayList;
import java.util.List;

import org.citygml4j.model.gml.AbstractCurve;
import org.citygml4j.model.gml.AbstractGeometry;
import org.citygml4j.model.gml.AbstractRingProperty;
import org.citygml4j.model.gml.AbstractSolid;
import org.citygml4j.model.gml.AbstractSurface;
import org.citygml4j.model.gml.AbstractSurfacePatch;
import org.citygml4j.model.gml.CompositeCurve;
import org.citygml4j.model.gml.CompositeSolid;
import org.citygml4j.model.gml.CompositeSurface;
import org.citygml4j.model.gml.CurveProperty;
import org.citygml4j.model.gml.GeometryProperty;
import org.citygml4j.model.gml.LineString;
import org.citygml4j.model.gml.LineStringSegment;
import org.citygml4j.model.gml.LineStringSegmentArrayProperty;
import org.citygml4j.model.gml.LinearRing;
import org.citygml4j.model.gml.MultiCurve;
import org.citygml4j.model.gml.MultiCurveProperty;
import org.citygml4j.model.gml.MultiPoint;
import org.citygml4j.model.gml.MultiPointProperty;
import org.citygml4j.model.gml.MultiSolid;
import org.citygml4j.model.gml.MultiSurface;
import org.citygml4j.model.gml.MultiSurfaceProperty;
import org.citygml4j.model.gml.OrientableSurface;
import org.citygml4j.model.gml.Point;
import org.citygml4j.model.gml.Polygon;
import org.citygml4j.model.gml.PosOrPointPropertyOrPointRep;
import org.citygml4j.model.gml.Rectangle;
import org.citygml4j.model.gml.Solid;
import org.citygml4j.model.gml.SolidProperty;
import org.citygml4j.model.gml.Surface;
import org.citygml4j.model.gml.SurfaceProperty;
import org.citygml4j.model.gml.Tin;
import org.citygml4j.model.gml.Triangle;
import org.citygml4j.model.gml.TriangulatedSurface;

import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ITriangle;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSolid;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomcomp.ICompositeCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableCurve;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISolid;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Tin;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Triangle;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_TriangulatedSurface;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiCurve;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiPoint;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSolid;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_CompositeCurve;
import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_CompositeSolid;
import fr.ign.cogit.geoxygene.spatial.geomcomp.GM_CompositeSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Point;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_Solid;

/**
 * 
 * @author MBrasebin
 */
public class ConvertyCityGMLGeometry {
  // La translation que l'on appliquera
  public static double coordXIni = 0;
  public static double coordYIni = 0;
  public static double coordZIni = 0;

  public static double xMin = Double.POSITIVE_INFINITY;
  public static double yMin = Double.POSITIVE_INFINITY;
  public static double zMin = Double.POSITIVE_INFINITY;

  public static double xMax = Double.NEGATIVE_INFINITY;
  public static double yMax = Double.NEGATIVE_INFINITY;
  public static double zMax = Double.NEGATIVE_INFINITY;

  public static IGeometry convertGMLGeometry(GeometryProperty geom) {
    if(geom == null){
      return null;
    }
  
    return ConvertyCityGMLGeometry.convertGMLGeometry(geom.getGeometry());

  }

  /**
   * Convertir n'importe quelle géométrie CityGML en géométrie géoxygene
   * 
   * @param geom une géométrie GML de la librairie CityGML4j
   * @return une géométrie GeOxygene issue de la conversion de la géométrie
   *         paramètre
   */
  public static IGeometry convertGMLGeometry(AbstractGeometry geom) {

    if (geom instanceof Solid) {

      return ConvertyCityGMLGeometry.convertGMLSolid((Solid) geom);

    } else if (geom instanceof CompositeSolid) {

      return ConvertyCityGMLGeometry
          .convertGMLCompositeSolid((CompositeSolid) geom);

    } else if (geom instanceof MultiSolid) {

      return ConvertyCityGMLGeometry.convertGMLMultiSolid((MultiSolid) geom);

    } else if (geom instanceof Polygon) {

      return ConvertyCityGMLGeometry.convertGMLPolygon((Polygon) geom);

    } else if (geom instanceof Rectangle) {

      return ConvertyCityGMLGeometry.convertGMLRectangle((Rectangle) geom);

    } else if (geom instanceof Triangle) {

      return ConvertyCityGMLGeometry.convertGMLTriangle((Triangle) geom);

    } else if (geom instanceof MultiSurface) {

      return ConvertyCityGMLGeometry
          .convertGMLMultiSurface((MultiSurface) geom);

    } else if (geom instanceof Tin) {

      return ConvertyCityGMLGeometry.convertGMLTin((Tin) geom);
      
      
    } else if (geom instanceof TriangulatedSurface) {
      
      return ConvertyCityGMLGeometry.convertGMLTriangulatedSurface((TriangulatedSurface) geom);

    } else if (geom instanceof OrientableSurface) {

      List<IOrientableSurface> lOS = ConvertyCityGMLGeometry
          .convertGMLOrientableSurface((OrientableSurface) geom);
      if (lOS.size() == 1) {
        return lOS.get(0);
      } else {

        return new GM_MultiSurface<IOrientableSurface>(lOS);
      }

    } else if (geom instanceof CompositeSurface) {

      ConvertyCityGMLGeometry
          .convertGMLCompositeSurface((CompositeSurface) geom);

    } else if (geom instanceof Surface) {
      List<IOrientableSurface> lOS = ConvertyCityGMLGeometry
          .convertGMLSurface((Surface) geom);
      if (lOS.size() == 1) {
        return lOS.get(0);
      } else {

        return new GM_MultiSurface<IOrientableSurface>(lOS);
      }

    } else if (geom instanceof LineString) {

      return ConvertyCityGMLGeometry.convertGMLLineString((LineString) geom);

    } else if (geom instanceof MultiCurve) {
      return ConvertyCityGMLGeometry.convertGMLMultiCurve((MultiCurve) geom);

    } else if (geom instanceof CompositeCurve) {
      return ConvertyCityGMLGeometry
          .convertGMLCompositeCurve((CompositeCurve) geom);

    } else if (geom instanceof MultiPoint) {
      return ConvertyCityGMLGeometry.convertGMLMultiPoint((MultiPoint) geom);

    } else if (geom instanceof Point) {
      return ConvertyCityGMLGeometry.convertGMLPoint((Point) geom);
    }
    // Type de géométrie non reconnu
    if( geom != null){
      System.out.println(geom.getClass());
    }

    return null;

  }

  // /////////////////////////////Les
  // primitives//////////////////////////////////////

  public static GM_TriangulatedSurface convertGMLTriangulatedSurface(
      TriangulatedSurface geom) {
    
    List<ITriangle> lTri = new ArrayList<ITriangle>();
    
    if(geom.isSetTrianglePatches()){
      
      int nbPatchs = geom.getTrianglePatches().getTriangle().size();
      
      
      for(int i=0;i<nbPatchs;i++){
        
        
        lTri.add(convertGMLTriangle(geom.getTrianglePatches().getTriangle().get(i)));
        
        
      }
        
      
    }
    
    return new GM_TriangulatedSurface(lTri);
  }

  public static GM_Tin convertGMLTin(Tin geom) {

    List<ILineString> lSBL = new ArrayList<ILineString>();

    // Break line
    if (geom.isSetBreakLines()) {

      List<LineStringSegmentArrayProperty> lGMLBL = geom.getBreakLines();
      int nbElem = lGMLBL.size();

      for (int i = 0; i < nbElem; i++) {

        List<LineStringSegment> lSS = lGMLBL.get(i).getLineStringSegment();

        int nbLSS = lSS.size();
        for (int j = 0; j < nbLSS; j++) {

          LineStringSegment lS = lSS.get(j);

          lSBL.add(new GM_LineString(convertGMLDirectPositionList(lS
              .getPosList())));

        }

      }

    }
    // Stop Line
    List<ILineString> lSSL = new ArrayList<ILineString>();

    if (geom.isSetStopLines()) {

      List<LineStringSegmentArrayProperty> lGMLBL = geom.getStopLines();
      int nbElem = lGMLBL.size();

      for (int i = 0; i < nbElem; i++) {

        List<LineStringSegment> lSS = lGMLBL.get(i).getLineStringSegment();

        int nbLSS = lSS.size();
        for (int j = 0; j < nbLSS; j++) {

          LineStringSegment lS = lSS.get(j);

          lSSL.add(new GM_LineString(convertGMLDirectPositionList(lS
              .getPosList())));

        }

      }

    }
    // ControlPoint

    IDirectPositionList iDPL = new DirectPositionList();

    if (geom.isSetControlPoint()) {
      iDPL.addAll(convertGMLDirectPositionList(geom.getControlPoint()
          .getPosList()));

    }

    double maxL = Double.NaN;

    if (geom.isSetMaxLength()) {
      maxL = geom.getMaxLength().getValue();
    }

    // TODO Auto-generated method stub
    return new GM_Tin(iDPL, lSSL, lSBL, (float) maxL);
  }

  /**
   * Convertit un DirectPosition GML en DirectPosition GeOxygene
   * 
   * @param dp le DirectPosition GML que l'on souhaite convertir
   * @return un DirectPosition de GeOxygene
   */
  public static DirectPosition convertGMLDirectPosition(
      org.citygml4j.model.gml.DirectPosition dp) {

    List<Double> lD = dp.getValue();

    ConvertyCityGMLGeometry.xMin = Math.min(ConvertyCityGMLGeometry.xMin,
        lD.get(0));
    ConvertyCityGMLGeometry.yMin = Math.min(ConvertyCityGMLGeometry.yMin,
        lD.get(1));
    ConvertyCityGMLGeometry.zMin = Math.min(ConvertyCityGMLGeometry.zMin,
        lD.get(2));

    ConvertyCityGMLGeometry.xMax = Math.max(ConvertyCityGMLGeometry.xMax,
        lD.get(0));
    ConvertyCityGMLGeometry.yMax = Math.max(ConvertyCityGMLGeometry.yMax,
        lD.get(1));
    ConvertyCityGMLGeometry.zMax = Math.max(ConvertyCityGMLGeometry.zMax,
        lD.get(2));

    return new DirectPosition(lD.get(0) - ConvertyCityGMLGeometry.coordXIni,
        lD.get(1) - ConvertyCityGMLGeometry.coordYIni, lD.get(2)
            - ConvertyCityGMLGeometry.coordZIni);
  }

  /**
   * Convertit un DirectPositionList de CityGML4j en DirectPositionList Geoxyene
   * 
   * @param dplGML un DirectPositionList GML à convertir
   * @return un DirectPositionList GeOxygene
   */
  public static DirectPositionList convertGMLDirectPositionList(
      org.citygml4j.model.gml.DirectPositionList dplGML) {

    DirectPositionList dplFinal = new DirectPositionList();

    List<Double> lD = dplGML.getValue();
    int nbElem = lD.size();

    for (int i = 0; i < nbElem / 3; i++) {

      dplFinal.add(new DirectPosition(lD.get(3 * i)
          - ConvertyCityGMLGeometry.coordXIni, lD.get(3 * i + 1)
          - ConvertyCityGMLGeometry.coordYIni, lD.get(3 * i + 2)
          - ConvertyCityGMLGeometry.coordZIni));

      ConvertyCityGMLGeometry.xMin = Math.min(ConvertyCityGMLGeometry.xMin,
          lD.get(3 * i));
      ConvertyCityGMLGeometry.yMin = Math.min(ConvertyCityGMLGeometry.yMin,
          lD.get(3 * i + 1));
      ConvertyCityGMLGeometry.zMin = Math.min(ConvertyCityGMLGeometry.zMin,
          lD.get(3 * i + 2));

      ConvertyCityGMLGeometry.xMax = Math.max(ConvertyCityGMLGeometry.xMax,
          lD.get(3 * i));
      ConvertyCityGMLGeometry.yMax = Math.max(ConvertyCityGMLGeometry.yMax,
          lD.get(3 * i + 1));
      ConvertyCityGMLGeometry.zMax = Math.max(ConvertyCityGMLGeometry.zMax,
          lD.get(3 * i + 2));

    }

    return dplFinal;
  }

  /**
   * Convertit en DirectPositionList les points properties
   * 
   * @param lPOPPOPR une liste de PosOrPointPropertyOrPointRep de CityGML4j
   * @return un objet DirectPositionList de GeOxygene correspondant à la
   *         conversion de l'objet paramètre
   */
  public static DirectPositionList convertPosOrPointPropertyOrPointRep(
      List<PosOrPointPropertyOrPointRep> lPOPPOPR) {

    int nbPOPPOPR = lPOPPOPR.size();

    DirectPositionList dplFinal = new DirectPositionList();

    for (int i = 0; i < nbPOPPOPR; i++) {
      DirectPosition dp = null;

      if (lPOPPOPR.get(i).getPointProperty() != null) {
        Point p = lPOPPOPR.get(i).getPointProperty().getPoint();
        dp = ConvertyCityGMLGeometry.convertGMLDirectPosition(p.getPos());

      } else if (lPOPPOPR.get(i).getPointRep() != null) {
        Point p = lPOPPOPR.get(i).getPointRep().getPoint();
        dp = ConvertyCityGMLGeometry.convertGMLDirectPosition(p.getPos());

      } else {
        dp = ConvertyCityGMLGeometry.convertGMLDirectPosition(lPOPPOPR.get(i)
            .getPos());

      }

      dplFinal.add(dp);
    }

    return dplFinal;

  }

  /**
   * Convertit un point de cityGML en point GeOxygene
   * 
   * @param p le point GML que l'on souhaite convertir
   * @return un GM_Point de GeOxygene
   */
  public static GM_Point convertGMLPoint(Point p) {

    return new GM_Point(ConvertyCityGMLGeometry.convertGMLDirectPosition(p
        .getPos()));
  }

  /**
   * Convertit un LineString CityGML en LineString GeOxygene
   * 
   * @param ls un LineString que l'on souhaite convertir
   * @return un GM_LineString de GeOxygene
   */
  public static GM_LineString convertGMLLineString(LineString ls) {

    DirectPositionList dpl = ConvertyCityGMLGeometry
        .convertGMLDirectPositionList(ls.getPosList());

    return new GM_LineString(dpl);

  }

  /**
   * Convertit un polygon de cityGML en polygon GeOxygene
   * 
   * @param pol un polygone GML que l'on souhaite convertir
   * @return un GM_Polygon de GeOxygene
   */
  public static GML_Polygon convertGMLPolygon(Polygon pol) {

    AbstractRingProperty ringExterior = pol.getExterior();
    LinearRing linearRing = ((LinearRing) ringExterior.getRing());

    DirectPositionList dplExt;

    if (linearRing.getPosList() != null) {

      dplExt = ConvertyCityGMLGeometry.convertGMLDirectPositionList(linearRing
          .getPosList());

    } else {

      dplExt = ConvertyCityGMLGeometry
          .convertPosOrPointPropertyOrPointRep(linearRing
              .getPosOrPointPropertyOrPointRep());
    }

    GML_Polygon poly = new GML_Polygon();
    GML_Ring ring = new GML_Ring(new GM_LineString(dplExt));

    if (linearRing.isSetId()) {
      ring.setID(linearRing.getId());
    }

    poly.setExterior(ring);

    if (pol.isSetId()) {
      poly.setID(pol.getId());
    }

    List<AbstractRingProperty> lRing = pol.getInterior();
    int nbInterior = lRing.size();

    if (poly.getID().equals("PolyID46_93_731494_37481")) {
      System.out.println();
    }

    for (int i = 0; i < nbInterior; i++) {

      linearRing = (LinearRing) lRing.get(i).getRing();

      if (linearRing.getPosList() != null) {

        dplExt = ConvertyCityGMLGeometry
            .convertGMLDirectPositionList(linearRing.getPosList());

      } else {

        dplExt = ConvertyCityGMLGeometry
            .convertPosOrPointPropertyOrPointRep(linearRing
                .getPosOrPointPropertyOrPointRep());
      }

      GML_Ring ringInt = new GML_Ring(new GM_LineString(dplExt));
      if (lRing.get(i).getRing().isSetId()) {
        ringInt.setID(lRing.get(i).getRing().getId());
      }

      poly.addInterior(ringInt);
    }

    return poly;

  }

  /**
   * Convertit un OrientableSurface CityGML en List de Surfaces GeOxygene
   * 
   * @param os l'OrientableSurface GML à convertir
   * @return une liste de GM_OrientableSurface issue de la surface initiale
   */
  public static List<IOrientableSurface> convertGMLOrientableSurface(
      OrientableSurface os) {
    AbstractSurface as = os.getBaseSurface().getSurface();
    return ConvertyCityGMLGeometry.convertGMLOrientableSurface(as);

  }

  public static List<IOrientableSurface> convertGMLOrientableSurface(
      AbstractSurface as) {

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>();

    if (as instanceof OrientableSurface) {

      lOS.addAll(ConvertyCityGMLGeometry
          .convertGMLOrientableSurface((OrientableSurface) as));
    } else if (as instanceof Polygon) {

      lOS.add(ConvertyCityGMLGeometry.convertGMLPolygon((Polygon) as));

    } else if (as instanceof MultiSurface) {

      lOS.addAll(ConvertyCityGMLGeometry
          .convertGMLMultiSurface((MultiSurface) as));
    } else if (as instanceof Surface) {

      lOS.addAll(ConvertyCityGMLGeometry.convertGMLSurface((Surface) as));
    } else if (as instanceof CompositeSurface) {

      lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSurface(
          (CompositeSurface) as).getGenerator());

    } else {

      System.out.println("OS non reconnu" + as.getClass());
    }

    if (lOS.size() == 0) {
      return null;
    }

    return lOS;

  }

  public static ISolid convertGMLSolid(SolidProperty sol) {

    if(sol == null){
      return null;
    }
   
    return ConvertyCityGMLGeometry.convertGMLSolid((Solid) sol.getSolid());

  }

  /**
   * Convertit un solide GML en GM_Solid GeOxygene
   * 
   * @param sol le Solid GML que l'on souhaite convertir
   * @return un GM_Solid Geoxygene
   */
  public static ISolid convertGMLSolid(Solid sol) {

    AbstractSurface as = sol.getExterior().getSurface();

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>();

    if (as instanceof OrientableSurface) {

      lOS.addAll(ConvertyCityGMLGeometry
          .convertGMLOrientableSurface((OrientableSurface) as));
    } else if (as instanceof Polygon) {

      lOS.add(ConvertyCityGMLGeometry.convertGMLPolygon((Polygon) as));

    } else if (as instanceof MultiSurface) {

      lOS.addAll(ConvertyCityGMLGeometry
          .convertGMLMultiSurface((MultiSurface) as));
    } else if (as instanceof Surface) {

      lOS.addAll(ConvertyCityGMLGeometry.convertGMLSurface((Surface) as));
    } else if (as instanceof CompositeSurface) {

      lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSurface(
          (CompositeSurface) as).getGenerator());

    } else {

      System.out.println("Solid non reconnu" + as.getClass());
    }

    if (lOS.size() == 0) {
      return null;
    }
    return new GM_Solid(lOS);

  }

  // /////////////////////////////Les Multis Géométries
  // /////////////////////////////////////////

  public static GM_MultiPoint convertGMLMultiPoint(MultiPointProperty multiP) {
    return ConvertyCityGMLGeometry.convertGMLMultiPoint(multiP.getMultiPoint());

  }

  /**
   * Conversion de multiPoints cityGML en multiPoints GeOxygene
   * 
   * @param multiP le multiPoints GML que l'on souhaite convertir
   * @return un GM_MultiPointGeoxygene
   */
  public static GM_MultiPoint convertGMLMultiPoint(MultiPoint multiP) {
    List<Point> lP = multiP.getPointMembers().getPoint();
    DirectPositionList dpl = new DirectPositionList();

    int nbPoints = lP.size();

    for (int i = 0; i < nbPoints; i++) {

      dpl.add(ConvertyCityGMLGeometry.convertGMLDirectPosition(lP.get(i)
          .getPos()));
    }

    return new GM_MultiPoint(dpl);
  }

  /**
   * Convertit les multiCurves CityGML en multiCurve GeOxygene
   * 
   * @param multiC un MultiCurve GML à convertir
   * @return un GM_MultiCurve GeOxygene
   */
  public static GM_MultiCurve<IOrientableCurve> convertGMLMultiCurve(
      MultiCurve multiC) {

    List<CurveProperty> multiCurves = multiC.getCurveMember();
    int nbCurves = multiCurves.size();

    List<IOrientableCurve> lCurves = new ArrayList<IOrientableCurve>(nbCurves);

    for (int i = 0; i < nbCurves; i++) {

      AbstractCurve c = multiCurves.get(i).getCurve();

      if (c instanceof LineString) {

        lCurves.add(ConvertyCityGMLGeometry
            .convertGMLLineString((LineString) c));

      } else if (c instanceof CompositeCurve) {

        lCurves.addAll(ConvertyCityGMLGeometry.convertGMLCompositeCurve(
            (CompositeCurve) c).getGenerator());
      } else {

        System.out.println("MS non reconnu" + c.getClass());
      }

    }

    if (lCurves.size() == 0) {
      return null;
    }

    return new GM_MultiCurve<IOrientableCurve>(lCurves);

  }

  public static GM_MultiCurve<IOrientableCurve> convertGMLMultiCurve(
      MultiCurveProperty multiC) {

    return ConvertyCityGMLGeometry.convertGMLMultiCurve(multiC.getMultiCurve());

  }

  public static IMultiSurface<IOrientableSurface> convertGMLMultiSurface(
      MultiSurfaceProperty multiS){ 

      if(multiS == null){
        return null;
      }

    return ConvertyCityGMLGeometry.convertGMLMultiSurface(multiS
        .getMultiSurface());

  }

  /**
   * Convertit une multisurface GML en GM_MultiSurface de GeOxygene
   * 
   * @param multiS multiSurface GML
   * @return GM_MultiSurface de GeOxygene
   */
  public static IMultiSurface<IOrientableSurface> convertGMLMultiSurface(
      MultiSurface multiS) {
    List<SurfaceProperty> multiSurfaces = multiS.getSurfaceMember();
    int nbSurfaces = multiSurfaces.size();

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(nbSurfaces);

    for (int i = 0; i < nbSurfaces; i++) {
      AbstractSurface as = multiSurfaces.get(i).getSurface();

      if (as instanceof OrientableSurface) {

        lOS.addAll(ConvertyCityGMLGeometry
            .convertGMLOrientableSurface((OrientableSurface) as));
      } else if (as instanceof Polygon) {

        lOS.add(ConvertyCityGMLGeometry.convertGMLPolygon((Polygon) as));

      } else if (as instanceof MultiSurface) {

        lOS.addAll(ConvertyCityGMLGeometry
            .convertGMLMultiSurface((MultiSurface) as));
      } else if (as instanceof Surface) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLSurface((Surface) as));
      } else if (as instanceof CompositeSurface) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSurface(
            (CompositeSurface) as).getGenerator());

      } else {

        System.out.println("Surface non reconnu" + as.getClass());
      }

    }

    return new GM_MultiSurface<IOrientableSurface>(lOS);
  }

  /**
   * Convertit un MultiSolid GML
   * 
   * @param mS MultiSolid GML à convertir
   * @return un MultiSolid GeOxygene
   */
  public static IMultiSolid<ISolid> convertGMLMultiSolid(MultiSolid mS) {

    List<AbstractSolid> lAS = mS.getSolidMembers().getSolid();
    int nbSolid = lAS.size();

    List<ISolid> lOS = new ArrayList<ISolid>();

    for (int i = 0; i < nbSolid; i++) {
      AbstractSolid as = lAS.get(i);

      if (as instanceof Solid) {
        lOS.add(ConvertyCityGMLGeometry.convertGMLSolid((Solid) as));

      } else if (as instanceof CompositeSolid) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSolid(
            (CompositeSolid) as).getGenerator());
      } else {

        if (as != null) {
          System.out.println("as non reconnu" + as.getClass());
        } else {
          System.out.println("Convert CityGML abstract Solid null");
        }
      }

    }

    if (lOS.size() == 0) {
      return null;
    }

    return new GM_MultiSolid<ISolid>(lOS);
  }

  // /////////////////////////////////Les objets
  // composites//////////////////////////////////////

  /**
   * Transforme les composites CurveCityGML en composites GeOxygene
   * 
   * @param compositeC le CompositeCurve GML à convertir
   * @return un GM_CompositeCurve GeOxygene
   */
  public static ICompositeCurve convertGMLCompositeCurve(
      CompositeCurve compositeC) {

    List<CurveProperty> lCP = compositeC.getCurveMember();
    int nbCurves = lCP.size();

    List<IOrientableCurve> lCurves = new ArrayList<IOrientableCurve>(nbCurves);

    for (int i = 0; i < nbCurves; i++) {
      AbstractCurve c = lCP.get(i).getCurve();

      if (c instanceof LineString) {

        lCurves.add(ConvertyCityGMLGeometry
            .convertGMLLineString((LineString) c));

      } else if (c instanceof CompositeCurve) {

        lCurves.addAll(ConvertyCityGMLGeometry.convertGMLCompositeCurve(
            (CompositeCurve) c).getGenerator());
      } else {
        System.out.println("c non reconnu" + c.getClass());
      }

    }

    GM_CompositeCurve cC = new GM_CompositeCurve();
    cC.getGenerator().addAll(lCurves);

    return cC;

  }

  /**
   * Convertit un CompositeSurface de GML en GM_CompositeSurface GeOxygene
   * 
   * @param compositeS CompositeSurface GML à convertir
   * @return GM_CompositeSurface issu de la conversion
   */
  public static GM_CompositeSurface convertGMLCompositeSurface(
      CompositeSurface compositeS) {

    List<SurfaceProperty> multiSurfaces = compositeS.getSurfaceMember();
    int nbSurfaces = multiSurfaces.size();

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(nbSurfaces);

    for (int i = 0; i < nbSurfaces; i++) {
      AbstractSurface as = multiSurfaces.get(i).getSurface();

      if (as instanceof OrientableSurface) {

        lOS.addAll(ConvertyCityGMLGeometry
            .convertGMLOrientableSurface((OrientableSurface) as));
      } else if (as instanceof Polygon) {

        lOS.add(ConvertyCityGMLGeometry.convertGMLPolygon((Polygon) as));

      } else if (as instanceof MultiSurface) {

        lOS.addAll(ConvertyCityGMLGeometry
            .convertGMLMultiSurface((MultiSurface) as));
      } else if (as instanceof Surface) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLSurface((Surface) as));
      } else if (as instanceof CompositeSurface) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSurface(
            (CompositeSurface) as).getGenerator());

      } else {
        if (as != null) {
          System.out.println("as non reconnu" + as.getClass());
        } else {
          System.out.println("ConvertCityGML : abstract solid null");
        }

      }

    }

    GM_CompositeSurface compS = new GM_CompositeSurface();
    compS.getGenerator().addAll(lOS);
    // compS.getElement().addAll(lOS);

    return compS;
  }

  /**
   * Convertit un CompositeSolid GML en GM_CompositeSolid GeOxygene
   * 
   * @param cS le CompositeSolid GML à convertir
   * @return un GM_CompositeSolid issu de la conversion
   */
  public static GM_CompositeSolid convertGMLCompositeSolid(CompositeSolid cS) {
    List<SolidProperty> lSP = cS.getSolidMember();

    int nbSolid = lSP.size();

    List<ISolid> lOS = new ArrayList<ISolid>(nbSolid);

    for (int i = 0; i < nbSolid; i++) {

      AbstractSolid as = lSP.get(i).getSolid();
      if (as instanceof Solid) {
        lOS.add(ConvertyCityGMLGeometry.convertGMLSolid((Solid) as));

      } else if (as instanceof CompositeSolid) {

        lOS.addAll(ConvertyCityGMLGeometry.convertGMLCompositeSolid(
            (CompositeSolid) as).getGenerator());
      } else {

        System.out.println("Solid non reconnu" + as.getClass());
      }

    }

    GM_CompositeSolid cs = new GM_CompositeSolid();
    cs.getGenerator().addAll(lOS);

    return cs;
  }

  // /////////////////////////////////////Les objets autres //
  // //////////////////////////////////

  /**
   * Convertit un objet Surface de GML en une liste de GM_OrientableSurface
   * GeOxygene
   * 
   * @param sur la surface que l'on souhaite convertir
   * @return une liste de GM_OrientableSurface issue de la conversion de l'objet
   *         paramètre
   */
  public static List<IOrientableSurface> convertGMLSurface(Surface sur) {
    List<? extends AbstractSurfacePatch> lASP = sur.getPatches()
        .getSurfacePatch();

    int nbSurfaces = lASP.size();

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(nbSurfaces);

    for (int i = 0; i < nbSurfaces; i++) {
      AbstractSurfacePatch as = lASP.get(i);

      if (as instanceof Triangle) {

        lOS.add(ConvertyCityGMLGeometry.convertGMLTriangle((Triangle) as));

      } else if (as instanceof Rectangle) {
        lOS.add(ConvertyCityGMLGeometry.convertGMLRectangle((Rectangle) as));
      } else {

        System.out.println("Patch non reconnu" + as.getClass());
      }
    }

    return lOS;
  }

  /**
   * Convertit un rectangle GML en GM_Polygon GeOxygene (utilisé pour la
   * conversion de MNT)
   * 
   * @param r le rectangle que l'on souhaite convertir
   * @return un GM_Polygon issu de la conversion du rectangle
   */
  public static GML_Polygon convertGMLRectangle(Rectangle r) {

    LinearRing linearRing = (LinearRing) r.getExterior().getRing();

    DirectPositionList dplExt = null;

    if (linearRing.getPosList() != null) {

      dplExt = ConvertyCityGMLGeometry.convertGMLDirectPositionList(linearRing
          .getPosList());

    } else {

      dplExt = ConvertyCityGMLGeometry
          .convertPosOrPointPropertyOrPointRep(linearRing
              .getPosOrPointPropertyOrPointRep());
    }
    GML_Polygon pol = new GML_Polygon(new GML_Ring(new GM_LineString(dplExt)));

    return pol;
  }

  /**
   * Convertit un triangle GML en GM_Triangle (utilisé lors de la conversion de
   * TIN)
   * 
   * @param t le triangle que l'on souhaite convertir
   * @return un GM_Triangle issu de l'objet initial
   */
  public static GM_Triangle convertGMLTriangle(Triangle t) {

    LinearRing linearRing = (LinearRing) t.getExterior().getRing();

    DirectPositionList dplExt = null;

    if (linearRing.getPosList() != null) {

      dplExt = ConvertyCityGMLGeometry.convertGMLDirectPositionList(linearRing
          .getPosList());

    } else {

      dplExt = ConvertyCityGMLGeometry
          .convertPosOrPointPropertyOrPointRep(linearRing
              .getPosOrPointPropertyOrPointRep());
    }

    GM_Triangle tri = new GM_Triangle(dplExt.get(0), dplExt.get(1),
        dplExt.get(2));

    return tri;
  }
}
