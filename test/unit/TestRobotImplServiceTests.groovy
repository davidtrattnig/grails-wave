import grails.test.*

class TestRobotImplServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRobotImpl() {
		mockLogging(TestRobotImplService, false)
		def rob = new TestRobotImplService()
		rob.processEvents(null) //stupid?
	
    }
}
