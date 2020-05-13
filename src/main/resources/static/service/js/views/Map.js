import { optionTemplate, subwayLinesItemTemplate } from '../../utils/templates.js'
import { defaultSubwayLines } from '../../utils/subwayMockData.js'
import tns from '../../lib/slider/tiny-slider.js'
import api from '../../api/index.js';

function Map() {
  const $subwayLinesSlider = document.querySelector('.subway-lines-slider')

  const initSubwayLinesSlider = () => {
    api.line.getAll()
    .then(data => {
      const wholeLines = data.lineDetailResponse;
      $subwayLinesSlider.innerHTML = wholeLines.map(line => subwayLinesItemTemplate(line))
      .join('')
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
    })
    .catch(error => {
      alert(error);
    });
  }

  const initSubwayLineOptions = () => {
    const subwayLineOptionTemplate = defaultSubwayLines.map(line => optionTemplate(line.title)).join('')
    const $stationSelectOptions = document.querySelector('#station-select-options')
    $stationSelectOptions.insertAdjacentHTML('afterbegin', subwayLineOptionTemplate)
  }

  this.init = () => {
    initSubwayLinesSlider()
  //  initSubwayLineOptions()
  }
}

const edge = new Map()
edge.init()
