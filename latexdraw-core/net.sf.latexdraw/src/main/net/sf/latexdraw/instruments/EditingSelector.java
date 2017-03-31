package net.sf.latexdraw.instruments;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractButton;
import net.sf.latexdraw.actions.ModifyPencilStyle;
import net.sf.latexdraw.actions.shape.AddShape;
import net.sf.latexdraw.badaboom.BadaboomCollector;
import net.sf.latexdraw.glib.models.ShapeFactory;
import net.sf.latexdraw.lang.LangTool;
import net.sf.latexdraw.util.LResources;
import org.malai.action.Action;
import org.malai.instrument.InteractorImpl;
import org.malai.swing.action.library.ActivateInactivateInstruments;
import org.malai.swing.instrument.WidgetInstrument;
import org.malai.swing.interaction.library.ButtonPressed;
import org.malai.swing.ui.SwingUIComposer;
import org.malai.swing.widget.MButton;
import org.malai.swing.widget.MToggleButton;

/**
 * This instrument selects the pencil or the hand.<br>
 * <br>
 * This file is part of LaTeXDraw.<br>
 * Copyright (c) 2005-2014 Arnaud BLOUIN<br>
 * <br>
 * LaTeXDraw is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * <br>
 * LaTeXDraw is distributed without any warranty; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br>
 * <br>
 * 05/09/2010<br>
 * @author Arnaud BLOUIN
 * @since 3.0
 */
public class EditingSelector extends WidgetInstrument {
	/** The instrument that converts PST code into shapes. */
	protected CodeInserter codeInserter;

	/** The button that allows to select the instrument Hand. */
	protected MToggleButton handB;

	/** The button that allows to select the instrument Pencil to draw dots. */
	protected MToggleButton dotB;

	/** The button that allows to select the instrument Pencil to draw free hand shapes. */
	protected MToggleButton freeHandB;

	/** The button that allows to select the instrument Pencil to add texts. */
	protected MToggleButton textB;

	/** The button that allows to select the instrument Pencil to add rectangles. */
	protected MToggleButton recB;

	/** The button that allows to select the instrument Pencil to add squares. */
	protected MToggleButton squareB;

	/** The button that allows to select the instrument Pencil to add ellipses. */
	protected MToggleButton ellipseB;

	/** The button that allows to select the instrument Pencil to add circles. */
	protected MToggleButton circleB;

	/** The button that allows to select the instrument Pencil to add lines. */
	protected MToggleButton linesB;

	/** The button that allows to select the instrument Pencil to add polygons. */
	protected MToggleButton polygonB;

	/** The button that allows to select the instrument Pencil to add bezier curves. */
	protected MToggleButton bezierB;

	/** The button that allows to select the instrument Pencil to add closed bezier curves. */
	protected MToggleButton bezierClosedB;

	/** The button that allows to select the instrument Pencil to add grids. */
	protected MToggleButton gridB;

	/** The button that allows to select the instrument Pencil to add axes. */
	protected MToggleButton axesB;

	/** The button that allows to select the instrument Pencil to add rhombuses. */
	protected MToggleButton rhombusB;

	/** The button that allows to select the instrument Pencil to add triangles. */
	protected MToggleButton triangleB;

	/** The button that allows to select the instrument Pencil to add arcs. */
	protected MToggleButton arcB;

	/** The button that allows to select the instrument Pencil to add pictures. */
	protected MToggleButton picB;

	/** The button that allows to select the instrument Pencil to add plotted curves. */
	protected MToggleButton plotB;

	/** The button that allows to insert some code (converted in shapes). */
	protected MButton codeB;

	/** The instrument Hand. */
	protected Hand hand;

	/** The instrument Pencil. */
	protected Pencil pencil;

	/** The instrument that manages instruments that customise shapes and the pencil. */
	protected MetaShapeCustomiser metaShapeCustomiser;

	/** The instrument that manages selected shapes. */
	protected Border border;

	/** The instrument used to delete shapes. */
	protected ShapeDeleter deleter;

	protected Map<MToggleButton, EditionChoice> button2EditingChoiceMap;


