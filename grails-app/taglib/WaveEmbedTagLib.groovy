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
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.grails.plugins.wave.WaveUtils

/**
 * {@link WaveEmbedTagLib}
 * <p>
 * @author david.trattnig
 */
class WaveEmbedTagLib {
	
	
	/** 
	 * Tag which has to be included in the head of a page 
     * necessary to correctly load and embed waves.
	 * <p>
	 * @provider wave provider url; by default value of config.grails.plugins.wave.defaultProvider
     */
	def waveHead = { attrs ->
		
		def waveEmbedAPI = CH.config.grails.plugins.wave.embedAPI
		def provider = attrs?.provider ?: CH.config.grails.plugins.wave.defaultProvider
		out << "<script src=\"${waveEmbedAPI}\" type=\"text/javascript\"></script>"
		out << """\
			<script type=\"text/javascript\">
				function loadWave(waveId, frameId, bgcolor, color, font, fontsize) {
	    			var wavePanel = new WavePanel(\"${provider}\");
					wavePanel.loadWave(waveId);
					wavePanel.setUIConfig(bgcolor, color, font, fontsize);
	    			wavePanel.init(document.getElementById(frameId));
	    		}
			</script>"""
	}

	/**
	 * Tag which actually renders the wave in form of an
	 * iframe in a newly created div#id container
	 * <p>
	 * @waveId the id of the wave (set either this or waveUrl)
	 * @waveUrl the url of the wave (set either this or waveId)
	 * @id the id of the div-container generated to hold the wave (optional).
	 * @style the style (e.g. width, height) of the rendered div container (optional)
	 * @bgcolor the background-color (optional)
	 * @color the font color (optional)
	 * @font the font-family (optional)
	 * @fontsize the font size (optional)
	 */
	def waveEmbed = { attrs ->

		def waveId = attrs?.waveId
		def waveUrl = attrs?.waveUrl

		if (!waveId && waveUrl) {
			waveId = WaveUtils.extractWaveId(waveUrl)
		}
		
		if (waveId) {
			def divId = attrs?.id ? attrs?.id : "wavebox_${waveId.hashCode()}"
			def style = attrs.style ? "style=\"${attrs.style}\"" : ""
			
			out << "<div id=\"${divId}\" class=\"wavebox\" ${style}></div>"
			out << '<script type="text/javascript">'
			out << "loadWave(\"${waveId}\", \"${divId}\", \"${attrs?.bgcolor}\", \"${attrs?.color}\", \"${attrs?.font}\", \"${attrs?.fontsize}\");"
			out << '</script>'
		} else {
			log.error "missing wave id while embedding wave"
		}
	}
}
