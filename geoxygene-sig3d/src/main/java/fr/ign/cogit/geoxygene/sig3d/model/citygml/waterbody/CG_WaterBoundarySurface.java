package fr.ign.cogit.geoxygene.sig3d.model.citygml.waterbody;

import org.citygml4j.jaxb.gml._3_1_1.SurfacePropertyType;
import org.citygml4j.model.citygml.waterbody.WaterBoundarySurface;
import org.citygml4j.model.citygml.waterbody.WaterClosureSurface;
import org.citygml4j.model.citygml.waterbody.WaterGroundSurface;
import org.citygml4j.model.citygml.waterbody.WaterSurface;

import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.core.CG_CityObject;
import fr.ign.cogit.geoxygene.sig3d.model.citygml.geometry.ConvertyCityGMLGeometry;

public abstract class CG_WaterBoundarySurface extends CG_CityObject {

  protected IOrientableSurface lod2Surface;
  protected IOrientableSurface lod3Surface;
  protected IOrientableSurface lod4Surface;

  public CG_WaterBoundarySurface(WaterBoundarySurface wBS) {
    super(wBS);

    if (wBS.isSetLod2Surface()) {
      this.setLod2Surface(ConvertyCityGMLGeometry.convertGMLOrientableSurface(
          wBS.getLod2Surface().getSurface()).get(0));
    }

    if (wBS.isSetLod3Surface()) {
      this.setLod3Surface(ConvertyCityGMLGeometry.convertGMLOrientableSurface(
          wBS.getLod3Surface().getSurface()).get(0));
    }

    if (wBS.isSetLod4Surface()) {
      this.setLod4Surface(ConvertyCityGMLGeometry.convertGMLOrientableSurface(
          wBS.getLod4Surface().getSurface()).get(0));
    }
  }

  public static CG_WaterBoundarySurface generateAbstractWaterBoundarySurface(
      WaterBoundarySurface wBS) {

    if (wBS instanceof WaterGroundSurface) {

      return new CG_WaterGroundSurface((WaterGroundSurface) wBS);

    } else if (wBS instanceof WaterSurface) {

      return new CG_WaterSurface((WaterSurface) wBS);

    } else if (wBS instanceof WaterClosureSurface) {
      return new CG_WaterClosureSurface((WaterClosureSurface) wBS);

    } else {

      System.out.println("Classe non gérée AbstractWaterBoundarySurfaceType"
          + wBS.getCityGMLClass());

    }

    return null;

  }

  /**
   * Gets the value of the lod2Surface property.
   * 
   * @return possible object is {@link SurfacePropertyType }
   * 
   */
  public IOrientableSurface getLod2Surface() {
    return this.lod2Surface;
  }

  /**
   * Sets the value of the lod2Surface property.
   * 
   * @param value allowed object is {@link SurfacePropertyType }
   * 
   */
  public void setLod2Surface(IOrientableSurface value) {
    this.lod2Surface = value;
  }

  public boolean isSetLod2Surface() {
    return (this.lod2Surface != null);
  }

  /**
   * Gets the value of the lod3Surface property.
   * 
   * @return possible object is {@link SurfacePropertyType }
   * 
   */
  public IOrientableSurface getLod3Surface() {
    return this.lod3Surface;
  }

  /**
   * Sets the value of the lod3Surface property.
   * 
   * @param value allowed object is {@link SurfacePropertyType }
   * 
   */
  public void setLod3Surface(IOrientableSurface value) {
    this.lod3Surface = value;
  }

  public boolean isSetLod3Surface() {
    return (this.lod3Surface != null);
  }

  /**
   * Gets the value of the lod4Surface property.
   * 
   * @return possible object is {@link SurfacePropertyType }
   * 
   */
  public IOrientableSurface getLod4Surface() {
    return this.lod4Surface;
  }

  /**
   * Sets the value of the lod4Surface property.
   * 
   * @param value allowed object is {@link SurfacePropertyType }
   * 
   */
  public void setLod4Surface(IOrientableSurface value) {
    this.lod4Surface = value;
  }

  public boolean isSetLod4Surface() {
    return (this.lod4Surface != null);
  }

}
