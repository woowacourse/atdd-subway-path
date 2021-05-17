<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINE, SET_LINES, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";

export default {
  name: "SectionDeleteButton",
  props: {
    lineId: {
      type: Number,
      required: true,
    },
    stationId: {
      type: Number,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINE, SET_LINES]),
    async onDeleteLine() {
      try {
        await fetch(`api/lines/${this.lineId}/sections?stationId=${this.stationId}`, {
          method: "DELETE",
        })
        .then(response => {
          if(!response.ok) {
            throw new Error(`${response.status}`);
          }
        });

        const line = await fetch(`api/lines/${this.lineId}`)
        .then(response => {
          if(!response.ok) {
            throw new Error(`${response.status}`);
          }
          return response.json();
        })
        this.setLine(line);

        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
