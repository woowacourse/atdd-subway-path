import { EVENT_TYPE } from '../../utils/constants.js';
import api from '../../api/index.js';
import { searchResultTemplate } from '../../utils/templates.js';

function Search() {
  const $departureStationName = document.querySelector(
    '#departure-station-name',
  );
  const $arrivalStationName = document.querySelector('#arrival-station-name');
  const $searchButton = document.querySelector('#search-button');
  const $searchResultContainer = document.querySelector(
    '#search-result-container',
  );
  const $favoriteButton = document.querySelector('#favorite-button');
  const $searchResult = document.querySelector('#search-result');
  const $shortestDistanceTab = document.querySelector(
    '#shortest-distance-tab',
  );
  const $minimumTimeTab = document.querySelector('#minimum-time-tab');
  const $shortestDistanceTabConfig = document.querySelector("#shortest-distance-tab-config");
  const $minimumTimeTabConfig = document.querySelector("#minimum-time-tab-config");

  let subwayStations = null;
  let searchPathInfo = null;

  const showSearchResult = (data) => {
    const isHidden = $searchResultContainer.classList.contains('hidden');
    if (isHidden) {
      $searchResultContainer.classList.remove('hidden');
    }
    $searchResult.innerHTML = searchResultTemplate(data);
  };

  const findStationIdByName = (name) => {
    return subwayStations.find((station) => station.name === name).id;
  };

  const setActiveTabConfigValue = (tab) => {
    tab.classList.add('border-l');
    tab.classList.add('border-t');
    tab.classList.add('border-r');
    tab.classList.add('text-gray-700');
    tab.classList.remove('bg-gray-200');
    tab.classList.remove('text-gray-500');
    tab.classList.remove('hover:text-gray-700');
  };

  const setUnactiveTabConfigValue = (tab) => {
    tab.classList.remove('border-l');
    tab.classList.remove('border-t');
    tab.classList.remove('border-r');
    tab.classList.remove('text-gray-700');
    tab.classList.add('bg-gray-200');
    tab.classList.add('text-gray-500');
    tab.classList.add('hover:text-gray-700');
  };

  const onSearchShortestDistance = (event) => {
    event.preventDefault();
    $shortestDistanceTab.classList.add('active-tab');
    $minimumTimeTab.classList.remove('active-tab');
    setActiveTabConfigValue($shortestDistanceTabConfig);
    setUnactiveTabConfigValue($minimumTimeTabConfig);
    showSearchResult(searchPathInfo.shortestDistancePath);
  };

  const onSearchMinimumTime = (event) => {
    event.preventDefault();
    $minimumTimeTab.classList.add('active-tab');
    $shortestDistanceTab.classList.remove('active-tab');
    setActiveTabConfigValue($minimumTimeTabConfig);
    setUnactiveTabConfigValue($shortestDistanceTabConfig);
    showSearchResult(searchPathInfo.shortestDurationPath);
  };

  const onSearchShortestPath = (event) => {
    event.preventDefault();
    const searchInput = {
      source: findStationIdByName($departureStationName.value),
      target: findStationIdByName($arrivalStationName.value),
    };

    api.path
    .find(searchInput)
    .then((data) => {
      if (!data.ok) {
        throw data;
      }
      return data.json();
    })
    .then((data) => {
      searchPathInfo = data;
      showSearchResult(data.shortestDistancePath);
    })
    .catch((error) => {
      error.text().then((error) => {
        alert(error);
      });
    });
  };

  const onToggleFavorite = (event) => {
    event.preventDefault();
    const isFavorite = $favoriteButton.classList.contains('mdi-star');
    const classList = $favoriteButton.classList;

    if (isFavorite) {
      classList.add('mdi-star-outline');
      classList.add('text-gray-600');
      classList.add('bg-yellow-500');
      classList.remove('mdi-star');
      classList.remove('text-yellow-500');
    } else {
      classList.remove('mdi-star-outline');
      classList.remove('text-gray-600');
      classList.remove('bg-yellow-500');
      classList.add('mdi-star');
      classList.add('text-yellow-500');
    }
  };

  const initEventListener = () => {
    $favoriteButton.addEventListener(EVENT_TYPE.CLICK, onToggleFavorite);
    $searchButton.addEventListener(EVENT_TYPE.CLICK, onSearchShortestPath);
    $shortestDistanceTab.addEventListener(
      EVENT_TYPE.CLICK,
      onSearchShortestDistance,
    );
    $minimumTimeTab.addEventListener(EVENT_TYPE.CLICK, onSearchMinimumTime);
  };

  this.init = async () => {
    subwayStations = await api.station.getAll();
    initEventListener();
  };
}

const search = new Search();
search.init();
