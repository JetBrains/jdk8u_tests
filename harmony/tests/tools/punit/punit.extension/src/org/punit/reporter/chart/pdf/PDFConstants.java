/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart.pdf;

import com.lowagie.text.*;

public class PDFConstants {
	public static final Font H1_FONT = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);

	public static final Font H2_FONT = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);

	public static final Font BIG_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);

	public static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.DEFAULTSIZE);

	public static final int DEFAULT_HEIGHT = 500;

	public static final int DEFAULT_WIDTH = 500;

	static final String WEBSITE = "https://sourceforge.net/projects/p-unit/"; //$NON-NLS-1$

	static final String POSTFIX = ".pdf"; //$NON-NLS-1$

}