	/**
	 * Creates the instruments selector.
	 * @param composerUI The composerUI that manages the widgets of the instrument.
	 * @param pen The pen to select.
	 * @param theHand The theHand to select.
	 * @param theBorder The instrument that manages selected shapes.
	 * @param customiser The instrument that manages instruments that customise shapes and the pen.
	 * @param theDeleter The instrument used to delete shapes.
	 * @param theInserter The instrument that converts PST code into shapes.
	 * @throws IllegalArgumentException If one of the given parameter is null.
	 * @since 3.0
	 */
	public EditingSelector(final SwingUIComposer<?> composerUI, final Pencil pen, final Hand theHand, final MetaShapeCustomiser customiser,
							final Border theBorder, final ShapeDeleter theDeleter, final CodeInserter theInserter) {
		super(composerUI);

		codeInserter		= Objects.requireNonNull(theInserter);
		deleter				= Objects.requireNonNull(theDeleter);
		border				= Objects.requireNonNull(theBorder);
		pencil 				= Objects.requireNonNull(pen);
		hand				= Objects.requireNonNull(theHand);
		metaShapeCustomiser	= Objects.requireNonNull(customiser);

		initialiseWidgets();
		initialiseEditingChoiceMap();
		codeInserter.setActivated(false);
		theHand.setActivated(true);
		pen.setActivated(false);
	}


	private void initialiseEditingChoiceMap() {
		button2EditingChoiceMap	= new HashMap<>();

		button2EditingChoiceMap.put(dotB, EditionChoice.DOT);
		button2EditingChoiceMap.put(textB, EditionChoice.TEXT);
		button2EditingChoiceMap.put(freeHandB, EditionChoice.FREE_HAND);
		button2EditingChoiceMap.put(arcB, EditionChoice.CIRCLE_ARC);
		button2EditingChoiceMap.put(axesB, EditionChoice.AXES);
		button2EditingChoiceMap.put(bezierB, EditionChoice.BEZIER_CURVE);
		button2EditingChoiceMap.put(bezierClosedB, EditionChoice.BEZIER_CURVE_CLOSED);
		button2EditingChoiceMap.put(circleB, EditionChoice.CIRCLE);
		button2EditingChoiceMap.put(ellipseB, EditionChoice.ELLIPSE);
		button2EditingChoiceMap.put(gridB, EditionChoice.GRID);
		button2EditingChoiceMap.put(linesB, EditionChoice.LINES);
		button2EditingChoiceMap.put(polygonB, EditionChoice.POLYGON);
		button2EditingChoiceMap.put(recB, EditionChoice.RECT);
		button2EditingChoiceMap.put(rhombusB, EditionChoice.RHOMBUS);
		button2EditingChoiceMap.put(squareB, EditionChoice.SQUARE);
		button2EditingChoiceMap.put(triangleB, EditionChoice.TRIANGLE);
		button2EditingChoiceMap.put(picB, EditionChoice.PICTURE);
		button2EditingChoiceMap.put(plotB, EditionChoice.PLOT);
	}


