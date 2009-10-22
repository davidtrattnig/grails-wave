includeTargets << grailsScript("Init")

pluginTemplatePath = "$wavePluginDir/src/templates"

target(main: "Creates a template controller of a wave robot") {
	generateWaveRobotService 'MyRobot'
	//generateRobotUrlMappings 'WaveRobot'
}

private void generateWaveRobotService(String name) {

	String srcPath = "$pluginTemplatePath/services/_${name}Service.groovy"
	String destPath = "$basedir/grails-app/services/${name}Service.groovy"
	def outFile = new File(destPath)
	if (outFile.exists()) {
		Ant.input addProperty: 'overwrite', message: "$outFile.name exists - overwrite? y/n"
		if ('y' == Ant.antProject.properties.'overwrite') {
			overwrite = true
		}
	}
	else {
		overwrite = true
	}

	println "generating ${name}Service.groovy ......"
	Ant.copy(file: srcPath, tofile: destPath, overwrite: overwrite)
}

private void generateRobotUrlMappings(String name) {

	String srcPath = "$pluginTemplatePath/conf/_${name}UrlMappings.groovy"
	String destPath = "$basedir/grails-app/conf/${name}UrlMappings.groovy"
	def outFile = new File(destPath)
	if (outFile.exists()) {
		Ant.input addProperty: 'overwrite', message: "$outFile.name exists - overwrite? y/n"
		if ('y' == Ant.antProject.properties.'overwrite') {
			overwrite = true
		}
	}
	else {
		overwrite = true
	}

	println "generating ${name}UrlMappings.groovy ......"
	Ant.copy(file: srcPath, tofile: destPath, overwrite: overwrite)
}

setDefaultTarget(main)
