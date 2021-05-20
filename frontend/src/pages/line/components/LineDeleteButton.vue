<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINES, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import {FETCH_METHODS, SNACKBAR_MESSAGES} from "../../../utils/constants";
import {fetchJson} from "@/utils/fetchJson";

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
        const deleteResponse = await fetchJson(`/api/lines/${this.line.id}`, FETCH_METHODS.DELETE);
        if (!deleteResponse.ok) {
          throw new Error(`${deleteResponse.status}`);
        }

        const linesResponse = await fetchJson("/api/lines", FETCH_METHODS.GET);
        if (!linesResponse.ok) {
          throw new Error(`${linesResponse.status}`);
        }
        this.setLines([...await linesResponse.json()]);

        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.LINE.DELETE.FAIL);
      }
    },
  },
};
</script>
