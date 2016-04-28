package org.eclipse.vorto.codegen.examples;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.vorto.codegen.api.IFileTemplate;
import org.eclipse.vorto.codegen.api.IMappingContext;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerEngine {

	public Collection<MappingRule> filterRules(IMappingContext context, Predicate<MappingRule> rule) {
		return context.getAllRules().stream().filter(rule).collect(Collectors.toList());
	}

	// A filter for a stereotype target with the name 'template'
	public Predicate<MappingRule> stereotypeTemplateFilter() {
		return new Predicate<MappingRule>() {
			public boolean test(MappingRule rule) {
				return rule.getTarget() instanceof StereoTypeTarget
						&& ((StereoTypeTarget) rule.getTarget()).getName().equals("template");
			}
		};
	}

	public IFileTemplate<InformationModel> ruleToTemplate(final Object object, final IMappingContext context, final MappingRule rule) {
		final Configuration cfg = newConfiguration();

		final StereoTypeTarget target = (StereoTypeTarget) rule.getTarget();
		final String templateName = getTargetValue(target, "templateName");
		
		final String templatedOutputFile = getTargetValue(target, "outputFile");
		final String outputFile = processTemplatedString(object, cfg, templatedOutputFile);
		final Path path = Paths.get(outputFile);
		final String folder = path.getParent().toString();
		final String fileName = path.getFileName().toString();

		return new IFileTemplate<InformationModel>() {
			public String getFileName(InformationModel context) {
				return fileName;
			}

			public String getPath(InformationModel context) {
				return folder;
			}

			public String getContent(InformationModel infoModel) {
				return processTemplatedString(object, cfg, new String(context.getNonMappingFile(templateName)));
			}
		};
	}
	
	private String processTemplatedString(final Object infomodel, final Configuration cfg, final String str) {
		try {
			Template template = new Template("templateName", new StringReader(str), cfg);
			Writer out = new StringWriter();
			template.process(infomodel, out);
			return out.toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private String getTargetValue(StereoTypeTarget target, String name) {
		for (Attribute attr : target.getAttributes()) {
			if (attr.getName().equals(name)) {
				return attr.getValue();
			}
		}

		return null;
	}

	private Configuration newConfiguration() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);

		cfg.setDefaultEncoding("UTF-8");

		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		return cfg;
	}
}
