/*
 * generated by Xtext 2.15.0
 */
package de.atb.typhondl.xtext.ui.wizard

import java.util.ArrayList
import org.eclipse.xtext.ui.XtextProjectHelper
import org.eclipse.xtext.ui.util.PluginProjectFactory
import org.eclipse.xtext.ui.wizard.template.IProjectGenerator
import org.eclipse.xtext.ui.wizard.template.IProjectTemplateProvider
import org.eclipse.xtext.ui.wizard.template.ProjectTemplate

/**
 * Create a list with all project templates to be shown in the template new project wizard.
 * 
 * Each template is able to generate one or more projects. Each project can be configured such that any number of files are included.
 */
class TyphonDLProjectTemplateProvider implements IProjectTemplateProvider {
	override getProjectTemplates() {
		#[new DockerCompose, new Kubernetes]
	}
}

@ProjectTemplate(label="Empty Project", icon="project_template.png", description="<p><b>Empty Model</b></p>")
final class EmptyProject {
	
	override generateProjects(IProjectGenerator generator) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub") //TODO empty project template
	}
	
}

@ProjectTemplate(label="Docker-Compose", icon="docker.png", description="<p><b>Docker-Compose</b></p>
<p>Descriptive text about using Docker-Compose</p>")
final class DockerCompose {
	
	var usedTypes = new ArrayList<DBType>
	
	override generateProjects(IProjectGenerator generator) {
		generator.generate(new PluginProjectFactory => [
			projectName = projectInfo.projectName
			location = projectInfo.locationPath
			val info = projectInfo as MyProjectInfo
			var data = info.data
			for (key : data.keySet) {
				if (!usedTypes.contains(data.get(key).type)) {
					usedTypes += data.get(key).type
				}
			}
			var appName = "myApplication" //TODO enter this at some place. Maybe first page of wizard? Get from ML-model?
			projectNatures += #["org.eclipse.sirius.nature.modelingproject",
								XtextProjectHelper.NATURE_ID]
			builderIds += #[XtextProjectHelper.BUILDER_ID]
		 	// TODO: «data.forEach[key, value| doStuff]»
			addFile('''deploymentModel.tdl''', '''
				platformtype default //TODO (e.g. AWS)
				containertype Docker
				«FOR db : usedTypes»
					dbtype «db»
				«ENDFOR»	
				
				platform platformname : default { //TODO
					cluster clustername { //TODO
						application «appName» { //TODO
							«FOR key : data.keySet»
								container «data.get(key).name» : Docker {
									dbType : «data.get(key).type»
									image = «data.get(key).dbms.toLowerCase»:latest; // TODO
									// TODO: evironment, volumes, networks, ports etc.
								}
							«ENDFOR»	
						}
					}
				}					
			''')
			addFile('''representation.aird''','''
			<?xml version="1.0" encoding="UTF-8"?>
			<viewpoint:DAnalysis xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:viewpoint="http://www.eclipse.org/sirius/1.1.0" xmi:id="_QL4C8OyqEeiO5ZfTEpz-IQ" version="13.0.0.201804031646">
			  <semanticResources>deploymentModel.tdl</semanticResources>
			</viewpoint:DAnalysis>
			''')
		])
	}
	
	def showData(String string, Database database) {
		'''«string» («database.type») : «database.dbms»
		'''
	}
	
}

@ProjectTemplate(label="Kubernetes", icon="kubernetes.png", description="<p><b>Kubernetes</b></p>
<p>Descriptive text about using Kubernetes</p>")
final class Kubernetes {
	val requiredGroup = group("Required Properties")
	var appName = text("Application Name:", "MyApplication", requiredGroup)

	override generateProjects(IProjectGenerator generator) {
		generator.generate(new PluginProjectFactory => [
			projectName = projectInfo.projectName
			location = projectInfo.locationPath
			val info = projectInfo as MyProjectInfo
			var data = info.data;
			projectNatures += #["org.eclipse.sirius.nature.modelingproject", XtextProjectHelper.NATURE_ID]
			builderIds += #[XtextProjectHelper.BUILDER_ID]
			addFile('''deploymentModel.tdl''', '''
				platformtype default //TODO (e.g. AWS)
				containertype Kubernetes
				«FOR key : data.keySet»
					dbtype «data.get(key).type»
				«ENDFOR»	
				
				platform platformname : default { //TODO
					cluster clustername { //TODO
						application «appName» {
							«FOR key : data.keySet»
								container «data.get(key).name» : Kubernetes {
									dbType : «data.get(key).type»
									image = «data.get(key).dbms.toLowerCase»:latest; // TODO
									// TODO: evironment, volumes, networks, ports etc.
								}
							«ENDFOR»	
						}
					}
				}					
			''')
			addFile('''representation.aird''','''
			<?xml version="1.0" encoding="UTF-8"?>
			<viewpoint:DAnalysis xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:viewpoint="http://www.eclipse.org/sirius/1.1.0" xmi:id="_QL4C8OyqEeiO5ZfTEpz-IQ" version="13.0.0.201804031646">
			  <semanticResources>deploymentModel.tdl</semanticResources>
			</viewpoint:DAnalysis>
			''')
		])
	}
}
