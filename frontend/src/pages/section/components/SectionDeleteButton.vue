<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import {
  SET_LINE,
  SET_LINES,
  SHOW_SNACKBAR,
} from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";
import { lineApiService } from "../../../api/modules/line";

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
        await lineApiService.deleteSection({
          lineId: this.lineId,
          stationId: this.stationId,
        });
        const line = await lineApiService.get(this.lineId);
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
