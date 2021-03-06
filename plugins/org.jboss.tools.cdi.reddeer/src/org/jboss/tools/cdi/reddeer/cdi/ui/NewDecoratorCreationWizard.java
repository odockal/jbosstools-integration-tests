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
package org.jboss.tools.cdi.reddeer.cdi.ui;

import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.condition.TableItemIsFound;

public class NewDecoratorCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Decorator";
	public static final String SHELL_TEXT = "New Decorator";
	
	public NewDecoratorCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setPublic(boolean isPublic){
		new RadioButton("public").toggle(isPublic);
	}
	
	public void setDefault(boolean isDefault){
		new RadioButton("default").toggle(isDefault);
	}
	
	public void setPrivate(boolean isPrivate){
		new RadioButton("private").toggle(isPrivate);
	}

	public void setProtected(boolean isProtected){
		new RadioButton("protected").toggle(isProtected);
	}
	
	public void setFinal(boolean isFinal){
		new CheckBox("final").toggle(isFinal);
	}
	
	public void setAbstract(boolean isAbstract){
		new CheckBox("abstract").toggle(isAbstract);
	}
	
	public void setStatic(boolean isStatic){
		new CheckBox("static").toggle(isStatic);
	}
	
	public void setRegisterInBeans(boolean register){
		new CheckBox("Register in beans.xml").toggle(register);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
	
	public void setDelegateFieldName(String name){
		new LabeledText("Delegate Field Name:").setText(name);
	}
	
	public String getName(){
		return new LabeledText("Name:").getText();
	}
	
	public String getPackage(){
		return new LabeledText("Package:").getText();
	}
	
	public void addDecoratedTypeInterfaces(String bindings){
		new PushButton("Add...").click();
		new DefaultShell("Implemented Interfaces Selection");
		new DefaultText(0).setText(bindings);
		String[] splitBindings = bindings.split("\\.");
		Table t = new DefaultTable();
		new WaitUntil(new TableItemIsFound(t, splitBindings[splitBindings.length-1]), TimePeriod.LONG);
		for(TableItem ti: new DefaultTable().getItems()){
			if(ti.getText().toLowerCase().contains(splitBindings[splitBindings.length-1].toLowerCase())){
				ti.select();
				break;
			}
		}
		new PushButton("OK").click();
		new DefaultShell("New Decorator");
	}
	
	public List<TableItem> getDecoratedTypeInterfaces(){
		return new DefaultTable().getItems();
	}


}
