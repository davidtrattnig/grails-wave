@artifact.package@
import org.grails.plugins.wave.*
import com.google.wave.api.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

/**
 * {@link @artifact.name@}
 *
 * A Wave robot implementation
 */
class @artifact.name@ implements GrailsWaveRobot {
	
    boolean transactional = true

	/**
	 * Set these properties for your custom robot. 
	 * Keep in mind to remove capabilities from the 
	 * list below which your robot has not implemented.
	 */
    static robotName = "Untitled Robot"
	static robotVersion = "0.1"
	static imageUrl = "http://${CH.config.google.appengine.application}.appspot.com/images/robot.jpg"
	static profileUrl = "http://${CH.config.google.appengine.application}.appspot.com"
	static robotCapabilities = [EventType.WAVELET_BLIP_CREATED,
								EventType.WAVELET_BLIP_REMOVED,
								EventType.WAVELET_PARTICIPANTS_CHANGED,
								EventType.WAVELET_SELF_ADDED,
								EventType.WAVELET_SELF_REMOVED,
								EventType.WAVELET_TIMESTAMP_CHANGED,
								EventType.WAVELET_TITLE_CHANGED,
								EventType.WAVELET_VERSION_CHANGED,
								EventType.BLIP_CONTRIBUTORS_CHANGED,
								EventType.BLIP_DELETED,
								EventType.BLIP_SUBMITTED,
								EventType.BLIP_TIMESTAMP_CHANGED,
								EventType.BLIP_VERSION_CHANGED,
								EventType.DOCUMENT_CHANGED,
								EventType.FORM_BUTTON_CLICKED]
	
    public void processEvents(RobotMessageBundle bundle) {

		// place your robot implementation here ...
    }
}
