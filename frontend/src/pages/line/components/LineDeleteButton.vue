<template>
  <v-btn @click="onDeleteLine(line.id)" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINES, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";

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
        // TODO Line을 삭제하는 API를 추가해주세요. (v)
        await fetch("lines/" + lineId, {method: "DELETE"});

        // TODO 전체 Line 데이터를 불러오는 API를 추가해주세요. (v)
        const lineResponse = await fetch("lines");
        if (!lineResponse.ok) {
          throw new Error(`${lineResponse.status}`);
        }
        const lines = await lineResponse.json();
        this.setLines([...lines])
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
