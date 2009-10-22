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
import org.bitfusion.grails.wave.*
import com.google.wave.api.*

/**
 * TestRobotImplService
 * 
 * Dummy implementation of an robot for testing purposes only.
 * This class is excluded from the Grails plugin package.
 *
 * @author: david.trattnig
 */
class TestRobotImplService implements GrailsWaveRobot {

    boolean transactional = true
	
    static robotName = "Robbie ro Bot"
	static robotVersion = "1.0"
	static robotCapabilities = [EventType.BLIP_SUBMITTED]
	static imageUrl = ""
	static profileUrl = ""
	
    public void processEvents(RobotMessageBundle eventsBundle) {
		
		log.info "ROBOT $robotName (v${robotVersion}): PROCESS EVENTS"
		log.info "eventsBundle - $eventsBundle"
    }
}
