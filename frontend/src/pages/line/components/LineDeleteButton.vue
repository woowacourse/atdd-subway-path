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
        const deleteResponse = await fetch(`http://localhost:8080/lines/${lineId}`, {
          method: "DELETE"
        });
        if (!deleteResponse.ok) {
          throw new Error(`${deleteResponse.status}`);
        }
        // TODO 전체 Line 데이터를 불러오는 API를 추가해주세요.
        const getResponse = await fetch("http://localhost:8080/lines");
        if (!getResponse.ok) {
          throw new Error(`${getResponse.status}`);
        }
        const lines = await getResponse.json();
        this.setLines([...lines]);
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
