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
        const section_response = await fetch(`/api/lines/${this.lineId}/sections?stationId=${this.stationId}`,
          {
            method: 'DELETE'
          })
        if (!section_response.ok) {
          throw new Error(`${section_response.status}`)
        }

        const response = await fetch(`/api/lines/${this.lineId}`)
        if (!response.ok) {
          throw new Error(`${response.status}`)
        }

        const line = await response.json()
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
