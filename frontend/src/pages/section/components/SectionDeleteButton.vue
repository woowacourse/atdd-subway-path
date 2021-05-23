<template>
  <v-btn @click="onDeleteLine" icon>
    <v-icon color="grey lighten-1">mdi-delete</v-icon>
  </v-btn>
</template>

<script>
import { mapMutations } from "vuex";
import { SET_LINE, SHOW_SNACKBAR } from "../../../store/shared/mutationTypes";
import { SNACKBAR_MESSAGES } from "../../../utils/constants";
import { get, remove } from "../../../utils/request";

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
        // TODO 해당 구간을 삭제하는 api를 작성해주세요.
        await remove(`/api/lines/${this.lineId}/sections?stationId=${this.stationId}`)
        // TODO 현재 active된 line의 데이터를 최신으로 불러와주세요.
        const line = await get(`/api/lines/${this.lineId}`)
        .then(res => res.json())
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
