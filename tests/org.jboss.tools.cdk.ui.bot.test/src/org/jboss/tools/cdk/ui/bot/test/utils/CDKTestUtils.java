package org.jboss.tools.cdk.ui.bot.test.utils;

import java.util.Map;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;

public class CDKTestUtils {

	private static Logger log = Logger.getLogger(CDKTestUtils.class);
	
	public static String getSystemProperty(String systemProperty) {
		String property = System.getProperty(systemProperty);
		if (!(property == null || property.equals("") || property.startsWith("${"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return property;
		}
		return null;
	}
	
	public static void checkParameterNotNull(Map<String, String> dict) {
		for (String key : dict.keySet()) {
			String value = dict.get(key);
			if (value == null) {
				throw new RedDeerException("Given key " + key + " value is null"); //$NON-NLS-1$
			}
			log.info("Given key " + key + " value is " + value);
		}	
	}
	
	public static void deleteCDEServer(String adapter) {
		log.info("Deleting Container Development Environment server adapter:" + adapter); //$NON-NLS-1$
		ServersView servers = new ServersView();
		servers.open();
		try {
			servers.getServer(adapter).delete(true);
		} catch (EclipseLayerException exc) {
			log.error(exc.getMessage());
			exc.printStackTrace();
		}
	}
	
	public static NewServerWizardDialog openNewServerWizardDialog() {
		log.info("Adding new Container Development Environment server adapter"); //$NON-NLS-1$
		// call new server dialog from servers view
		ServersView view = new ServersView();
		view.open();
		NewServerWizardDialog dialog = view.newServer();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		return dialog;
	}

}
