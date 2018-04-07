package ca.uqac.nyemo.utils.wavelet; /**
 * Author Mark Bishop; 2014
 * License GNU v3; 
 * This class is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



import ca.uqac.nyemo.utils.wavelet.gnu.jel.CompilationException;
import ca.uqac.nyemo.utils.wavelet.gnu.jel.CompiledExpression;
import ca.uqac.nyemo.utils.wavelet.gnu.jel.Evaluator;
import ca.uqac.nyemo.utils.wavelet.gnu.jel.Library;

/**
 * 
 * * Class responsibility: Provide a container for xy data and methods for
 * creating xy data sets. Also provide ways to plot and/or tabulate the values.
 * * A few public static methods for determining minima and maxima of sequence
 * arrays are also included. *
 */

public class Signal {

	double[][] xy;
	String function;

	//Color color;
	int pointSize;

	/**
	 * Construct using sequential x and y data.
	 * 
	 * @param x
	 * @param y
	 */
	public Signal(double[] x, double[] y) {
		int n = x.length;
		xy = new double[2][n];
		for (int i = 0; i < n; i++) {
			xy[0][i] = x[i];
			xy[1][i] = y[i];
		}
	}

	/**
	 * Alternative constructor that takes both x and y in one array object
	 * 
	 * @param xy
	 *            a double[][] such that: x = xy[0] and y = xy[1]
	 */
	public Signal(double[][] xy) {
		this.xy = xy;
	}

	/**
	 * Alternative constructor that takes both x and y in one array object and
	 * also takes a String that may be used as a label or in a subsequent JEL
	 * compilation. *
	 * 
	 * @param xy
	 *            a double[][] such that: x = xy[0] and y = xy[1]
	 * @param name
	 *            a label or JEL expression for subsequent use.
	 */
	public Signal(double[][] xy, String name) {
		this.xy = xy;
		this.function = name;
	}

	/**
	 * Alternative constructor for computing sequential xy data conditionally at
	 * run time. X data is computed using parameter values of either: {x
	 * minimum, x maximum, signal length} or {x minimum, delta x, signal length}
	 * 
	 * @param xMin
	 *            Starting (lowest) value for range (x).
	 * @param rangeParameter
	 *            Interpreted as x maximum when rangeOption is MinMax and as
	 *            delta x when rangeOption is Incremental
	 * @param length
	 *            total number of values
	 * @param function
	 *            A JEL compatible String representing the expression used to
	 *            compute y = f(x) For example, 2*sin(x)
	 * @param rangeOption
	 *            either RangeOption.MinMax or RangeOption.Incremental
	 */
	public Signal(double xMin, double rangeParameter, int length,
			String function, RangeOption rangeOption) {
		this.function = function;
		if (rangeOption == RangeOption.MinMax) {
			double dx = dxFromCountRange(rangeParameter, xMin, length);
			createXY(xMin, dx, length);
		} else {
			double xMax = rangeParameter;
			createXY(xMin, xMax, length);
		}
	}

	public double[][] getXY() {
		return xy;
	}

	public double[] getX() {
		return xy[0];
	}

	public double[] getY() {
		return xy[1];
	}

	public int count() {
		return xy[0].length;
	}




