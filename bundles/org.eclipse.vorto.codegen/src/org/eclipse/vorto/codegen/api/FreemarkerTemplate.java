package org.eclipse.vorto.codegen.api;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class FreemarkerTemplate<Context> implements IFileTemplate<Context> {

	private Configuration cfg;
	private String folder;
	private String fileName;
	private String content;
	
	public FreemarkerTemplate(String templateName, String outputPathName, IMappingContext mappingContext) {
		cfg = newConfiguration();
		final Path path = Paths.get(outputPathName);
		this.folder = path.getParent().toString();
		this.fileName = path.getFileName().toString();
		this.content = new String(mappingContext.getNonMappingFile(templateName));
	}
	
	public String getContent(Context context) {
		try {
			Template template = new Template("templateName", new StringReader(content), cfg);
			Writer out = new StringWriter();
			template.process(context, out);
			return out.toString();
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	public String getFileName(Context context) {
		return fileName;
	}

	public String getPath(Context context) {
		return folder;
	}

	private Configuration newConfiguration() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
		cfg.setDefaultEncoding("UTF-8");
		return cfg;
	}
}
