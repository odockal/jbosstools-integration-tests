package org.jboss.tools.cdi.reddeer.xhtml;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;


public class NewXHTMLWizard extends NewMenuWizard{
	
	public static final String CATEGORY="JBoss Tools Web";
	public static final String NAME="XHTML Page";
	public static final String SHELL_TEXT = "New XHTML Page";
	
	public NewXHTMLWizard(){
		super(SHELL_TEXT,CATEGORY,NAME);
	}
	
	@Override
	public void finish() {
		super.finish();
		ShellIsAvailable shellToWait = new ShellIsAvailable("Browser Engine");
		new WaitUntil(shellToWait, TimePeriod.MEDIUM, false);
		if (shellToWait.getResult() != null) {
			new PushButton(new WithTextMatcher(new RegexMatcher("Stay with.*"))).click();
			new WaitWhile(shellToWait, TimePeriod.MEDIUM);
		}
	}

}
