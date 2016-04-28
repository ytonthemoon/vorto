package org.eclipse.vorto.core.model;

import java.io.File;

import org.eclipse.vorto.core.api.model.mapping.MappingModel;

public class MappingSetting {
	private MappingModel model;
	private File rootDirectory;

	public MappingModel getModel() {
		return model;
	}

	public void setModel(MappingModel model) {
		this.model = model;
	}

	public File getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(File rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
}
