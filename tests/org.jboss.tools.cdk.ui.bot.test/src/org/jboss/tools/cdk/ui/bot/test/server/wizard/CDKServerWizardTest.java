package org.jboss.tools.cdk.ui.bot.test.server.wizard;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.cdk.reddeer.ui.wizard.NewCDK3ServerContainerWizardPage;
import org.jboss.tools.cdk.reddeer.ui.wizard.NewCDKServerContainerWizardPage;
import org.jboss.tools.cdk.ui.bot.test.CDKAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import org.eclipse.jface.dialogs.TitleAreaDialog;

/**
 * Test of CDK3 Server Wizard
 * @author odockal
 *
 */
public class CDKServerWizardTest extends CDKAbstractTest {

	public static final String CDK_SERVER_NAME = "Red Hat Container Development Kit"; //$NON-NLS-1$

	public static final String CDK3_SERVER_NAME = "Red Hat Container Development Kit 3"; //$NON-NLS-1$
	
	private static Logger log = Logger.getLogger(CDKServerWizardTest.class);
	
	@Test
	public void testCDK2xServerType() {
		assertServerType(CDK_SERVER_NAME);
	}
	
	@Test
	public void testCDK3ServerType() {
		assertServerType(CDK3_SERVER_NAME);
	}
	
	@Test
	public void testNewCDKServerWizard() {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		page.selectType(SERVER_TYPE_GROUP, CDK_SERVER_NAME);
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		dialog.next();
		NewCDKServerContainerWizardPage containerPage = new NewCDKServerContainerWizardPage();
		
		assertTrue(containerPage.getDomain().equalsIgnoreCase(CREDENTIALS_DOMAIN));
		
		assertFalse(dialog.getPageTitle().contains(""));
		
		// activate title area text - error report by clicking into label
		containerPage.setFolder("");
		
		log.info("Setting credentials"); //$NON-NLS-1$
		containerPage.setCredentials(USERNAME, PASSWORD);
		
	}
	
	private void assertServerType(String serverType) {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		try {
			TreeItem item = new DefaultTreeItem(new String[] {SERVER_TYPE_GROUP}).getItem(serverType);
			item.select();
			assertTrue(item.getText().equalsIgnoreCase(serverType));
			new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		} catch (CoreLayerException coreExp) {
			log.error(coreExp.getMessage());
			fail("Server type " + serverType + " was not found in New Server Wizard");
		}
		assertEquals(new LabeledText("Server's host name:").getText(), "localhost");
		assertEquals(new LabeledText("Server name:").getText(), "Container Development Environment");
		assertTrue(dialog.isNextEnabled());
		dialog.cancel();
	}
}
