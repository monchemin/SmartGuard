package ca.uqac.nyemo.utils.wavelet; /**
 * 
 * @author Mark Bishop; 2014 License GNU v3; This class is free software: you
 *         can redistribute it and/or modify it under the terms of the GNU
 *         General Public License as published by the Free Software Foundation,
 *         either version 3 of the License, or (at your option) any later
 *         version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Class responsibility: provide named variables for expression compilation as
 * described in the JEL documentation
 */

// Add variables as needed. In the demo I only use x or t for my independent
// variable.
public class VariableProvider {
	public double xvar;
	public double tvar;
	public double picon = Math.PI;

	// Add others as needed eg:
	// public double yvar;

	// Let the user pass either t or x as the independent variable.
	public double x() {
		return xvar;
	};

	public double t() {
		return xvar;
	};

	// Enables a lower case pi
	public double pi() {
		return picon;
	};

	// // Not used in this demo, but provides a hint for those who want to
	// compile multivariate functions.
	// public double y() {
	// return yvar;
	// };

}
