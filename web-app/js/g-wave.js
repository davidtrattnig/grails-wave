
/**
 * loads a wave into the given frame
 * 
 * @param waveId
 * @param frameId
 * @param bgcolor
 * @param color
 * @param font
 * @param fontsize
 * @return wavePanel
 */
function loadWave(waveId, frameId, bgcolor, color, font, fontsize) {
	var wavePanel = new WavePanel(_waveProvider);
	wavePanel.loadWave(waveId);
	wavePanel.setUIConfig(bgcolor, color, font, fontsize);
	wavePanel.init(document.getElementById(frameId));
	return wavePanel
}