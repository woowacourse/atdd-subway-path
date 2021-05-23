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
        const deleteSectionsResponse = await fetch(`http://localhost:8080/lines/${this.lineId}/sections?stationId=${this.stationId}`, {
          method: "DELETE"
        });
        if (!deleteSectionsResponse.ok){
          throw new Error(`${deleteSectionsResponse.status}`);
        }

        const getLinesResponse = await fetch(`http://localhost:8080/lines/${this.lineId}`);
        const line = await getLinesResponse.json();
        if (!getLinesResponse.ok){
          throw new Error(`${getLinesResponse.status}`);
        }
        this.setLine({ ...line });
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
