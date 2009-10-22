class WaveDefaultUrlMappings {
	
    static mappings = {
	  "/_wave/capabilities"(controller:"waveRobotBase", action:"capabilitiesXml")
	  "/_wave/capabilities.xml"(controller:"waveRobotBase", action:"capabilitiesXml")
	  "/_wave/robot/profile"(controller:"waveRobotBase", action:"robotProfile")
	  "/_wave/robot/jsonrpc"(controller:"waveRobotBase", action:"processEvents")
	}
}