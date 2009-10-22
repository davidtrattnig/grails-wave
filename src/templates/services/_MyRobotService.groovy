import org.bitfusion.grails.wave.*
import com.google.wave.api.*

/**
 * {@link MyRobotService}
 *
 * Robot Template Implementation
 */
class MyRobotService implements GrailsWaveRobot {

    boolean transactional = true

	/**
	 * Set these properties for your custom robot. 
	 * keep in mind to remove capabilities from the 
	 * list below which your robot has not implemented.
	 */
    static robotName = "Robot Name"
	static robotVersion = "0.1"
	static imageUrl = "http://"
	static profileUrl = "http://"
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
	
    public void processEvents(RobotMessageBundle eventsBundle) {

		// place your robot implementation here ...
    }
}