	@Override
	protected void initialiseWidgets() {
		/* Creation of the widgets of the instrument. */
		handB = new MToggleButton(LResources.SELECT_ICON);
		handB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.136") + //$NON-NLS-1$
 				LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.137") + //$NON-NLS-1$
 				LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.138")); //$NON-NLS-1$

		dotB = new MToggleButton(LResources.DOT_ICON);
		dotB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.117")); //$NON-NLS-1$

		freeHandB = new MToggleButton(LResources.FREE_HAND_ICON);
		freeHandB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.7")); //$NON-NLS-1$

		textB = new MToggleButton(LResources.TEXT_ICON);
		textB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.60")); //$NON-NLS-1$

		recB = new MToggleButton(LResources.RECT_ICON);
 		recB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.119")); //$NON-NLS-1$

		squareB = new MToggleButton(LResources.SQUARE_ICON);
 		squareB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.120")); //$NON-NLS-1$

		ellipseB = new MToggleButton(LResources.ELLIPSE_ICON);
 		ellipseB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.125")); //$NON-NLS-1$

		circleB = new MToggleButton(LResources.CIRCLE_ICON);
 		circleB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.127")); //$NON-NLS-1$

		axesB = new MToggleButton(LResources.AXES_ICON);
 		axesB.setToolTipText(LangTool.INSTANCE.getString18("LaTeXDrawFrame.17")); //$NON-NLS-1$

		gridB = new MToggleButton(LResources.GRID_ICON);
		gridB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.133")); //$NON-NLS-1$

		bezierB = new MToggleButton(LResources.BEZIER_CURVE_ICON);
 		bezierB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.132")); //$NON-NLS-1$

		bezierClosedB = new MToggleButton(LResources.CLOSED_BEZIER_ICON);
 		bezierClosedB.setToolTipText(LangTool.INSTANCE.getString19("LaTeXDrawFrame.11")); //$NON-NLS-1$

		arcB = new MToggleButton(LResources.ARC_ICON);
 		arcB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.128")); //$NON-NLS-1$

		triangleB = new MToggleButton(LResources.TRIANGLE_ICON);
 		triangleB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.124")); //$NON-NLS-1$

		rhombusB = new MToggleButton(LResources.RHOMBUS_ICON);
 		rhombusB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.123")); //$NON-NLS-1$

		polygonB = new MToggleButton(LResources.POLYGON_ICON);
 		polygonB.setToolTipText(LangTool.INSTANCE.getStringLaTeXDrawFrame("LaTeXDrawFrame.121")); //$NON-NLS-1$

		linesB = new MToggleButton(LResources.LINES_ICON);
		linesB.setToolTipText(LangTool.INSTANCE.getStringActions("Instruments.4")); //$NON-NLS-1$

		picB = new MToggleButton(LResources.INSERT_PIC_ICON);
		picB.setToolTipText(LangTool.INSTANCE.getString16("LaTeXDrawFrame.1")); //$NON-NLS-1$

		plotB = new MToggleButton(LResources.PLOT_ICON);
		plotB.setToolTipText(LangTool.INSTANCE.getStringActions("Instruments.5")); //$NON-NLS-1$

		codeB = new MButton(LResources.TEX_EDITOR_ICON);
		codeB.setToolTipText(LangTool.INSTANCE.getString16("LaTeXDrawFrame.0")); //$NON-NLS-1$
	}


	@Override
	public void interimFeedback() {
		if(hand.isActivated()) {
			handB.setSelected(true);
			textB.setSelected(false);
			freeHandB.setSelected(false);
			recB.setSelected(false);
			squareB.setSelected(false);
			ellipseB.setSelected(false);
			arcB.setSelected(false);
			circleB.setSelected(false);
			axesB.setSelected(false);
			gridB.setSelected(false);
			dotB.setSelected(false);
			polygonB.setSelected(false);
			linesB.setSelected(false);
			rhombusB.setSelected(false);
			triangleB.setSelected(false);
			bezierB.setSelected(false);
			bezierClosedB.setSelected(false);
			picB.setSelected(false);
			plotB.setSelected(false);
		} else if(pencil.isActivated()){
			final EditionChoice ec = pencil.currentChoice();

			recB.setSelected(ec==EditionChoice.RECT);
			squareB.setSelected(ec==EditionChoice.SQUARE);
			ellipseB.setSelected(ec==EditionChoice.ELLIPSE);
			arcB.setSelected(ec==EditionChoice.CIRCLE_ARC);
			circleB.setSelected(ec==EditionChoice.CIRCLE);
			axesB.setSelected(ec==EditionChoice.AXES);
			gridB.setSelected(ec==EditionChoice.GRID);
			dotB.setSelected(ec==EditionChoice.DOT);
			polygonB.setSelected(ec==EditionChoice.POLYGON);
			linesB.setSelected(ec==EditionChoice.LINES);
			rhombusB.setSelected(ec==EditionChoice.RHOMBUS);
			triangleB.setSelected(ec==EditionChoice.TRIANGLE);
			bezierB.setSelected(ec==EditionChoice.BEZIER_CURVE);
			bezierClosedB.setSelected(ec==EditionChoice.BEZIER_CURVE_CLOSED);
			textB.setSelected(ec==EditionChoice.TEXT);
			freeHandB.setSelected(ec==EditionChoice.FREE_HAND);
			picB.setSelected(ec==EditionChoice.PICTURE);
			plotB.setSelected(ec==EditionChoice.PLOT);
			handB.setSelected(false);
		}
	}



	@Override
	protected void initialiseInteractors() {
		try{
			addInteractor(new ButtonPressed2AddText(this));
			addInteractor(new ButtonPressed2DefineStylePencil(this));
			addInteractor(new ButtonPressed2ActivateIns(this));
			addInteractor(new ButtonPressed2LaunchCodeInserter(this));
		}catch(InstantiationException | IllegalAccessException e){
			BadaboomCollector.INSTANCE.add(e);
		}
	}


