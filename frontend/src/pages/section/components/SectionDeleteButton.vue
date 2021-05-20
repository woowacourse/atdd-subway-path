<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINE, SHOW_SNACKBAR } from "@/store/shared/mutationTypes";
import {FETCH_METHODS, SNACKBAR_MESSAGES} from "@/utils/constants";
import {fetchJson} from "@/utils/fetchJson";

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
        const response = await fetchJson(`/api/lines/${this.lineId}/sections?stationId=${this.stationId}`, FETCH_METHODS.DELETE);
        if(!response.ok) {
          throw new Error(`${response.status}`);
        }

        const linesResponse = await fetchJson(`/api/lines/${this.lineId}`, FETCH_METHODS.GET);
        if (!linesResponse.ok) {
          throw new Error(`${linesResponse.status}`);
        }
        this.setLine({...await linesResponse.json()});

        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
