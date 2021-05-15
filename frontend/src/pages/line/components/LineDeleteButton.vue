<template>
  <v-btn @click="onDeleteLine(line.id)" icon>
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
    async onDeleteLine(lineId) {
      try {
        // TODO Line을 삭제하는 API를 추가해주세요.
        const response1 = await fetch(`http://localhost:8080/lines/${lineId}`, {
          method: "DELETE"
        });
        if (!response1.ok) {
          throw new Error(`${response1.status}`);
        }
        // TODO 전체 Line 데이터를 불러오는 API를 추가해주세요.
        const response2 = await fetch("http://localhost:8080/lines");
        if (!response2.ok) {
          throw new Error(`${response2.status}`);
        }
        const lines = await response2.json();
        this.setLines([...lines]);
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
