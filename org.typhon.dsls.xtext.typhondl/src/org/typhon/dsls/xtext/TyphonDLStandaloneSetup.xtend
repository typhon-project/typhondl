/*
 * generated by Xtext 2.15.0
 */
package org.typhon.dsls.xtext


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class TyphonDLStandaloneSetup extends TyphonDLStandaloneSetupGenerated {

	def static void doSetup() {
		new TyphonDLStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}