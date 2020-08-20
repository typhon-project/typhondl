/*-
 * #%L
 * de.atb.typhondl.xtext
 * %%
 * Copyright (C) 2018 - 2020 ATB
 * %%
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * #L%
 */
/*
 * generated by Xtext 2.18.0.M3
 */
package de.atb.typhondl.xtext.formatting2

import com.google.inject.Inject
import de.atb.typhondl.xtext.services.TyphonDLGrammarAccess
import de.atb.typhondl.xtext.typhonDL.Application
import de.atb.typhondl.xtext.typhonDL.Cluster
import de.atb.typhondl.xtext.typhonDL.Cluster_Network
import de.atb.typhondl.xtext.typhonDL.Container
import de.atb.typhondl.xtext.typhonDL.ContainerType
import de.atb.typhondl.xtext.typhonDL.DB
import de.atb.typhondl.xtext.typhonDL.DBType
import de.atb.typhondl.xtext.typhonDL.DeploymentModel
import de.atb.typhondl.xtext.typhonDL.IMAGE
import de.atb.typhondl.xtext.typhonDL.Import
import de.atb.typhondl.xtext.typhonDL.Key_KeyValueList
import de.atb.typhondl.xtext.typhonDL.Key_ValueArray
import de.atb.typhondl.xtext.typhonDL.Software
import de.atb.typhondl.xtext.typhonDL.Platform
import de.atb.typhondl.xtext.typhonDL.PlatformType
import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import de.atb.typhondl.xtext.typhonDL.Key_Values
import de.atb.typhondl.xtext.typhonDL.ClusterType
import de.atb.typhondl.xtext.typhonDL.Ports
import de.atb.typhondl.xtext.typhonDL.HelmList
import de.atb.typhondl.xtext.typhonDL.Resources
import de.atb.typhondl.xtext.typhonDL.Credentials
import de.atb.typhondl.xtext.typhonDL.Environment
import de.atb.typhondl.xtext.typhonDL.Replication
import de.atb.typhondl.xtext.typhonDL.Volumes
import de.atb.typhondl.xtext.typhonDL.Volume_Properties

class TyphonDLFormatter extends AbstractFormatter2 {

	@Inject extension TyphonDLGrammarAccess

	def dispatch void format(DeploymentModel deploymentModel, extension IFormattableDocument document) {
		for (metaModel : deploymentModel.guiMetaInformation) {
			metaModel.format
		}
		for (model : deploymentModel.elements) {
			model.format
		}
	}

	def dispatch void format(Import imported, extension IFormattableDocument document) {
		imported.append[newLine]
	}

