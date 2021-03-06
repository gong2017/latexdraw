package net.sf.latexdraw.instrument.hand;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.sf.latexdraw.instrument.CompositeGUIVoidCommand;
import net.sf.latexdraw.instrument.Hand;
import net.sf.latexdraw.instrument.MetaShapeCustomiser;
import net.sf.latexdraw.instrument.Pencil;
import net.sf.latexdraw.instrument.ShapeDotCustomiser;
import net.sf.latexdraw.instrument.ShapePropInjector;
import net.sf.latexdraw.instrument.TestDotStyleGUI;
import net.sf.latexdraw.instrument.TextSetter;
import net.sf.latexdraw.model.api.shape.Dot;
import net.sf.latexdraw.model.api.shape.DotStyle;
import net.sf.latexdraw.util.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TestHandDotStyle extends TestDotStyleGUI {
	@Override
	protected Injector createInjector() {
		return new ShapePropInjector() {
			@Override
			protected void configure() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
				super.configure();
				bindToSupplier(Stage.class, () -> stage);
				pencil = mock(Pencil.class);
				bindToInstance(Pencil.class, pencil);
				bindToInstance(TextSetter.class, mock(TextSetter.class));
				bindAsEagerSingleton(Hand.class);
				bindAsEagerSingleton(ShapeDotCustomiser.class);
				bindToInstance(MetaShapeCustomiser.class, mock(MetaShapeCustomiser.class));
			}
		};
	}

	@Test
	public void testControllerNotActivatedWhenSelectionEmpty() {
		new CompositeGUIVoidCommand(activateHand, updateIns, checkInsDeactivated).execute();
	}

	@Test
	public void testControllerActivatedWhenSelectionDot() {
		new CompositeGUIVoidCommand(selectionAddDot, activateHand, updateIns).execute();
		assertTrue(ins.isActivated());
		assertTrue(titledPane.isVisible());
	}

	@Test
	public void testControllerDeactivatedWhenSelectionNotDot() {
		new CompositeGUIVoidCommand(selectionAddRec, activateHand, updateIns).execute();
		assertFalse(ins.isActivated());
		assertFalse(titledPane.isVisible());
	}

	@Test
	public void testControllerDeactivatedWhenSelectionEmpty() {
		new CompositeGUIVoidCommand(activateHand, updateIns).execute();
		assertFalse(ins.isActivated());
		assertFalse(titledPane.isVisible());
	}

	@Test
	public void testDotSizeSelection() {
		doTestSpinner(new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddRec, selectionAddDot, updateIns), dotSizeField,
			incrementDotSize, Arrays.asList(
			() ->  ((Dot) drawing.getSelection().getShapeAt(0).orElseThrow()).getDiametre(),
			() ->  ((Dot) drawing.getSelection().getShapeAt(2).orElseThrow()).getDiametre()));
	}

	@Test
	public void testSelectDotStyleSelection() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddRec, selectionAddDot, updateIns).execute();
		final DotStyle style = dotCB.getSelectionModel().getSelectedItem();
		selectNextDotStyle.execute();
		waitFXEvents.execute();
		final DotStyle newStyle = dotCB.getSelectionModel().getSelectedItem();
		assertEquals(newStyle, ((Dot) drawing.getSelection().getShapeAt(0).orElseThrow()).getDotStyle());
		assertEquals(newStyle, ((Dot) drawing.getSelection().getShapeAt(2).orElseThrow()).getDotStyle());
		assertNotEquals(style, newStyle);
	}

	@Test
	public void testSelectFillingNotEnabledWhenNonFillableStyleSelection() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, updateIns).execute();
		setDotStyle.execute(DotStyle.ASTERISK);
		waitFXEvents.execute();
		assertTrue(fillingB.isDisabled());
	}

	@Test
	public void testSelectFillingEnabledWhenFillableStyleSelection() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddRec, selectionAddDot, updateIns, setDotStyleFillable).execute();
		assertFalse(fillingB.isDisabled());
	}

	@Test
	public void testPickLineColourSelection() {
		new CompositeGUIVoidCommand(activateHand, selectionAddDot, selectionAddBezier, selectionAddDot, setDotStyleFillable, updateIns).execute();
		final Color col = fillingB.getValue();
		pickFillingColour.execute();
		waitFXEvents.execute();
		assertEquals(fillingB.getValue(), ((Dot) drawing.getSelection().getShapeAt(0).orElseThrow()).getDotFillingCol().toJFX());
		assertEquals(fillingB.getValue(), ((Dot) drawing.getSelection().getShapeAt(2).orElseThrow()).getDotFillingCol().toJFX());
		assertNotEquals(col, fillingB.getValue());
	}
}
