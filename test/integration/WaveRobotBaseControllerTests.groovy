import grails.test.*
import grails.converters.*
import com.google.wave.api.*
import org.grails.plugins.wave.*

class WaveRobotBaseControllerTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()	
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testCapabilitiesXMLwithoutActiveRobot() {

		//mockController(WaveRobotBaseController)
		def wc = new WaveRobotBaseController()
		wc.waveService = new WaveService()
		
		//no robot activated:
		wc.capabilitiesXml()		
		def res = wc.response.contentAsString
		assertEquals "no active robot!", res
	}
	
    void testCapabilitiesXMLResult() {
		
		//the required controller response to test against:
		def template = '''<?xmlversion="1.0"?><w:robotxmlns:w='http://wave.google.com/extensions/robots/1.0'><w:capabilities><w:capabilityname='BLIP_SUBMITTED'/></w:capabilities><w:profilename='RobbieroBot'imageurl='http://image.url'profileurl='http://test.profile'/><w:version>1.0</w:version></w:robot>'''
		
		def wc = new WaveRobotBaseController()
		wc.waveService = new WaveService()
		wc.waveService.robot = new TestRobotImplService()
		
		//set additional robot properties
		wc.waveService.robot.profileUrl = "http://test.profile"
		wc.waveService.robot.imageUrl = "http://image.url"

		//now the robot is active:
		wc.capabilitiesXml()		
		def res = wc.response.contentAsString	
		res = res.replaceAll("\n","")
		   		 .replaceAll("\t","")
				 .replaceAll(" ","")
		println "tem:"+template 
		println "res:"+res

		assertEquals template, res
    }

	void testRobotProfile() {
		
		def wc = new WaveRobotBaseController()
		wc.waveService = new WaveService()
		wc.waveService.robot = new TestRobotImplService()	
		
		//set additional robot properties
		TestRobotImplService.profileUrl = "http://test.profile"
		TestRobotImplService.imageUrl = "http://image.url"
		
		wc.robotProfile()
		def res = wc.response.contentAsString
		println "profile response: $res"
		
		def json = JSON.parse(res)
		assertEquals "http://test.profile", json.profileUrl
		assertEquals "http://image.url", json.imageUrl
		assertEquals "Robbie ro Bot", json.name
	}
}
