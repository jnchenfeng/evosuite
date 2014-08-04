package org.evosuite.symbolic.vm.string.reader;

import java.io.Reader;
import java.io.StringReader;

import org.evosuite.symbolic.expr.reader.StringReaderExpr;
import org.evosuite.symbolic.expr.str.StringValue;
import org.evosuite.symbolic.vm.NonNullReference;
import org.evosuite.symbolic.vm.SymbolicEnvironment;
import org.evosuite.symbolic.vm.SymbolicFunction;
import org.evosuite.symbolic.vm.SymbolicHeap;
import org.evosuite.symbolic.vm.string.Types;

public final class Reader_Read extends SymbolicFunction {

	private static final String READ = "read";

	public Reader_Read(SymbolicEnvironment env) {
		super(env, Types.JAVA_IO_READER, READ, Types.TO_INT_DESCRIPTOR);
	}

	@Override
	public Object executeFunction() {

		Reader conc_reader = (Reader) this.getConcReceiver();

		if (conc_reader instanceof StringReader) {
			NonNullReference symb_str_reader = this.getSymbReceiver();
			StringReader conc_str_reader = (StringReader) conc_reader;

			StringReaderExpr stringReaderExpr = (StringReaderExpr) env.heap
					.getField(Types.JAVA_IO_STRING_READER,
							SymbolicHeap.$STRING_READER_VALUE, conc_str_reader,
							symb_str_reader);

			if (stringReaderExpr != null
					&& stringReaderExpr.containsSymbolicVariable()) {

				StringValue symb_string = stringReaderExpr.getString();
				String conc_string = symb_string.getConcreteValue();

				int currPosition = stringReaderExpr.getReaderPosition();

				if (currPosition < conc_string.length()) {
					// update symbolic string reader
					currPosition++;

					int conc_string_reader_value;
					if (currPosition >= conc_string.length()) {
						conc_string_reader_value = -1;
					} else {
						conc_string_reader_value = conc_string
								.charAt(currPosition);
					}

					StringReaderExpr newStringReaderExpr = new StringReaderExpr(
							(long) conc_string_reader_value, symb_string,
							currPosition);
					env.heap.putField(Types.JAVA_IO_STRING_READER,
							SymbolicHeap.$STRING_READER_VALUE, conc_str_reader,
							symb_str_reader, newStringReaderExpr);

				}

				// returns STRING_READER(string,currPosition)
				return stringReaderExpr;

			}

		}

		return this.getSymbIntegerRetVal();
	}
}