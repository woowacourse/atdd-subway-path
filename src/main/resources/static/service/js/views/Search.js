import { EVENT_TYPE } from '../../utils/constants.js'
import {container, lastContainer} from "../../utils/templates.js";

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $durationContainer = document.querySelector('#duration-container');
  const $distanceContainer = document.querySelector('#distance-container');
  const $shortPathContainer = document.querySelector('#short-path-container');

  const showSearchResult = () => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
  }

  const onSearch = async event => {
    event.preventDefault()
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value
    }
    let pathDetail = await fetch(`/path?source=${searchInput.source}&target=${searchInput.target}`).then(data => data.json());
    $durationContainer.textContent = pathDetail.duration + "분";
    $distanceContainer.textContent = pathDetail.distance + "m";

    $shortPathContainer.innerHTML = pathDetail.stations
        .filter(station => station.name !== $arrivalStationName.value)
        .map(station => container(station))
        .join("")
        .concat(lastContainer($arrivalStationName.value));
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
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearch)
  }

  this.init = () => {
    initEventListener()
  }
}

const login = new Search()
login.init()
