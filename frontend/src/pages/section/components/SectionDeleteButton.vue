<template>
  <v-btn @click="onDeleteLine()" icon>
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
      type: Object,
      required: true,
    },
    stationId: {
      type: Object,
      required: true,
    },
  },
  methods: {
    ...mapMutations([SHOW_SNACKBAR, SET_LINE]),
    async onDeleteLine() {
      try {
        const lineId = this.lineId;
        const response = await fetch(`/api/lines/${lineId}/sections?`+ new URLSearchParams({
          stationId: this.stationId}),{
          method: "DELETE",
          headers: {"Content-Type" : "application/json"},
        });
        if (!response.ok) {
          throw new Error(`${response.status}`);
        }

        const line = await fetch(`/api/lines/${lineId}`)
            .then(response => response.json());
        this.setLine({ ...line })
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
