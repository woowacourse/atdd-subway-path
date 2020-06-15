import { EVENT_TYPE } from '../../utils/constants.js'
import api from "../../api/index.js"
import {searchResultTemplate} from "../../../admin/utils/templates.js";

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')

  const showSearchResult = (searchInput) => {
    api.path.find(searchInput).then(pathResponse =>
        $searchResultContainer.innerHTML = searchResultTemplate(pathResponse)
    ).catch(error => alert("검색 중 오류가 발생했습니다."))

  };

  const onSearch = event => {
    event.preventDefault();
    const isSearchMinimumTimeButton = event.target.classList.contains("search-minimum-time");
    if (isSearchMinimumTimeButton) {
      return;
    }

    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      pathType: "DISTANCE"
    };
    showSearchResult(searchInput)
  };

  const onSearchMininumTimeButtonClicked = event => {
    const isSearchMinimumTimeButton = event.target.classList.contains("search-minimum-time");
    if (!isSearchMinimumTimeButton) {
      return;
    }

    const searchInput = {
      source: $departureStationName.value,
      target: $arrivalStationName.value,
      pathType: "DURATION"
    };
    showSearchResult(searchInput)
  };

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
    // $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite)
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearch);
    $searchResultContainer.addEventListener(EVENT_TYPE.CLICK, onSearchMininumTimeButtonClicked);
  }

  this.init = () => {
    initEventListener()
  }
}

const login = new Search()
login.init()
