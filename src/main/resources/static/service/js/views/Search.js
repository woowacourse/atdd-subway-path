import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'
import {PathStationTemplate} from "../../utils/templates.js";

function Search() {
  const $departureStationName = document.querySelector('#departure-station-name')
    const $arrivalStationName = document.querySelector('#arrival-station-name')
    const $searchButton = document.querySelector('#search-button')
    const $searchResultContainer = document.querySelector('#search-result-container')
    const $shortestDistanceButton = document.querySelector('#shortest-distance-button')
    const $shortestDurationButton = document.querySelector('#shortest-duration-button')
    const $shortestDistanceHighlight = document.querySelector('#shortest-distance-highlight')
    const $shortestDurationHighlight = document.querySelector('#shortest-duration-highlight')
    const $sourceStation = document.querySelector('#source-station');
    const $middleStations = document.querySelector('#middle-stations');
    const $targetStations = document.querySelector('#target-station');
    const $favoriteButton = document.querySelector('#favorite-button');
    const allStations = new Map();
    let $typeName = 'distance';

    const showSearchResult = () => {
        const isHidden = $searchResultContainer.classList.contains('hidden')
        if (isHidden) {
            $searchResultContainer.classList.remove('hidden')
        }
    }

    const highlightButton = () => {
    if ($typeName === 'distance') {
      $shortestDistanceHighlight.classList.add('border-l', 'border-t', 'border-r');
      $shortestDistanceHighlight.classList.remove('bg-gray-200', 'text-gray-500', 'hover:text-gray-700');
      $shortestDurationHighlight.classList.add('bg-gray-200', 'text-gray-500', 'hover:text-gray-700');
      $shortestDurationHighlight.classList.remove('border-l', 'border-t', 'border-r');
    }

    if ($typeName === 'duration') {
      $shortestDurationHighlight.classList.add('border-l', 'border-t', 'border-r');
      $shortestDurationHighlight.classList.remove('bg-gray-200', 'text-gray-500', 'hover:text-gray-700');
      $shortestDistanceHighlight.classList.add('bg-gray-200', 'text-gray-500', 'hover:text-gray-700');
      $shortestDistanceHighlight.classList.remove('border-l', 'border-t', 'border-r');
    }
  }

  const onShortestDistanceResult = event => {
    $typeName = 'distance';
      highlightButton();
      onSearch(event)
  }

  const onShortestDurationResult = event => {
    $typeName = 'duration';
      highlightButton();
      onSearch(event)
  }

    const onSearch = event => {
        event.preventDefault()

        $sourceStation.innerText = '';
        $middleStations.innerHTML = '';
        $targetStations.innerText = '';

        const departureStationId = allStations.get($departureStationName.value.trim());
        const arrivalStationId = allStations.get($arrivalStationName.value.trim());
        if (!departureStationId || !arrivalStationId) {
            alert(ERROR_MESSAGE.NOT_EXIST_STATION);
            return;
        }

        const searchInput = {
            source: departureStationId,
            target: arrivalStationId,
            type: $typeName
        }

        findPathResult(searchInput, $typeName)
    }

  const findPathResult = (searchInput, typeName) => {
      searchInput.type = typeName;

      api.path.find(searchInput).then(response => {
          if (response.status === 200) {
              response.json().then(data => onShowResult(data))
          } else {
              response.json().then(data => {
                  alert(ERROR_MESSAGE[data.frontMessageKey]);
              });
          }
      })
  }

    const onShowResult = async data => {
        document.querySelector('#distance').innerText = data['totalDistance'] + 'km';
        document.querySelector('#duration').innerText = data['totalDuration'] + '분';

        const stations = data['stationNames'];
        $sourceStation.innerText = stations[0];
        for (let i = 1; i < stations.length - 1; i++) {
            $middleStations.insertAdjacentHTML('beforeend', PathStationTemplate(stations[i]));
        }
        $targetStations.innerText = await stations[stations.length - 1];
        showSearchResult()
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
        $shortestDistanceButton.addEventListener(EVENT_TYPE.CLICK, onShortestDistanceResult)
        $shortestDurationButton.addEventListener(EVENT_TYPE.CLICK, onShortestDurationResult)
    }

    const initStations = () => {
        api.station.getAll().then(stations => stations.forEach(
            station => {
                allStations.set(station.name, station.id);
            }
        ))
    }

    this.init = () => {
        initStations();
        initEventListener();
    }
}

const login = new Search()
login.init()
