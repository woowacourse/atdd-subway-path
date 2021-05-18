<template>
  <v-btn icon @click="onDeleteLine">
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import {mapMutations} from "vuex";
import {SET_LINE, SHOW_SNACKBAR} from "../../../store/shared/mutationTypes";
import {SNACKBAR_MESSAGES} from "../../../utils/constants";
import {deleteFetch, getFetch} from "@/utils/fetch";

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
        await deleteFetch(`/api/section/${this.stationId}`, {
          lineId: this.lineId,
          stationId: this.stationId,
        })
        const line = await getFetch(`/api/line/${this.lineId}`)
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
