<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINE, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";

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
    ...mapMutations([SHOW_SNACKBAR, SET_LINE]),
    async onDeleteLine() {
      try {
        await fetch(`/api/lines/${this.lineId}/sections?stationId=${this.stationId}`, {
          method: "DELETE"
        });
        // await fetch("/api/section/{id}", {
        // lineId: this.lineId,
        // stationId: this.stationId,
        // })
        const lineResponse = await fetch(`/api/lines/${this.lineId}`)
        const lines = await lineResponse.json();

        this.setLine({ ...lines })
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
