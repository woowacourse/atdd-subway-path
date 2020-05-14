import {EVENT_TYPE} from '../../utils/constants.js'
import {pathTemplate} from '../../utils/templates.js'
import api from "../../api/index.js"

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $minDistanceButton = document.querySelector("#min-distance-button")
  const $minDurationButton = document.querySelector("#min-duration-button")
  const $drawingPart = document.querySelector("#drawing-part")

  const showSearchResult = async (data) => {
      const path = await api.path.find(data)

      $drawingPart.innerHTML = pathTemplate(path)

      const isHidden = $searchResultContainer.classList.contains('hidden')
      if (isHidden) {
        $searchResultContainer.classList.remove('hidden')
      }
  }

  const onSearch = type => event => {
    event.preventDefault()
    if (event.target.id === "min-distance-button") {
      event.target.classList.remove("bg-gray-200")
      $minDurationButton.classList.add("bg-gray-200")
    } else if (event.target.id === "min-duration-button") {
      event.target.classList.remove("bg-gray-200")
      $minDistanceButton.classList.add("bg-gray-200")
    }
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      type
    }
    showSearchResult(searchInput)
  }

  const onToggleFavorite = event => {
    event.preventDefault()
    const isFavorite = $favoriteButton.classList.contains('mdi-star')
    const classList = $favoriteButton.classList

    if (isFavorite) {
      classList.add('mdi-star-outline')
      classList.add('text-gray-600')
      classList.remove('mdi-star')
      classList.remove('text-yellow-500')
    } else {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
    }
  }

  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite)
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearch("distance"))
    $minDistanceButton.addEventListener(EVENT_TYPE.CLICK, onSearch("distance"))
    $minDurationButton.addEventListener(EVENT_TYPE.CLICK, onSearch("duration"))
  }

  this.init = () => {
    initEventListener()
  }

  this.onSearch = type => onSearch(type);
}

const search = new Search()
search.init()

Window.onPathSearch = type => search.onSearch(type);
