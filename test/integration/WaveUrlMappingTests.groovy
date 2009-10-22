import grails.test.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH


class WaveUrlMappingTests extends GrailsUrlMappingsTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRobotUrlMappings() {

		static mappings = [UrlMappings, WaveDefaultUrlMappings]
		assertForwardUrlMapping("/_wave/capabilities.xml", controller: "waveRobotBase", action: "capabilitiesXml")
		assertForwardUrlMapping("/_wave/robot/jsonrpc", controller: "waveRobotBase", action: "processEvents")
		assertForwardUrlMapping("/_wave/robot/profile", controller: "waveRobotBase", action: "robotProfile")
		
		if (CH.config.grails.mime.file.extensions == false) {
			assertReverseUrlMapping("/_wave/capabilities.xml", controller: "waveRobotBase", action: "capabilitiesXml")
		} else {
			assertReverseUrlMapping("/_wave/capabilities", controller: "waveRobotBase", action: "capabilitiesXml")
		}
		assertReverseUrlMapping("/_wave/robot/jsonrpc", controller: "waveRobotBase", action: "processEvents")
		assertReverseUrlMapping("/_wave/robot/profile", controller: "waveRobotBase", action: "robotProfile")
	}
}
