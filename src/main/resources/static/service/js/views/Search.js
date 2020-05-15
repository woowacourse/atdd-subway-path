import {EVENT_TYPE} from '../../utils/constants.js'
import {container, lastContainer, resultTemplate} from "../../utils/templates.js";
import api from "../../../service/api/index.js";

function Search() {
    const $departureStationName = document.querySelector('#departure-station-name')
    const $arrivalStationName = document.querySelector('#arrival-station-name')
    const $searchButton = document.querySelector('#search-button')
    const $searchResultContainer = document.querySelector('#search-result-container')
    const $favoriteButton = document.querySelector('#favorite-button')
    const $durationContainer = document.querySelector('#duration-container');
    const $distanceContainer = document.querySelector('#distance-container');
    let $shortPathContainer = document.querySelector('#short-path-container');
    let $distanceShowButton = document.querySelector("#shortest-distance-show-button");
    let $durationShowButton = document.querySelector("#shortest-duration-show-button");
    let $distancePathDetail;
    let $durationPathDetail;

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
        if (searchInput.source === searchInput.target) {
            alert("출발역과 도착역은 같을 수 없습니다.");
            return;
        }
        let pathDetails = await api.path.findPath(searchInput);
        $distancePathDetail = pathDetails[0];
        $durationPathDetail = pathDetails[1];
        $durationContainer.textContent = $distancePathDetail.duration + "분";
        $distanceContainer.textContent = $distancePathDetail.distance + "m";

        $shortPathContainer.innerHTML = $distancePathDetail.stations
            .filter(station => station.name !== $arrivalStationName.value)
            .map(station => container(station))
            .join("")
            .concat(lastContainer($arrivalStationName.value));
        showSearchResult(searchInput)
    }

    const onClickDistanceButton = () => {
        $searchResultContainer.innerHTML = resultTemplate($distancePathDetail);
        $durationShowButton = document.querySelector("#shortest-duration-show-button");
        $durationShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDurationButton)

        document.querySelector('#short-path-container').innerHTML = $distancePathDetail.stations
            .filter(station => station.name !== $arrivalStationName.value)
            .map(station => container(station))
            .join("")
            .concat(lastContainer($arrivalStationName.value));
    }

    const onClickDurationButton = () => {
        $searchResultContainer.innerHTML = resultTemplate($durationPathDetail);
        $distanceShowButton = document.querySelector("#shortest-distance-show-button");
        $distanceShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDistanceButton)

        document.querySelector('#short-path-container').innerHTML = $durationPathDetail.stations
            .filter(station => station.name !== $arrivalStationName.value)
            .map(station => container(station))
            .join("")
            .concat(lastContainer($arrivalStationName.value));

        $distanceShowButton.classList.add("bg-gray-200");

        document.querySelector("#shortest-duration-show-button").classList.remove("bg-gray-200");
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
        $distanceShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDistanceButton)
        $durationShowButton.addEventListener(EVENT_TYPE.CLICK, onClickDurationButton)
    }

    this.init = () => {
        initEventListener()
    }
}

const login = new Search()
login.init()
