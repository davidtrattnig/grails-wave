import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.plugins.PluginManagerHolder as PMH
import org.codehaus.groovy.grails.plugins.web.taglib.JavascriptTagLib             
import org.grails.plugins.wave.WaveUtils

class WaveEmbedTagLibTests extends TagLibUnitTestCase {
	
	def final VALID_WAVE_ID = "wavesandbox.com!w+GqEj91kE%F"
	def final VALID_WAVE_URL = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI"
	def final VALID_PROVIDER = "https://wave.google.com/a/wavesandbox.com/"
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testWaveHead() {

		CH.config.grails.plugins.wave.embedAPI = "http://embed.API"  
		
		assertNotNull PMH.pluginManager
		JavascriptTagLib.metaClass.static.getPluginManager = {PMH.pluginManager}
		assertNotNull JavascriptTagLib.pluginManager
		
		def we = new WaveEmbedTagLib()
		
		//create tag without provider attribute
		def res = we.waveHead()
		assertNotNull res
		
		//create tag with custom wave provider
		res = we.waveHead(provider:VALID_PROVIDER)
		assertNotNull res
		
		println res.toString()

		//there should be three "script" tags
		def response = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parseText(res.toString())
		assertNotNull response
		assertEquals 3, response.script.size()
    }

	void testWaveEmbedInvalid() {
		def we = new WaveEmbedTagLib()
				
		//create invalid wave
		def res = we.waveEmbed()
		println res
		assertEquals 0, res.size()
		//def div = new XmlSlurper().parseText(res)
		//assertNull div		
	}
	
	void testWaveEmbedGeneral() {
		def we = new WaveEmbedTagLib()
				
		//create wave with default frame id
		def res = we.waveEmbed(waveId:VALID_WAVE_ID)
		assertNotNull res
		def div = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parseText(res.toString())
		assertNotNull div
	}
	
	void testWaveEmbedByURL() {
		def tmplId = WaveUtils.extractWaveId(VALID_WAVE_URL)
		def we = new WaveEmbedTagLib()
		
		//create wave with default frame id
		def res = we.waveEmbed(waveUrl:VALID_WAVE_URL)
		assertNotNull res
		def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parseText(res.toString())
		assertNotNull html
		println "html:${html.text()}"
		
		assertNotNull html.div
		assertNotNull html.script
		
		def matcher = ( html =~ /.*loadWave\(.(.*).,.*\)/ )
		println "matcher size: ${matcher?.size()}"
		assertEquals tmplId, matcher[0][1].split("\"")[0]
	}

	void testWaveEmbedWithCustomFrame() {
		def we = new WaveEmbedTagLib()
				
		//create wave with a given valid wave id and frame id
		def res = we.waveEmbed(waveId:VALID_WAVE_ID, "attrs.id":"myWaveFrame")
		assertNotNull res		
	}
}
