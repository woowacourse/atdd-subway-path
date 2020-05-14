import { subwayLinesItemTemplate } from '../../utils/templates.js'
import tns from '../../lib/slider/tiny-slider.js'
import api from '../../api/index.js'

function Edge() {
  const $subwayLinesSlider = document.querySelector('.subway-lines-slider')
  let subwayLines = []

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

  const initSubwayLinesView = () => {
    api.line
    .getAllDetail()
    .then(data => {
      subwayLines = data.lineDetailResponse
      if (subwayLines.length > 0) {
        $subwayLinesSlider.innerHTML = subwayLines.map(line => subwayLinesItemTemplate(line)).join('')
        initSubwayLinesSlider()
      }
    })
    .catch(error => {
      alert('데이터를 불러오는데 실패했습니다.')
    })
  }

  this.init = () => {
    initSubwayLinesView()
  }
}

const edge = new Edge()
edge.init()

