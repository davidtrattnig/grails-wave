import org.codehaus.groovy.grails.commons.ConfigurationHolder

class WaveGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
	    "grails-app/views/error.gsp",
		"lib/tagsoup-1.2.jar",
		"grails-app/service/TestRobotService.groovy"
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

		def config = ConfigurationHolder.getConfig()
		if(!config.grails.plugin.wave.defaultProvider) {
		 	//config.grails.plugin.wave.defaultProvider="http://wave.google.com/a/wavesandbox.com/"
			config.grails.plugin.wave.defaultProvider="https://wave.google.com/wave/"
		}
		if(!config.grails.plugin.wave.embedAPI) {
			config.grails.plugin.wave.embedAPI="https://wave-api.appspot.com/public/embed.js"
		}
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
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
