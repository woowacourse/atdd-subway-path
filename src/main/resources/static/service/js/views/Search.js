import {EVENT_TYPE, SEARCH_TYPE} from '../../utils/constants.js'
import api from "../../../service/api/index.js";
import {searchResultTemplate} from "../../utils/templates.js";

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $searchResult = document.querySelector("#search-result")
  const $favoriteButton = document.querySelector('#favorite-button')
  const $distanceShowButton = document.querySelector("#shortest-distance-show-button");
  const $durationShowButton = document.querySelector("#shortest-duration-show-button");

  const showSearchResult = data => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
    $searchResult.innerHTML = searchResultTemplate(data)
  }

  const onSearch = async type => {
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      type: type
    }
    if (searchInput.source === searchInput.target) {
      alert("출발역과 도착역은 같을 수 없습니다.");
      return;
    }
    const pathDetails = await api.path.findPath(searchInput);
    showSearchResult(pathDetails)
  }

  const onClickDistanceButton = event => {
    event.preventDefault()
    $distanceShowButton.classList.add('active-tab')
    $durationShowButton.classList.remove('active-tab')
    onSearch(SEARCH_TYPE.DISTANCE).then()
  }

  const onClickDurationButton = event => {
    event.preventDefault()
    $durationShowButton.classList.add('active-tab')
    $distanceShowButton.classList.remove('active-tab')
    onSearch(SEARCH_TYPE.DURATION).then()
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
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onClickDurationButton)
    $distanceShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDistanceButton)
    $durationShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDurationButton)
  }

  this.init = () => {
    initEventListener()
  }
}

const login = new Search()
login.init()
