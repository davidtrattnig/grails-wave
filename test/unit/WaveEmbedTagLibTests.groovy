import grails.test.*

class WaveEmbedTagLibTests extends TagLibUnitTestCase {
	
	def final VALID_WAVE_ID = "wavesandbox.com!w+GqEj91kE%F"
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

	void testWaveEmbedWithCustomFrame() {
		def we = new WaveEmbedTagLib()
				
		//create wave with a given valid wave id and frame id
		def res = we.waveEmbed(waveId:VALID_WAVE_ID, "attrs.id":"myWaveFrame")
		assertNotNull res		
	}
}
