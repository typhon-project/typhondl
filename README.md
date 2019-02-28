[![Build Status](http://typhon.clmsuk.com:8081/buildStatus/icon?job=TyphonDL)](http://typhon.clmsuk.com:8081/job/TyphonDL/)

# TyphonDL
> Eclipse Xtext plugin

## What you need:
- [Eclipse Photon (4.8)](https://www.eclipse.org/downloads/packages/release/photon/r/eclipse-ide-java-ee-developers)
- JDK 8
- Xtext Plugin
- Sirius Plugin (check mark for Xtext support)
- (optional) "YAML Editor" Plugin

## How to run
Load all projects (except example.typhondl) into your eclipse-working directory (git-support with egit actually works quit well).
Open `org.typhon.dsls.xtext.typhondl/src/org.typhon.dsls.xtext/TyphonDL.xtext` in Eclipse. Right-click inside opened editor `Run As -> Generate Xtext Artefacts`. Next right-click on the Project `org.typhon.dsls.xtext.typhondl` in your Project Explorer `Run As -> Eclipse Application`

Add `example.typhondl` as a project to your new Runtime environment. You should be able to edit the .tdl-file and see the Sirius-modeltree (open prespective -> Sirius). You should also see the automatically generated yaml-file in the `src-gen`-folder.

At some point you will be asked if you want to convert 'example.typhondl' to an Xtext project. Answer with YES.

## TODO
- AssignmentList should look like:
	name:
		- value
		- value
- correct indentation in yaml-file