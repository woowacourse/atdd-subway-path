<template>
  <v-btn icon @click="onDeleteLine(line.id)">
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
        const response = await fetch(`/api/lines/${lineId}`, {
          method: "DELETE",
          headers: {"Content-Type": "application/json"}
        })
        if (!response.ok) {
          throw new Error(`${response.status}`);
        }
        const lines = await fetch("/api/lines")
            .then(data => data.json());
        this.setLines([...lines])
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
