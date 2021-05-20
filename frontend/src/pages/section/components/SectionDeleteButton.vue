<template>
  <v-btn icon @click="onDeleteLine">
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINE, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "SectionDeleteButton",
  props: {
    lineId: {
      type: String,
      required: true,
    },
    stationId: {
      type: String,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINE]),
    async onDeleteLine() {
      try {
        // TODO 해당 구간을 삭제하는 api를 작성해주세요. [o]
        // await fetch("/api/section/{id}", {
        // lineId: this.lineId,
        // stationId: this.stationId,
        // })

        await fetch("http://localhost:8080/lines/" + this.lineId + "/sections?stationId=" + this.stationId, {
          method: "Delete",
          headers: {
            "Content-Type": "application/json",
          }
        })

        // TODO 현재 active된 line의 데이터를 최신으로 불러와주세요. [o]
        // const line = await fetch("/api/line/{lineId}")
        // this.setLine({ ...line })

        await fetch("http://localhost:8080/lines/" + this.lineId, {
          method: "Get",
          headers: {
            'Content-Type': 'application/json'
          }
        }).then(function (response) {
          return response.json();
        }).then((data) => {
          this.setLine({...data})
        });

        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
