import grails.test.*
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
		mockConfig '''
		            grails.plugin.wave.embedAPI = "http://embed.API"
					grails.plugin.wave.defaultProvider = "http://default.provider"
		        '''
		
		def we = new WaveEmbedTagLib()
				
		//create tag without provider attribute
		def res = we.waveHead()
		assertNotNull res
		
		//create tag with custom wave provider
		res = we.waveHead(provider:VALID_PROVIDER)
		assertNotNull res
		
		//check if the required html tags are available
		//there should be 4 <script> tags (waveHead has called twice)
		def response = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parseText(res.toString())
		assertNotNull response
		response.script.each {
			System.out.println "scriptLine:"+it
		}
		assertEquals 4, response.script.size()
		//TODO improve test .. regex matcher
    }

	void testWaveEmbedInvalid() {
		def we = new WaveEmbedTagLib()
				
		//create invalid wave
		def res = we.waveEmbed()
		assertNull res
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
