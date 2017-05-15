/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test.server.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.WidgetIsFound;
import org.jboss.reddeer.core.matcher.ClassMatcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.preferences.OpenShift3SSLCertificatePreferencePage;
import org.jboss.tools.cdk.reddeer.ui.CDEServersView;
import org.jboss.tools.cdk.ui.bot.test.CDKAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class CDKDevstudioAbstractTest extends CDKAbstractTest {

	protected ServersView serversView;
	
	protected Server server;
	
	private static final Logger log = Logger.getLogger(CDKDevstudioAbstractTest.class);
	
	public static final String SERVER_ADAPTER = "Container Development Environment"; //$NON-NLS-1$
	
	protected abstract Server getCDEServer();
	
	protected abstract ServersView getServersView();
	
	protected abstract void setServersView(ServersView view);
	
	protected abstract void setCDEServer(Server server);
	
	protected abstract String getServerAdapter();
	
	@BeforeClass
	public static void setUpEnvironemnt() {
		log.info("Checking given program arguments"); //$NON-NLS-1$
		Map<String, String> dict = new HashMap<>();
		dict.put("Username", USERNAME);
		dict.put("Password", PASSWORD);
		CDKTestUtils.checkParameterNotNull(dict);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
	
	@Before
	public void setUpServers() {
		log.info("Open Servers view tab"); //$NON-NLS-1$
		setServersView(new CDEServersView());
		getServersView().open();
		log.info("Getting server object from Servers View with name: " + getServerAdapter()); //$NON-NLS-1$
		setCDEServer(getServersView().getServer(getServerAdapter()));
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
	
	@After
	public void tearDownServers() {
		if (getCDEServer().getLabel().getState() == ServerState.STARTED) {
			getCDEServer().stop();
		}
		// remove SSL Certificate to be added at next server start at method annotated with before
		deleteCertificates();
		setCDEServer(null);
		getServersView().close();
	}
	
	protected void startServerAdapter() {
		log.info("Starting server adapter"); //$NON-NLS-1$
		try {
			getCDEServer().start();
		} catch (ServersViewException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		printCertificates();
		checkAvailableServers();
		assertEquals(ServerState.STARTED, getCDEServer().getLabel().getState());
	}
	
	private void checkAvailableServers() {
		for (Server serverItem : getServersView().getServers()) {
			String serverName = serverItem.getLabel().getName();
			log.info(serverName);
		}
		assertTrue(getCDEServer().getLabel().getName().contains(getServerAdapter()));
	}
	
	private static void printCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.printCertificates();
		dialog.ok();
	}
	
	private static void deleteCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.deleteAll();
		preferencePage.apply();
		dialog.ok();
	}
	
	// removes access redhat com credentials used for first cdk run
	protected static void removeAccessRedHatCredentials() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		dialog.select("JBoss Tools", "Credentials"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
	        new WaitUntil(new WidgetIsFound<org.eclipse.swt.custom.CLabel>(
	        		new ClassMatcher(org.eclipse.swt.custom.CLabel.class), 
	        		new WithMnemonicTextMatcher("Credentials")), TimePeriod.NORMAL); //$NON-NLS-1$
	        new DefaultCLabel("Credentials"); //$NON-NLS-1$
	        DefaultTree tree = new DefaultTree(1);
	        TreeItem item = TreeViewerHandler.getInstance().getTreeItem(tree, new String[]{CREDENTIALS_DOMAIN, USERNAME});
	        item.select();
	        new PushButton(new WithTextMatcher("Remove User")).click(); //$NON-NLS-1$
	        new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
        } catch (WaitTimeoutExpiredException exc) {
        	log.error("JBoss Tools - Credentials preferences page has timed out"); //$NON-NLS-1$
        	exc.printStackTrace();
        } catch (JFaceLayerException exc) {
        	log.error("JBoss Tools - Credentials does not contain required username to be deleted"); //$NON-NLS-1$
        	exc.printStackTrace();
        } finally {
	        dialog.ok();
		}
	}
	
}
