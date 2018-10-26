package net.sf.latexdraw.view.svg;

import java.lang.reflect.InvocationTargetException;
import net.sf.latexdraw.badaboom.BadaboomCollector;
import net.sf.latexdraw.data.ConfigureInjection;
import net.sf.latexdraw.data.InjectionExtension;
import net.sf.latexdraw.models.interfaces.shape.IShape;
import net.sf.latexdraw.parsers.svg.SVGAttributes;
import net.sf.latexdraw.parsers.svg.SVGDefsElement;
import net.sf.latexdraw.parsers.svg.SVGDocument;
import net.sf.latexdraw.parsers.svg.SVGElement;
import net.sf.latexdraw.parsers.svg.SVGSVGElement;
import net.sf.latexdraw.util.Injector;
import net.sf.latexdraw.util.LNamespace;
import net.sf.latexdraw.util.SystemService;
import net.sf.latexdraw.view.PolymorphicConversion;
import net.sf.latexdraw.view.jfx.ViewFactory;
import net.sf.latexdraw.view.latex.DviPsColors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.malai.command.CommandsRegistry;
import org.malai.undo.UndoCollector;

@ExtendWith(InjectionExtension.class)
abstract class TestSVGBase<T extends IShape> implements PolymorphicConversion<T> {
	SVGDocument doc;
	SVGShapesFactory factory;

	@ConfigureInjection
	Injector createInjector() {
		return new Injector() {
			@Override
			protected void configure() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
				bindAsEagerSingleton(SystemService.class);
				bindAsEagerSingleton(ViewFactory.class);
				bindAsEagerSingleton(SVGShapesFactory.class);
			}
		};
	}

	@BeforeEach
	void setUp(final SVGShapesFactory svgfac) {
		factory = svgfac;
		doc = new SVGDocument();
		final SVGSVGElement root = doc.getFirstChild();
		root.setAttribute("xmlns:" + LNamespace.LATEXDRAW_NAMESPACE, LNamespace.LATEXDRAW_NAMESPACE_URI);
		root.appendChild(new SVGDefsElement(doc));
		root.setAttribute(SVGAttributes.SVG_VERSION, "1.1");
		root.setAttribute(SVGAttributes.SVG_BASE_PROFILE, "full");
	}

	@AfterEach
	public void tearDown() {
		DviPsColors.INSTANCE.clearUserColours();
		CommandsRegistry.INSTANCE.clear();
		CommandsRegistry.INSTANCE.removeAllHandlers();
		BadaboomCollector.INSTANCE.clear();
		UndoCollector.INSTANCE.clear();
	}

	@Override
	public T produceOutputShapeFrom(final T sh) {
		final SVGElement elt = factory.createSVGElement(sh, doc);
		doc.getFirstChild().appendChild(elt);
		return (T) factory.createShape(elt);
	}
}
