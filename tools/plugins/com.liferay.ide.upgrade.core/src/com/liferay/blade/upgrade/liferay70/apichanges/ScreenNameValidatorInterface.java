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

package com.liferay.blade.upgrade.liferay70.apichanges;

import com.liferay.blade.api.FileMigrator;
import com.liferay.blade.api.JavaFile;
import com.liferay.blade.api.SearchResult;
import com.liferay.blade.upgrade.liferay70.JavaFileMigrator;

import java.io.File;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Gregory Amerson
 */
@Component(property = {
	"file.extensions=java", "problem.title=Added New Methods in the ScreenNameValidator Interface",
	"problem.summary=The ScreenNameValidator interface has new methods getDescription(Locale) and getJSValidation().",
	"problem.tickets=LPS-53409", "problem.section=#added-new-methods-in-the-screennamevalidator-interface",
	"implName=ScreenNameValidatorInterface"
},
	service = FileMigrator.class)
public class ScreenNameValidatorInterface extends JavaFileMigrator {

	@Override
	protected List<SearchResult> searchFile(File file, JavaFile javaFileChecker) {
		return javaFileChecker.findImplementsInterface("ScreenNameValidator");
	}

}