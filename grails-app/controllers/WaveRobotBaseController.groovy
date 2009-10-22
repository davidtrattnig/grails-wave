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
import com.google.wave.api.*

import com.google.wave.api.impl.ElementSerializer;
import com.google.wave.api.impl.EventDataSerializer;
import com.google.wave.api.impl.EventMessageBundle;
import com.google.wave.api.impl.EventMessageBundleSerializer;
import com.google.wave.api.impl.OperationMessageBundle;
import com.google.wave.api.impl.OperationSerializer;
import com.google.wave.api.impl.RobotMessageBundleImpl;

import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.SerializerState;
import com.metaparadigm.jsonrpc.UnmarshallException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import grails.converters.*;
import org.bitfusion.grails.wave.*;
import com.google.wave.api.*
import com.google.wave.api.impl.OperationMessageBundle

/**
 * WaveRobotBaseController
 * <p>
 * Default robot controller implementation. Some of the code is inherited
 * of the <code>com.google.wave.api.AbstractRobotServlet</code> class and
 * and refactored as Grails/Groovy controller code.
 * <p>
 * @author david.trattnig
 */
class WaveRobotBaseController {
	
	WaveService waveService
	private JSONSerializer serializer	
	
	/**
	 * Renders the mandatory Wave <code>capabilities.xml</code> document
	 * holding the meta data of the implemented robot
	 */
	def capabilitiesXml = {
		if (waveService.hasActiveRobot()) {
			render(contentType:"text/xml") {
				mkp.yieldUnescaped('<?xml version="1.0"?>\n')
		      		"w:robot"("xmlns:w":"http://wave.google.com/extensions/robots/1.0") {
		       		"w:capabilities" { 
						waveService.robot.robotCapabilities.each{capability->
							"w:capability"("name":"$capability") 
						}
					}
					"w:profile"("name":"${waveService.robot.robotName}", 
								"imageurl":"${waveService.robot.imageUrl}", 
								"profileurl":"${waveService.robot.profileUrl}")
					"w:version" {mkp.yieldUnescaped(waveService.robot.robotVersion)}
		  		}
			}
		} else {
			render "no active robot!"
		}
	}
	
	/**
	 * Renders the active robot's profile 
	 */
	def robotProfile = {
		def rob = waveService.robot
		def profile = ["profileUrl":rob.profileUrl,
						"imageUrl":rob.imageUrl,
						"name":rob.robotName]						

		render profile as JSON
	}
	
	/**
	 * Triggers the procession of the robot implementation
	 * by the wave service.
	 */
	def processEvents = {
		if (waveService.hasActiveRobot()) {
			log.info "--beforeProcessEvents - Robot ${waveService.robot.robotName}"
		    RobotMessageBundle eventsBundle = deserializeEvents(request)

		    // Log All Events
		    for (Event event : eventsBundle.events) {
		      log.info "${event.type} [ ${event.wavelet.waveId} ${event.wavelet.waveletId}"
			try {
				def content = event.blip.document.text.replace("\n", "\\n")
		        log.info "[${event.blip.blipId}] [${content}]"
		      } catch(NullPointerException npx) {
		        log.info "] [null]"
		      }
		    }	
			
			log.info "--beforeProcessEvents - Robot ${waveService.robot.robotName}"
			waveService.robot.processEvents(eventsBundle)
			log.info "--afterProcessEvents - Robot ${waveService.robot.robotName}"

			eventsBundle.operations.version = waveService.robot.robotVersion
			render serializeOperations(eventsBundle.operations, response)
		} else {
			log.warn "tried to process events while robot is inactive"
		}
		
	}

	
	// WAVE API IMPLS
	
	private RobotMessageBundle deserializeEvents(HttpServletRequest req) throws IOException {

		String json = getRequestBody(req)
	    log.info "Incoming events: $json"

	    JSONSerializer serializer = getJSONSerializer();
	    RobotMessageBundleImpl events = null;

	    try {
	      JSONObject jsonObject = new JSONObject(json);
	      events = new RobotMessageBundleImpl((EventMessageBundle) serializer.unmarshall(
	          new SerializerState(), EventMessageBundle.class, jsonObject), getRobotAddress(request));
	    } catch (JSONException jsonx) {
	      jsonx.printStackTrace();
	    } catch (UnmarshallException e) {
	      e.printStackTrace();
	    }

	    return events;
	}
	
	private HttpServletResponse serializeOperations(OperationMessageBundle operations, HttpServletResponse resp) {
	    try {
	      String json = serializer.toJSON(operations);
	      log.info("Outgoing operations: " + json);

	      resp.setContentType("application/json");
	      resp.setCharacterEncoding("utf-8");
	      resp.getWriter().write(json);
	      resp.setStatus(200);
		  log.info "operations serialized successfully!"
	    } catch (IOException iox) {
	      iox.printStackTrace();
		  log.error "error while serializing ops:"
		  log.error iox
	      resp.setStatus(500);
	    } catch (MarshallException mx) {
	      mx.printStackTrace();
		  log.error "error while serializing ops:"
		  log.error mx
	      resp.setStatus(500);
	    }
		return resp
	}

	private String getRequestBody(HttpServletRequest req) throws IOException {

		StringBuilder json = new StringBuilder();
		BufferedReader reader = req.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
	  		json.append(line);
		}
		return json.toString();
	}

	private JSONSerializer getJSONSerializer() {
	    if (serializer != null) {
	      return serializer;
	    }

	    serializer = new JSONSerializer();
	    try {
	      serializer.registerDefaultSerializers();
	      serializer.registerSerializer(new EventMessageBundleSerializer());
	      serializer.registerSerializer(new EventDataSerializer());
	      serializer.registerSerializer(new ElementSerializer());
	      serializer.registerSerializer(new OperationSerializer());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }

	    return serializer;
	}

	protected String getRobotAddress(request) {
		return request.remoteHost
	}
}