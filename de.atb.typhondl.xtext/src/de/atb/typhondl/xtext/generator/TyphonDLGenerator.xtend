/*
 * generated by Xtext 2.15.0
 */
package de.atb.typhondl.xtext.generator

import de.atb.typhondl.xtext.typhonDL.Application
import de.atb.typhondl.xtext.typhonDL.Container
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import de.atb.typhondl.xtext.typhonDL.Key_Value
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray
import de.atb.typhondl.xtext.typhonDL.Key_ValueList
import java.util.ArrayList
import de.atb.typhondl.xtext.typhonDL.ContainerType
import de.atb.typhondl.xtext.typhonDL.SupportedDBMS
import de.atb.typhondl.xtext.typhonDL.MariaDB
import de.atb.typhondl.xtext.typhonDL.Mongo

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class TyphonDLGenerator extends AbstractGenerator {

	val yamlList = new ArrayList<String>
	ArrayList<ContainerObject> containerList
	Resource resource;
	
	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		// TODO different compile for each technology
		containerList = new ArrayList<ContainerObject>
		this.resource = resource
		
		for (db : resource.allContents.toIterable.filter(SupportedDBMS)) {
			fsa.generateFile("databases/" + db.name, db.saveDBs)
		}
		for (app : resource.allContents.toIterable.filter(Application)) {
			val typeList = new ArrayList<ContainerType>()
			for (container : app.containers){
				if (!typeList.contains(container.type)) {
					typeList.add(container.type)
				}
				containerList.add(createContainerObjects(container))
			}
			for (containerType : typeList){
				if (containerType.name.equalsIgnoreCase("docker")){
					//fsa.generateFile(app.name + "/docker-compose.yaml", app.compose)
					//yamlList.add(app.name + "/docker-compose.yaml")
					//fsa.generateFile("scripts/Start" + app.name + ".java", app.dockerScript)
					fsa.generateFile("pom.xml", app.dockerPom)
					fsa.generateFile("src/main/java/de/atb/typhondl/docker/DockerContainerTest.java", app.dockerjava)
				}
				if (containerType.name.equalsIgnoreCase("kubernetes")){
					//fsa.generateFile(app.name + "/docker-compose.yaml", app.compose)
					fsa.generateFile("scripts/Start" + app.name + ".java", app.kubernetesScript)
					//yamlList.add(app.name + "/docker-compose.yaml")
					fsa.generateFile("scripts/pom.xml", app.kubernetesPom)
				}
			}
			
		}
	}
	
	def dispatch saveDBs(MariaDB db)'''
	name = «db.name»
	«db.DBname.eClass.instanceClass.simpleName» = «db.DBname.value»
	«db.image.eClass.instanceClass.simpleName» = «db.image.value»
	«db.userName.eClass.instanceClass.simpleName» = «db.userName.value»
	«db.password.eClass.instanceClass.simpleName» = «db.password.value»
	«db.rootPassword.eClass.instanceClass.simpleName» = «db.rootPassword.value»
	'''
		
	
	
	def dispatch saveDBs(Mongo db)'''
	name = «db.name»
	«db.image.eClass.instanceClass.simpleName» = «db.image.value»
	«db.userName.eClass.instanceClass.simpleName» = «db.userName.value»
	«db.password.eClass.instanceClass.simpleName» = «db.password.value»
	'''
		
	
	
	def dockerjava(Application app) '''
	import java.util.List;
	import java.util.concurrent.TimeUnit;
	
	import com.github.dockerjava.api.DockerClient;
	import com.github.dockerjava.api.command.CreateContainerResponse;
	import com.github.dockerjava.api.model.Info;
	import com.github.dockerjava.api.model.SearchItem;
	import com.github.dockerjava.core.DockerClientBuilder;
	import com.github.dockerjava.core.command.PullImageResultCallback;
	
	/**
	 * Generated Java-code
	 * @author Flug
	 */
	public class DockerContainerTest {
		public static void main(String[] args) {
			DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://localhost:2375").build();
			«FOR container:app.containers»
			«container.createJava»
			«ENDFOR»
		}
	}
	'''
	
	def createJava(Container container)'''
	String image = «container.database.image.value»;
	try {
		System.out.println("Trying to pull " + image);
		dockerClient.pullImageCmd(image).exec(new PullImageResultCallback()).awaitCompletion(60, TimeUnit.SECONDS);
		System.out.println("done pulling");
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	CreateContainerResponse «container.name» = dockerClient.createContainerCmd(image).exec();
	dockerClient.startContainerCmd(«container.name».getId()).exec();
	'''

	// TODO: maybe read in compose file reference https://docs.docker.com/compose/compose-file/#labels 
	def ContainerObject createContainerObjects(Container container) {
		val containerObject = new ContainerObject => [
			name = container.name
			tech = container.type.name
		]		
		for (property : container.properties){
			switch property.name {
				case "image" : containerObject.image = property.saveProp
				case "ports" : containerObject.ports = property.saveProp
				case "volumes" : containerObject.volumes = property.saveProp
			}
		}

		return containerObject
	}
	
	def dispatch String saveProp(Key_Value key_value){
		return key_value.value
	}
	
	def dispatch String saveProp(Key_ValueArray array){
		return ""
	}
	
	def dispatch String saveProp(Key_ValueList list){
		return ""
	}
	
	override void afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context){
		for (file : yamlList){
			fsa.generateFile(file, fsa.readTextFile(file).toString.replace("\t","  ").replace("tab","  "))
		}		
	}
	
	
