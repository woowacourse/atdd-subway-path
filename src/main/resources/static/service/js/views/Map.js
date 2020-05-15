import tns from '../../lib/slider/tiny-slider.js'

function Map() {
  const initSubwayLinesSlider = () => {
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
      edgePadding: 2
    })
  }

  this.init = () => {
    initSubwayLinesSlider()
  }
}

const edge = new Map()
edge.init()
