# TyphonDL
> Eclipse Xtext plugin

A brief description of the plugin

## What you need:
- [Eclipse Photon (4.8)](https://www.eclipse.org/downloads/packages/release/photon/r/eclipse-ide-java-ee-developers)
- JDK 10
- Xtext Plugin
- Sirius Plugin (check mark for Xtext support)

## How to run
Load all projects (except example.typhondl) into your eclipse-working directory (git-support with egit actually works quit well).
Open `org.typhon.dsls.xtext.typhondl/src/org.typhon.dsls.xtext/TyphonDL.xtext` in Eclipse. Right-click inside opened editor `Run As -> Generate Xtext Artefacts`. Next right-click on the Project `org.typhon.dsls.xtext.typhondl` in your Project Explorer `Run As -> Eclipse Application`

Add `example.typhondl` as a project to your new Runtime environment. You should be able to edit the .tdl-file and see the Sirius-modeltree

At some point you will be asked if you want to convert 'example.typhondl' to an Xtext project. Answer with YES.

To auto-generate yaml-file add `example.gen.typhondl` to the Project Explorer of your Runtime environment. Open the .tdl file, change something, revert it and save the file. You will find the .yaml-file in `src-gen`.

## TODO
- update .gitignore. Folders named 'src-gen' should not be uploaded if not in an example folder
- AssignmentList should look like:
	name:
		- value
		- value
- correct indentation in yaml-file
- add a Sirius design to have a graphical representation of the model