	@Override
	public void setActivated(final boolean isActivated) {
		super.setActivated(isActivated);

		composer.setWidgetVisible(arcB, isActivated);
		composer.setWidgetVisible(axesB, isActivated);
		composer.setWidgetVisible(bezierB, isActivated);
		composer.setWidgetVisible(bezierClosedB, isActivated);
		composer.setWidgetVisible(circleB, isActivated);
		composer.setWidgetVisible(recB, isActivated);
		composer.setWidgetVisible(squareB, isActivated);
		composer.setWidgetVisible(ellipseB, isActivated);
		composer.setWidgetVisible(gridB, isActivated);
		composer.setWidgetVisible(polygonB, isActivated);
		composer.setWidgetVisible(dotB, isActivated);
		composer.setWidgetVisible(linesB, isActivated);
		composer.setWidgetVisible(rhombusB, isActivated);
		composer.setWidgetVisible(triangleB, isActivated);
		composer.setWidgetVisible(textB, isActivated);
		composer.setWidgetVisible(freeHandB, isActivated);
		composer.setWidgetVisible(handB, isActivated);
		composer.setWidgetVisible(picB, isActivated);
		composer.setWidgetVisible(codeB, isActivated);
		composer.setWidgetVisible(plotB, isActivated);
	}


	/**
	 * @param ab The widget to test.
	 * @return True if the given widget is a widget of the instrument.
	 * @since 3.0
	 */
	public boolean isWidget(final Object ab) {
		return ab!=null && (button2EditingChoiceMap.get(ab)!=null || ab==handB || ab==codeB);
	}


	/**
	 * @return The button that allows to insert PST code.
	 * @since 3.0
	 */
	public MButton getCodeB() {
		return codeB;
	}

