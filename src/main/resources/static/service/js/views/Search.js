import { EVENT_TYPE } from '../../utils/constants.js'
import { firstSearchResultTemplate, endSearchResultTemplate, searchResultTemplate} from '../../utils/templates.js';

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name');
  const $arrivalStationName = document.querySelector('#arrival-station-name');
  const $searchButton = document.querySelector('#search-button');
  const $searchResultContainer = document.querySelector('#search-result-container');
  const $favoriteButton = document.querySelector('#favorite-button');

  const showSearchResult = (responses) => {
    const isHidden = $searchResultContainer.classList.contains('hidden');
    const $pathResult = document.querySelector('#path-list');
    const $totalDistance = document.querySelector('#total-distance');
    const $totalDuration = document.querySelector('#total-duration');
    $totalDistance.innerText = responses.totalDistance + "km";
    $totalDuration.innerText = responses.totalDuration + "분";
    const length = responses.stations.length;
    $pathResult.innerHTML = firstSearchResultTemplate(responses.stations[0]);
    $pathResult.insertAdjacentHTML("beforeend", responses.stations.slice(1, length-1).map(searchResultTemplate).join(''));
    $pathResult.insertAdjacentHTML("beforeend", endSearchResultTemplate(responses.stations[length-1]));


    if (isHidden) {
      $searchResultContainer.classList.remove('hidden');
    }
  }

  const onSearch = event => {
    const $pathResult = document.querySelector('#path-list');
    event.preventDefault()
    const searchInput = {
      sourceName: $departureStationName.value,
      targetName: $arrivalStationName.value
    }
    console.log(searchInput);
    fetch('/path', {
      method:'POST',
      headers:{
        'Content-Type':'application/json'
      },
      body: JSON.stringify(searchInput)
    }).then(response=>response.json())
    .then(jsonResponse=>{
      console.log(jsonResponse);
      showSearchResult(jsonResponse);
    });

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
