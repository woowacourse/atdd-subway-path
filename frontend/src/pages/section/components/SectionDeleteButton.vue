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
        // TODO [완료] 해당 구간을 삭제하는 api를 작성해주세요.
        const deleteLineResponse = await fetch(`http://localhost:8080/api/lines/${this.lineId}/sections?stationId=${this.stationId}`, {
          method: 'DELETE'
        });
        if (!deleteLineResponse.ok) {
          throw new Error(`${deleteLineResponse.status}`);
        }

        // TODO [완료] 현재 active된 line의 데이터를 최신으로 불러와주세요.
        const getLineResponse = await fetch(`http://localhost:8080/api/lines/${this.lineId}`);
        const line = await getLineResponse.json();
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
