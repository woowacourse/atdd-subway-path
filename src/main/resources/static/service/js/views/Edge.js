import {subwayLinesItemTemplate} from "../../utils/templates.js";
import api from "../../api/index.js"
import tns from "../../lib/slider/tiny-slider.js";

function Edge() {
    const $subwayLinesSlider = document.querySelector(".subway-lines-slider");

    const initSubwayLinesSlider = async () => {
        const { lineDetailResponses : subwayLines } = await api.line.getAll();
        $subwayLinesSlider.innerHTML = subwayLines
            .map(line => subwayLinesItemTemplate(line))
            .join("");
        tns({
            container: ".subway-lines-slider",
            loop: true,
            slideBy: "page",
            speed: 400,
            autoplayButtonOutput: false,
            mouseDrag: true,
            lazyload: true,
            controlsContainer: "#slider-controls",
            items: 1,
            edgePadding: 25
        });
    };

    this.init = () => {
        initSubwayLinesSlider();
    };
}

const edge = new Edge();
edge.init();
