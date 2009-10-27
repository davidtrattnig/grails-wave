import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import com.google.wave.api.*
import org.grails.plugins.wave.WaveService

class WaveServiceTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testExtractWaveIdFromUrl() {
		
		def ws = new WaveService()
		def waveUrl="", waveId=""
		
		waveId = ws.extractWaveId(waveUrl)
		assertNull waveId
		
		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI.8"
		waveId = ws.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI.8", waveId
		
		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI"
		waveId = ws.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI", waveId
		
		waveUrl = "https://wave.google.com/wave/wave:googlewave.com!w%252Bh4UDikrUI.888"
		waveId = ws.extractWaveId(waveUrl)
		assertEquals "googlewave.com!w+h4UDikrUI.888", waveId
	}
	
	void testRobotWithoutRobotImpl() {
		
		def wave = new WaveService()
		assertNull wave.robot
		
		assert false == wave.hasActiveRobot()
		assertNull wave.robot
	}
	
	void testSimpleRobotRetrieval() {
		
		def wave = new WaveService()
		wave.robot = new TestRobotImplService()
		
		assert wave.robot
		assert wave.hasActiveRobot()
		assertEquals "Robbie ro Bot", wave.robot.robotName
		assertEquals "1.0", wave.robot.robotVersion
		assertEquals 1, wave.robot.robotCapabilities.size()
		assertEquals EventType.BLIP_SUBMITTED, wave.robot.robotCapabilities[0]
	
	}
	
	void testRobotRetrievalWithCustomBean() {
		
		def wave = new WaveService()
		assertNull wave.robot
		assertEquals WaveService.DEFAULT_ROBOT_BEAN_NAME, wave.robotBeanName
		
		wave.robotBeanName = "testRobotImplService"
		assert "testRobotImplService", wave.robotBeanName
		assert true == wave.hasActiveRobot()
		assertNotNull wave.robot
	}
	
	void testRobotRetrievalWithConfiguredBean() {
		
		CH.config.grails.plugins.wave.robotBeanName = "testRobotImplService"  
		def wave = new WaveService()
		assertEquals "testRobotImplService", wave.robotBeanName 
		assert wave.hasActiveRobot()
		assertNotNull wave.robot
	}
}
