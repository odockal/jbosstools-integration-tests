/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi11;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beansxml.discovery.template.BeanDiscoveryInArchivesTemplate;
import org.junit.Test;

/**
 * JBIDE-
 * @author odockal
 *
 */
@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class BeanDiscoveryInImplicitArchivesTestCDI11 extends BeanDiscoveryInArchivesTemplate {

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "13"));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, FAMILY, ServerMatcher.WildFly()),
					new RequirementMatcher(JBossServer.class, VERSION, "13"),
					new RequirementMatcher(JRE.class, VERSION, "1.8"));
		}
	}
	
	public BeanDiscoveryInImplicitArchivesTestCDI11() {
		CDIVersion = "1.2";
	}
	
	@Test
	public void testValidationOfBeanDiscoveryInImplicitArchives() {
		super.validationOfBeanDiscoveryInImplicitArchives();
	}
}