	def dispatch void format(Software software, extension IFormattableDocument document) {
		interior(
			software.regionFor.keyword('{').append[newLine],
			software.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		software.image.format
		software.environment.format
		software.uri.append[newLine]
		for (property : software.parameters) {
			property.format
			property.append[newLine]
		}
	}

	def dispatch void format(DB db, extension IFormattableDocument document) {
		interior(
			db.regionFor.keyword('{').append[newLine],
			db.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		db.image.format
		db.helm.format
		db.credentials.format
		db.environment.format
		db.uri.append[newLine]
		for (property : db.parameters) {
			property.format
			property.append[newLine]
		}
	}
	
    def dispatch void format(Environment environment, extension IFormattableDocument document) {
        interior(
            environment.regionFor.keyword('{').append[newLine],
            environment.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
        )
        environment.parameters.format
    }	
	
	def dispatch void format(Credentials credentials, extension IFormattableDocument document) {
	    interior(
            credentials.regionFor.keyword('{').append[newLine],
            credentials.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
        )
        credentials.regionFor.keyword('username').prepend[newLine]
        credentials.regionFor.keyword('password').prepend[newLine]
	}
	
	def dispatch void format(HelmList helmList, extension IFormattableDocument document) {
	    interior(
	        helmList.regionFor.keyword('{').append[newLine],
            helmList.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
	    )
	    helmList.regionFor.keyword('repoName').prepend[newLine]
        helmList.regionFor.keyword('repoAddress').prepend[newLine]
        helmList.regionFor.keyword('chartName').prepend[newLine]
	    for (property : helmList.parameters) {
	        property.format
	        property.prepend[newLine]
	    }    
	}
	
	def dispatch void format(Ports ports, extension IFormattableDocument document) {
		interior(
			ports.regionFor.keyword('{').append[newLine],
			ports.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (port : ports.key_values) {
			port.format
		}
	}

	def dispatch void format(IMAGE image, extension IFormattableDocument document) {
		image.append[newLine]
	}

	def dispatch void format(PlatformType platformType, extension IFormattableDocument document) {
		platformType.append[newLine]
	}

	def dispatch void format(ContainerType containerType, extension IFormattableDocument document) {
		containerType.append[newLine]
	}
	
	def dispatch void format(ClusterType clusterType, extension IFormattableDocument document) {
		clusterType.append[newLine]
	}

	def dispatch void format(DBType dbType, extension IFormattableDocument document) {
		interior(
			dbType.regionFor.keyword('{').append[newLine],
			dbType.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		dbType.image.format
		for (image : dbType.images) {
			image.format
		}
	}

	def dispatch void format(Platform platform, extension IFormattableDocument document) {
		interior(
			platform.regionFor.keyword('{').append[newLine],
			platform.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (cluster : platform.clusters) {
			cluster.format
		}
	}

	def dispatch void format(Cluster cluster, extension IFormattableDocument document) {
		interior(
			cluster.regionFor.keyword('{').append[newLine],
			cluster.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (network : cluster.networks) {
			network.append[newLine]
			network.format
		}
		for (app : cluster.applications) {
			app.format
		}
		for (property : cluster.properties) {
		    property.format
		}
	}

	def dispatch void format(Cluster_Network network, extension IFormattableDocument document) {
		interior(
			network.regionFor.keyword('{').append[newLine],
			network.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		network.append[newLine]
		for (key_value : network.key_values) {
			key_value.format
		}
	}

	def dispatch void format(Application app, extension IFormattableDocument document) {
		interior(
			app.regionFor.keyword('{').append[newLine],
			app.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (container : app.containers) {
			container.format
		}
	}

	def dispatch void format(Container container, extension IFormattableDocument document) {
		interior(
			container.regionFor.keyword('{').append[newLine],
			container.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (depends_on : container.depends_on) {
			depends_on.append[newLine]
		}
		container.deploys.append[newLine]
		container.networks.append[newLine]
		container.ports.format
		container.resources.format
		container.replication.format
		container.uri.append[newLine]
		container.volumes.format
		for (property : container.properties) {
			property.format
			property.append[newLine]
		}
	}
	
	def dispatch void format(Volumes volumes, extension IFormattableDocument document) {
	    for (decls : volumes.decls) {
	        decls.format
	    }
	}
	
	def dispatch void format(Volume_Properties decls, extension IFormattableDocument document) {
	    interior(
            decls.regionFor.keyword('{').append[newLine],
            decls.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
        )
        decls.regionFor.keyword('volumeName').prepend[newLine]
        decls.regionFor.keyword('volumeType').prepend[newLine]
        decls.regionFor.keyword('mountPath').prepend[newLine]
        for (property : decls.specifics) {
            property.prepend[newLine]
            property.format
            property.append[newLine]
        }
	}
	
	def dispatch void format(Replication replication, extension IFormattableDocument document) {
	    interior(
            replication.regionFor.keyword('{').append[newLine],
            replication.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
        )
        replication.regionFor.keyword('replicas').prepend[newLine]
        replication.regionFor.keyword('mode').prepend[newLine]
	}
	
	def dispatch void format(Resources resources, extension IFormattableDocument document) {
	    interior(
            resources.regionFor.keyword('{').append[newLine],
            resources.regionFor.keyword('}').prepend[newLine].append[newLine],
            [indent]
        )
        resources.regionFor.keyword('limitCPU').prepend[newLine]
        resources.regionFor.keyword('limitMemory').prepend[newLine]
        resources.regionFor.keyword('reservationCPU').prepend[newLine]
        resources.regionFor.keyword('reservationMemory').prepend[newLine]
	}

	def dispatch void format(Key_Values key_values, extension IFormattableDocument document) {
		key_values.append[newLine]
	}

	def dispatch void format(Key_ValueArray key_valueArray, extension IFormattableDocument document) {
		key_valueArray.append[newLine]
	}

	def dispatch void format(Key_KeyValueList key_keyValueList, extension IFormattableDocument document) {
		interior(
			key_keyValueList.regionFor.keyword('{').append[newLine],
			key_keyValueList.regionFor.keyword('}').prepend[newLine].append[newLine],
			[indent]
		)
		for (propertiy : key_keyValueList.properties) {
			propertiy.format
		}
	}

}
