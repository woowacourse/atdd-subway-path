import { EVENT_TYPE } from '../../utils/constants.js'
import api from "../../api/index.js";
import { middlePathTemplate } from "../../utils/templates.js";
import { firstPathTemplate } from "../../utils/templates.js";
import { lastPathTemplate } from "../../utils/templates.js";

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
  const $arrivalStationName = document.querySelector('#arrival-station-name')
  const $searchButton = document.querySelector('#search-button')
  const $searchResultContainer = document.querySelector('#search-result-container')
  const $favoriteButton = document.querySelector('#favorite-button')
  const $durationSum = document.querySelector('#duration-sum')
  const $distanceSum = document.querySelector('#distance-sum')
  const $stationList = document.querySelector('#station-list')
  const $shortestDistance = document.querySelector('#shortest-distance')
  const $shortestTime = document.querySelector('#shortest-time')

  const showSearchResult = () => {
    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
  }

  const onSearch = event => {
    var value = $searchButton.dataset.selected;
    if(event.target.id == 'shortest-time'){
      $shortestTime.classList.remove('bg-gray-200');
      $shortestTime.classList.remove('text-gray-500');
      $shortestTime.classList.add('text-gray-700');
      $shortestTime.classList.add('bg-white');
      $shortestTime.classList.add('border-l');
      $shortestTime.classList.add('border-t');
      $shortestTime.classList.add('border-r');

      $shortestDistance.classList.add('bg-gray-200');
      $shortestDistance.classList.remove('text-gray-700');
      $shortestDistance.classList.add('text-gray-500');
      $shortestDistance.classList.remove('bg-white');
      $shortestDistance.classList.remove('border-l');
      $shortestDistance.classList.remove('border-t');
      $shortestDistance.classList.remove('border-r');

      $searchButton.dataset.selected = 'duration';
      value = 'duration';
    }

    if(event.target.id == 'shortest-distance'){
      $shortestDistance.classList.remove('bg-gray-200');
      $shortestDistance.classList.remove('text-gray-500');
      $shortestDistance.classList.add('text-gray-700');
      $shortestDistance.classList.add('bg-white');
      $shortestDistance.classList.add('border-l');
      $shortestDistance.classList.add('border-t');
      $shortestDistance.classList.add('border-r');

      $shortestTime.classList.add('bg-gray-200');
      $shortestTime.classList.remove('text-gray-700');
      $shortestTime.classList.add('text-gray-500');
      $shortestTime.classList.remove('bg-white');
      $shortestTime.classList.remove('border-l');
      $shortestTime.classList.remove('border-t');
      $shortestTime.classList.remove('border-r');

      $searchButton.dataset.selected = 'distance';
      value = 'distance';
    }

    event.preventDefault()
    const searchInput = {
      startStationName: $departureStationName.value,
      targetStationName: $arrivalStationName.value,
      type: value
    }
    api.path.find(searchInput).then(data => {
      if(data.hasOwnProperty('error')){
        console.log(data);
        alert(data.message);
        return;
      }
      $durationSum.innerText = data.durationSum + "분";
      $distanceSum.innerText = data.distanceSum + "km";

      $stationList.innerHTML = "";
      const firstStationNameTemplate = firstPathTemplate(data.pathStationNames[0]);
      const middleStationNameTemplate = data.pathStationNames.slice(1,data.pathStationNames.length -1)
        .map(stationName => middlePathTemplate(stationName))
        .join("");
      const lastStationNameTemplate = lastPathTemplate(data.pathStationNames[data.pathStationNames.length -1]);

      $stationList.insertAdjacentHTML(
        "beforeend",
        firstStationNameTemplate
      );
      $stationList.insertAdjacentHTML(
        "beforeend",
        middleStationNameTemplate
      );
      $stationList.insertAdjacentHTML(
        "beforeend",
        lastStationNameTemplate
      );
      showSearchResult(searchInput)
    })
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
    $shortestTime.addEventListener(EVENT_TYPE.CLICK, onSearch)
    $shortestDistance.addEventListener(EVENT_TYPE.CLICK, onSearch)
  }

  this.init = () => {
    initEventListener()
  }
}

const login = new Search()
login.init()
