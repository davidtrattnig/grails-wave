import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import com.google.wave.api.*
import org.grails.plugins.wave.WaveService

class WaveServiceTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
        //mockLogging(WaveService.class, true)
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	/** there is one existing robot services which should be auto-mapped */
	void testRobotRetrievalByType() {

		def wave = getWaveService()
		assertNotNull wave
		//assertEquals "TestRobotImplService", wave.robotBeanName
		assertNotNull wave.robot
	}
	
	void testRobotWithoutRobotImpl() {
		
		//there is a auto-wired TestRobotImplService..
		def wave = getWaveService()
		assertNotNull wave.robot
		
		//remove the single robot instance from the context
		wave.robot = null
		assert false == wave.hasActiveRobot()
		assertNull wave.robot
	}
	
	void testSimpleRobotRetrieval() {
		
		def wave = getWaveService()
		wave.robot = new TestRobotImplService()
		
		assert wave.robot
		assert wave.hasActiveRobot()
		assertEquals "Robbie ro Bot", wave.robot.robotName
		assertEquals "1.0", wave.robot.robotVersion
		assertEquals 1, wave.robot.robotCapabilities.size()
		assertEquals EventType.BLIP_SUBMITTED, wave.robot.robotCapabilities[0]
	
	}
	
	void testRobotRetrievalWithCustomBean() {
		
		def robotBeanName = CH.config.grails.plugins.wave.robotBeanName
		def wave = getWaveService()
		
		wave.robot = null
		assertNull wave.robotBeanName
	
		wave.robotByBeanName = "testRobotImplService"
		assert "testRobotImplService", wave.robotBeanName
		assert true == wave.hasActiveRobot()
		assertNotNull wave.robot
	}
	
	void testRobotRetrievalWithConfiguredBean() {
		
		CH.config.grails.plugins.wave.robotBeanName = "testRobotImplService"  
		def wave = getWaveService()
		assertEquals "testRobotImplService", wave.robotBeanName 
		assert wave.hasActiveRobot()
		assertNotNull wave.robot
	}
	
	
	/*utils*/
	
	private WaveService getWaveService() {
		def appContext = AH.application.getMainContext()
		//(new GrailsWavePlugin()).doWithApplicationContext(appContext)
		return appContext.getBean("waveService")
	}
}
