/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.ide.core.templates;

import com.liferay.ide.core.LiferayCore;
import com.liferay.ide.core.util.CoreUtil;

import freemarker.template.Template;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Gregory Amerson
 * @author Cindy Li
 */
public class TemplateOperation implements ITemplateOperation {

	public TemplateOperation(TemplateModel model) {
		this.model = model;
	}

	public boolean canExecute() {
		try {
			if (((outputFile == null) && (outputBuffer == null)) || (model == null) || (getTemplate() == null)) {
				return false;
			}

			String[] names = model.getRequiredVarNames();

			if (CoreUtil.isNullOrEmpty(names)) {
				return true;
			}

			for (String name : names) {
				if (!getContext().containsKey(name)) {
					LiferayCore.logError("Could not execute template operation: context var " + name + " not found.");

					return false;
				}
			}

			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void execute(IProgressMonitor monitor) throws Exception {
		if (!canExecute()) {
			return;
		}

		StringWriter writer = new StringWriter();

		TemplateContext templateContext = (TemplateContext)getContext();

		getTemplate().process(templateContext.getMap(), writer);

		String result = writer.toString();

		if (outputFile != null) {
			if (outputFile.exists()) {
				outputFile.setContents(new ByteArrayInputStream(result.getBytes()), true, true, monitor);
			}
			else {
				outputFile.create(new ByteArrayInputStream(result.getBytes()), true, monitor);
			}
		}
		else if (outputBuffer != null) {
			outputBuffer.delete(0, outputBuffer.length());

			outputBuffer.append(result);
		}
	}

	public ITemplateContext getContext() {
		if (context == null) {
			context = createContext();
		}

		return context;
	}

	public void setOutputBuffer(StringBuffer buffer) {
		outputBuffer = buffer;
	}

	public void setOutputFile(IFile file) {
		outputFile = file;
	}

	protected TemplateContext createContext() {
		return new TemplateContext();
	}

	protected Template getTemplate() throws Exception {
		if (model == null) {
			return null;
		}

		if (template == null) {
			template = model.getConfig().getTemplate(model.getResource());
		}

		return template;
	}

	protected ITemplateContext context;
	protected TemplateModel model;
	protected StringBuffer outputBuffer;
	protected IFile outputFile;
	protected Template template;

}