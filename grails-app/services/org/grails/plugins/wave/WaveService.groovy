/* Copyright (c) 2009 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.wave

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import java.util.regex.Matcher

/**
 * WaveService
 * 
 * Default Service to interact with wave and entry point
 * to retrieve robot implementations.
 * 
 * @author: david.trattnig
 */
class WaveService {
	
	boolean transactional = true
	
	public static final String DEFAULT_ROBOT_BEAN_NAME = "myRobotService"
	private String robotBeanName
	public GrailsWaveRobot robot = null
	
	/**
	 * Default constructor for WaveService
	 */
	public WaveService() {
		
		def name = CH.config.grails.plugins.wave.robotBeanName
		if (name) {
			robotBeanName = name
			log.debug "initialized robotBeanName from config property"
		} else {
			robotBeanName = DEFAULT_ROBOT_BEAN_NAME
		}
	}
	
	/**
	 * Retrieves a bean of the assigned robot implementation.
	 * <code>Null</code> if no implementation is set (robot disabled).
	 */
	public GrailsWaveRobot getRobot() {

		if (!robot) {
			def name = getRobotBeanName()
			if (name) {
				log.info "no robot assigned - querying default robot bean..."
				def appContext = AH.application.getMainContext() 
				
				try {
					robot = appContext.getBean(name)
					log.info "loaded robot service bean ${name}"
					print "loaded robot $robot"
				} catch(NoSuchBeanDefinitionException e) {
					log.warn "artefact ${name} could not be loaded!"
					e.printStackTrace()
				}
			} else {
				log.error "no robotBeanName defined!"
			}
		}
		
		log.debug "retrieving robot ${robot}..."
		return robot
	}
	
	/**
	 * Sets the current robot implementation
	 *
	 * @param robot the robot object
	 */
	public void setRobot(GrailsWaveRobot robot) {
		this.robot = robot
	}

	/**
	 * Returns true in case an robot implementation is assigned;
	 * the robot is active.
	 */
	public boolean hasActiveRobot() {
		return getRobot()
	}

	/**
	 * Returns the name of the spring bean holding the 
	 * robot implementation. By default the name is looked up in the
	 * configuration property <code>grails.plugin.wave.robotImplBeanName</code>
	 * and otherwise can be set programmatically via the respective setter
	 */
	public String getRobotBeanName() {						
		return robotBeanName
	}
	
	/**
	 * Sets the name of the linked robot implementation bean programatically.
	 * An alternative way is to set the name in your Grails config file on
	 * the property <code>grails.plugin.wave.robotBeanName</code>
	 * 
	 * @param name the name of the bean to be used as robot implementation
	 */
	public String setRobotBeanName(String name) {
		robotBeanName = name
		log.debug "set robotBeanName to '${name}'"
	}
	
	/**
	 * Extracts and decodes a waveId from the given Wave URL 
	 * 
	 * @param waveUrl
	 */
	public String extractWaveId(String waveUrl) {
		
		def waveId=null
		
		//The wave url is double-encoded
		2.times {
			waveUrl = URLDecoder.decode(waveUrl)
		}		
		//It should match following part "wave:googlewave.com!w+h4UDikrUI.8"
		Matcher matcher = (waveUrl =~ /wave:(.*)\.(.*)\!(.*)\.?(\d+)?/ )
		log.debug "found ${matcher.count} matches for url '${waveUrl}'"
		
		if (matcher.count==1) {
			waveId = matcher[0][0].split(":")[1]
			log.debug "extracted wave id '${waveId}'"
		} else {
			log.warn "tried to extract waveId from a non-wave url!"
		}
		
		return waveId
	}
}
