import { optionTemplate, subwayLinesItemTemplate } from '../../utils/templates.js'
import { defaultSubwayLines } from '../../utils/subwayMockData.js'
import tns from '../../lib/slider/tiny-slider.js'
import api from "../../../service/api/index.js";

function Map() {
  const $subwayLinesSlider = document.querySelector('.subway-lines-slider')

  const initSubwayLinesSlider = async () => {
    let lineDetails = (await api.line.getAll()).lineDetailResponses;
    $subwayLinesSlider.innerHTML = lineDetails.map(line => subwayLinesItemTemplate(line)).join('')
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

  this.init = () => {
    initSubwayLinesSlider()
  }
}

const edge = new Map()
edge.init()
