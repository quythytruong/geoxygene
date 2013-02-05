/*******************************************************************************
 * This software is released under the licence CeCILL
 * 
 * see Licence_CeCILL-C_fr.html see Licence_CeCILL-C_en.html
 * 
 * see <a href="http://www.cecill.info/">http://www.cecill.info/a>
 * 
 * @copyright IGN
 ******************************************************************************/
package fr.ign.cogit.cartagen.pearep.mgcp;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import fr.ign.cogit.cartagen.core.defaultschema.GeneObjDefault;
import fr.ign.cogit.cartagen.core.genericschema.hydro.IWaterArea;
import fr.ign.cogit.cartagen.pearep.vmap.PeaRepDbType;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.schemageo.api.hydro.SurfaceDEau;
import fr.ign.cogit.geoxygene.schemageo.impl.hydro.SurfaceDEauImpl;
import fr.ign.cogit.geoxygene.schemageo.impl.support.reseau.ReseauImpl;

/*
 * ###### IGN / CartAGen ###### Title: WaterArea Description: Surfaces d'eau
 * Author: J. Renard Date: 18/09/2009
 */
public class MGCPWaterArea extends GeneObjDefault implements IWaterArea,
    MGCPFeature {

  /**
   * Associated Geoxygene schema object
   */
  private SurfaceDEau geoxObj;
  private String name;

  // VMAP attributes
  private long acc, hyp, prc, scc, smc;
  private String nfi, nfn;
  private double length, width;

  /**
   * Cosntructor from lakeresa class from VMAP2i model
   */
  public MGCPWaterArea(IPolygon poly, HashMap<String, Object> attributes,
      @SuppressWarnings("unused") PeaRepDbType type) {
    super();
    this.geoxObj = new SurfaceDEauImpl();
    this.geoxObj.setGeom(poly);
    this.setInitialGeom(poly);
    this.setEliminated(false);

    // attributes present in Mgcp++
    this.name = (String) attributes.get("nam");
    this.acc = (Long) attributes.get("acc");
    this.hyp = (Long) attributes.get("hyp");
    this.prc = (Long) attributes.get("prc");
    this.scc = (Long) attributes.get("scc");
    this.smc = (Long) attributes.get("smc");

    this.nfi = (String) attributes.get("nfi");
    this.nfn = (String) attributes.get("nfn");

    // computeLengthWidth();
  }

  /**
   * Compute the length of the water area using its skeleton and its width
   * divising the area by the computed length.
   */
  @SuppressWarnings("unused")
  private void computeLengthWidth() {
    // TODO
  }

  /**
   * Default constructor, used by Hibernate.
   */
  public MGCPWaterArea() {
    super();
  }

  @Override
  @Transient
  public IFeature getGeoxObj() {
    return this.geoxObj;
  }

  @Override
  @Type(type = "fr.ign.cogit.cartagen.software.interfaceCartagen.hibernate.GeOxygeneGeometryUserType")
  public IPolygon getGeom() {
    return (IPolygon) super.getGeom();
  }

  @Override
  @Column(name = "CartAGenDB_name")
  public String getDbName() {
    return super.getDbName();
  }

  @Override
  @Id
  public int getId() {
    return super.getId();
  }

  @Override
  public boolean isEliminated() {
    return super.isEliminated();
  }

  @Override
  public int getSymbolId() {
    return super.getSymbolId();
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public long getPrc() {
    return this.prc;
  }

  public void setPrc(long prc) {
    this.prc = prc;
  }

  public long getSmc() {
    return this.smc;
  }

  public void setSmc(long smc) {
    this.smc = smc;
  }

  public long getScc() {
    return this.scc;
  }

  public void setScc(long scc) {
    this.scc = scc;
  }

  public String getNfi() {
    return this.nfi;
  }

  public void setNfi(String nfi) {
    this.nfi = nfi;
  }

  public String getNfn() {
    return this.nfn;
  }

  public void setNfn(String nfn) {
    this.nfn = nfn;
  }

  @Override
  public void restoreGeoxObjects() {
    this.geoxObj = new SurfaceDEauImpl(new ReseauImpl(), this.getGeom());
    this.geoxObj.setNom(this.name);
  }

  @Override
  public Map<String, Object> getAttributeMap(MGCPFeature feat) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Useful to query on feature area.
   * @return
   */
  public double getArea() {
    return this.getGeom().area();
  }

  public void setLength(double length) {
    this.length = length;
  }

  public double getLength() {
    return this.length;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public double getWidth() {
    return this.width;
  }

  public long getAcc() {
    return this.acc;
  }

  public void setAcc(long acc) {
    this.acc = acc;
  }

  public long getHyp() {
    return this.hyp;
  }

  public void setHyp(long hyp) {
    this.hyp = hyp;
  }
}
