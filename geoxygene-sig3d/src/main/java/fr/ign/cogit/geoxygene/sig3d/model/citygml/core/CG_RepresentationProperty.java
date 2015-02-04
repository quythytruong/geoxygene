//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.4
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2011.08.25 at 08:55:07 AM CEST
//

package fr.ign.cogit.geoxygene.sig3d.model.citygml.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import fr.ign.cogit.geoxygene.schema.schemaConceptuelISOJeu.AssociationType;

/**
 * Denotes the relation of a _CityObject to its implicit geometry
 * representation, which is a representation of a geometry by referencing a
 * prototype and transforming it to its real position in space. The
 * ImplicitRepresentationPropertyType element must either carry a reference to a
 * ImplicitGeometry object or contain a ImplicitGeometry object inline, but
 * neither both nor none.
 * 
 * <p>
 * Java class for ImplicitRepresentationPropertyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ImplicitRepresentationPropertyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.opengis.net/gml}AssociationType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element ref="{http://www.opengis.net/citygml/1.0}ImplicitGeometry"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImplicitRepresentationPropertyType")
public class CG_RepresentationProperty extends AssociationType {

}