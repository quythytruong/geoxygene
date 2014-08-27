/*******************************************************************************
 * This file is part of the GeOxygene project source files.
 * 
 * GeOxygene aims at providing an open framework which implements OGC/ISO
 * specifications for the development and deployment of geographic (GIS)
 * applications. It is a open source contribution of the COGIT laboratory at the
 * Institut Géographique National (the French National Mapping Agency).
 * 
 * See: http://oxygene-project.sourceforge.net
 * 
 * Copyright (C) 2005 Institut Géographique National
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library (see file LICENSE if present); if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 *******************************************************************************/

package fr.ign.cogit.geoxygene.appli.render.primitive;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import test.app.GLBezierShadingComplex;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiCurve;
import fr.ign.cogit.geoxygene.appli.Viewport;
import fr.ign.cogit.geoxygene.appli.gl.GLComplexFactory;
import fr.ign.cogit.geoxygene.appli.gl.GLPaintingComplex;
import fr.ign.cogit.geoxygene.appli.gl.LineTesselator;
import fr.ign.cogit.geoxygene.appli.render.texture.BasicTextureExpressiveRendering;
import fr.ign.cogit.geoxygene.appli.render.texture.StrokeTextureExpressiveRendering;
import fr.ign.cogit.geoxygene.appli.task.AbstractTask;
import fr.ign.cogit.geoxygene.appli.task.Task;
import fr.ign.cogit.geoxygene.appli.task.TaskState;
import fr.ign.cogit.geoxygene.function.ConstantFunction;
import fr.ign.cogit.geoxygene.function.Function1D;
import fr.ign.cogit.geoxygene.style.LineSymbolizer;
import fr.ign.cogit.geoxygene.style.Symbolizer;
import fr.ign.cogit.geoxygene.style.expressive.BasicTextureExpressiveRenderingDescriptor;
import fr.ign.cogit.geoxygene.style.expressive.ExpressiveRenderingDescriptor;
import fr.ign.cogit.geoxygene.style.expressive.StrokeTextureExpressiveRenderingDescriptor;
import fr.ign.cogit.geoxygene.util.gl.GLComplex;
import fr.ign.cogit.geoxygene.util.gl.GLSimpleComplex;

/**
 * @author JeT
 * 
 */
public class DisplayableCurve extends AbstractTask implements GLDisplayable {

    private static final Logger logger = Logger
            .getLogger(DisplayableCurve.class.getName()); // logger

    private static final Colorizer partialColorizer = new SolidColorizer(
            Color.red);
    public static final Function1D DefaultLineWidthFunction = new ConstantFunction(
            1);
    public static final Function1D DefaultLineShiftFunction = new ConstantFunction(
            0);

    private final List<ILineString> curves = new ArrayList<ILineString>();
    private Symbolizer symbolizer = null;
    private List<GLComplex> fullRepresentation = null;
    private GLComplex partialRepresentation = null;
    private long displayCount = 0; // number of time it has been displayed
    private Date lastDisplayTime; // last time it has been displayed
    private final Object currentTaskLock = new Object();
    private final Task currentTask = null;
    private final int taskCount = 0;

    // private Colorizer colorizer = null;
    // private Parameterizer parameterizer = null;

    // private Texture texture = null;

    /**
     * Constructor using a IMultiCurve
     */
    public DisplayableCurve(String name, Viewport viewport,
            IMultiCurve<?> multiCurve, Symbolizer symbolizer) {
        super(name);
        this.symbolizer = symbolizer;

        for (Object lineString : multiCurve.getList()) {
            if (lineString instanceof ILineString) {
                this.curves.add((ILineString) lineString);
            } else {
                logger.warn("multisurface does not contain only ILineString but "
                        + this.curves.getClass().getSimpleName());
            }
        }
    }

