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
	
	@Deprecated
	public static final String DEFAULT_ROBOT_BEAN_NAME = "myRobotService"

	private String robotBeanName = null
	private GrailsWaveRobot robot = null
	
	/**
	 * Retrieves a bean of the assigned robot implementation.
	 * <code>Null</code> if no implementation is set (robot disabled).
	 * When dealing with a robot implementation keep in mind to check
	 * if robot functionality is activated (property robotActive=true)
	 */
	public GrailsWaveRobot getRobot() {		
		return robot
	} 
	
	/**
	 * Sets the current robot implementation. The robot is not
	 * activated by default. Set the property robotActive=true
	 * to enable robot functionality being processed.
	 *
	 * @param robot the robot object
	 */
	public void setRobot(GrailsWaveRobot robot) {
		this.robot = robot
		log.info "activated robot ${robot}"
	}

	
	/**
	 * Returns true in case an robot implementation is assigned;
	 * the robot is active.
	 */
	public boolean hasActiveRobot() {
		return getRobot()
	}

	/**
	 * Returns the name of the spring bean name holding the 
	 * robot implementation. 
	 */
	@Deprecated
	public String getRobotBeanName() {						
		return robotBeanName
	}
	
	/**
	 * Sets the robot implementation bean name.
	 * An alternative way is to set the name in your Grails config file on
	 * the property <code>grails.plugin.wave.robotBeanName</code>
	 * 
	 * @param name the name of the bean to be used as robot implementation
	 * @deprecated use <code>setRobotByBeanName</code> instead
	 */
	@Deprecated
	public String setRobotBeanName(String name) {
		robotBeanName = name
		log.debug "set robotBeanName to '${name}'"
		initRobotByBeanName()
	}
	
	/**
	 * Sets the name of the linked robot implementation bean programatically.
	 * An alternative way is to set the name in your Grails config file on
	 * the property <code>grails.plugin.wave.robotBeanName</code>
	 * 
	 * @param name the name of the bean to be used as robot implementation
	 */
	public String setRobotByBeanName(String name) {
		robotBeanName = name
		log.debug "set robotBeanName to '${name}'"
		initRobotByBeanName(name)
	}
	
	//TODO add custom exception
	private void initRobotByBeanName(String name) throws NoSuchBeanDefinitionException {
		
		if (!name) throw new NoSuchBeanDefinitionException("Invalid robot bean name <null>")
		
		log.info "no robot assigned - querying default robot bean..."
		def appContext = AH.application.getMainContext() 
		
		try {
			robot = appContext.getBean(name)
			log.info "loaded robot service bean ${name}"
			print "loaded robot $robot"
		} catch(NoSuchBeanDefinitionException e) {
			log.error "artefact ${name} could not be loaded!", e
			throw e
		}		
	}
}