	/**
	 * Call samplingFunction() to create x = xy[0] and use JEL to determine y =
	 * xy[1]
	 * 
	 * @param xMin
	 * @param dx
	 * @param length
	 */
	private void createXY(double xMin, double dx, int length) {
		xy = new double[2][];
		xy[0] = xSamplingFunction(xMin, dx, length);
		try {
			xy[1] = compileExpression(xy[0], function);
		} catch (CompilationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate x using range parameters.
	 * 
	 * @param xMin
	 * @param dx
	 *            delta x
	 * @param count
	 * @return
	 */
	private static double[] xSamplingFunction(double xMin, double dx, int count) {
		int n = count;
		double[] x = new double[n];
		double element = xMin;
		for (int i = 0; i < n; i++) {
			x[i] = element;
			element += dx;
		}
		return x;
	}

	/**
	 * This method provides the interface to JEL. "x" is used as the variable
	 * here but it may refer to "t" from the calling function
	 * 
	 * @see "JEL Documentation at GNU Project"
	 * @param x
	 *            vector containing x values
	 * @param expr
	 *            String representation of function e.g. sin(2*pi*16*x)
	 * @return a vector containing the f(x) values
	 * @throws CompilationException
	 */
	public static double[] compileExpression(double[] x, String expr)
			throws CompilationException {
		if (!(expr.contains("x") || expr.contains("t"))) {
			expr = "(0.0*x)+(" + expr + ")";
		}
		int n = x.length;
		double[] fX = new double[n];
		// Set up class for Java Math namespace
		@SuppressWarnings("rawtypes")
		Class[] staticLib = new Class[1];
		try {
			staticLib[0] = Class.forName("java.lang.Math");
		} catch (ClassNotFoundException e) {
			System.out.print("1");
		}
		// Setup class for variables
		@SuppressWarnings("rawtypes")
		Class[] dynamicLib = new Class[1];
		VariableProvider variables = new VariableProvider();
		Object[] context = new Object[1];
		context[0] = variables;
		dynamicLib[0] = variables.getClass();
		Library lib = new Library(staticLib, dynamicLib, null, null, null);
		// // Commented because we have stateless members
		// // See http://www.gnu.org/software/jel/api/index.html
		// try {
		// lib.markStateDependent("random", null);
		// } catch (Exception e) {
		// System.out.print("1");
		// }
		CompiledExpression expr_c = null;
		try {
			expr_c = Evaluator.compile(expr, lib);
		} catch (CompilationException ce) {
			System.err.print("--- COMPILATION ERROR :");
			System.err.println(ce.getMessage());
			System.err.print("                       ");
			System.err.println(expr);
			int column = ce.getColumn(); // Column, where error was found
			for (int i = 0; i < column + 23 - 1; i++)
				System.err.print(' ');
			System.err.println('^');
		}
		if (expr != null) {
			try {
				for (int i = 0; i < n; i++) {
					variables.xvar = x[i]; // Value of the variable
					fX[i] = (double) expr_c.evaluate(context);
				}
			} catch (Throwable e) {
				System.err.println("Exception emerged from JEL compiled"
						+ " code (IT'S OK) :"); // Konstantin's message
				System.err.print(e);
			}
		}
		return fX;
	}

	/**
	 * This method provides the interface to JEL. "x" is used as the variable
	 * here but it may refer to "t" from the calling function
	 * 
	 * @see "JEL Documentation at GNU Project"
	 * @param x
	 *            vector containing x values
	 * @param expr
	 *            String representation of function e.g. sin(2*pi*16*x)
	 * @return f(x) where f is defined by expr
	 * @throws CompilationException
	 */
	public static double compileExpression(double x, String expr)
			throws CompilationException {
		if (!(expr.contains("x") || expr.contains("t"))) {
			expr = "(0.0*x)+(" + expr + ")";
		}
		double fX = 0;
		// Set up class for Java Math namespace
		@SuppressWarnings("rawtypes")
		Class[] staticLib = new Class[1];
		try {
			staticLib[0] = Class.forName("java.lang.Math");
		} catch (ClassNotFoundException e) {
			System.out.print("1");
		}
		// Setup class for variables
		@SuppressWarnings("rawtypes")
		Class[] dynamicLib = new Class[1];
		VariableProvider variables = new VariableProvider();
		Object[] context = new Object[1];
		context[0] = variables;
		dynamicLib[0] = variables.getClass();
		Library lib = new Library(staticLib, dynamicLib, null, null, null);
		// // Commented because we have stateless members
		// // See http://www.gnu.org/software/jel/api/index.html
		// try {
		// lib.markStateDependent("random", null);
		// } catch (Exception e) {
		// System.out.print("1");
		// }
		CompiledExpression expr_c = null;
		try {
			expr_c = Evaluator.compile(expr, lib);
		} catch (CompilationException ce) {
			System.err.print("--- COMPILATION ERROR :");
			System.err.println(ce.getMessage());
			System.err.print("                       ");
			System.err.println(expr);
			int column = ce.getColumn(); // Column, where error was found
			for (int i = 0; i < column + 23 - 1; i++)
				System.err.print(' ');
			System.err.println('^');
		}
		if (expr != null) {
			try {
				variables.xvar = x;
				fX = (double) expr_c.evaluate(context);

			} catch (Throwable e) {
				System.err.println("Exception emerged from JEL compiled"
						+ " code (IT'S OK) :"); // Konstantin's message
				System.err.print(e);
			}
		}
		return fX;
	}

	public static int max(int[] vector) {
		int max = vector[0];
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] > max) {
				max = vector[i];
			}
		}
		return max;
	}

	public static int min(int[] vector) {
		int min = vector[0];
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] < min) {
				min = vector[i];
			}
		}
		return min;
	}

	public static double max(double[] vector) {
		double max = vector[0];
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] > max) {
				max = vector[i];
			}
		}
		return max;
	}

	public static double min(double[] vector) {
		double min = vector[0];
		for (int i = 1; i < vector.length; i++) {
			if (vector[i] < min) {
				min = vector[i];
			}
		}
		return min;
	}

	public static double min(double[][] A) {
		int m = A.length;
		int n = A[0].length;
		double min = A[0][0];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (A[i][j] < min) {
					min = A[i][j];
				}
			}
		}
		return min;
	}

	public static double dxFromCountRange(double max, double min, int count) {
		double dCount = (double) count;
		double dx = (max - min) / (dCount - 1.0);
		return dx;
	}

	public static enum RangeOption implements Runnable {
		Incremental {
			@Override
			public void run() {
				isMinMax(false);
			}
		},
		MinMax {
			@Override
			public void run() {
				isMinMax(true);
			}
		};
		private static boolean isMinMax(boolean value) {
			return value;
		}
	}
}
