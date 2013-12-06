package de.tud.eclipse_achievements.ui;

import org.eclipse.osgi.util.NLS;

/**
 * @author Christian Vogel
 */
public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "com.dubture.symfony.ui.messages"; //$NON-NLS-1$
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {}

}
