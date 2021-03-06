/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.maven.ui.bot.test.configurator;

import java.io.FileNotFoundException;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.maven.ui.bot.test.utils.ProjectHasNature;
import org.junit.Test;

/**
 * @author Rastislav Wagner
 * 
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT)
public class JPAConfiguratorTest extends AbstractConfiguratorsTest {
	
	private String projectNameNoRuntime = PROJECT_NAME_JPA + "_noRuntime";
	
	@Test
	public void testJPAConfigurator() throws FileNotFoundException {
		createWebProject(projectNameNoRuntime, null, false);
		convertToMavenProject(projectNameNoRuntime, "war", false);
		addPersistence(projectNameNoRuntime);
		updateConf(projectNameNoRuntime);
		new WaitUntil(new ProjectHasNature(projectNameNoRuntime, JPA_FACET, "2.0"));
	}
}