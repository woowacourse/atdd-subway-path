import { EVENT_TYPE } from '../../utils/constants.js'
import api from '../../api/index.js';
import { ERROR_MESSAGE } from '../../../admin/utils/constants.js';
import { searchResultTemplate } from '../../utils/templates.js';

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $searchResult = document.querySelector('#search-result')

  const showSearchResult = (data) => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
    $searchResult.innerHTML = searchResultTemplate(data)
  }

  const onSearch = event => {
    event.preventDefault()
    const searchInput = {
      source: $departureStationName.value.trim(),
      target: $arrivalStationName.value.trim()
    }
    api.path.find(searchInput)
    .then(data => showSearchResult(data))
    .catch(error => alert(ERROR_MESSAGE.COMMON))
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