//	def compose(Application app)'''
//		version: '3.7'«»
//		
//		services: «FOR container:app.containers»
//				  tab«container.compile»
//				  «ENDFOR»
//	'''
	
	def dockerPom(Application app)'''
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>de.atb</groupId>
  <artifactId>typhondl.docker</artifactId>
  <version>0.0.1-SNAPSHOT</version>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
    <groupId>com.github.docker-java</groupId>
    <artifactId>docker-java</artifactId>
    <version>3.1.2</version>
</dependency>
  </dependencies>

  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <artifactId>maven-resources-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.0</version>
        </plugin>
        <plugin>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.19.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-install-plugin</artifactId>
          <version>2.5.2</version>
        </plugin>
        <plugin>
          <artifactId>maven-deploy-plugin</artifactId>
          <version>2.8.2</version>
        </plugin>
        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
        <plugin>
          <artifactId>maven-site-plugin</artifactId>
          <version>3.7.1</version>
        </plugin>
        <plugin>
          <artifactId>maven-project-info-reports-plugin</artifactId>
          <version>3.0.0</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
	'''
	
	def kubernetesPom(Application app)'''
	<dependency>
	    <groupId>io.kubernetes</groupId>
	    <artifactId>client-java</artifactId>
	    <version>3.0.0</version>
	    <scope>compile</scope>
	</dependency>
	'''
	
	def dockerScript(Application app)'''
	public class Start«app.name»{
		
		public static void main(String [] args) {
			DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
			«FOR container:containerList»
			«container.create»
			«ENDFOR»

		}
	}
	'''
	
	def create(ContainerObject container)'''	
	
	// creating container «container.name»
	CreateContainerResponse «container.name»
		= dockerClient.createContainerCmd("«container.image»")
			.withCmd("--bind_ip_all")
			.withName("«container.name»")
			.withHostName("flug")
			«IF (container.ports !== null)»
			.withPortBindings(PortBinding.parse("«container.ports»"))
			«ENDIF»
			«IF (container.volumes !== null)»
			.withBinds(Bind.parse("«container.volumes»")).exec();
			«ENDIF»
	dockerClient.startContainerCmd(container.getId()).exec();
	 
	dockerClient.stopContainerCmd(container.getId()).exec();
	 
	dockerClient.killContainerCmd(container.getId()).exec();
	'''

	def kubernetesScript(Application app)'''
	/*
	Copyright 2018 The Kubernetes Authors.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	    http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	*/
	package io.kubernetes.client.examples;
	
	import io.kubernetes.client.ApiException;
	import io.kubernetes.client.custom.IntOrString;
	import io.kubernetes.client.models.V1Pod;
	import io.kubernetes.client.models.V1PodBuilder;
	import io.kubernetes.client.models.V1Service;
	import io.kubernetes.client.models.V1ServiceBuilder;
	import io.kubernetes.client.util.Yaml;
	import java.io.IOException;
	import java.util.HashMap;
	
	/**
	 * A simple example of how to parse a Kubernetes object.
	 *
	 * <p>Easiest way to run this: mvn exec:java
	 * -Dexec.mainClass="io.kubernetes.client.examples.YamlExample"
	 *
	 * <p>From inside $REPO_DIR/examples
	 */
	public class YamlExample {
	  public static void main(String[] args) throws IOException, ApiException, ClassNotFoundException {
	    V1Pod pod =
	        new V1PodBuilder()
	            .withNewMetadata()
	            .withName("apod")
	            .endMetadata()
	            .withNewSpec()
	            .addNewContainer()
	            .withName("www")
	            .withImage("nginx")
	            .withNewResources()
	            .withLimits(new HashMap<>())
	            .endResources()
	            .endContainer()
	            .endSpec()
	            .build();
	    System.out.println(Yaml.dump(pod));
	
	    V1Service svc =
	        new V1ServiceBuilder()
	            .withNewMetadata()
	            .withName("aservice")
	            .endMetadata()
	            .withNewSpec()
	            .withSessionAffinity("ClientIP")
	            .withType("NodePort")
	            .addNewPort()
	            .withProtocol("TCP")
	            .withName("client")
	            .withPort(8008)
	            .withNodePort(8080)
	            .withTargetPort(new IntOrString(8080))
	            .endPort()
	            .endSpec()
	            .build();
	    System.out.println(Yaml.dump(svc));
	  }
	}
	'''
//	
//	def compile(Container container)'''
//	«container.name»:
//	«FOR property:container.properties»
//	tabtab«property.compileProp»
//	«ENDFOR»
//	'''
//	
//	def dispatch compileProp(Key_Value key_value)'''
//	«key_value.name»: «key_value.value»
//	'''
//	
//	def dispatch compileProp(Key_ValueArray array)'''
//	«array.name»: [
//	tabtabtab«array.value»«FOR value:array.values»,
//	tabtabtab«value»«ENDFOR»
//	tabtab]
//	'''
//	
//	def dispatch compileProp(Key_ValueList list)'''
//	«list.name»:
//	«FOR string:list.environmentVars»
//	tabtabtab- «string.substring(1,string.length-1)» 
//	«ENDFOR»
//	'''

}