	/**
	 * @return The button that allows the select instrument Hand.
	 * @since 3.0
	 */
	public MToggleButton getHandB() {
		return handB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw free hand shapes.
	 * @since 3.0
	 */
	public MToggleButton getFreeHandB() {
		return freeHandB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to add texts.
	 * @since 3.0
	 */
	public MToggleButton getTextB() {
		return textB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw dots.
	 * @since 3.0
	 */
	public MToggleButton getDotB() {
		return dotB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw rectangles.
	 * @since 3.0
	 */
	public MToggleButton getRecB() {
		return recB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw squares.
	 * @since 3.0
	 */
	public MToggleButton getSquareB() {
		return squareB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw ellipses.
	 * @since 3.0
	 */
	public MToggleButton getEllipseB() {
		return ellipseB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw circles.
	 * @since 3.0
	 */
	public MToggleButton getCircleB() {
		return circleB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw lines.
	 * @since 3.0
	 */
	public MToggleButton getLinesB() {
		return linesB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw polygons.
	 * @since 3.0
	 */
	public MToggleButton getPolygonB() {
		return polygonB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw bezier curves.
	 * @since 3.0
	 */
	public MToggleButton getBezierB() {
		return bezierB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw closed bezier curves.
	 * @since 3.0
	 */
	public MToggleButton getBezierClosedB() {
		return bezierClosedB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw grids.
	 * @since 3.0
	 */
	public MToggleButton getGridB() {
		return gridB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw axes.
	 * @since 3.0
	 */
	public MToggleButton getAxesB() {
		return axesB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw rhombuses.
	 * @since 3.0
	 */
	public MToggleButton getRhombusB() {
		return rhombusB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw triangles.
	 * @since 3.0
	 */
	public MToggleButton getTriangleB() {
		return triangleB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to draw arcs.
	 * @since 3.0
	 */
	public MToggleButton getArcB() {
		return arcB;
	}

	/**
	 * @return The button that allows the select instrument Pencil to add pictures.
	 * @since 3.0
	 */
	public MToggleButton getPicB() {
		return picB;
	}

	/** @return The button that allows the select instrument Pencil to add pictures. */
	public MToggleButton getPlotB() { return plotB; }

	@Override
	public void onActionDone(final Action action) {
		super.onActionDone(action);
		hand.canvas().requestFocus();
	}
}



/**
 * This link allows to activate the code inserter instrument.
 */
class ButtonPressed2LaunchCodeInserter extends InteractorImpl<ActivateInactivateInstruments, ButtonPressed, EditingSelector> {
	ButtonPressed2LaunchCodeInserter(final EditingSelector ins) throws InstantiationException, IllegalAccessException {
		super(ins, false, ActivateInactivateInstruments.class, ButtonPressed.class);
	}

	@Override
	public void initAction() {
		action.addInstrumentToActivate(instrument.codeInserter);
	}

	@Override
	public boolean isConditionRespected() {
		return interaction.getButton()==instrument.codeB;
	}
}



/**
 * This link allows the modify the link of shape the pencil will create using a ButtonPressed interaction.
 */
class ButtonPressed2DefineStylePencil extends InteractorImpl<ModifyPencilStyle, ButtonPressed, EditingSelector> {
	protected ButtonPressed2DefineStylePencil(final EditingSelector ins) throws InstantiationException, IllegalAccessException {
		super(ins, false, ModifyPencilStyle.class, ButtonPressed.class);
	}


	@Override
	public void initAction() {
		action.setEditingChoice(instrument.button2EditingChoiceMap.get(interaction.getButton()));
		action.setPencil(instrument.pencil);
		action.setStatusBar(instrument.codeInserter.statusBar());
	}

	@Override
	public boolean isConditionRespected() {
		return instrument.button2EditingChoiceMap.get(interaction.getButton())!=null;
	}
}


/**
 * When the user types a text using the text field (instrument text setter) and then he
 * selects another kind of editing, the typed text must be added to the canvas.
 */
class ButtonPressed2AddText extends InteractorImpl<AddShape, ButtonPressed, EditingSelector> {
	protected ButtonPressed2AddText(final EditingSelector ins) throws InstantiationException, IllegalAccessException {
		super(ins, false, AddShape.class, ButtonPressed.class);
	}

	@Override
	public void initAction() {
		action.setDrawing(instrument.pencil.canvas().getDrawing());
		action.setShape(ShapeFactory.createText(ShapeFactory.createPoint(instrument.pencil.textSetter().relativePoint),
						instrument.pencil.textSetter().getTextField().getText()));
	}

	@Override
	public boolean isConditionRespected() {
		return instrument.pencil.textSetter().isActivated() && !instrument.pencil.textSetter().getTextField().getText().isEmpty() &&
				interaction.getButton()==instrument.textB;
	}
}



/**
 * This link maps a ButtonPressed interaction to an action that activates/desactivates instruments.
 */
class ButtonPressed2ActivateIns extends InteractorImpl<ActivateInactivateInstruments, ButtonPressed, EditingSelector> {
	protected ButtonPressed2ActivateIns(final EditingSelector ins) throws InstantiationException, IllegalAccessException {
		super(ins, false, ActivateInactivateInstruments.class, ButtonPressed.class);
	}


	@Override
	public void initAction() {
		final AbstractButton ab = interaction.getButton();

		action.setActivateFirst(false);

		if(ab!=instrument.textB)
			action.addInstrumentToInactivate(instrument.pencil.textSetter());

		/* Selection of the instruments to activate/desactivate. */
		if(ab==instrument.handB) {
			final boolean noSelection = instrument.hand.canvas().getDrawing().getSelection().isEmpty();
			action.addInstrumentToActivate(instrument.hand);

			if(!noSelection)
				action.addInstrumentToActivate(instrument.deleter);

			action.addInstrumentToInactivate(instrument.pencil);

			if(noSelection) {
				action.addInstrumentToInactivate(instrument.metaShapeCustomiser);
				action.addInstrumentToInactivate(instrument.border);
			}
			else {
				action.addInstrumentToActivate(instrument.metaShapeCustomiser);
				action.addInstrumentToActivate(instrument.border);
			}
		} else {
			action.addInstrumentToInactivate(instrument.hand);
			action.addInstrumentToInactivate(instrument.border);
			action.addInstrumentToInactivate(instrument.deleter);
			action.addInstrumentToActivate(instrument.pencil);
			action.addInstrumentToActivate(instrument.metaShapeCustomiser);
		}
	}


	@Override
	public boolean isConditionRespected() {
		/* The pressed button must be a button of the instrument. */
		return instrument.codeB!=interaction.getButton() && instrument.isWidget(interaction.getButton());
	}
}
