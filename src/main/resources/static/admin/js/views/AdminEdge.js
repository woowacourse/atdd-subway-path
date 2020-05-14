import { optionTemplate, subwayLinesItemTemplate, } from "../../utils/templates.js";
import tns from "../../lib/slider/tiny-slider.js";
import { EVENT_TYPE, KEY_TYPE } from "../../utils/constants.js";
import Modal from "../../ui/Modal.js";

function AdminEdge() {
  const $subwayLinesSlider = document.querySelector(".subway-lines-slider");
  const createSubwayEdgeModal = new Modal();
  const $createSubmitButton = document.querySelector("#submit-button");

  const initSubwayLinesSlider = () => {
    let statusCode;

    fetch('/lines/detail', {
      method: 'GET',
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json();
    }).then(jsonResponse => {
      $subwayLinesSlider.innerHTML = jsonResponse.lineDetailResponse
      .map(line => subwayLinesItemTemplate(line))
      .join("");
      tns({
        container: ".subway-lines-slider",
        loop: true,
        slideBy: "page",
        speed: 400,
        autoplayButtonOutput: false,
        mouseDrag: true,
        lazyLoad: true,
        controlsContainer: "#slider-controls",
        items: 1,
        edgePadding: 25
      });
    }).catch(response => response.json().then(error => alert(error.message)));
  };

  const initSubwayLineOptions = () => {

    fetch('/lines', {
      method: 'GET',
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json();
    })
    .then(jsonResponse => {
      const subwayLineOptionTemplate = jsonResponse
      .map(line => optionTemplate(line))
      .join("");
      const $lineSelectOptions = document.querySelector(
        "#line-select-options"
      );
      $lineSelectOptions.insertAdjacentHTML(
        "afterbegin",
        subwayLineOptionTemplate
      );
    }).catch(response => response.json().then(error => alert(error.message)));
  };

  const onCreateStationHandler = event => {
    if (event.key !== KEY_TYPE.ENTER && event.type !== EVENT_TYPE.CLICK) {
      return;
    }
    let statusCode;
    const selectLines = document.querySelector("#line-select-options");
    const selectPreStation = document.querySelector("#pre-station-select-options");
    const selectStation = document.querySelector("#station-select-options");

    const data = {
      lineId: selectLines.options[selectLines.selectedIndex].value,
      preStationId: selectPreStation.value.trim(),
      stationId: selectStation.value.trim(),
      distance: document.querySelector("#distance").value,
      duration: document.querySelector("#duration").value
    }

    validate(data);

    fetch('/lines/' + data.lineId + '/stations', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    }).then(response => {
      if (response.status >= 400) {
        throw response;
      }
      return response.json();
    }).then(jsonResponse => async function (jsonResponse) {
      if (statusCode !== 500) {
        const response = await fetch('/lines/detail', {
          method: 'GET'
        });
        const jsonResponse = await response.json();
        $subwayLinesSlider.innerHTML = jsonResponse
        .map(line => subwayLinesItemTemplate(line))
        .join("");
        tns({
          container: ".subway-lines-slider",
          loop: true,
          slideBy: "page",
          speed: 400,
          autoplayButtonOutput: false,
          mouseDrag: true,
          lazyLoad: true,
          controlsContainer: "#slider-controls",
          items: 1,
          edgePadding: 25
        });
      }
    }).catch(response => response.json().then(error => alert(error.message)));
  }

  function validate(data) {
    const lineId = data.lineId;
    const list = document.querySelectorAll(".list-item");
    const array = Array.from(list);

    if (duplicatedName(lineId, array, data)) {
      alert("추가하려는 역이 이미 존재합니다.");
      throw new Error();
    }

    if (notExistingPreStationName(lineId, array, data)) {
      alert("이전 역이 적절하지 않습니다.");
      throw new Error();
    }
  }

  function duplicatedName(lineId, array, data) {
    return array.some(element => {
      return Number(element.dataset.lineId) === Number(lineId) && Number(element.dataset.stationId) === Number(
        data.stationId);
    });
  }

  function notExistingPreStationName(lineId, array, data) {
    if (data.preStationId === "") {
      return false;
    }
    for (const element of array) {
      if (Number(element.dataset.lineId) === Number(lineId) && Number(element.dataset.stationId) === Number(
        data.preStationId)) {
        return false;
      }
    }
    return array.length !== 0;

  }

  const onRemoveStationHandler = event => {
    const $target = event.target;
    const isDeleteButton = $target.classList.contains("mdi-delete");
    if (isDeleteButton) {
      $target.closest(".list-item").remove();

      const lineId = $target.closest(".list-item").dataset.lineId;
      const stationId = $target.closest(".list-item").dataset.stationId;
      fetch("/lines/" + lineId + "/stations/" + stationId, {
        method: 'DELETE'
      }).catch(alert);
    }
  };

  const initEventListeners = () => {
    $subwayLinesSlider.addEventListener(
      EVENT_TYPE.CLICK,
      onRemoveStationHandler
    );
    $createSubmitButton.addEventListener(
      EVENT_TYPE.CLICK,
      onCreateStationHandler
    );
  };

  function initSubwayStationOptions() {
    fetch('/stations', {
      method: 'GET',
    }).then(response => response.json())
    .then(stations => {
      const stationsTemplate = stations.map(optionTemplate).join('');

      const $preStationSelectOptions = document.querySelector(
        "#pre-station-select-options"
      );
      const $stationSelectOptions = document.querySelector(
        "#station-select-options"
      );
      $stationSelectOptions.insertAdjacentHTML(
        "afterbegin",
        stationsTemplate
      );

      $preStationSelectOptions.insertAdjacentHTML(
        "afterbegin",
        '<option value=null>없음</option>'
      );

      $preStationSelectOptions.insertAdjacentHTML(
        "afterbegin",
        stationsTemplate
      );
    })
  }

  this.init = () => {
    initSubwayLinesSlider();
    initSubwayLineOptions();
    initSubwayStationOptions();
    initEventListeners();

    fetch('/lines/detail', {
      method: 'GET'
    }).then(response => {
      if (!response.ok) {
        throw response;
      }
      return response.json()
    })
    .then(lines => {
      $subwayLinesSlider.innerHTML = lines.lineDetailResponse
      .map(lineDetailResponse => subwayLinesItemTemplate(lineDetailResponse))
      .join("");
      tns({
        container: ".subway-lines-slider",
        loop: true,
        slideBy: "page",
        speed: 400,
        autoplayButtonOutput: false,
        mouseDrag: true,
        lazyLoad: true,
        controlsContainer: "#slider-controls",
        items: 1,
        edgePadding: 25
      });
    }).catch(response => response.json().then(error => alert(error.message)));
  };
}

const adminEdge = new AdminEdge();
adminEdge.init();
