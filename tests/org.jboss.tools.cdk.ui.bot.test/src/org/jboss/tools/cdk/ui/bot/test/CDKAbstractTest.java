package org.jboss.tools.cdk.ui.bot.test;

import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;

public abstract class CDKAbstractTest {

	public static final String SERVER_HOST = "localhost"; //$NON-NLS-1$
	
	public static final String SERVER_TYPE_GROUP = "Red Hat JBoss Middleware"; //$NON-NLS-1$
	
	public static final String CREDENTIALS_DOMAIN = "access.redhat.com"; //$NON-NLS-1$
	
	public static final String USERNAME;
	
	public static final String PASSWORD;
	
	static {
		USERNAME = CDKTestUtils.getSystemProperty("developers.username"); //$NON-NLS-1$
		PASSWORD = CDKTestUtils.getSystemProperty("developers.password"); //$NON-NLS-1$
	}
}
