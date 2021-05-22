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
        // TODO 해당 구간을 삭제하는 api를 작성해주세요. (완료)
        const deleteRequest = {
          method: 'DELETE',
          headers: {
            'Authorization': 'Bearer' + this.$store.state.accessToken
          }
        }
        await fetch("/api/lines/" + this.lineId + "/sections?stationId=" + this.stationId , deleteRequest);

        // await fetch("/api/section/{id}", {
        // lineId: this.lineId,
        // stationId: this.stationId,
        // })
        // TODO 현재 active된 line의 데이터를 최신으로 불러와주세요.(완료)
        const lineRequest = {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer' + this.$store.state.accessToken
          },
        };
        const lineResponse = await fetch("/api/lines/" + this.lineId, lineRequest);
        const line = await lineResponse.json();
        this.setLine({...line}); // stations 데이터를 단 한개 존재하는 저장소에 등록

        // const line = await fetch("/api/line/{lineId}")
        // this.setLine({ ...line })
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS);
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL);
        throw new Error(e);
      }
    },
  },
};
</script>
