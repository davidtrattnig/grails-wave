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

import java.util.regex.Matcher

/**
 * {@link WaveUtils}
 * <p>
 * Utilities for working with Wave
 * <p>
 * @author david.trattnig
 * @since 0.4
 */
class WaveUtils {
	
	
	/**
	 * Extracts and decodes a waveId from the given Wave URL 
	 * 
	 * @param waveUrl
	 */
	public static String extractWaveId(String waveUrl) {
		
		def waveId=null
		
		//The wave url is double-encoded
		2.times {
			waveUrl = URLDecoder.decode(waveUrl)
		}		
		//It should match following part "wave:googlewave.com!w+h4UDikrUI.8"
		Matcher matcher = (waveUrl =~ /wave:(.*)\.(.*)\!(.*)\.?(\d+)?/ )
		//log.debug "found ${matcher.count} matches for url '${waveUrl}'"
		
		if (matcher.count==1) {
			waveId = matcher[0][0].split(":")[1]
		} else {
			//log.warn "tried to extract waveId from a non-wave url!"
		}
		
		return waveId
	}
}