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
        const lineId = this.lineId
        const stationId = this.stationId
        const response = await fetch(`/api/lines/${lineId}/sections?stationId=${stationId}`, {
          method: "DELETE",
          headers: {"Content-Type": "application/json"}
        })
        if (!response.ok) {
          throw new Error(`${response.status}`);
        }
        const line = await fetch(`/api/lines/${lineId}`).then(data => data.json())
        this.setLine({...line})
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
