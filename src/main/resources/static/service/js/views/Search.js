import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js'
import { searchResultTemplate } from '../../utils/templates.js'

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $searchResult = document.querySelector('#search-result')
  const $shortestDistanceTab = document.querySelector('#shortest-distance-tab')
  const $minimumTimeTab = document.querySelector('#minimum-time-tab')

  const showSearchResult = data => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
    $searchResult.innerHTML = searchResultTemplate(data)
  }

  const onSearchShortestDuration = event => {
    event.preventDefault()
    $minimumTimeTab.classList.add('active-tab')
    $shortestDistanceTab.classList.remove('active-tab')
    const results = JSON.parse(localStorage.getItem("results"));
    showSearchResult(results.response.duration)
  }

  const onSearchShortestDistance = event => {
    event.preventDefault()
    $shortestDistanceTab.classList.add('active-tab')
    $minimumTimeTab.classList.remove('active-tab')
    const results = JSON.parse(localStorage.getItem("results"));
    showSearchResult(results.response.distance)
  }

  const onToggleFavorite = event => {
    event.preventDefault()
    const isFavorite = $favoriteButton.classList.contains('mdi-star')
    const classList = $favoriteButton.classList

    if (isFavorite) {
      classList.add('mdi-star-outline')
      classList.add('text-gray-600')
      classList.add('bg-yellow-500')
      classList.remove('mdi-star')
      classList.remove('text-yellow-500')
    } else {
      classList.remove('mdi-star-outline')
      classList.remove('text-gray-600')
      classList.remove('bg-yellow-500')
      classList.add('mdi-star')
      classList.add('text-yellow-500')
    }
  }

  const onSearch = async (event) => {
    event.preventDefault();
    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
    }
    const responses = await api.path.find(searchInput);
    localStorage.setItem("results", JSON.stringify(responses));
    onSearchShortestDistance(event);
  }

  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite)
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearch)
    $shortestDistanceTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDistance)
    $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchShortestDuration)
  }

  this.init = () => {
    initEventListener()
  }
}

const search = new Search()
search.init()
