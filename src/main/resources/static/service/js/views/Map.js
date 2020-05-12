import {optionTemplate, subwayLinesItemTemplate} from '../../utils/templates.js'
import tns from '../../lib/slider/tiny-slider.js'
import api from '../../api/index.js'

function Map() {
	const $subwayLinesSlider = document.querySelector('.subway-lines-slider')
	let subwayLines = null;

	const initSubwayLinesSlider = () => {
		$subwayLinesSlider.innerHTML = subwayLines.map(
			line => subwayLinesItemTemplate(line)).join('');
		tns({
			container: '.subway-lines-slider',
			loop: true,
			slideBy: 'page',
			speed: 400,
			fixedWidth: 300,
			autoplayButtonOutput: false,
			mouseDrag: true,
			lazyload: true,
			controlsContainer: '#slider-controls',
			items: 3,
			edgePadding: 25
		})
	}

	const initSubwayLineOptions = () => {
		const subwayLineOptionTemplate = subwayLines
		.map(line => optionTemplate(line.name))
		.join('');
		const $stationSelectOptions = document.querySelector(
			'#station-select-options')
		$stationSelectOptions.insertAdjacentHTML('afterbegin',
			subwayLineOptionTemplate)
	}

	this.init = async () => {
		subwayLines = await api.line.getAll();
		initSubwayLinesSlider();
		initSubwayLineOptions();
	}
}

const edge = new Map();
edge.init();
