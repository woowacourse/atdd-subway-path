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

  const showSearchResult = (data) => {
    console.log(data);
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

    const isHidden = $searchResultContainer.classList.contains('hidden')
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden')
    }
  }

  const setSelected = classList => {
    classList.remove('bg-gray-200');
    classList.remove('text-gray-500');
    classList.add('text-gray-700');
    classList.add('bg-white');
    classList.add('border-l');
    classList.add('border-t');
    classList.add('border-r');
  }

  const setUnSelected = classList => {
    classList.add('bg-gray-200');
    classList.remove('text-gray-700');
    classList.add('text-gray-500');
    classList.remove('bg-white');
    classList.remove('border-l');
    classList.remove('border-t');
    classList.remove('border-r');
  }

  const onSearch = event => {
    var value = $searchButton.dataset.selected;
    if(event.target.id == 'shortest-time'){
      setSelected($shortestTime.classList)
      setUnSelected($shortestDistance.classList);
      $searchButton.dataset.selected = 'duration';
      value = 'duration';
    }
    if(event.target.id == 'shortest-distance'){
      setSelected($shortestDistance.classList);
      setUnSelected($shortestTime.classList);
      $searchButton.dataset.selected = 'distance';
      value = 'distance';
    }

    event.preventDefault()
    const searchInput = {
      startStationName: $departureStationName.value,
      targetStationName: $arrivalStationName.value,
      type: value
    }
    api.path.find(searchInput).then(async data => {
      if(!data.ok){
        alert(await data.text());
        return;
      }
      showSearchResult(data);
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
    $shortestTime.addEventListener(EVENT_TYPE.CLICK, onSearch)
    $shortestDistance.addEventListener(EVENT_TYPE.CLICK, onSearch)
  }

  this.init = () => {
    initEventListener()
  }
}

const login = new Search()
login.init()
