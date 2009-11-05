import grails.test.*
import org.grails.plugins.wave.*

class WaveUtilsTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
		mockLogging(WaveUtils, true)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testExtractWaveIdFromURL() {

		
		def waveUrl="", waveId=""

		waveId = WaveUtils.extractWaveId(waveUrl)
		assertNull waveId

		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI.8"
		waveId = WaveUtils.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI.8", waveId

		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI"
		waveId = WaveUtils.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI", waveId

		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI.888"
		waveId = WaveUtils.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI.888", waveId
	}		
}