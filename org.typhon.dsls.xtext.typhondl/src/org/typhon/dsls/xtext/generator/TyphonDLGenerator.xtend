/*
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.typhon.dsls.xtext.typhonDL.Application
import org.typhon.dsls.xtext.typhonDL.Assignment
//import org.typhon.dsls.xtext.typhonDL.AssignmentList
import org.typhon.dsls.xtext.typhonDL.CommaSeparatedAssignmentList
import org.typhon.dsls.xtext.typhonDL.Container
import org.typhon.dsls.xtext.typhonDL.EnvList

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class TyphonDLGenerator extends AbstractGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		
		for (app : resource.allContents.toIterable.filter(Application)) {
			fsa.generateFile("docker-compose.yaml", app.compile);
		}
		
//		fsa.generateFile('greetings.txt', 'People to greet: ' + 
//			resource.allContents
//				.filter(Greeting)
//				.map[name]
//				.join(', '))
	}
	
	def compile(Application app)'''
		version: '3.5'
		
		services: «FOR container:app.containers»
				    	«container.compile»
				  «ENDFOR»
	'''
	
	def compile(Container container)'''
	  	«container.name»:
	«FOR property:container.properties»
	  «property.compileProp»
	«ENDFOR»
	'''
	
	def dispatch compileProp(Assignment assignment)'''
	  «assignment.name»: «assignment.value»
	'''
	
//	def dispatch compileProp(AssignmentList assList)'''
//	«assList.name»:
//	«FOR assignment:assList.assignments»
//		- «assignment.value»
//	«ENDFOR»
//	'''	
	
	def dispatch compileProp(CommaSeparatedAssignmentList commaAssList)'''
	«commaAssList.name»: [
		«commaAssList.value»«FOR value:commaAssList.values»,
	«value»«ENDFOR»
	]
	'''
	
	def dispatch compileProp(EnvList envList)'''
	«envList.name»:
		«FOR string:envList.environmentVars»
«««			cut off quotation marks:
			- «string.substring(1,string.length-1)» 
		«ENDFOR»
	'''
}