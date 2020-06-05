import {ERROR_MESSAGE, EVENT_TYPE} from '../../utils/constants.js'
import {colorSelectOptionTemplate, subwayLinesTemplate} from '../../utils/templates.js'
import {subwayLineColorOptions} from '../../utils/defaultSubwayData.js'
import Modal from '../../ui/Modal.js'
import api from '../../api/index.js'

function AdminLine() {
  const $subwayLineList = document.querySelector('#subway-line-list')
  const $subwayLineNameInput = document.querySelector('#subway-line-name')
  const $subwayLineColorInput = document.querySelector('#subway-line-color')
  const $subwayLineStartTime = document.querySelector('#subway-start-time')
  const $subwayLineEndTime = document.querySelector('#subway-end-time')
  const $subwayIntervalTime = document.querySelector('#subway-interval-time')
  const $subwayLineFormSubmitButton = document.querySelector('#submit-button')
  const $submitButton = document.querySelector('#submit-button')
  const $subwayLineCreateButton = document.querySelector('#subway-line-create-btn')
  let $activeSubwayLineItem = null

  const subwayLineModal = new Modal()

  const createSubwayLine = () => {
    const newSubwayLine = {
      name: $subwayLineNameInput.value,
      color: $subwayLineColorInput.value,
      startTime: $subwayLineStartTime.value,
      endTime: $subwayLineEndTime.value,
      intervalTime: $subwayIntervalTime.value
    }
    api.line
    .create(newSubwayLine)
    .then(response => {
      $subwayLineList.insertAdjacentHTML('beforeend', subwayLinesTemplate(response))
      subwayLineModal.toggle()
    })
    .catch(error => {
      alert('에러가 발생했습니다.')
    })
  }

  const onDeleteSubwayLineHandler = event => {
    const $target = event.target
    const $subwayLineItem = $target.closest('.subway-line-item')
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }
    const lineId = $subwayLineItem.dataset.id
    api.line
    .delete(lineId)
    .then(() => {
      $subwayLineItem.remove()
    })
    .catch(error => {
      alert(error)
    })
  }

  const onShowUpdateSubwayLineModal = event => {
    const $target = event.target
    const isUpdateButton = $target.classList.contains('mdi-pencil')
    if (!isUpdateButton) {
      return
    }
    const $subwayLineItem = $target.closest('.subway-line-item')
    $activeSubwayLineItem = $subwayLineItem
    const lineId = $subwayLineItem.dataset.id
    api.line
    .get(lineId)
    .then(line => {
      $subwayLineNameInput.value = line.name
      $subwayLineColorInput.value = line.color
      $subwayLineStartTime.value = line.startTime
      $subwayLineEndTime.value = line.endTime
      $subwayIntervalTime.value = line.intervalTime
      subwayLineModal.toggle()
      $submitButton.classList.add('update-submit-button')
    })
    .catch(() => {
      alert(ERROR_MESSAGE.COMMON)
    })
  }

  const updateSubwayLine = () => {
    const updatedSubwayLine = {
      name: $subwayLineNameInput.value,
      color: $subwayLineColorInput.value,
      startTime: $subwayLineStartTime.value,
      endTime: $subwayLineEndTime.value,
      intervalTime: $subwayIntervalTime.value
    }
    api.line
    .update($activeSubwayLineItem.dataset.id, updatedSubwayLine)
    .then(() => {
      subwayLineModal.toggle()
      $activeSubwayLineItem.querySelector('.line-name').innerText = updatedSubwayLine.name
    })
    .catch(error => alert(ERROR_MESSAGE.COMMON))
  }

  const onSubmitHandler = event => {
    event.preventDefault()
    const $target = event.target
    const isUpdateSubmit = !!$activeSubwayLineItem
    isUpdateSubmit ? updateSubwayLine($target) : createSubwayLine()
  }

  const onShowLineInformation = event => {
    const $target = event.target;

    const isUpdateButton = $target.classList.contains("mdi-pencil");
    const isDeleteButton = $target.classList.contains("mdi-delete");

    if (!isUpdateButton && !isDeleteButton) {
      const selectedLine = $target.closest(".subway-line-item");
      const selectedLineId = selectedLine.dataset.id;
      console.log(selectedLineId);

      api.line.get(selectedLineId)
      .then(response => fillLineInformation(response.startTime,
        response.endTime,
        response.intervalTime));
    }
  };

  const fillLineInformation = (start, end, interval) => {
    const $subwayLineStartTime = document.querySelector("#line-start-time");
    const $subwayLineEndTime = document.querySelector("#line-end-time");
    const $subwayLineIntervalTime = document.querySelector("#line-interval-time");

    $subwayLineStartTime.innerText = start;
    $subwayLineEndTime.innerText = end;
    $subwayLineIntervalTime.innerText = interval;
  }

  const onCreateLineFormInitializeHandler = () => {
    $activeSubwayLineItem = null
    $subwayLineNameInput.value = ''
    $subwayLineColorInput.value = ''
    $subwayLineStartTime.value = ''
    $subwayLineEndTime.value = ''
    $subwayIntervalTime.value = ''
  }

  const onSelectColorHandler = event => {
    event.preventDefault()
    const $target = event.target
    if ($target.classList.contains('color-select-option')) {
      $subwayLineColorInput.value = $target.dataset.color
    }
  }

  const initCreateSubwayLineForm = () => {
    const $colorSelectContainer = document.querySelector('#subway-line-color-select-container')
    const color = subwayLineColorOptions.map((option, index) => colorSelectOptionTemplate(
      option,
      index)).join('')
    $colorSelectContainer.insertAdjacentHTML("beforeend", color)
    $colorSelectContainer.addEventListener(EVENT_TYPE.CLICK, onSelectColorHandler)
  }

  const initEventListeners = () => {
    $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onDeleteSubwayLineHandler)
    $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onShowUpdateSubwayLineModal)
    $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onShowLineInformation)
    $subwayLineFormSubmitButton.addEventListener(EVENT_TYPE.CLICK, onSubmitHandler)
    $subwayLineCreateButton.addEventListener(EVENT_TYPE.CLICK, onCreateLineFormInitializeHandler)
  }

  this.init = () => {
    initEventListeners()
    initCreateSubwayLineForm()
  }
}

const adminLine = new AdminLine()
adminLine.init()
