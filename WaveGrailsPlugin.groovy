import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.grails.plugins.wave.GrailsWaveRobot
import org.grails.plugins.wave.*

class WaveGrailsPlugin {
    // the plugin version
    def version = "0.5"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
	    "grails-app/views/error.gsp",
		"lib/tagsoup-1.2.jar",
		"lib/svnkit.jar",
		"grails-app/services/TestRobotImplService.groovy"
    ]

    def author = "David Trattnig"
    def authorEmail = "david.trattnig@gmail.com"
    def title = "Google Wave API plugin for Grails"
    def description = '''\\
A plugin that integrates the Google Wave API with Grails"
'''
    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/wave"

    def doWithSpring = {

		if(!CH.config.grails.plugins.wave.defaultProvider) {
		 	//config.grails.plugins.wave.defaultProvider="http://wave.google.com/a/wavesandbox.com/"
			CH.config.grails.plugins.wave.defaultProvider="https://wave.google.com/wave/"
		}
		if(!CH.config.grails.plugins.wave.embedAPI) {
			CH.config.grails.plugins.wave.embedAPI="https://wave-api.appspot.com/public/embed.js"
		}
    }

    def doWithApplicationContext = { appContext ->

		def waveService = appContext.getBean("waveService")			
		def robotBeanName = CH.config.grails.plugins.wave.robotBeanName
		
		if (robotBeanName) {
			waveService.robotByBeanName = robotBeanName
			log.info "initialized robotBeanName '${robotBeanName}' from config property"
		} else {		
			def services = application.serviceClasses
			def robots = services.reference.wrappedInstance.grep{ it instanceof GrailsWaveRobot }
			
			if (robots) {
				if (robots.size()==1) {	
					waveService.robot = robots.first()
					log.info "initialized robotBean '${waveService.robot}' by object type query"		
				} else {
					log.warn "multiple robots available (${robots.size()}) - set the prop 'grails.plugins.wave.robotBeanName' to your active robot bean name."
				}
			} else {
				log.info "no wave robot bean/name defined. robot message handling disabled."
			}
		}
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
