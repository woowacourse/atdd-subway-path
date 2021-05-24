<template>
  <v-btn icon @click="onDeleteLine">
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "LineDeleteButton",
  props: {
    line: {
      type: Object,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINES]),
    async onDeleteLine() {
      try {
        // TODO Line을 삭제하는 API를 추가해주세요. [o]
        // await fetch("/api/lines/{id}")

        await fetch("http://localhost:8080/lines/" + this.line.id, {
          method: "Delete",
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.lineForm)
        })

        // TODO 전체 Line 데이터를 불러오는 API를 추가해주세요. [o]
        // const lines = await fetch("/api/lines")
        // this.setLines([...lines])

        await fetch("http://localhost:8080/lines", {
          method: "Get",
          headers: {
            'Content-Type': 'application/json'
          },
        }).then(function (response) {
          return response.json();
        }).then((data) => {
          this.setLines([...data])
        });

        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