    /**
     * Constructor using a ILineString
     */
    public DisplayableCurve(String name, Viewport viewport,
            ILineString lineString, Symbolizer symbolizer) {
        super(name);
        this.symbolizer = symbolizer;
        this.curves.add(lineString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.task.Task#isProgressable()
     */
    @Override
    public boolean isProgressable() {
        return false;
    }

    /**
     * @return the displayCount
     */
    @Override
    public long getDisplayCount() {
        return this.displayCount;
    }

    /**
     * @return the lastDisplayTime
     */
    @Override
    public Date getLastDisplayTime() {
        return this.lastDisplayTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.task.Task#isPausable()
     */
    @Override
    public boolean isPausable() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.task.Task#isStopable()
     */
    @Override
    public boolean isStoppable() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        super.setState(TaskState.INITIALIZING);
        this.fullRepresentation = null;
        super.setState(TaskState.RUNNING);

        // if (this.getTexture() != null) {
        // this.generateWithDistanceField();
        // }

        if (this.symbolizer instanceof LineSymbolizer) {
            LineSymbolizer lineSymbolizer = (LineSymbolizer) this.symbolizer;
            if (lineSymbolizer.getStroke().getExpressiveRendering() == null) {
                this.generateWithLineSymbolizer(lineSymbolizer);
            } else {
                this.generateWithExpressiveStroke(lineSymbolizer);
            }
        } else {
            logger.error("Curve rendering do not handle "
                    + this.symbolizer.getClass().getSimpleName());
            super.setState(TaskState.ERROR);
            return;
        }
        super.setState(TaskState.FINALIZING);
        super.setState(TaskState.FINISHED);
    }

    private void generateWithLineSymbolizer(LineSymbolizer symbolizer) {
        List<GLComplex> complexes = new ArrayList<GLComplex>();
        // return GLComplexFactory.createFilledPolygon(multiSurface,
        // symbolizer.getStroke().getColor());
        IEnvelope envelope = IGeometryUtil.getEnvelope(this.curves);
        double minX = envelope.minX();
        double minY = envelope.minY();

        // BasicStroke awtStroke =
        // GLComplexFactory.geoxygeneStrokeToAWTStroke(this.viewport,
        // symbolizer);
        // GLComplex outline =
        // GLComplexFactory.createOutlineMultiSurface(this.polygons, awtStroke,
        // minX, minY);
        GLSimpleComplex line = LineTesselator.createThickLine(this.getName()
                + "-full", this.curves, symbolizer.getStroke(), minX, minY);
        line.setColor(symbolizer.getStroke().getColor());
        line.setOverallOpacity(symbolizer.getStroke().getColor().getAlpha());
        complexes.add(line);
        this.fullRepresentation = complexes;
    }

    private void generateWithExpressiveStroke(LineSymbolizer symbolizer) {
        ExpressiveRenderingDescriptor style = symbolizer.getStroke()
                .getExpressiveRendering();
        if (style == null) {
            throw new IllegalStateException(
                    "this method can only be called with a valid expressive stroke");
        }
        if ((style instanceof StrokeTextureExpressiveRenderingDescriptor)) {
            StrokeTextureExpressiveRenderingDescriptor strtex = (StrokeTextureExpressiveRenderingDescriptor) style;
            List<GLComplex> complexes = new ArrayList<GLComplex>();
            // return GLComplexFactory.createFilledPolygon(multiSurface,
            // symbolizer.getStroke().getColor());
            IEnvelope envelope = IGeometryUtil.getEnvelope(this.curves);
            double minX = envelope.minX();
            double minY = envelope.minY();

            GLPaintingComplex complex = new GLPaintingComplex(this.getName()
                    + "-expressive-full", minX, minY);
            GLComplexFactory.createThickCurves(this.getName(), complex,
                    symbolizer.getStroke(), minX, minY, strtex, this.curves);
            complex.setExpressiveRendering(new StrokeTextureExpressiveRendering(
                    strtex));
            complexes.add(complex);
            this.fullRepresentation = complexes;
            return;
        } else if ((style instanceof BasicTextureExpressiveRenderingDescriptor)) {
            BasicTextureExpressiveRenderingDescriptor strtex = (BasicTextureExpressiveRenderingDescriptor) style;
            List<GLComplex> complexes = new ArrayList<GLComplex>();
            // return GLComplexFactory.createFilledPolygon(multiSurface,
            // symbolizer.getStroke().getColor());
            IEnvelope envelope = IGeometryUtil.getEnvelope(this.curves);
            double minX = envelope.minX();
            double minY = envelope.minY();

            GLBezierShadingComplex complex = new GLBezierShadingComplex(
                    this.getName() + "-expressive-full", minX, minY);
            GLComplexFactory.createThickCurves(this.getName(), complex,
                    symbolizer.getStroke(), minX, minY, strtex, this.curves);
            complex.setExpressiveRendering(new BasicTextureExpressiveRendering(
                    strtex));
            complexes.add(complex);
            this.fullRepresentation = complexes;
            return;
        }
        throw new IllegalStateException("LineSymbolizer cannot handle "
                + style.getClass().getSimpleName());
    }

    // /**
    // * @param widthFunction
    // * @param shiftFunction
    // * @return
    // */
    // public GLPaintingComplex createComplex(String id, List<ILineString>
    // lines,
    // double minX, double minY, Function1D widthFunction,
    // Function1D shiftFunction, double sampleSize, double minAngle) {
    // GLPaintingComplex complex = new GLPaintingComplex(id, minX, minY);
    // synchronized (this.currentTaskLock) {
    // this.taskCount = lines.size();
    // }
    // for (ILineString line : lines) {
    // try {
    // Task tesselateThickLineTask = LinePaintingTesselator
    // .tesselateThickLine(id, complex,
    // line.getControlPoint(), widthFunction,
    // shiftFunction, sampleSize, minAngle, minX,
    // minY, new SolidColorizer(Color.black));
    // synchronized (this.currentTaskLock) {
    // this.currentTask = tesselateThickLineTask;
    // }
    // } catch (FunctionEvaluationException e) {
    // e.printStackTrace();
    // }
    // }
    //
    // return complex;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.task.AbstractTask#getProgress()
     */
    @Override
    public double getProgress() {
        if (this.taskCount <= 0) {
            return 0.;
        }
        synchronized (this.currentTaskLock) {
            if (this.currentTask == null) {
                return 0.;
            }
            return this.currentTask.getProgress() / this.taskCount;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.render.primitive.GLDisplayable#
     * getPartialRepresentation()
     */
    @Override
    public GLComplex getPartialRepresentation() {
        if (this.partialRepresentation == null) {
            IEnvelope envelope = IGeometryUtil.getEnvelope(this.curves);
            double minX = envelope.minX();
            double minY = envelope.minY();
            this.partialRepresentation = GLComplexFactory.createQuickLine(
                    this.getName() + "-partial", this.curves, partialColorizer,
                    null, minX, minY);
        }
        this.displayIncrement();
        return this.partialRepresentation;
    }

    private void displayIncrement() {
        this.displayCount++;
        this.lastDisplayTime = new Date();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.ign.cogit.geoxygene.appli.render.primitive.GLDisplayable#
     * getFullRepresentation()
     */
    @Override
    public Collection<GLComplex> getFullRepresentation() {
        if (this.fullRepresentation != null) {
            this.displayIncrement();
        }
        return this.fullRepresentation;
    }

}
