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

import org.hibernate.annotations.Type;

import fr.ign.cogit.cartagen.core.defaultschema.GeneObjDefault;
import fr.ign.cogit.cartagen.core.genericschema.urban.IBuildPoint;
import fr.ign.cogit.cartagen.pearep.vmap.PeaRepDbType;
import fr.ign.cogit.geoxygene.api.feature.IFeature;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.schemageo.api.bati.AutreConstruction;
import fr.ign.cogit.geoxygene.schemageo.impl.bati.AutreConstructionImpl;

public class MGCPBuildPoint extends GeneObjDefault implements IBuildPoint,
    MGCPFeature {

  private AutreConstruction geoxObj;

  // VMAP attributes
  private String aoo, hgt, len, name, nfi, nfn, voi, wid, zv2;
  private long acc, afc, cef, cfc, cit, cus, ddc, ebt, gfc, icf, mfc, paf, psf,
      res, rfc, sfy, suc, tfc, uuc, coe, fun, hwt, smc, ssr, caa;

  public MGCPBuildPoint(IPoint point, HashMap<String, Object> attributes,
      @SuppressWarnings("unused") PeaRepDbType type) {
    super();
    this.geoxObj = new AutreConstructionImpl(point);
    this.setInitialGeom(point);
    this.setEliminated(false);

    this.acc = (Long) attributes.get("acc");
    this.afc = (Long) attributes.get("afc");
    this.cef = (Long) attributes.get("cef");
    this.cfc = (Long) attributes.get("cfc");
    this.cit = (Long) attributes.get("cit");
    this.cus = (Long) attributes.get("cus");
    this.ddc = (Long) attributes.get("ddc");
    this.ebt = (Long) attributes.get("ebt");
    this.gfc = (Long) attributes.get("gfc");
    this.icf = (Long) attributes.get("icf");
    this.mfc = (Long) attributes.get("mfc");
    this.paf = (Long) attributes.get("paf");
    this.psf = (Long) attributes.get("psf");
    this.res = (Long) attributes.get("res");
    this.rfc = (Long) attributes.get("rfc");
    this.sfy = (Long) attributes.get("sfy");
    this.suc = (Long) attributes.get("suc");
    this.tfc = (Long) attributes.get("tfc");

    this.coe = (Long) attributes.get("coe");
    this.fun = (Long) attributes.get("fun");
    this.hwt = (Long) attributes.get("hwt");
    this.smc = (Long) attributes.get("smc");
    this.ssr = (Long) attributes.get("ssr");
    this.caa = (Long) attributes.get("caa");

    this.aoo = (String) attributes.get("aoo");
    this.name = (String) attributes.get("nam");
    this.hgt = (String) attributes.get("hgt");
    this.len = (String) attributes.get("len_");
    this.nfi = (String) attributes.get("nfi");
    this.nfn = (String) attributes.get("nfn");
    this.voi = (String) attributes.get("voi");
    this.wid = (String) attributes.get("wid");
    this.zv2 = (String) attributes.get("zv2");

  }

  @Override
  public IFeature getGeoxObj() {
    return this.geoxObj;
  }

  @Override
  @Type(type = "fr.ign.cogit.cartagen.software.interfaceCartagen.hibernate.GeOxygeneGeometryUserType")
  public IPoint getGeom() {
    return (IPoint) super.getGeom();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getVoi() {
    return this.voi;
  }

  public void setVoi(String voi) {
    this.voi = voi;
  }

  public long getAcc() {
    return this.acc;
  }

  public void setAcc(long acc) {
    this.acc = acc;
  }

  public String getAoo() {
    return this.aoo;
  }

  public void setAoo(String aoo) {
    this.aoo = aoo;
  }

  public long getCoe() {
    return this.coe;
  }

  public void setCoe(long coe) {
    this.coe = coe;
  }

  public String getHgt() {
    return this.hgt;
  }

  public void setHgt(String hgt) {
    this.hgt = hgt;
  }

  public long getHwt() {
    return this.hwt;
  }

  public void setHwt(long hwt) {
    this.hwt = hwt;
  }

  public String getLen() {
    return this.len;
  }

  public void setLen(String len) {
    this.len = len;
  }

  public long getSmc() {
    return this.smc;
  }

  public void setSmc(long smc) {
    this.smc = smc;
  }

  public long getSsr() {
    return this.ssr;
  }

  public void setSsr(long ssr) {
    this.ssr = ssr;
  }

  public String getWid() {
    return this.wid;
  }

  public void setWid(String wid) {
    this.wid = wid;
  }

  @Override
  public Map<String, Object> getAttributeMap(MGCPFeature feat) {
    // TODO Auto-generated method stub
    return null;
  }

  public long getAfc() {
    return this.afc;
  }

  public void setAfc(long afc) {
    this.afc = afc;
  }

  public long getCef() {
    return this.cef;
  }

  public void setCef(long cef) {
    this.cef = cef;
  }

  public long getCfc() {
    return this.cfc;
  }

  public void setCfc(long cfc) {
    this.cfc = cfc;
  }

  public long getCit() {
    return this.cit;
  }

  public void setCit(long cit) {
    this.cit = cit;
  }

  public long getCus() {
    return this.cus;
  }

  public void setCus(long cus) {
    this.cus = cus;
  }

  public long getDdc() {
    return this.ddc;
  }

  public void setDdc(long ddc) {
    this.ddc = ddc;
  }

  public long getEbt() {
    return this.ebt;
  }

  public void setEbt(long ebt) {
    this.ebt = ebt;
  }

  public long getGfc() {
    return this.gfc;
  }

  public void setGfc(long gfc) {
    this.gfc = gfc;
  }

  public long getIcf() {
    return this.icf;
  }

  public void setIcf(long icf) {
    this.icf = icf;
  }

  public long getMfc() {
    return this.mfc;
  }

  public void setMfc(long mfc) {
    this.mfc = mfc;
  }

  public long getPaf() {
    return this.paf;
  }

  public void setPaf(long paf) {
    this.paf = paf;
  }

  public long getPsf() {
    return this.psf;
  }

  public void setPsf(long psf) {
    this.psf = psf;
  }

  public long getRes() {
    return this.res;
  }

  public void setRes(long res) {
    this.res = res;
  }

  public long getRfc() {
    return this.rfc;
  }

  public void setRfc(long rfc) {
    this.rfc = rfc;
  }

  public long getSfy() {
    return this.sfy;
  }

  public void setSfy(long sfy) {
    this.sfy = sfy;
  }

  public long getSuc() {
    return this.suc;
  }

  public void setSuc(long suc) {
    this.suc = suc;
  }

  public long getTfc() {
    return this.tfc;
  }

  public void setTfc(long tfc) {
    this.tfc = tfc;
  }

  public long getUuc() {
    return this.uuc;
  }

  public void setUuc(long uuc) {
    this.uuc = uuc;
  }

  public long getFun() {
    return this.fun;
  }

  public void setFun(long fun) {
    this.fun = fun;
  }

  public long getCaa() {
    return this.caa;
  }

  public void setCaa(long caa) {
    this.caa = caa;
  }

  public String getZv2() {
    return this.zv2;
  }

  public void setZv2(String zv2) {
    this.zv2 = zv2;
  }

}
