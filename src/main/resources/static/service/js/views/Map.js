import {subwayLinesItemTemplate} from '../../utils/templates.js'
import tns from '../../lib/slider/tiny-slider.js'
import api from '../../api/index.js'

function Map() {
  const $subwayLinesSlider = document.querySelector('.subway-lines-slider')

  const initSubwayLinesView = () => {
    api.line.getAllDetail().then(data => {
      const subwayLines = data.lineDetailResponses
      if (subwayLines.length > 0) {
        $subwayLinesSlider.innerHTML = subwayLines.map(line => subwayLinesItemTemplate(line)).join('')
        initSubwayLinesSlider();
      }
    })
  }

  const initSubwayLinesSlider = () => {
    tns({
      container: '.subway-lines-slider',
      loop: true,
      slideBy: 'page',
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      controlsContainer: '#slider-controls',
      items: 1,
      edgePadding: 25
    })
  }

  this.init = () => {
    initSubwayLinesView()
  }
}

const edge = new Map()
edge.init()
