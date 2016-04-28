package org.eclipse.vorto.codegen.examples

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IMappingContext
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource
import org.eclipse.vorto.core.api.model.mapping.MappingRule
import org.eclipse.vorto.core.api.model.mapping.Source

class FreeMarker implements IVortoCodeGenerator {

	FreeMarkerEngine engine = new FreeMarkerEngine();

	override generate(InformationModel infomodel, IMappingContext mappingContext) {
		val output = new GenerationResultZip(infomodel,getServiceKey());
		val generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		// get only the rules that have a stere otype target and 'template' as name
		for(MappingRule rule : engine.filterRules(mappingContext, engine.stereotypeTemplateFilter())) {
			rule.sources.forEach[ source | {
				var model = getSource(source);
				if (model == null) {
					model = infomodel;
				}
				generator.addTask(new GeneratorTaskFromFileTemplate(engine.ruleToTemplate(model, mappingContext, rule)));
			}];
		}
		
		generator.generate(infomodel, mappingContext,output);
		return output
	}
	
	override getServiceKey() {
		return "FreeMarkerGenerator";
	}
	
	// TODO : Need to handle all possibilities
	def getSource(Source source) {
		if (source instanceof InfoModelPropertySource) {
			var im = source as InfoModelPropertySource;
			if (im.property == null) {
				return im.model
			} else {
				return im.property.type
			}
		} else if (source instanceof InfoModelAttributeSource) {
			var im = source as InfoModelAttributeSource;
			
		} else if(source instanceof FunctionBlockSource) {
			var im = source as FunctionBlockSource;
		}
		
		return null;
	}
}
