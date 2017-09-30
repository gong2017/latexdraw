package net.sf.latexdraw.parsers.pst2;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import net.sf.latexdraw.parsers.pst.PSTContext;
import net.sf.latexdraw.parsers.pst.PSTLatexdrawListener;
import net.sf.latexdraw.parsers.pst.PSTLexer;
import net.sf.latexdraw.parsers.pst.PSTParser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Before;

import static org.junit.Assert.fail;

public abstract class TestPSTParser {
	PSTLatexdrawListener listener;

	@Before
	public void setUp() throws Exception {
		listener = new PSTLatexdrawListener();
		listener.LOG.addHandler(new Handler() {
			@Override
			public void publish(final LogRecord record) {
				fail(record.getMessage());
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() throws SecurityException {
			}
		});
	}

	void parser(final String code) {
		final PSTParser parser = new PSTParser(new CommonTokenStream(new PSTLexer(CharStreams.fromString(code))));
		parser.addParseListener(listener);
		parser.addErrorListener(new ErrorListener());
		parser.pstCode(new PSTContext(false));
	}

	public abstract String getCommandName();

	public abstract String getBasicCoordinates();

	static class ErrorListener extends BaseErrorListener {
		@Override
		public void syntaxError(final Recognizer<?, ?> recognizer, final Object offendingSymbol, final int line, final int charPositionInLine, final
		String msg, final RecognitionException e) {
			fail(msg);
		}
	}
}
